import React, { useEffect, useState } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { logout } from "../../features/auth/authSlice";
import MessengerDropdown from "../Message/MessengerDropdown";
import NotificationDropdown from "../Notification/NotificationDropdown";
import { Home, BarChart2, Calendar } from "lucide-react";
import { ChatService } from "../../services/chatService";
import { db } from "../../app/firebaseConfig";
import { collection, onSnapshot, query, where } from "firebase/firestore";

const Navbar = ({ onOpenChat }) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [showMenu, setShowMenu] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showMessenger, setShowMessenger] = useState(false);
  const [unreadMessageCount, setUnreadMessageCount] = useState(0);

  const { user } = useSelector((state) => state.auth);
  const location = useLocation();
  const currentPath = location.pathname;

  const notifications = [
    { id: 1, text: "Bạn có 1 lời mời tham gia sự kiện", time: "2 giờ trước" },
    { id: 2, text: "3 cựu học sinh mới đăng ký", time: "5 giờ trước" },
  ];

  useEffect(() => {
    if (!user?.id) return;
    let unsubscribe;

    const fetchUnreadCount = async () => {
      const result = await ChatService.getTotalUnreadCount(user.id.toString());
      console.log("UnreadMessageCount: ", result);

      if (result.success) {
        setUnreadMessageCount(result.count);
      }
    };

    fetchUnreadCount();

    // Thêm listener realtime

    const setupChatListener = async () => {
      const chatsRef = collection(db, "chats");
      const q = query(
        chatsRef,
        where("participants", "array-contains", user.id.toString())
      );

      unsubscribe = onSnapshot(q, (snapshot) => {
        let total = 0;
        snapshot.forEach((doc) => {
          const data = doc.data();
          if (data.participantInfo?.[user.id.toString()]?.hasUnread) {
            total++;
          }
        });
        setUnreadMessageCount(total);
      });
    };

    setupChatListener();

    return () => {
      if (unsubscribe) unsubscribe();
    };
  }, [user?.id]);

  const handleLogout = async () => {
    try {
      dispatch(logout());
      setShowMenu(false);
      navigate("/login");
    } catch (err) {
      console.error("Logout failed", err);
    }
  };

  const handleMarkAllAsRead = () => {
    setShowNotifications(false);
  };

  const closeAllDropdowns = () => {
    setShowMenu(false);
    setShowNotifications(false);
    setShowMessenger(false);
  };

  return (
    <nav className="bg-white shadow px-6 py-3 fixed top-0 left-0 right-0 z-50">
      <div className="max-w-7xl mx-auto flex items-center justify-between">
        {/* Logo */}
        <div>
          <Link to="/" className="text-2xl font-bold text-blue-600">
            AlumniConnect
          </Link>
        </div>

        {/* Menu giữa */}
        <div className="hidden md:flex space-x-6">
          <Link
            to="/home"
            className={`flex items-center gap-2 px-3 py-2 rounded-md transition-all ${
              currentPath === "/home"
                ? "bg-blue-100 text-blue-600 font-semibold"
                : "text-gray-700 hover:bg-gray-100 hover:text-blue-500"
            }`}
          >
            <Home className="w-5 h-5" />
            <span>Trang chủ</span>
          </Link>
          <Link
            to="/survey"
            className={`flex items-center gap-2 px-3 py-2 rounded-md transition-all ${
              currentPath === "/survey"
                ? "bg-blue-100 text-blue-600 font-semibold"
                : "text-gray-700 hover:bg-gray-100 hover:text-blue-500"
            }`}
          >
            <BarChart2 className="w-5 h-5" />
            <span>Khảo sát</span>
          </Link>
          <Link
            to="/events"
            className={`flex items-center gap-2 px-3 py-2 rounded-md transition-all ${
              currentPath === "/events"
                ? "bg-blue-100 text-blue-600 font-semibold"
                : "text-gray-700 hover:bg-gray-100 hover:text-blue-500"
            }`}
          >
            <Calendar className="w-5 h-5" />
            <span>Sự kiện</span>
          </Link>
        </div>

        {/* Bên phải */}
        <div className="flex items-center space-x-4">
          {user && (
            <>
              {/* Messenger Icon */}
              <div className="relative">
                <button
                  onClick={() => {
                    setShowMessenger(!showMessenger);
                    setShowNotifications(false);
                  }}
                  className={`p-2 rounded-full transition-colors duration-200 relative ${
                    showMessenger
                      ? "bg-blue-100 text-blue-600"
                      : "hover:bg-gray-300 active:bg-gray-400 text-gray-600"
                  }`}
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="h-6 w-6 text-gray-600"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"
                    />
                  </svg>
                  {unreadMessageCount > 0 && (
                    <span className="absolute -top-1 -right-1 bg-red-500 text-white text-xs font-semibold rounded-full h-5 w-5 flex items-center justify-center shadow-md">
                      {unreadMessageCount}
                    </span>
                  )}
                </button>

                {showMessenger && (
                  <MessengerDropdown
                    currentUser={user}
                    onClose={closeAllDropdowns}
                    onOpenChat={onOpenChat}
                  />
                )}
              </div>

              {/* Notification Icon */}
              <div className="relative">
                <button
                  onClick={() => {
                    setShowNotifications(!showNotifications);
                    setShowMessenger(false);
                  }}
                  className={`p-2 rounded-full transition-colors duration-200 relative ${
                    showNotifications
                      ? "bg-blue-100 text-blue-600"
                      : "hover:bg-gray-300 active:bg-gray-400 text-gray-600"
                  }`}
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="h-6 w-6 text-gray-600"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"
                    />
                  </svg>
                  {notifications.length > 0 && (
                    <span className="absolute -top-1 -right-1 bg-red-500 text-white text-xs font-semibold rounded-full h-5 w-5 flex items-center justify-center shadow-md">
                      {notifications.length}
                    </span>
                  )}
                </button>

                {showNotifications && (
                  <NotificationDropdown
                    notifications={notifications}
                    onClose={closeAllDropdowns}
                    onMarkAllAsRead={handleMarkAllAsRead}
                  />
                )}
              </div>
            </>
          )}

          {/* Avatar */}
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
                  src={user.avatar || "/default-avatar.png"}
                  alt="avatar"
                  className="w-10 h-10 rounded-full cursor-pointer border-2 border-blue-500"
                  onClick={() => {
                    setShowMenu(!showMenu);
                    setShowNotifications(false);
                    setShowMessenger(false);
                  }}
                />

                {showMenu && (
                  <div className="absolute right-0 mt-2 w-48 bg-white shadow-lg rounded-md z-50 border border-gray-200">
                    <Link
                      to={`/profile/${user.id}`}
                      className="block px-4 py-2 hover:bg-gray-100 text-gray-700"
                      onClick={closeAllDropdowns}
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
      </div>
    </nav>
  );
};

export default Navbar;
