import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchCurrentUser, loginUser } from "../features/auth/authSlice";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import {
  FiUser,
  FiLock,
  FiEye,
  FiEyeOff,
  FiLogIn,
  FiAlertCircle,
  FiCheck,
} from "react-icons/fi";

const LoginPage = () => {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    role: "ALUMNI",
  });
  const [showPassword, setShowPassword] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [loginSuccess, setLoginSuccess] = useState(false);
  const [rememberMe, setRememberMe] = useState(false);

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { error } = useSelector((state) => state.auth);
  const [valid, setValid] = useState(null);

  useEffect(() => {
    document.title = "Đăng nhập | Hệ thống";
    // Load saved credentials if "Remember me" was checked
    const savedUsername = localStorage.getItem("username");
    const savedRole = localStorage.getItem("role");
    if (savedUsername && savedRole) {
      setFormData((prev) => ({
        ...prev,
        username: savedUsername,
        role: savedRole,
      }));
      setRememberMe(true);
    }
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const toggleShowPassword = () => {
    setShowPassword(!showPassword);
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      await dispatch(loginUser(formData)).unwrap();

      setLoginSuccess(true);
      navigate("/home");
    } catch (err) {
      console.error("Login failed:", err);
      setValid(err);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      transition={{ duration: 0.3 }}
      className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4"
    >
      <motion.div
        initial={{ y: -20 }}
        animate={{ y: 0 }}
        className="w-full max-w-md bg-white rounded-xl shadow-lg overflow-hidden"
      >
        <div className="bg-gradient-to-r from-blue-600 to-indigo-700 p-6 text-white">
          <div className="flex items-center space-x-3">
            <FiLogIn className="text-2xl" />
            <h2 className="text-2xl font-bold">Đăng nhập hệ thống</h2>
          </div>
          <p className="text-blue-100 mt-1">
            Vui lòng nhập thông tin đăng nhập của bạn
          </p>
        </div>

        <form onSubmit={handleLogin} className="p-6 space-y-5">
          {loginSuccess ? (
            <motion.div
              initial={{ scale: 0.8 }}
              animate={{ scale: 1 }}
              className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative"
            >
              <div className="flex items-center">
                <FiCheck className="mr-2 text-xl" />
                <span>
                  Đăng nhập thành công! Bạn sẽ được chuyển đến trang chủ.
                </span>
              </div>
            </motion.div>
          ) : (
            <>
              {error && (
                <motion.div
                  initial={{ opacity: 0, y: -10 }}
                  animate={{ opacity: 1, y: 0 }}
                  className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative"
                >
                  <div className="flex items-center">
                    <FiAlertCircle className="mr-2 text-xl" />
                    <span>{error}</span>
                  </div>
                </motion.div>
              )}

              <div className="space-y-4">
                {/* Tên đăng nhập */}
                <div>
                  <label className="block text-gray-700 text-sm font-medium mb-1">
                    Tên đăng nhập <span className="text-red-500">*</span>
                  </label>
                  <div className="relative">
                    <FiUser className="absolute left-3 top-3.5 text-gray-400" />
                    <input
                      type="text"
                      name="username"
                      placeholder="Nhập tên đăng nhập"
                      className="w-full p-3 pl-10 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
                      value={formData.username}
                      onChange={handleChange}
                      required
                    />
                  </div>
                  {valid && valid.username && (
                    <div className="text-red-500 text-sm italic mt-1">
                      {valid.username}
                    </div>
                  )}
                </div>

                {/* Mật khẩu */}
                <div>
                  <label className="block text-gray-700 text-sm font-medium mb-1">
                    Mật khẩu <span className="text-red-500">*</span>
                  </label>
                  <div className="relative">
                    <FiLock className="absolute left-3 top-3.5 text-gray-400" />
                    <input
                      type={showPassword ? "text" : "password"}
                      name="password"
                      placeholder="Nhập mật khẩu"
                      className="w-full p-3 pl-10 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
                      value={formData.password}
                      onChange={handleChange}
                      required
                    />
                    <button
                      type="button"
                      onClick={toggleShowPassword}
                      className="absolute right-3 top-3 text-gray-500 hover:text-gray-700"
                    >
                      {showPassword ? <FiEyeOff /> : <FiEye />}
                    </button>
                  </div>
                  {valid && valid.password && (
                    <div className="text-red-500 text-sm italic mt-1">
                      {valid.password}
                    </div>
                  )}
                </div>

                {/* Vai trò */}
                <div>
                  <label className="block text-gray-700 text-sm font-medium mb-1">
                    Vai trò <span className="text-red-500">*</span>
                  </label>
                  <select
                    name="role"
                    className="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition appearance-none bg-white"
                    value={formData.role}
                    onChange={handleChange}
                  >
                    <option value="ALUMNI">Cựu sinh viên</option>
                    <option value="TEACHER">Giảng viên</option>
                    <option value="ADMIN">Quản trị viên</option>
                  </select>
                </div>

                {/* Remember me */}
                <div className="flex items-center justify-between">
                  <div className="flex items-center">
                    <input
                      id="remember-me"
                      name="remember-me"
                      type="checkbox"
                      checked={rememberMe}
                      onChange={(e) => setRememberMe(e.target.checked)}
                      className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                    />
                    <label
                      htmlFor="remember-me"
                      className="ml-2 block text-sm text-gray-700"
                    >
                      Ghi nhớ đăng nhập
                    </label>
                  </div>

                  <div className="text-sm">
                    <a
                      href="/forgot-password"
                      className="font-medium text-blue-600 hover:text-blue-500"
                    >
                      Quên mật khẩu?
                    </a>
                  </div>
                </div>
              </div>

              <div className="pt-2">
                <motion.button
                  whileHover={{ scale: 1.02 }}
                  whileTap={{ scale: 0.98 }}
                  type="submit"
                  disabled={isSubmitting}
                  className={`w-full py-3 px-4 rounded-lg font-medium text-white transition ${
                    isSubmitting
                      ? "bg-blue-400 cursor-not-allowed"
                      : "bg-blue-600 hover:bg-blue-700"
                  }`}
                >
                  {isSubmitting ? (
                    <span className="flex items-center justify-center">
                      <svg
                        className="animate-spin -ml-1 mr-2 h-4 w-4 text-white"
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none"
                        viewBox="0 0 24 24"
                      >
                        <circle
                          className="opacity-25"
                          cx="12"
                          cy="12"
                          r="10"
                          stroke="currentColor"
                          strokeWidth="4"
                        ></circle>
                        <path
                          className="opacity-75"
                          fill="currentColor"
                          d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                        ></path>
                      </svg>
                      Đang đăng nhập...
                    </span>
                  ) : (
                    <span className="flex items-center justify-center">
                      <FiLogIn className="mr-2" />
                      Đăng nhập
                    </span>
                  )}
                </motion.button>
              </div>
            </>
          )}
        </form>

        {!loginSuccess && (
          <div className="px-6 pb-6 text-center">
            <p className="text-sm text-gray-600">
              Chưa có tài khoản?{" "}
              <a
                href="/register"
                className="text-blue-600 hover:underline font-medium"
              >
                Đăng ký ngay
              </a>
            </p>
          </div>
        )}
      </motion.div>
    </motion.div>
  );
};

export default LoginPage;
