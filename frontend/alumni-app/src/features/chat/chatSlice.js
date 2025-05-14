import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { ChatService } from "../../services/chatService";
import { getAllUsers } from "../../services/userService";

const INITIAL_PAGINATION = {
  page: 1,
  size: 10,
  totalPages: 1,
  totalItems: 0,
  hasMore: true,
};

export const fetchUserChats = createAsyncThunk(
  "chat/fetchUserChats",
  async (userId, { rejectWithValue }) => {
    try {
      const chats = await new Promise((resolve) => {
        const unsubscribe = ChatService.subscribeToUserChats(
          userId,
          (chatsData) => {
            resolve(chatsData);
            unsubscribe();
          }
        );
      });
      return chats.map((chat) => ({
        ...chat,
        participants: chat.participants.map((id) => id.toString()),
      }));
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

export const fetchAllUsers = createAsyncThunk(
  "chat/fetchAllUsers",
  async ({ token, page = 1, keyword = "" }, { rejectWithValue, getState }) => {
    try {
      const { chat } = getState();
      const size = chat.pagination.size;

      const response = await getAllUsers({ page, size, keyword }, token);

      const isPaginated = response?.totalPages !== undefined;

      return {
        data: isPaginated ? response.content : response,
        pagination: {
          page,
          size,
          totalPages: isPaginated ? response.totalPages : 1,
          totalItems: isPaginated ? response.totalElements : response.length,
          hasMore: isPaginated ? page < response.totalPages : false,
        },
        isFirstLoad: page === 1,
      };
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

const chatSlice = createSlice({
  name: "chat",
  initialState: {
    users: [],
    chats: [],
    loading: false,
    loadingMore: false,
    error: null,
    lastFetch: null,
    pagination: INITIAL_PAGINATION,
    searchTerm: "",
  },
  reducers: {
    markChatAsRead: (state, action) => {
      const { chatId, userId } = action.payload;
      const chat = state.chats.find((c) => c.id === chatId);
      if (chat) {
        chat.participantInfo[userId].hasUnread = false;
      }
    },
    setSearchTerm: (state, action) => {
      state.searchTerm = action.payload;
    },
    resetPagination: (state) => {
      state.pagination = INITIAL_PAGINATION;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchUserChats.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchUserChats.fulfilled, (state, action) => {
        state.chats = action.payload;
        state.loading = false;
        state.lastFetch = Date.now();
      })
      .addCase(fetchUserChats.rejected, (state, action) => {
        state.error = action.payload;
        state.loading = false;
      })
      .addCase(fetchAllUsers.pending, (state, action) => {
        if (action.meta.arg.page === 1) {
          state.loading = true;
        } else {
          state.loadingMore = true;
        }
      })
      .addCase(fetchAllUsers.fulfilled, (state, action) => {
        const { data, pagination, isFirstLoad } = action.payload;

        state.users = isFirstLoad
          ? data
          : [
              ...new Map(
                [...state.users, ...data].map((item) => [item.id, item])
              ).values(),
            ];

        state.pagination = pagination;
        state.loading = false;
        state.loadingMore = false;
      })
      .addCase(fetchAllUsers.rejected, (state, action) => {
        state.error = action.payload;
        state.loading = false;
        state.loadingMore = false;
      });
  },
});

export const { markChatAsRead, setSearchTerm, resetPagination } =
  chatSlice.actions;
export default chatSlice.reducer;
