import React from 'react';
import PostItem from './PostItem';

const dummyPosts = [
  { id: 1, author: 'Nguyễn Văn A', content: 'Chào mọi người, lâu quá mới gặp lại!', timestamp: '2 giờ trước' },
  { id: 2, author: 'Trần Thị B', content: 'Ai còn nhớ lớp K18A không nhỉ?', timestamp: '1 ngày trước' },
];

const PostList = () => {
  return (
    <div className="space-y-4">
      {dummyPosts.map((post) => (
        <PostItem key={post.id} post={post} />
      ))}
    </div>
  );
};

export default PostList;
