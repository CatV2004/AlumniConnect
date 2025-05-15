import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { login, register, getCurrentUser } from "../../services/authService";

export const loginUser = createAsyncThunk(
  "auth/loginUser",
  async ({ username, password, role }, { rejectWithValue }) => {
    try {
      return await login({ username, password, role });
    } catch (error) {
      return rejectWithValue(
        error.response?.data || { message: "Đăng nhập thất bại!" }
      );
    }
  }
);

export const registerUser = createAsyncThunk(
  "auth/registerUser",
  async (userData, { rejectWithValue }) => {
    try {
      return await register(userData);
    } catch (error) {
      return rejectWithValue(
        error.response?.data || { message: "Đăng ký thất bại!" }
      );
    }
  }
);

export const fetchCurrentUser = createAsyncThunk(
  "auth/fetchCurrentUser",
  async (_, { rejectWithValue, getState }) => {
    const token = getState().auth.token;
    try {
      return await getCurrentUser(token);
    } catch (error) {
      return rejectWithValue({
        message:
          error.response?.data?.message || "Không thể lấy thông tin người dùng",
        status: error.response?.status,
      });
    }
  }
);

const authSlice = createSlice({
  name: "auth",
  initialState: {
    token: localStorage.getItem("token") || null,
    role: localStorage.getItem("role") || null,
    user: null,
    loading: false,
    error: null,
  },
  reducers: {
    logout: (state) => {
      state.token = null;
      state.role = null;
      state.user = null;
      state.error = null;
      localStorage.removeItem("token");
      localStorage.removeItem("role");
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(loginUser.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(loginUser.fulfilled, (state, action) => {
        state.loading = false;
        state.token = action.payload.token;
        state.role = action.payload.role;

        localStorage.setItem("token", action.payload.token);
        localStorage.setItem("role", action.payload.role);
      })
      .addCase(loginUser.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload?.message || "Đăng nhập thất bại!";
      })
      .addCase(registerUser.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(registerUser.fulfilled, (state) => {
        state.loading = false;
      })
      .addCase(registerUser.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload?.message || "Đăng ký thất bại!";
      })
      .addCase(fetchCurrentUser.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchCurrentUser.fulfilled, (state, action) => {
        state.loading = false;
        state.user = action.payload;
      })
      .addCase(fetchCurrentUser.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
        state.user = null;
        console.log("error: ", action.payload?.message);
      });
  },
});

export const { logout } = authSlice.actions;
export default authSlice.reducer;
