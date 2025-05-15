// src/pages/DeletedPostsPage.js
import DeletedPostList from "../components/PostList/DeletedPostList";

const DeletedPostsPage = () => {
  return (
    <div className="container mx-auto py-4">
      <h1 className="text-2xl font-semibold mb-4">Các bài viết đã xóa gần đây</h1>
      <DeletedPostList />
    </div>
  );
};

export default DeletedPostsPage;
