// import { useState } from "react";
// import { ChatService } from "../services/chatService";

// export const useChatManager = () => {
//   const [chats, setChats] = useState([]);
//   const [unreadCount, setUnreadCount] = useState(0);
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState(null);

//   // Lấy danh sách cuộc trò chuyện
//   const subscribeToChats = (userId) => {
//     return ChatService.subscribeToUserChats(userId, (chats) => {
//       setChats(chats);
//       setLoading(false);

//       // Tính toán số tin nhắn chưa đọc
//       const unread = chats.filter(
//         (chat) =>
//           chat.lastSenderId !== userId &&
//           chat.participantInfo?.[userId]?.hasUnread
//       ).length;

//       setUnreadCount(unread);
//     });
//   };

//   // Lấy tổng số tin nhắn chưa đọc
//   const fetchTotalUnreadCount = async (userId) => {
//     try {
//       const { success, count } = await ChatService.getTotalUnreadCount(userId);
//       if (success) setUnreadCount(count);
//     } catch (err) {
//       console.error("Failed to fetch unread count:", err);
//     }
//   };

//   return {
//     chats,
//     unreadCount,
//     loading,
//     error,
//     subscribeToChats,
//     fetchTotalUnreadCount,
//     sendMessage: ChatService.sendMessage,
//     getOrCreateChat: ChatService.getOrCreateChat,
//     markMessagesAsRead: ChatService.markMessagesAsRead,
//     subscribeToMessages: ChatService.subscribeToMessages,
//   };
// };



import { useState } from "react";
import { ChatService } from "../services/chatService";

const useChat = (currentUser) => {
  const [activeChats, setActiveChats] = useState([]);
  const [chatMessages, setChatMessages] = useState({});

  const handleOpenChat = async (chatInfo) => {
    if (!activeChats.some((c) => c.chatId === chatInfo.chatId)) {
      const newChats = [...activeChats, chatInfo].slice(-2);
      setActiveChats(newChats);

      const unsubscribe = ChatService.subscribeToMessages(
        chatInfo.chatId,
        (messages) => {
          setChatMessages((prev) => ({
            ...prev,
            [chatInfo.chatId]: messages,
          }));
        }
      );

      await ChatService.markMessagesAsRead(
        chatInfo.chatId,
        currentUser.id.toString()
      );

      return unsubscribe;
    }
  };

  const handleCloseChat = (chatId) => {
    setActiveChats((prev) => prev.filter((c) => c.chatId !== chatId));
    setChatMessages((prev) => {
      const newMessages = { ...prev };
      delete newMessages[chatId];
      return newMessages;
    });
  };

  const handleSendMessage = async (chatId, message) => {
    return await ChatService.sendMessage(chatId, {
      ...message,
      senderId: currentUser.id.toString(),
    });
  };

  return {
    activeChats,
    chatMessages,
    handleOpenChat,
    handleCloseChat,
    handleSendMessage,
  };
};

export default useChat;
