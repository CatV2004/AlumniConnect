import { useState } from "react";
import { MoreVertical, Pencil, Trash2, Bookmark, Flag } from "lucide-react";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "react-toastify";
import { softDeletePost } from "../../services/postService"; // Import softDeletePost function
import { fetchPosts } from "../../features/posts/postSlice";

const PostOptionsDropdown = ({ post, onEdit }) => {
  const [isOpen, setIsOpen] = useState(false);
  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.user);
  const token = useSelector((state) => state.auth.token); // Token for authorization

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const handleEdit = () => {
    onEdit();
    setIsOpen(false);
  };

  const handleDelete = async () => {
    if (window.confirm("Bạn có chắc chắn muốn xóa bài viết này?")) {
      try {
        await softDeletePost(post.id, token);
        dispatch(fetchPosts({ page: 1, size: 3, refresh: true }));
        toast.success("Xóa bài viết thành công");
        setIsOpen(false);
      } catch (error) {
        toast.error("Xóa bài viết thất bại: " + error.message);
        setIsOpen(false);
      }
    }
  };

  const handleSave = () => {
    // TODO: Implement save functionality
    setIsOpen(false);
  };

  const handleReport = () => {
    // TODO: Implement report functionality
    setIsOpen(false);
  };

  return (
    <div className="relative">
      <button
        onClick={toggleDropdown}
        className="p-1 rounded-full hover:bg-gray-100"
      >
        <MoreVertical className="w-5 h-5 text-gray-500" />
      </button>

      {isOpen && (
        <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg z-10 border border-gray-200">
          <div className="py-1">
            {/* Chỉ hiển thị nút Edit nếu là post của người dùng hiện tại */}
            {currentUser && currentUser.id === post.userId.id && (
              <DropdownItem
                onClick={handleEdit}
                icon={<Pencil className="w-4 h-4" />}
              >
                Chỉnh sửa
              </DropdownItem>
            )}

            {/* Chỉ hiển thị nút Delete nếu là post của người dùng hiện tại */}
            {currentUser && currentUser.id === post.userId.id && (
              <DropdownItem
                onClick={handleDelete}
                icon={<Trash2 className="w-4 h-4" />}
              >
                Xóa
              </DropdownItem>
            )}

            <DropdownItem
              onClick={handleSave}
              icon={<Bookmark className="w-4 h-4" />}
            >
              Lưu bài viết
            </DropdownItem>
            <DropdownItem
              onClick={handleReport}
              icon={<Flag className="w-4 h-4" />}
            >
              Báo cáo
            </DropdownItem>
          </div>
        </div>
      )}
    </div>
  );
};

const DropdownItem = ({ icon, children, onClick }) => (
  <button
    onClick={onClick}
    className="flex items-center w-full px-4 py-2 text-sm hover:bg-gray-100 transition-colors duration-150"
  >
    <span className="mr-3 text-gray-500">{icon}</span>
    <span>{children}</span>
  </button>
);

export default PostOptionsDropdown;
