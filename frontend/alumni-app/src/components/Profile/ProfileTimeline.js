import React, { useEffect } from 'react';
import { useInView } from 'react-intersection-observer';
import PostSkeleton from '../PostList/PostSkeleton';
import PostItem from '../PostList/PostItem';

const ProfileTimeline = ({ posts, loadMore, hasMore, loading }) => {
  const { ref, inView } = useInView();

  useEffect(() => {
    if (inView && hasMore && !loading) {
      loadMore();
    }
  }, [inView, hasMore, loadMore, loading]);

  if (loading && posts.length === 0) {
    return (
      <div className="max-w-xl mx-auto space-y-4">
        {[...Array(3)].map((_, i) => <PostSkeleton key={i} />)}
      </div>
    );
  }

  return (
    <div className="max-w-xl mx-auto">
      <div className="space-y-4">
        {posts.map(post => (
          <PostItem key={post.id} post={post} />
        ))}
      </div>

      {loading && posts.length > 0 && (
        <div className="space-y-4">
          {[...Array(2)].map((_, i) => <PostSkeleton key={i} />)}
        </div>
      )}

      {hasMore && !loading && (
        <div ref={ref} className="flex justify-center py-4">
          <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-blue-500"></div>
        </div>
      )}

      {!hasMore && posts.length > 0 && (
        <p className="text-center text-gray-500 py-4">
          You've seen all posts
        </p>
      )}

      {!loading && posts.length === 0 && (
        <div className="text-center py-8 text-gray-500">
          No posts yet
        </div>
      )}
    </div>
  );
};

export default ProfileTimeline;