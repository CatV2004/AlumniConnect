import React, { useEffect, useState, useCallback, useRef } from "react";
import { useSelector, useDispatch } from "react-redux";
import { Link } from "react-router-dom";
import { ChatService } from "../../services/chatService";
import {
  fetchAllUsers,
  fetchUserChats,
  markChatAsRead,
  setSearchTerm,
  resetPagination,
} from "../../features/chat/chatSlice";
import { debounce } from "../../app/utils/debounceUtils";

const MessengerDropdown = ({ currentUser, onClose, onOpenChat }) => {
  const dispatch = useDispatch();
  const token = useSelector((state) => state.auth.token);
  const {
    users,
    chats,
    loading,
    loadingMore,
    pagination,
    searchTerm: reduxSearchTerm,
  } = useSelector((state) => state.chat);

  const [localSearchTerm, setLocalSearchTerm] = useState("");
  const [unreadCount, setUnreadCount] = useState(0);
  const dropdownRef = useRef(null);

  // Debounce search term
  const debouncedSearch = useCallback(
    debounce((term) => {
      dispatch(setSearchTerm(term));
      dispatch(resetPagination());
      dispatch(
        fetchAllUsers({
          token,
          page: 1,
          keyword: term,
        })
      );
    }, 300),
    [dispatch, token]
  );

  // Load initial data
  useEffect(() => {
    if (currentUser?.id) {
      dispatch(fetchUserChats(currentUser.id.toString()));
    }

    dispatch(
      fetchAllUsers({
        token,
        page: 1,
      })
    );
  }, [currentUser, dispatch, token]);

  // Calculate unread count
  useEffect(() => {
    if (chats && currentUser?.id) {
      const count = chats.reduce((total, chat) => {
        return (
          total +
          (chat.participantInfo?.[currentUser.id.toString()]?.hasUnread ? 1 : 0)
        );
      }, 0);
      setUnreadCount(count);
    }
  }, [chats, currentUser?.id]);

  const handleSearchChange = (e) => {
    const term = e.target.value;
    setLocalSearchTerm(term);
    debouncedSearch(term);
  };

  const handleScroll = useCallback(() => {
    if (!dropdownRef.current || loading || loadingMore || !pagination.hasMore)
      return;

    const { scrollTop, scrollHeight, clientHeight } = dropdownRef.current;
    const isNearBottom = scrollHeight - (scrollTop + clientHeight) < 100;

    if (isNearBottom) {
      dispatch(
        fetchAllUsers({
          token,
          page: pagination.page + 1,
          keyword: reduxSearchTerm,
        })
      );
    }
  }, [loading, loadingMore, pagination, reduxSearchTerm, dispatch, token]);

  // Combine user and chat data
  const getCombinedData = useCallback(() => {
    const combined = [];

    // Add existing chats
    chats.forEach((chat) => {
      const otherParticipantId = chat.participants.find(
        (id) => id !== currentUser.id.toString()
      );

      const user = users.find((u) => u.id.toString() === otherParticipantId);

      if (user) {
        const firstName = user.firstName;
        const lastName = user.lastName;
        const fullName =
          firstName!=null && lastName!=null ? `${firstName} ${lastName}` : "ADMIN";
        combined.push({
          type: "chat",
          id: chat.id,
          userId: user.id,
          name: fullName,
          avatar: user.avatar,
          lastMessage: chat.lastMessage,
          lastMessageTime: chat.lastMessageTime,
          hasUnread:
            chat.participantInfo?.[currentUser.id.toString()]?.hasUnread,
          isOnline: false,
        });
      }
    });

    // Add users without chats (when not searching)
    if (!reduxSearchTerm) {
      users.forEach((user) => {
        if (user.id.toString() === currentUser.id.toString()) return;

        const hasChat = chats.some((chat) =>
          chat.participants.includes(user.id.toString())
        );

        if (!hasChat) {
          combined.push({
            type: "user",
            id: `user_${user.id}`,
            userId: user.id,
            name: `${user.firstName} ${user.lastName}`,
            avatar: user.avatar,
            lastMessage: null,
            lastMessageTime: null,
            hasUnread: false,
            isOnline: false,
          });
        }
      });
    }

    // Sort by last message time or name
    return combined.sort((a, b) => {
      if (a.lastMessageTime && b.lastMessageTime) {
        return b.lastMessageTime - a.lastMessageTime;
      }
      if (a.lastMessageTime) return -1;
      if (b.lastMessageTime) return 1;
      return a.name.localeCompare(b.name);
    });
  }, [chats, users, currentUser.id, reduxSearchTerm]);

  const filteredItems = getCombinedData().filter(
    (item) =>
      item.name.toLowerCase().includes(reduxSearchTerm.toLowerCase()) ||
      (item.lastMessage &&
        item.lastMessage.toLowerCase().includes(reduxSearchTerm.toLowerCase()))
  );

  const handleChatClick = async (item) => {
    if (item.type === "user") {
      const result = await ChatService.getOrCreateChat(
        currentUser.id.toString(),
        item.userId.toString()
      );

      if (result.success) {
        onOpenChat({
          chatId: result.chatId,
          participant: {
            id: item.userId,
            name: item.name,
            avatar: item.avatar,
          },
        });
      }
    } else {
      const user = users.find(
        (u) => u.id.toString() === item.userId.toString()
      );

      if (user) {
        onOpenChat({
          chatId: item.id,
          participant: {
            id: item.userId,
            name: item.name,
            avatar: item.avatar,
          },
        });

        if (item.hasUnread) {
          await ChatService.markMessagesAsRead(
            item.id,
            currentUser.id.toString()
          );
          dispatch(
            markChatAsRead({
              chatId: item.id,
              userId: currentUser.id.toString(),
            })
          );
        }
      }
    }

    onClose();
  };

  const formatTime = (timestamp) => {
    if (!timestamp) return "";

    const date = new Date(timestamp);
    const now = new Date();

    if (date.toDateString() === now.toDateString()) {
      return date.toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      });
    }

    if (date.getFullYear() === now.getFullYear()) {
      return date.toLocaleDateString([], { month: "short", day: "numeric" });
    }

    return date.toLocaleDateString([], {
      year: "numeric",
      month: "short",
      day: "numeric",
    });
  };

  return (
    <div className="absolute right-0 mt-2 w-96 bg-white shadow-xl rounded-lg z-50 border border-gray-200 overflow-hidden">
      {/* Header */}
      <div className="p-4 border-b border-gray-200 bg-gray-50">
        <div className="flex justify-between items-center mb-3">
          <h3 className="text-lg font-bold text-gray-800">
            Chat{" "}
            {unreadCount > 0 && (
              <span className="ml-2 bg-blue-500 text-white text-xs px-2 py-1 rounded-full">
                {unreadCount}
              </span>
            )}
          </h3>
          <div className="flex space-x-2">
            <button className="p-1 rounded-full hover:bg-gray-200">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="h-5 w-5 text-gray-600"
                viewBox="0 0 20 20"
                fill="currentColor"
              >
                <path d="M8 9a3 3 0 100-6 3 3 0 000 6zM8 11a6 6 0 016 6H2a6 6 0 016-6z" />
              </svg>
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

        {/* Search Bar */}
        <div className="relative">
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-5 w-5 text-gray-400"
              viewBox="0 0 20 20"
              fill="currentColor"
            >
              <path
                fillRule="evenodd"
                d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
                clipRule="evenodd"
              />
            </svg>
          </div>
          <input
            type="text"
            placeholder="Tìm kiếm trên Messenger"
            className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-full bg-white shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            value={localSearchTerm}
            onChange={handleSearchChange}
          />
        </div>
      </div>

      {/* Message List with Infinite Scroll */}
      <div
        className="max-h-96 overflow-y-auto"
        ref={dropdownRef}
        onScroll={handleScroll}
      >
        {loading ? (
          <div className="flex justify-center items-center h-32">
            <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-blue-500"></div>
          </div>
        ) : filteredItems.length > 0 ? (
          <>
            {filteredItems.map((item) => (
              <div
                key={item.id}
                className="flex items-center p-3 hover:bg-gray-50 border-b border-gray-100 transition-colors duration-150 cursor-pointer"
                onClick={() => handleChatClick(item)}
              >
                <div className="relative">
                  <img
                    src={item.avatar || "/default-avatar.png"}
                    alt={item.name}
                    className="w-12 h-12 rounded-full mr-3 object-cover"
                  />
                  {item.isOnline && (
                    <div className="absolute bottom-0 right-2 w-3 h-3 bg-green-500 rounded-full border-2 border-white"></div>
                  )}
                </div>
                <div className="flex-1 min-w-0">
                  <div className="flex justify-between items-baseline">
                    <h4 className="font-semibold text-gray-800 truncate">
                      {item.name}
                    </h4>
                    {item.lastMessageTime && (
                      <span className="text-xs text-gray-400 ml-2 whitespace-nowrap">
                        {formatTime(item.lastMessageTime)}
                      </span>
                    )}
                  </div>
                  <div className="flex items-center">
                    {item.hasUnread ? (
                      <div className="h-2 w-2 rounded-full bg-blue-500 mr-1"></div>
                    ) : item.lastMessage ? (
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        className="h-4 w-4 text-gray-400 mr-1"
                        viewBox="0 0 20 20"
                        fill="currentColor"
                      >
                        <path
                          fillRule="evenodd"
                          d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                          clipRule="evenodd"
                        />
                      </svg>
                    ) : null}
                    <p className="text-sm text-gray-500 truncate">
                      {item.lastMessage || "Bắt đầu cuộc trò chuyện"}
                    </p>
                  </div>
                </div>
              </div>
            ))}

            {/* Loading indicator khi đang load thêm */}
            {loadingMore && (
              <div className="flex justify-center items-center py-4">
                <div className="flex items-center space-x-2">
                  <div
                    className="w-3 h-3 bg-blue-500 rounded-full animate-bounce"
                    style={{ animationDelay: "0ms" }}
                  ></div>
                  <div
                    className="w-3 h-3 bg-blue-500 rounded-full animate-bounce"
                    style={{ animationDelay: "150ms" }}
                  ></div>
                  <div
                    className="w-3 h-3 bg-blue-500 rounded-full animate-bounce"
                    style={{ animationDelay: "300ms" }}
                  ></div>
                </div>
              </div>
            )}

            {/* Thông báo khi đã load hết */}
            {!loadingMore &&
              !pagination.hasMore &&
              filteredItems.length > 0 && (
                <div className="text-center py-3 text-sm text-gray-500">
                  Đã hiển thị tất cả người dùng
                </div>
              )}
          </>
        ) : (
          <div className="p-4 text-center text-gray-500">
            {reduxSearchTerm
              ? "Không tìm thấy kết quả phù hợp"
              : "Không có tin nhắn"}
          </div>
        )}
      </div>

      {/* Footer */}
      <div className="p-3 border-t border-gray-200 bg-gray-50 text-center">
        <Link
          to="/messages"
          className="inline-flex items-center text-blue-500 text-sm font-medium"
          onClick={onClose}
        >
          <span>Xem tất cả trong Messenger</span>
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

export default MessengerDropdown;
