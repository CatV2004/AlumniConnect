import React from 'react';
import PostCard from '../PostList/PostList';

const ProfileTimeline = ({ posts }) => {
  if (!posts.length) {
    return <p className="text-center text-gray-500">Chưa có bài viết nào.</p>;
  }

  return (
    <div className="space-y-4">
      {posts.map(post => (
        <PostCard key={post.id} post={post} />
      ))}
    </div>
  );
};

export default ProfileTimeline;
