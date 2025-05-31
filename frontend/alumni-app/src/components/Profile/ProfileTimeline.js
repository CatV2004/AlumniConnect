import React, { useEffect } from 'react';
import { useInView } from 'react-intersection-observer';
import PostList from '../PostList/PostList';

const ProfileTimeline = ({ posts, loadMore, hasMore, loading }) => {
  const { ref, inView } = useInView();

  useEffect(() => {
    if (inView && hasMore && !loading) {
      loadMore();
    }
  }, [inView, hasMore, loadMore, loading]);

  return (
    <div className="max-w-xl mx-auto">
      <PostList 
        posts={posts}
        loading={loading}
        error={null}
        hasMore={hasMore}
        fetchMoreData={loadMore}
        customEmptyMessage={
          <div className="text-center py-8 text-gray-500">
            No posts yet
          </div>
        }
      />
    </div>
  );
};

export default ProfileTimeline;