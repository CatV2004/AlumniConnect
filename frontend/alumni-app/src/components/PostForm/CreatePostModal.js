import React, { useState } from 'react';

const CreatePostModal = ({ onClose, user }) => {
  const [content, setContent] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!content.trim()) return;
    console.log('Post:', content);
    setContent('');
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-xl w-full max-w-md p-4 shadow-xl relative">
        <button
          onClick={onClose}
          className="absolute top-2 right-3 text-gray-500 hover:text-black"
        >
          ✖
        </button>
        <h2 className="text-lg font-semibold text-center mb-4">Tạo bài viết</h2>
        <div className="flex items-center space-x-3 mb-3">
          <img
            src={user?.avatar || '/default-avatar.png'}
            alt="avatar"
            className="w-10 h-10 rounded-full"
          />
          <span className="font-medium">{user?.lastName || 'Bạn'}</span>
        </div>
        <form onSubmit={handleSubmit}>
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            rows="5"
            placeholder="Bạn đang nghĩ gì?"
            className="w-full border border-gray-300 rounded p-2 resize-none"
          />
          <button
            type="submit"
            className="bg-blue-600 text-white mt-3 py-2 px-4 rounded hover:bg-blue-700 w-full"
          >
            Đăng
          </button>
        </form>
      </div>
    </div>
  );
};

export default CreatePostModal;
