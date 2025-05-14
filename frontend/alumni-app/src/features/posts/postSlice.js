import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import {
  createPost as createPostService,
  updatePost as updatePostService,
  fetchPosts as fetchPostsService,
} from "../../services/postService";
import {
  getSurveyStatistics as getSurveyStatisticsService,
} from "../../services/surveyPostService";

export const fetchSurveyStatistics = createAsyncThunk(
  "posts/fetchSurveyStatistics",
  async (surveyPostId, { getState, rejectWithValue }) => {
    try {
      const token = getState().auth.token;
      const data = await getSurveyStatisticsService(surveyPostId, token);
      return { surveyPostId, data };
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || error.message);
    }
  }
);

export const fetchPosts = createAsyncThunk(
  "posts/fetchPosts",
  async ({ page = 1, size = 10, refresh = false }, { rejectWithValue }) => {
    try {
      const data = await fetchPostsService({ page, size });
      return {
        posts: data.content,
        currentPage: page,
        totalPages: data.totalPages,
        hasMore: !data.last,
        refresh,
      };
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || error.message);
    }
  }
);

export const fetchSurveyPosts = createAsyncThunk(
  "posts/fetchSurveyPosts",
  async ({ page = 1, size = 10, refresh = false }, { rejectWithValue }) => {
    try {
      const data = await fetchPostsService({ page, size, hasSurvey: true });
      return {
        posts: data.content,
        currentPage: page,
        totalPages: data.totalPages,
        hasMore: !data.last,
        refresh,
      };
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || error.message);
    }
  }
);

export const createPost = createAsyncThunk(
  "posts/createPost",
  async (formData, { getState, rejectWithValue, dispatch }) => {
    try {
      const token = getState().auth.token;
      const response = await createPostService(formData, token);

      // Sau khi tạo post thành công, fetch lại dữ liệu từ trang 1
      dispatch(fetchPosts({ page: 1, size: 10, refresh: true }));

      return response.data;
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || error.message);
    }
  }
);

export const updatePost = createAsyncThunk(
  "posts/updatePost",
  async ({ postId, formData }, { getState, rejectWithValue, dispatch }) => {
    try {
      const token = getState().auth.token;
      const response = await updatePostService(postId, formData, token);

      dispatch(fetchPosts({ page: 1, size: 10, refresh: true }));

      return response;
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || error.message);
    }
  }
);

const postSlice = createSlice({
  name: "posts",
  initialState: {
    posts: [],
    surveyPosts: [],
    loading: false,
    error: null,
    currentPage: 0,
    totalPages: 0,
    hasMore: true,
    surveyCurrentPage: 0,
    surveyTotalPages: 0,
    surveyHasMore: true,
    isCreating: false, 
    surveyStats: {
      loading: false,
      error: null,
      data: {}, 
    },
  },
  reducers: {
    addNewPost: (state, action) => {
      state.posts.unshift(action.payload);
    },
    removePost: (state, action) => {
      state.posts = state.posts.filter((post) => post.id !== action.payload);
    },
    resetPosts: (state) => {
      state.posts = [];
      state.currentPage = 0;
      state.hasMore = true;
    },
    resetSurveyPosts: (state) => {
      state.surveyPosts = [];
      state.surveyCurrentPage = 0;
      state.surveyHasMore = true;
    },
    clearSurveyStats: (state) => {
      state.surveyStats.data = {};
      state.surveyStats.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // Xử lý fetch posts
      .addCase(fetchPosts.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchPosts.fulfilled, (state, action) => {
        state.loading = false;
        state.posts = action.payload.refresh
          ? action.payload.posts // Nếu là refresh thì thay thế toàn bộ
          : [...state.posts, ...action.payload.posts]; // Nếu load more thì nối thêm
        state.currentPage = action.payload.currentPage;
        state.totalPages = action.payload.totalPages;
        state.hasMore = action.payload.hasMore;
      })
      .addCase(fetchPosts.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })

      // Xử lý create post
      .addCase(createPost.pending, (state) => {
        state.isCreating = true;
        state.error = null;
      })
      .addCase(createPost.fulfilled, (state, action) => {
        state.isCreating = false;
      })
      .addCase(createPost.rejected, (state, action) => {
        state.isCreating = false;
        state.error = action.payload;
      })

      // Xử lý update post
      .addCase(updatePost.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updatePost.fulfilled, (state, action) => {
        state.loading = false;
        // Cập nhật post trong danh sách nếu cần
        const updatedPost = action.payload;
        const index = state.posts.findIndex((p) => p.id === updatedPost.id);
        if (index !== -1) {
          state.posts[index] = updatedPost;
        }
      })
      .addCase(updatePost.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })

      // Xử lý fetch surveyPosts
      .addCase(fetchSurveyPosts.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchSurveyPosts.fulfilled, (state, action) => {
        state.loading = false;
        state.surveyPosts = action.payload.refresh
          ? action.payload.posts
          : [...state.surveyPosts, ...action.payload.posts];
        state.surveyCurrentPage = action.payload.currentPage;
        state.surveyTotalPages = action.payload.totalPages;
        state.surveyHasMore = action.payload.hasMore;
      })
      .addCase(fetchSurveyPosts.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })

      .addCase(fetchSurveyStatistics.pending, (state) => {
        state.surveyStats.loading = true;
        state.surveyStats.error = null;
      })
      .addCase(fetchSurveyStatistics.fulfilled, (state, action) => {
        state.surveyStats.loading = false;
        state.surveyStats.data[action.payload.surveyPostId] = action.payload.data;
      })
      .addCase(fetchSurveyStatistics.rejected, (state, action) => {
        state.surveyStats.loading = false;
        state.surveyStats.error = action.payload;
      });
  },
});

export const { addNewPost, removePost, resetPosts, resetSurveyPosts, clearSurveyStats } = postSlice.actions;
export default postSlice.reducer;
