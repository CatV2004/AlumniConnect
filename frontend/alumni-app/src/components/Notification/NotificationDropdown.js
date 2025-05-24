import React from "react";
import { Link } from "react-router-dom";

const NotificationDropdown = ({
  notifications = [],
  onClose,
  onMarkAllAsRead,
  onMarkAsRead,
}) => {
  const formatTime = (timeString) => {
    // Thêm hàm format thời gian phù hợp
    return new Date(timeString).toLocaleString();
  };
  const safeNotifications = Array.isArray(notifications) ? notifications : [];
  return (
    <div className="absolute right-0 mt-2 w-96 bg-white shadow-xl rounded-lg z-50 border border-gray-200 overflow-hidden">
      {/* Header */}
      <div className="p-4 border-b border-gray-200 bg-gray-50">
        <div className="flex justify-between items-center">
          <h3 className="text-lg font-bold text-gray-800">Thông báo</h3>
          <div className="flex space-x-2">
            <button
              onClick={() => {
                onMarkAllAsRead();
                onClose();
              }}
              className="text-sm text-blue-500 hover:underline"
            >
              Đánh dấu đã đọc
            </button>
            <button
              onClick={onClose}
              className="p-1 rounded-full hover:bg-gray-200"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="h-5 w-5 text-gray-600"
                viewBox="0 0 20 20"
                fill="currentColor"
              >
                <path
                  fillRule="evenodd"
                  d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                  clipRule="evenodd"
                />
              </svg>
            </button>
          </div>
        </div>
      </div>

      {/* Notification List */}
      <div className="max-h-96 overflow-y-auto">
        {safeNotifications.length > 0 ? (
          safeNotifications.map((notification) => (
            <Link
              key={notification.id}
              to={notification.link || "#"}
              className="flex items-center p-3 hover:bg-gray-50 border-b border-gray-100 transition-colors duration-150"
              onClick={() => {
                if (!notification.read) {
                  onMarkAsRead(notification.id);
                }
                onClose();
              }}
            >
              <div className="relative">
                <img
                  src={notification.senderAvatar || "/default-avatar.png"}
                  alt="avatar"
                  className="w-12 h-12 rounded-full mr-3 object-cover"
                />
                {!notification.read && (
                  <div className="absolute bottom-0 right-2 w-3 h-3 bg-blue-500 rounded-full border-2 border-white"></div>
                )}
              </div>
              <div className="flex-1 min-w-0">
                <div className="text-gray-800 font-medium truncate">
                  {notification.message}
                </div>
                <div className="text-xs text-gray-400 mt-1">
                  {formatTime(notification.createdAt)}
                </div>
              </div>
            </Link>
          ))
        ) : (
          <div className="p-4 text-center text-gray-500">
            <p className="text-lg font-medium">Không có thông báo nào</p>
            <p className="text-sm text-gray-400 mt-1">
              Khi có thông báo mới, chúng sẽ xuất hiện ở đây
            </p>
          </div>
        )}
      </div>

      {/* Footer */}
      <div className="p-3 border-t border-gray-200 bg-gray-50 text-center">
        <Link
          to="/notifications"
          className="inline-flex items-center text-blue-500 text-sm font-medium"
          onClick={onClose}
        >
          <span>Xem tất cả thông báo</span>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-4 w-4 ml-1"
            viewBox="0 0 20 20"
            fill="currentColor"
          >
            <path
              fillRule="evenodd"
              d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
              clipRule="evenodd"
            />
          </svg>
        </Link>
      </div>
    </div>
  );
};

export default NotificationDropdown;
