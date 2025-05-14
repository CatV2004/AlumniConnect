import React, { useState, useEffect } from 'react';
import PostItem from '../PostList/PostItem';
import { useInView } from 'react-intersection-observer';

const ProfileTimeline = ({ posts, loadMore, hasMore }) => {
  const [loading, setLoading] = useState(false);
  const { ref, inView } = useInView();

  useEffect(() => {
    if (inView && hasMore && !loading) {
      setLoading(true);
      loadMore().finally(() => setLoading(false));
    }
  }, [inView, hasMore, loadMore, loading]);

  return (
    <div className="space-y-4">
      {posts.map(post => (
        <PostItem key={post.id} post={post} />
      ))}

      {hasMore && (
        <div ref={ref} className="flex justify-center py-4">
          <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-blue-500"></div>
        </div>
      )}
    </div>
  );
};

export default ProfileTimeline;