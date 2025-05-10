import React from 'react';
import ChatBox from './ChatBox';

const ChatContainer = ({ 
  activeChats, 
  chatMessages, 
  currentUser, 
  onCloseChat, 
  onSendMessage 
}) => {
  return (
    <div className="fixed bottom-4 right-4 z-50 flex flex-row-reverse space-x-4 space-x-reverse">
      {activeChats.map((chat, index) => (
        <ChatBox
          key={chat.chatId}
          chat={chat}
          messages={chatMessages[chat.chatId] || []}
          currentUser={currentUser}
          onClose={() => onCloseChat(chat.chatId)}
          onSendMessage={(message) => onSendMessage(chat.chatId, message)}
          style={{
            transform: `translateX(-${index * 310}px)`,
            zIndex: index + 50
          }}
        />
      ))}
    </div>
  );
};

export default ChatContainer;