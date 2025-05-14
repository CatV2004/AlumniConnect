import { useEffect, useState } from "react";
import { getDeletedPosts, forceDeletePost, restorePost } from "../../services/postService";
import PostSkeleton from "../PostList/PostSkeleton";
import { useSelector } from "react-redux";
import PostItemDeleted from "./PostItemDeleted";

const DeletedPostList = () => {
  const { token } = useSelector((state) => state.auth);
  const [deletedPosts, setDeletedPosts] = useState({ content: [], totalItems: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchDeletedPosts = async () => {
      try {
        const data = await getDeletedPosts(token);
        setDeletedPosts(data);
        setLoading(false);
      } catch (err) {
        setError("Error loading deleted posts");
        setLoading(false);
      }
    };

    if (token) {
      fetchDeletedPosts();
    }
  }, [token]);

  const handleForceDelete = async (postId) => {
    try {
      await forceDeletePost(postId, token);
      setDeletedPosts((prev) => ({
        ...prev,
        content: prev.content.filter((post) => post.id !== postId),
        totalItems: prev.totalItems - 1,
      }));
      alert("Post deleted permanently");
    } catch (err) {
      alert("Error deleting post");
    }
  };

  const handleRestorePost = async (postId) => {
    try {
      await restorePost(postId, token);
      setDeletedPosts((prev) => ({
        ...prev,
        content: prev.content.filter((post) => post.id !== postId),
        totalItems: prev.totalItems - 1,
      }));
      alert("Post restored successfully");
    } catch (err) {
      alert("Error restoring post");
    }
  };

  if (loading) {
    return (
      <div className="max-w-3xl mx-auto space-y-6">
        {[...Array(3)].map((_, i) => (
          <PostSkeleton key={i} />
        ))}
      </div>
    );
  }

  if (error) {
    return <div className="text-red-500 text-center">{error}</div>;
  }

  return (
    <div className="max-w-3xl mx-auto space-y-6">
      {deletedPosts.content.length > 0 ? (
        deletedPosts.content.map((post) => (
          <div key={post.id} className="bg-white p-4 rounded-lg shadow-md space-y-4">
            <PostItemDeleted post={post} />
            <div className="flex justify-end space-x-4">
              <button
                onClick={() => handleForceDelete(post.id)}
                className="bg-red-600 text-white py-2 px-6 rounded-lg shadow-md hover:bg-red-700 transition duration-300 ease-in-out"
              >
                Xóa vĩnh viễn
              </button>
              <button
                onClick={() => handleRestorePost(post.id)}
                className="bg-green-600 text-white py-2 px-6 rounded-lg shadow-md hover:bg-green-700 transition duration-300 ease-in-out"
              >
                Khôi phục
              </button>
            </div>
          </div>
        ))
      ) : (
        <p className="text-center text-gray-500">Không có bài viết nào</p>
      )}
    </div>
  );
};

export default DeletedPostList;
