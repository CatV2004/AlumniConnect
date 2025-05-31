import PostList from "../components/PostList/PostList";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import CreatePostBar from "../components/PostForm";
import { useNavigate } from "react-router-dom";
import { fetchPosts } from "../features/posts/postSlice";
import LeftSidebar from "../components/layout/LeftSidebar";
import RightSidebar from "../components/layout/RightSidebar";

const Home = () => {
  const dispatch = useDispatch();
  const { user } = useSelector((state) => state.auth);
  const { posts, loading, error, hasMore, currentPage } = useSelector(
    (state) => state.posts
  );
  const navigate = useNavigate();

  useEffect(() => {
    if (posts.length === 0) {
      dispatch(fetchPosts({ page: 1, size: 3, refresh: true }));
    }
  }, [dispatch]);
  const handleDeletedPostsClick = () => {
    navigate("/deleted-posts");
  };

  const fetchMoreData = () => {
    if (hasMore) {
      dispatch(fetchPosts({ page: currentPage + 1, size: 3 }));
    }
  };

  return (
    <div className="flex justify-center">
      {/* Left Sidebar */}
      <LeftSidebar />

      {/* Phần nội dung chính */}
      <div className="w-full lg:w-3/5 space-y-6">
        <CreatePostBar user={user} />
        <PostList
          posts={posts}
          loading={loading}
          error={error}
          hasMore={hasMore}
          fetchMoreData={fetchMoreData}
        />
      </div>

      {/* Right Sidebar */}
      <RightSidebar />
    </div>
  );
};

export default Home;
