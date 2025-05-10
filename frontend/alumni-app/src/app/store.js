import { configureStore } from '@reduxjs/toolkit';
import postReducer from '../features/posts/postSlice';
import authReducer from '../features/auth/authSlice';
import userReducer  from '../features/users/userSlice';
import chatReducer  from '../features/chat/chatSlice';


const store = configureStore({
  reducer: {
    auth: authReducer,
    posts: postReducer,
    users: userReducer,
    chat: chatReducer,
  },
});

export default store;
