import { configureStore } from '@reduxjs/toolkit';
import postReducer from '../features/posts/postSlice';
import authReducer from '../features/auth/authSlice';

const store = configureStore({
  reducer: {
    auth: authReducer,
    posts: postReducer,
  },
});

export default store;
