// import React, { useState } from "react";
// import Navbar from "../components/layout/Navbar";
// import PostList from "../components/PostList/PostList";
// import CreatePostBar from "../components/PostForm";
// import { useSelector } from "react-redux";
// import ChatContainer from "../components/Message/ChatContainer";
// import { ChatService } from "../services/chatService";

// const Home = () => {
//   const { user } = useSelector((state) => state.auth);
//   const [activeChats, setActiveChats] = useState([]);
//   const [chatMessages, setChatMessages] = useState({});

//   const handleOpenChat = async (chatInfo) => {
//     if (!activeChats.some((c) => c.chatId === chatInfo.chatId)) {
//       const newChats = [...activeChats, chatInfo].slice(-4);
//       setActiveChats(newChats);

//       // Subscribe to chat messages
//       const unsubscribe = ChatService.subscribeToMessages(
//         chatInfo.chatId,
//         (messages) => {
//           setChatMessages((prev) => ({
//             ...prev,
//             [chatInfo.chatId]: messages,
//           }));
//         }
//       );

//       // Mark messages as read when opening chat
//       await ChatService.markMessagesAsRead(chatInfo.chatId, user.id.toString());

//       return unsubscribe;
//     }
//   };

//   const handleCloseChat = (chatId) => {
//     setActiveChats(activeChats.filter((c) => c.chatId !== chatId));
//     setChatMessages((prev) => {
//       const newMessages = { ...prev };
//       delete newMessages[chatId];
//       return newMessages;
//     });
//   };
//   return (
//     <div className="bg-gray-100 min-h-screen pt-20">
//       <Navbar onOpenChat={handleOpenChat} />
//       <div className="flex justify-center max-w-7xl mx-auto px-4 pt-6">
//         {/* Left Sidebar */}
//         <aside className="w-1/5 hidden lg:block pr-4">
//           <div className="bg-white rounded-xl shadow p-4">
//             <h2 className="font-semibold text-gray-700 mb-2">Danh mục</h2>
//             <ul className="text-sm text-gray-600 space-y-1">
//               <li>Tin mới</li>
//               <li>Nhóm</li>
//               <li>Khảo sát</li>
//               <li>Sự kiện</li>
//             </ul>
//           </div>
//         </aside>

//         {/* Main content */}
//         <main className="w-full lg:w-3/5 space-y-6">
//           <CreatePostBar user={user} />
//           <PostList />
//         </main>

//         {/* Right Sidebar */}
//         <aside className="w-1/5 hidden lg:block pl-4">
//           <div className="bg-white rounded-xl shadow p-4">
//             <h2 className="font-semibold text-gray-700 mb-2">Gợi ý</h2>
//             <p className="text-sm text-gray-600">Tính năng mới sắp ra mắt...</p>
//           </div>
//         </aside>
//       </div>
//       {/* Chat container */}
//       <ChatContainer
//         activeChats={activeChats}
//         chatMessages={chatMessages}
//         currentUser={user}
//         onCloseChat={handleCloseChat}
//         onSendMessage={async (chatId, message) => {
//           return await ChatService.sendMessage(chatId, {
//             ...message,
//             senderId: user.id.toString(),
//           });
//         }}
//       />
//     </div>
//   );
// };

// export default Home;

import PostList from "../components/PostList/PostList";
import CreatePostBar from "../components/PostForm";
import { useSelector } from "react-redux";

const Home = () => {
  const { user } = useSelector((state) => state.auth);

  return (
    <div className="flex justify-center">
      {/* Left Sidebar - chỉ hiện trong Home */}
      <aside className="w-1/5 hidden lg:block pr-4">
        <div className="bg-white rounded-xl shadow p-4">
          <h2 className="font-semibold text-gray-700 mb-2">Danh mục</h2>
          <ul className="text-sm text-gray-600 space-y-1">
            <li>Tin mới</li>
            <li>Nhóm</li>
            <li>Khảo sát</li>
            <li>Sự kiện</li>
          </ul>
        </div>
      </aside>

      {/* Phần nội dung chính */}
      <div className="w-full lg:w-3/5 space-y-6">
        <CreatePostBar user={user} />
        <PostList />
      </div>

      {/* Right Sidebar - chỉ hiện trong Home */}
      <aside className="w-1/5 hidden lg:block pl-4">
        <div className="bg-white rounded-xl shadow p-4">
          <h2 className="font-semibold text-gray-700 mb-2">Gợi ý</h2>
          <p className="text-sm text-gray-600">Tính năng mới sắp ra mắt...</p>
        </div>
      </aside>
    </div>
  );
};

export default Home;
