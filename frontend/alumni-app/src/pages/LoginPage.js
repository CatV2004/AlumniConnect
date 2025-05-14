import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { loginUser } from '../features/auth/authSlice';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('ALUMNI');
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const { loading, error } = useSelector((state) => state.auth);

  const handleLogin = (e) => {
    e.preventDefault();
    dispatch(loginUser({ username, password, role }))
      .unwrap()
      .then(() => navigate('/home'));
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-100 via-white to-blue-200">
      <div className="w-full max-w-md bg-white shadow-2xl rounded-xl p-8 animate-fade-in-up">
        <h2 className="text-3xl font-bold text-center text-blue-600 mb-6">Chào mừng trở lại</h2>

        {error && (
          <p className="text-red-600 text-center font-medium mb-4">{error}</p>
        )}

        <form onSubmit={handleLogin} className="space-y-4">
          <div>
            <label htmlFor="username" className="block text-sm font-medium text-gray-700">Tên đăng nhập</label>
            <input
              type="text"
              id="username"
              className="mt-1 w-full px-4 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400 transition"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>

          <div>
            <label htmlFor="password" className="block text-sm font-medium text-gray-700">Mật khẩu</label>
            <input
              type="password"
              id="password"
              className="mt-1 w-full px-4 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400 transition"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <div>
            <label htmlFor="role" className="block text-sm font-medium text-gray-700">Vai trò</label>
            <select
              id="role"
              className="mt-1 w-full px-4 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400 transition"
              value={role}
              onChange={(e) => setRole(e.target.value)}
            >
              <option value="ALUMNI">Cựu sinh viên</option>
              <option value="TEACHER">Giảng viên</option>
              <option value="ADMIN">Quản trị viên</option>
            </select>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full py-2 px-4 bg-blue-600 text-white font-semibold rounded-md hover:bg-blue-700 transition duration-300 transform hover:scale-105 focus:outline-none"
          >
            {loading ? 'Đang đăng nhập...' : 'Đăng nhập'}
          </button>
        </form>

        <p className="mt-6 text-center text-sm text-gray-600">
          Chưa có tài khoản? <a href="/register" className="text-blue-600 hover:underline">Đăng ký</a>
        </p>
      </div>
    </div>
  );
};

export default LoginPage;
