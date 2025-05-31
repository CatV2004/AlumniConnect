import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchCurrentUser, logout } from "./authSlice";

const AuthProvider = ({ children }) => {
  const dispatch = useDispatch();
  const { token, user, loading, error } = useSelector((state) => state.auth);
  console.log("token in auth provider: ", token);

  useEffect(() => {
    if (error?.status === 401) {
      dispatch(logout());
      window.location.href = "/login"; 
    }
  }, [error, dispatch]);

  useEffect(() => {
    if (token && !user && !loading) {
      dispatch(fetchCurrentUser());
    }
  }, [token, user, dispatch, loading]);

  if (token && !user) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50 p-6">
        <div className="w-full max-w-md mx-auto text-center">
          {/* Animated spinner */}
          <div className="flex justify-center mb-6">
            <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-blue-500"></div>
          </div>

          {/* Loading text with subtle animation */}
          <h2 className="text-2xl font-semibold text-gray-800 mb-2 animate-pulse">
            Đang tải thông tin người dùng
          </h2>

          {/* Progress indicator */}
          <div className="w-full bg-gray-200 rounded-full h-2.5 mt-4">
            <div
              className="bg-blue-600 h-2.5 rounded-full animate-pulse"
              style={{ width: "45%" }}
            ></div>
          </div>

          {/* Optional helpful message */}
          <p className="mt-4 text-gray-500 text-sm">
            Vui lòng chờ trong giây lát...
          </p>
        </div>
      </div>
    );
  }

  return children;
};

export default AuthProvider;
