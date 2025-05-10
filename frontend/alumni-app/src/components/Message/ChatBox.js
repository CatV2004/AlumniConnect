import React, { useState, useRef, useEffect } from "react";
import { FiX, FiMinus, FiPaperclip, FiSmile, FiSend } from "react-icons/fi";
import { format } from "date-fns";
import { vi } from "date-fns/locale";

const ChatBox = ({
  chat,
  messages = [],
  currentUser,
  onClose,
  onSendMessage,
  style,
}) => {
  const [message, setMessage] = useState("");
  const [isOnline, setIsOnline] = useState(false); // Có thể thêm logic kiểm tra online sau
  const messagesEndRef = useRef(null);

  const handleSendMessage = async (e) => {
    e.preventDefault();
    if (message.trim()) {
      const newMessage = {
        text: message,
        senderId: currentUser.id.toString(),
        timestamp: new Date(),
      };

      setMessage("");
      await onSendMessage(newMessage);
    }
  };

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const formatTime = (timestamp) => {
    if (!timestamp) return "";
    const date = timestamp.toDate ? timestamp.toDate() : new Date(timestamp);
    return format(date, "HH:mm", { locale: vi });
  };

  const isCurrentUser = (senderId) => {
    return senderId === currentUser.id.toString();
  };

  return (
    <div
      className="fixed bottom-4 right-4 w-80 bg-white rounded-lg shadow-xl border border-gray-200 flex flex-col z-50"
      style={style}
    >
      {/* Header */}
      <div className="flex justify-between items-center p-3 bg-blue-600 text-white rounded-t-lg">
        <div className="flex items-center">
          <img
            src={chat.participant.avatar || "/default-avatar.png"}
            alt={chat.participant.name}
            className="w-8 h-8 rounded-full mr-2 object-cover"
          />
          <div>
            <div className="font-medium">{chat.participant.name}</div>
            <div className="text-xs">
              {isOnline ? (
                <span className="text-green-300">Đang hoạt động</span>
              ) : (
                <span className="text-blue-200">Offline</span>
              )}
            </div>
          </div>
        </div>
        <div className="flex space-x-2">
          <button
            onClick={() => setIsOnline(!isOnline)} // Tạm thời để toggle, có thể thay bằng logic thực tế
            className="hover:bg-blue-700 p-1 rounded"
          >
            <FiMinus size={16} />
          </button>
          <button onClick={onClose} className="hover:bg-blue-700 p-1 rounded">
            <FiX size={16} />
          </button>
        </div>
      </div>

      {/* Messages */}
      <div
        className="flex-1 p-3 overflow-y-auto bg-gray-50"
        style={{ maxHeight: "280px", minHeight: "280px" }}
      >
        {messages.length === 0 ? (
          <div className="text-center text-gray-500 py-4">
            Bắt đầu cuộc trò chuyện với {chat.participant.name}
          </div>
        ) : (
          messages.map((msg, index) => {
            const fromCurrentUser = isCurrentUser(msg.senderId);
            const avatarUrl = fromCurrentUser
              ? currentUser.avatar || "/default-avatar.png"
              : chat.participant.avatar || "/default-avatar.png";

            return (
              <div
                key={index}
                className={`mb-3 flex ${
                  fromCurrentUser ? "justify-end" : "justify-start"
                }`}
              >
                {!fromCurrentUser && (
                  <img
                    src={avatarUrl}
                    alt="avatar"
                    className="w-7 h-7 rounded-full object-cover mr-2 self-end"
                  />
                )}
                <div
                  className={`max-w-xs p-2 rounded-lg relative ${
                    fromCurrentUser
                      ? "bg-blue-500 text-white"
                      : "bg-white border border-gray-200 text-gray-800"
                  }`}
                >
                  <div className="text-sm">{msg.text}</div>
                  <div
                    className={`text-xs mt-1 text-right ${
                      fromCurrentUser ? "text-blue-100" : "text-gray-500"
                    }`}
                  >
                    {formatTime(msg.timestamp)}
                    {fromCurrentUser && (
                      <span className="ml-1">
                        {msg.seen ? (
                          <span className="text-blue-300">✓✓</span>
                        ) : (
                          <span className="text-gray-400">✓</span>
                        )}
                      </span>
                    )}
                  </div>
                </div>
                {fromCurrentUser && (
                  <img
                    src={avatarUrl}
                    alt="avatar"
                    className="w-7 h-7 rounded-full object-cover ml-2 self-end"
                  />
                )}
              </div>
            );
          })
        )}
        <div ref={messagesEndRef} />
      </div>

      {/* Input */}
      <form
        onSubmit={handleSendMessage}
        className="p-2 border-t border-gray-200 bg-white"
      >
        <div className="flex items-center">
          <button
            type="button"
            className="p-2 text-gray-500 hover:text-gray-700"
          >
            <FiPaperclip size={18} />
          </button>
          <input
            type="text"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            placeholder="Nhắn tin..."
            className="flex-1 border border-gray-300 rounded-full px-3 py-2 text-sm focus:outline-none focus:ring-1 focus:ring-blue-500"
          />
          <button
            type="button"
            className="p-2 text-gray-500 hover:text-gray-700"
          >
            <FiSmile size={18} />
          </button>
          <button
            type="submit"
            className="p-2 text-blue-600 hover:text-blue-800 disabled:text-gray-400"
            disabled={!message.trim()}
          >
            <FiSend size={18} />
          </button>
        </div>
      </form>
    </div>
  );
};

export default ChatBox;
