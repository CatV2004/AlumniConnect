import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { logout } from '../../features/auth/authSlice'; 

const Navbar = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [showMenu, setShowMenu] = useState(false);

  const { user } = useSelector((state) => state.auth); 

  const handleLogout = () => {
    dispatch(logout());
    setShowMenu(false);
    navigate('/login');
  };

  return (
    <nav className="bg-white shadow px-6 py-3">
      <div className="max-w-7xl mx-auto flex items-center justify-between">
        {/* Logo */}
        <div>
          <Link to="/" className="text-2xl font-bold text-blue-600">AlumniConnect</Link>
        </div>

        {/* Menu giữa */}
        <div className="hidden md:flex space-x-8">
          <Link to="/home" className="text-gray-700 hover:text-blue-500 font-medium">Trang chủ</Link>
          <Link to="/survey" className="text-gray-700 hover:text-blue-500 font-medium">Khảo sát</Link>
          <Link to="/events" className="text-gray-700 hover:text-blue-500 font-medium">Sự kiện</Link>
        </div>

        {/* Bên phải */}
        <div className="relative">
          {!user ? (
            <Link
              to="/login"
              className="bg-blue-600 text-white px-4 py-1 rounded-md hover:bg-blue-700"
            >
              Đăng nhập
            </Link>
          ) : (
            <div>
              <img
                src={user.avatar || '/default-avatar.png'}
                alt="avatar"
                className="w-10 h-10 rounded-full cursor-pointer border-2 border-blue-500"
                onClick={() => setShowMenu(!showMenu)}
              />

              {showMenu && (
                <div className="absolute right-0 mt-2 w-40 bg-white shadow-lg rounded-md z-50">
                  <Link
                    to={`/profile/${user.id}`}
                    className="block px-4 py-2 hover:bg-gray-100 text-gray-700"
                    onClick={() => setShowMenu(false)}
                  >
                    Trang cá nhân
                  </Link>
                  <button
                    onClick={handleLogout}
                    className="block w-full text-left px-4 py-2 hover:bg-gray-100 text-red-500"
                  >
                    Đăng xuất
                  </button>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
