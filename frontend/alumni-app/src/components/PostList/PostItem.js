import React from 'react';

const PostItem = ({ post }) => {
  return (
    <div className="bg-white p-4 rounded shadow">
      <div className="font-semibold text-gray-800">{post.author}</div>
      <div className="text-sm text-gray-500 mb-2">{post.timestamp}</div>
      <p className="text-gray-700">{post.content}</p>
    </div>
  );
};

export default PostItem;
