import { useEffect, useState, useCallback } from "react";
import PostItem from "./PostItem";
import { useDispatch, useSelector } from "react-redux";
import { fetchPosts, createPost } from "../../features/posts/postSlice";
import InfiniteScroll from "react-infinite-scroll-component";
import PostSkeleton from "./PostSkeleton";

const PostList = () => {
  const dispatch = useDispatch();
  const { posts, loading, error, hasMore, currentPage } = useSelector(
    (state) => state.posts
  );

  // Load initial posts
  useEffect(() => {
    if (posts.length === 0) {
      dispatch(fetchPosts({ page: 1, size: 3, refresh: true }));
    }
  }, [dispatch]);

  // Handle infinite scroll
  const fetchMoreData = useCallback(() => {
    if (hasMore) {
      dispatch(fetchPosts({ page: currentPage + 1, size: 10 }));
    }
  }, [dispatch, hasMore, currentPage]);

  if (loading && posts.length === 0) {
    return (
      <div className="max-w-xl mx-auto space-y-4">
        {[...Array(3)].map((_, i) => (
          <PostSkeleton key={i} />
        ))}
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-center text-red-500 p-4">
        Error loading posts: {error}
      </div>
    );
  }

  return (
    <div className=" mx-auto">
      <InfiniteScroll
        dataLength={posts.length}
        next={fetchMoreData}
        hasMore={hasMore}
        loader={
          <div className="space-y-4">
            {[...Array(2)].map((_, i) => (
              <PostSkeleton key={i} />
            ))}
          </div>
        }
        endMessage={
          <p className="text-center text-gray-500 py-4">
            You've seen all posts
          </p>
        }
        scrollThreshold={0.8}
      >
        <div className="space-y-4">
          {posts.map((post) => (
            <PostItem key={post.id} post={post} />
          ))}
        </div>
      </InfiniteScroll>
    </div>
  );
};

export default PostList;
