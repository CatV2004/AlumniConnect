import { useNavigate } from "react-router-dom";

const LeftSidebar = () => {
  const navigate = useNavigate();

  const handleDeletedPostsClick = () => {
    navigate("/deleted-posts");
  };

  return (
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
  );
};

export default LeftSidebar;
