import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { registerUser } from "../features/auth/authSlice";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import {
  FiUser,
  FiLock,
  FiMail,
  FiPhone,
  FiImage,
  FiEye,
  FiEyeOff,
  FiCheck,
  FiX,
} from "react-icons/fi";

const RegisterPage = () => {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    confirmPassword: "",
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    studentCode: "",
  });
  const [avatar, setAvatar] = useState(null);
  const [localError, setLocalError] = useState(null);
  const [showPassword, setShowPassword] = useState({
    password: false,
    confirmPassword: false,
  });
  const [passwordStrength, setPasswordStrength] = useState(0);
  const [avatarPreview, setAvatarPreview] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [success, setSuccess] = useState(false);
  const [errors, setErrors] = useState(null);

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { loading, error } = useSelector((state) => state.auth);

  useEffect(() => {
    document.title = "Đăng ký | Hệ thống";
  }, []);

  // Kiểm tra độ mạnh mật khẩu
  useEffect(() => {
    if (formData.password) {
      const strength = calculatePasswordStrength(formData.password);
      setPasswordStrength(strength);
    } else {
      setPasswordStrength(0);
    }
  }, [formData.password]);

  const calculatePasswordStrength = (password) => {
    let strength = 0;
    if (password.length > 5) strength += 1;
    if (password.length > 8) strength += 1;
    if (/[A-Z]/.test(password)) strength += 1;
    if (/[0-9]/.test(password)) strength += 1;
    if (/[^A-Za-z0-9]/.test(password)) strength += 1;
    return strength;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    setLocalError(null);
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      if (file.size > 5 * 1024 * 1024) {
        setLocalError("Kích thước ảnh không được vượt quá 5MB");
        return;
      }
      setAvatar(file);
      setAvatarPreview(URL.createObjectURL(file));
    }
  };

  const toggleShowPassword = (field) => {
    setShowPassword({ ...showPassword, [field]: !showPassword[field] });
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    setLocalError(null);

    if (formData.password !== formData.confirmPassword) {
      setLocalError("Mật khẩu và Nhập lại mật khẩu không khớp.");
      setIsSubmitting(false);
      return;
    }

    if (passwordStrength < 3) {
      setLocalError("Mật khẩu quá yếu. Vui lòng chọn mật khẩu mạnh hơn.");
      setIsSubmitting(false);
      return;
    }

    const formDataToSend = new FormData();
    Object.entries(formData).forEach(([key, value]) => {
      formDataToSend.append(key, value);
    });
    if (avatar) formDataToSend.append("avatar", avatar);

    try {
      await dispatch(registerUser(formDataToSend)).unwrap();
      setSuccess(true);
      setTimeout(() => {
        navigate("/login");
      }, 1500);
    } catch (err) {
      console.error("Registration failed: ", err);
      setErrors(err);
    } finally {
      setIsSubmitting(false);
    }
  };

  const getPasswordStrengthColor = () => {
    if (passwordStrength <= 2) return "bg-red-500";
    if (passwordStrength === 3) return "bg-yellow-500";
    return "bg-green-500";
  };

  const getPasswordStrengthText = () => {
    if (passwordStrength === 0) return "";
    if (passwordStrength <= 2) return "Yếu";
    if (passwordStrength === 3) return "Trung bình";
    return "Mạnh";
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
        className="w-full max-w-2xl bg-white rounded-xl shadow-lg overflow-hidden"
      >
        <div className="bg-gradient-to-r from-blue-600 to-indigo-700 p-6 text-white">
          <div className="flex items-center space-x-3">
            <FiUser className="text-2xl" />
            <h2 className="text-2xl font-bold">Đăng ký tài khoản</h2>
          </div>
          <p className="text-blue-100 mt-1">
            Vui lòng điền đầy đủ thông tin để tạo tài khoản mới
          </p>
        </div>

        <form onSubmit={handleRegister} className="p-6 space-y-5">
          {success ? (
            <motion.div
              initial={{ scale: 0.8 }}
              animate={{ scale: 1 }}
              className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative"
            >
              <div className="flex items-center">
                <FiCheck className="mr-2 text-xl" />
                <span>
                  Đăng ký thành công! Vui lòng chờ người quản trị xác nhận.
                </span>
              </div>
            </motion.div>
          ) : (
            <>
              {(error || localError) && (
                <motion.div
                  initial={{ opacity: 0, y: -10 }}
                  animate={{ opacity: 1, y: 0 }}
                  className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative"
                >
                  <div className="flex items-center">
                    <FiX className="mr-2 text-xl" />
                    <span>{localError || error}</span>
                  </div>
                </motion.div>
              )}

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {/* Họ và tên */}
                <div>
                  <label className="block text-gray-700 text-sm font-medium mb-1">
                    Họ <span className="text-red-500">*</span>
                  </label>
                  <div className="relative">
                    <input
                      type="text"
                      name="firstName"
                      placeholder="Nhập họ của bạn"
                      className="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
                      value={formData.firstName}
                      onChange={handleChange}
                      required
                    />
                  </div>
                  {errors && errors.firstName && (
                    <div className="text-red-500 text-sm italic mt-1">
                      {errors.firstName}
                    </div>
                  )}
                </div>

                <div>
                  <label className="block text-gray-700 text-sm font-medium mb-1">
                    Tên <span className="text-red-500">*</span>
                  </label>
                  <div className="relative">
                    <input
                      type="text"
                      name="lastName"
                      placeholder="Nhập tên của bạn"
                      className="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
                      value={formData.lastName}
                      onChange={handleChange}
                      required
                    />
                  </div>
                  {errors && errors.lastName && (
                    <div className="text-red-500 text-sm italic mt-1">
                      {errors.lastName}
                    </div>
                  )}
                </div>

                {/* Email và SĐT */}
                <div>
                  <label className="block text-gray-700 text-sm font-medium mb-1">
                    Email <span className="text-red-500">*</span>
                  </label>
                  <div className="relative">
                    <FiMail className="absolute left-3 top-3.5 text-gray-400" />
                    <input
                      type="email"
                      name="email"
                      placeholder="Nhập email của bạn"
                      className="w-full p-3 pl-10 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
                      value={formData.email}
                      onChange={handleChange}
                      required
                    />
                  </div>
                  {errors && errors.email && (
                    <div className="text-red-500 text-sm italic mt-1">
                      {errors.email}
                    </div>
                  )}
                </div>

                <div>
                  <label className="block text-gray-700 text-sm font-medium mb-1">
                    Số điện thoại
                  </label>
                  <div className="relative">
                    <FiPhone className="absolute left-3 top-3.5 text-gray-400" />
                    <input
                      type="text"
                      name="phone"
                      placeholder="Nhập số điện thoại"
                      className="w-full p-3 pl-10 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
                      value={formData.phone}
                      onChange={handleChange}
                    />
                  </div>
                  {errors && errors.phone && (
                    <div className="text-red-500 text-sm italic mt-1">
                      {errors.phone}
                    </div>
                  )}
                </div>

                {/* Mã sinh viên và Tên đăng nhập */}
                <div>
                  <label className="block text-gray-700 text-sm font-medium mb-1">
                    Mã sinh viên <span className="text-red-500">*</span>
                  </label>
                  <div className="relative">
                    <input
                      type="text"
                      name="studentCode"
                      placeholder="Nhập mã sinh viên"
                      className="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
                      value={formData.studentCode}
                      onChange={handleChange}
                      required
                    />
                  </div>
                  {errors && errors.studentCode && (
                    <div className="text-red-500 text-sm italic mt-1">
                      {errors.studentCode}
                    </div>
                  )}
                </div>

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
                  {errors && errors.username && (
                    <div className="text-red-500 text-sm italic mt-1">
                      {errors.username}
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
                      type={showPassword.password ? "text" : "password"}
                      name="password"
                      placeholder="Nhập mật khẩu"
                      className="w-full p-3 pl-10 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
                      value={formData.password}
                      onChange={handleChange}
                      required
                    />
                    <button
                      type="button"
                      onClick={() => toggleShowPassword("password")}
                      className="absolute right-3 top-3 text-gray-500 hover:text-gray-700"
                    >
                      {showPassword.password ? <FiEyeOff /> : <FiEye />}
                    </button>
                    {errors && errors.password && (
                      <div className="text-red-500 text-sm italic mt-1">
                        {errors.password}
                      </div>
                    )}
                  </div>
                  {formData.password && (
                    <motion.div
                      initial={{ opacity: 0, height: 0 }}
                      animate={{ opacity: 1, height: "auto" }}
                      className="mt-2"
                    >
                      <div className="flex items-center justify-between text-xs text-gray-500 mb-1">
                        <span>Độ mạnh mật khẩu:</span>
                        <span
                          className={`font-medium ${
                            passwordStrength <= 2
                              ? "text-red-500"
                              : passwordStrength === 3
                              ? "text-yellow-500"
                              : "text-green-500"
                          }`}
                        >
                          {getPasswordStrengthText()}
                        </span>
                      </div>
                      <div className="w-full bg-gray-200 rounded-full h-1.5">
                        <div
                          className={`h-1.5 rounded-full ${getPasswordStrengthColor()}`}
                          style={{
                            width: `${(passwordStrength / 5) * 100}%`,
                            transition: "width 0.3s ease",
                          }}
                        ></div>
                      </div>
                    </motion.div>
                  )}
                </div>

                {/* Xác nhận mật khẩu */}
                <div>
                  <label className="block text-gray-700 text-sm font-medium mb-1">
                    Xác nhận mật khẩu <span className="text-red-500">*</span>
                  </label>
                  <div className="relative">
                    <FiLock className="absolute left-3 top-3.5 text-gray-400" />
                    <input
                      type={showPassword.confirmPassword ? "text" : "password"}
                      name="confirmPassword"
                      placeholder="Nhập lại mật khẩu"
                      className="w-full p-3 pl-10 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
                      value={formData.confirmPassword}
                      onChange={handleChange}
                      required
                    />
                    <button
                      type="button"
                      onClick={() => toggleShowPassword("confirmPassword")}
                      className="absolute right-3 top-3 text-gray-500 hover:text-gray-700"
                    >
                      {showPassword.confirmPassword ? <FiEyeOff /> : <FiEye />}
                    </button>
                  </div>
                  {errors && errors.confirmPassword && (
                    <div className="text-red-500 text-sm italic mt-1">
                      {errors.confirmPassword}
                    </div>
                  )}
                </div>

                {/* Avatar */}
                <div className="md:col-span-2">
                  <label className="block text-gray-700 text-sm font-medium mb-1">
                    Ảnh đại diện
                  </label>
                  <div className="flex items-center space-x-4">
                    <div className="relative">
                      <label className="cursor-pointer">
                        <div className="w-16 h-16 rounded-full bg-gray-200 flex items-center justify-center overflow-hidden border-2 border-gray-300 hover:border-blue-500 transition">
                          {avatarPreview ? (
                            <img
                              src={avatarPreview}
                              alt="Avatar preview"
                              className="w-full h-full object-cover"
                            />
                          ) : (
                            <FiImage className="text-gray-400 text-xl" />
                          )}
                        </div>
                        <input
                          type="file"
                          onChange={handleFileChange}
                          accept="image/*"
                          className="hidden"
                        />
                      </label>
                    </div>
                    <div className="flex-1">
                      <p className="text-sm text-gray-600">
                        Chọn ảnh đại diện (JPEG, PNG, tối đa 5MB)
                      </p>
                      {avatar && (
                        <motion.p
                          initial={{ opacity: 0 }}
                          animate={{ opacity: 1 }}
                          className="text-xs text-green-600 mt-1"
                        >
                          Đã chọn: {avatar.name}
                        </motion.p>
                      )}
                      {errors && errors.avatar && (
                        <div className="text-red-500 text-sm italic mt-1">
                          {errors.avatar}
                        </div>
                      )}
                    </div>
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
                      Đang đăng ký...
                    </span>
                  ) : (
                    "Đăng ký tài khoản"
                  )}
                </motion.button>
              </div>
            </>
          )}
        </form>

        {!success && (
          <div className="px-6 pb-6 text-center">
            <p className="text-sm text-gray-600">
              Đã có tài khoản?{" "}
              <a
                href="/login"
                className="text-blue-600 hover:underline font-medium"
              >
                Đăng nhập ngay
              </a>
            </p>
          </div>
        )}
      </motion.div>
    </motion.div>
  );
};

export default RegisterPage;
