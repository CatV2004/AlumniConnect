import React, { useEffect, useState } from "react";
import { fetchExpiredSurveyPosts } from "../services/surveyPostService";
import PostList from "../components/PostList/PostList";
import { useSelector } from "react-redux";

const ExpiredSurveyPostsPage = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(1);
  const [size] = useState(3);
  const [total, setTotal] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const token = useSelector((state) => state.auth.token)

  const fetchPosts = async (pageNum) => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetchExpiredSurveyPosts(
        { page: pageNum, size },
        token
      );
      
      setPosts(prev => pageNum === 1 ? response.data : [...prev, ...response.data]);
      setTotal(response.total);
      setHasMore(pageNum * size < response.total);
    } catch (err) {
      setError(err.message || "Lỗi khi tải dữ liệu khảo sát hết hạn");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Load initial data
  useEffect(() => {
    fetchPosts(1);
  }, []);

  // Handle infinite scroll
  const fetchMoreData = () => {
    if (!loading && hasMore) {
      const nextPage = page + 1;
      setPage(nextPage);
      fetchPosts(nextPage);
    }
  };

  // Handle manual pagination (optional)
  const handlePageChange = (newPage) => {
    if (newPage !== page) {
      setPage(newPage);
      fetchPosts(newPage);
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-semibold mb-4">Danh sách khảo sát đã hết hạn</h1>

      <PostList
        posts={posts}
        loading={loading && page === 1}
        error={error}
        hasMore={hasMore}
        fetchMoreData={fetchMoreData}
        customEmptyMessage={
          <p className="text-center py-8 text-gray-500">
            Không có khảo sát hết hạn nào
          </p>
        }
      />

      {/* Optional: Traditional pagination */}
      {total > size && (
        <div className="flex justify-center space-x-2 mt-4">
          <button
            disabled={page === 1 || loading}
            onClick={() => handlePageChange(page - 1)}
            className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
          >
            Trước
          </button>
          <span className="px-3 py-1">
            {page} / {Math.ceil(total / size)}
          </span>
          <button
            disabled={page === Math.ceil(total / size) || loading}
            onClick={() => handlePageChange(page + 1)}
            className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
          >
            Tiếp
          </button>
        </div>
      )}
    </div>
  );
};

export default ExpiredSurveyPostsPage;