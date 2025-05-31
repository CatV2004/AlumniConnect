import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { changePassword } from "../services/userService";
import { useSelector } from "react-redux";
import { motion } from "framer-motion";
import { FiLock, FiEye, FiEyeOff, FiCheck, FiX, FiAlertCircle } from "react-icons/fi";

const ChangePasswordPage = () => {
  const token = useSelector((state) => state.auth.token);
  const navigate = useNavigate();
  const [form, setForm] = useState({
    oldPassword: "",
    newPassword: "",
    confirmPassword: "",
  });
  const [errors, setErrors] = useState({
    oldPassword: "",
    newPassword: "",
    confirmPassword: "",
    general: ""
  });
  const [showPassword, setShowPassword] = useState({
    old: false,
    new: false,
    confirm: false,
  });
  const [passwordStrength, setPasswordStrength] = useState(0);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    document.title = "Đổi mật khẩu | Hệ thống";
  }, []);

  useEffect(() => {
    if (form.newPassword) {
      const strength = calculatePasswordStrength(form.newPassword);
      setPasswordStrength(strength);
    } else {
      setPasswordStrength(0);
    }
  }, [form.newPassword]);

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
    setForm({ ...form, [name]: value });
    // Clear error when user types
    setErrors({ ...errors, [name]: "", general: "" });
  };

  const toggleShowPassword = (field) => {
    setShowPassword({ ...showPassword, [field]: !showPassword[field] });
  };

  const validateForm = () => {
    let isValid = true;
    const newErrors = {
      oldPassword: "",
      newPassword: "",
      confirmPassword: "",
      general: ""
    };

    if (!form.oldPassword) {
      newErrors.oldPassword = "Vui lòng nhập mật khẩu hiện tại";
      isValid = false;
    }

    if (!form.newPassword) {
      newErrors.newPassword = "Vui lòng nhập mật khẩu mới";
      isValid = false;
    } else if (passwordStrength < 3) {
      newErrors.newPassword = "Mật khẩu quá yếu. Vui lòng chọn mật khẩu mạnh hơn";
      isValid = false;
    }

    if (!form.confirmPassword) {
      newErrors.confirmPassword = "Vui lòng xác nhận mật khẩu mới";
      isValid = false;
    } else if (form.newPassword !== form.confirmPassword) {
      newErrors.confirmPassword = "Mật khẩu mới không khớp";
      isValid = false;
    }

    setErrors(newErrors);
    return isValid;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    
    if (!validateForm()) {
      setIsSubmitting(false);
      return;
    }

    try {
      await changePassword(token, form.oldPassword, form.newPassword);
      setSuccess(true);
      setTimeout(() => {
        navigate(-1);
      }, 1500);
    } catch (err) {
      // Xử lý lỗi từ backend
      if (err.response?.data) {
        const backendError = err.response.data;
        
        if (backendError.message === "Mật khẩu cũ không đúng") {
          setErrors({
            ...errors,
            oldPassword: backendError.message,
            general: ""
          });
        } else if (backendError.newPassword) {
          setErrors({
            ...errors,
            newPassword: backendError.newPassword,
            general: ""
          });
        } else {
          setErrors({
            ...errors,
            general: backendError.message || "Đã xảy ra lỗi khi đổi mật khẩu!"
          });
        }
      } else {
        setErrors({
          ...errors,
          general: "Đã xảy ra lỗi khi đổi mật khẩu!"
        });
      }
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

  // Hàm render input field để giảm code trùng lặp
  const renderInputField = (field, label, placeholder) => (
    <div>
      <label className="block text-gray-700 text-sm font-medium mb-1">
        {label}
      </label>
      <div className="relative">
        <input
          type={showPassword[field] ? "text" : "password"}
          name={field}
          placeholder={placeholder}
          className={`w-full p-3 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition ${
            errors[field] ? "border-red-500" : "border-gray-300"
          }`}
          value={form[field]}
          onChange={handleChange}
          required
        />
        <button
          type="button"
          onClick={() => toggleShowPassword(field)}
          className="absolute right-3 top-3 text-gray-500 hover:text-gray-700"
        >
          {showPassword[field] ? <FiEyeOff /> : <FiEye />}
        </button>
      </div>
      {errors[field] && (
        <motion.p 
          initial={{ opacity: 0, y: -5 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-red-500 text-sm mt-1 flex items-start"
        >
          <FiAlertCircle className="mr-1 mt-0.5 flex-shrink-0" />
          {errors[field]}
        </motion.p>
      )}
    </div>
  );

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
            <FiLock className="text-2xl" />
            <h2 className="text-2xl font-bold">Đổi mật khẩu</h2>
          </div>
          <p className="text-blue-100 mt-1">
            Vui lòng nhập mật khẩu hiện tại và mật khẩu mới của bạn
          </p>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-5">
          {success ? (
            <motion.div
              initial={{ scale: 0.8 }}
              animate={{ scale: 1 }}
              className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative"
            >
              <div className="flex items-center">
                <FiCheck className="mr-2 text-xl" />
                <span>Đổi mật khẩu thành công! Bạn sẽ được chuyển về trang trước.</span>
              </div>
            </motion.div>
          ) : (
            <>
              {errors.general && (
                <motion.div
                  initial={{ opacity: 0, y: -10 }}
                  animate={{ opacity: 1, y: 0 }}
                  className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative"
                >
                  <div className="flex items-center">
                    <FiX className="mr-2 text-xl" />
                    <span>{errors.general}</span>
                  </div>
                </motion.div>
              )}

              <div className="space-y-4">
                {/* Mật khẩu hiện tại */}
                {renderInputField(
                  "oldPassword",
                  "Mật khẩu hiện tại",
                  "Nhập mật khẩu hiện tại"
                )}

                {/* Mật khẩu mới */}
                <div>
                  {renderInputField(
                    "newPassword",
                    "Mật khẩu mới",
                    "Nhập mật khẩu mới"
                  )}
                  {form.newPassword && (
                    <motion.div
                      initial={{ opacity: 0, height: 0 }}
                      animate={{ opacity: 1, height: "auto" }}
                      className="mt-2"
                    >
                      <div className="flex items-center justify-between text-xs text-gray-500 mb-1">
                        <span>Độ mạnh mật khẩu:</span>
                        <span className={`font-medium ${
                          passwordStrength <= 2 ? "text-red-500" : 
                          passwordStrength === 3 ? "text-yellow-500" : 
                          "text-green-500"
                        }`}>
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

                {/* Xác nhận mật khẩu mới */}
                {renderInputField(
                  "confirmPassword",
                  "Xác nhận mật khẩu mới",
                  "Nhập lại mật khẩu mới"
                )}
              </div>

              <div className="pt-2">
                <motion.button
                  whileHover={{ scale: 1.02 }}
                  whileTap={{ scale: 0.98 }}
                  type="submit"
                  disabled={isSubmitting}
                  className={`w-full py-3 px-4 rounded-lg font-medium text-white transition ${
                    isSubmitting ? "bg-blue-400 cursor-not-allowed" : "bg-blue-600 hover:bg-blue-700"
                  }`}
                >
                  {isSubmitting ? (
                    <span className="flex items-center justify-center">
                      <svg className="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                        <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                      </svg>
                      Đang xử lý...
                    </span>
                  ) : (
                    "Cập nhật mật khẩu"
                  )}
                </motion.button>
              </div>
            </>
          )}
        </form>
      </motion.div>
    </motion.div>
  );
};

export default ChangePasswordPage;