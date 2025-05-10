import { Outlet } from "react-router-dom";
import Navbar from "../../components/layout/Navbar";
import ChatContainer from "../../components/Message/ChatContainer";
import { useState } from "react";
import { useSelector } from "react-redux";
import { ChatService } from "../../services/chatService";

const Layout = () => {
  const [activeChats, setActiveChats] = useState([]);
  const [chatMessages, setChatMessages] = useState({});
  const { user } = useSelector((state) => state.auth);

  const handleOpenChat = async (chatInfo) => {
    if (!activeChats.some((c) => c.chatId === chatInfo.chatId)) {
      const newChats = [...activeChats, chatInfo].slice(-2);
      setActiveChats(newChats);

      // Subscribe to chat messages
      const unsubscribe = ChatService.subscribeToMessages(
        chatInfo.chatId,
        (messages) => {
          setChatMessages((prev) => ({
            ...prev,
            [chatInfo.chatId]: messages,
          }));
        }
      );

      // Mark messages as read when opening chat
      await ChatService.markMessagesAsRead(chatInfo.chatId, user.id.toString());

      return unsubscribe;
    }
  };

  const handleCloseChat = (chatId) => {
    setActiveChats(activeChats.filter((c) => c.chatId !== chatId));
    setChatMessages((prev) => {
      const newMessages = { ...prev };
      delete newMessages[chatId];
      return newMessages;
    });
  };

  return (
    <div className="bg-gray-100 min-h-screen pt-20">
      <Navbar onOpenChat={handleOpenChat} />

      {/* Phần main content không có sidebar */}
      <main className="max-w-7xl mx-auto px-4 pt-6">
        <Outlet />
      </main>

      <ChatContainer
        activeChats={activeChats}
        chatMessages={chatMessages}
        currentUser={user}
        onCloseChat={handleCloseChat}
        onSendMessage={async (chatId, message) => {
          return await ChatService.sendMessage(chatId, {
            ...message,
            senderId: user.id.toString(),
          });
        }}
      />
    </div>
  );
};

export default Layout;
