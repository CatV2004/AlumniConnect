import PostList from "../components/PostList/PostList";
import CreatePostBar from "../components/PostForm";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

const Home = () => {
  const { user } = useSelector((state) => state.auth);
  const navigate = useNavigate();

  const handleDeletedPostsClick = () => {
    navigate("/deleted-posts");
  };

  return (
    <div className="flex justify-center">
      {/* Left Sidebar - chỉ hiện trong Home */}
      <aside className="w-1/5 hidden lg:block pr-4">
        <div className="bg-white rounded-xl shadow p-4">
          <h2 className="font-semibold text-gray-700 mb-2">Danh mục</h2>
          <ul className="text-sm text-gray-600 space-y-1">
            <li>Tin mới</li>
            <li>Nhóm</li>
            <li>Khảo sát</li>
            <li>Sự kiện</li>
            <li
              onClick={handleDeletedPostsClick}
              className="cursor-pointer hover:text-blue-500"
            >
              Bài viết đã xóa
            </li>
          </ul>
        </div>
      </aside>

      {/* Phần nội dung chính */}
      <div className="w-full lg:w-3/5 space-y-6">
        <CreatePostBar user={user} />
        <PostList />
      </div>

      {/* Right Sidebar - chỉ hiện trong Home */}
      <aside className="w-1/5 hidden lg:block pl-4">
        <div className="bg-white rounded-xl shadow p-4">
          <h2 className="font-semibold text-gray-700 mb-2">Gợi ý</h2>
          <p className="text-sm text-gray-600">Tính năng mới sắp ra mắt...</p>
        </div>
      </aside>
    </div>
  );
};

export default Home;
