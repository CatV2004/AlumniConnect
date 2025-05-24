import { useState } from "react";
import { MoreVertical, Pencil, Trash2, Bookmark, Flag, Loader2 } from "lucide-react";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "react-toastify";
import { forceDeletePost, softDeletePost } from "../../services/postService";
import { fetchPosts } from "../../features/posts/postSlice";

const PostOptionsDropdown = ({ post, onEdit }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [deleteType, setDeleteType] = useState(null); // 'soft' or 'force'
  
  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.user);
  const token = useSelector((state) => state.auth.token);

  const toggleDropdown = () => setIsOpen(!isOpen);

  const handleEdit = () => {
    onEdit();
    setIsOpen(false);
  };

  const openDeleteModal = (type) => {
    setDeleteType(type);
    setShowDeleteModal(true);
    setIsOpen(false);
  };

  const handleDelete = async () => {
    setIsDeleting(true);
    try {
      if (deleteType === 'force') {
        await forceDeletePost(post.id, token);
        toast.success("Bài viết đã được xóa vĩnh viễn");
      } else {
        await softDeletePost(post.id, token);
        toast.success("Bài viết đã được xóa");
      }
      
      dispatch(fetchPosts({ page: 1, size: 3, refresh: true }));
    } catch (error) {
      toast.error(`Xóa bài viết thất bại: ${error.response?.data?.message || error.message}`);
    } finally {
      setIsDeleting(false);
      setShowDeleteModal(false);
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

  const isAdmin = currentUser?.role === "ADMIN";
  const isOwner = currentUser?.id === post.userId.id;
  const canForceDelete = isAdmin && !isOwner;

  return (
    <div className="relative">
      <button
        onClick={toggleDropdown}
        className="p-1 rounded-full hover:bg-gray-100 transition-colors"
        aria-label="Tùy chọn bài viết"
      >
        <MoreVertical className="w-5 h-5 text-gray-500" />
      </button>

      {isOpen && (
        <div className="absolute right-0 mt-2 w-56 bg-white rounded-md shadow-lg z-20 border border-gray-200 divide-y divide-gray-100">
          <div className="py-1">
            {isOwner && (
              <DropdownItem
                onClick={handleEdit}
                icon={<Pencil className="w-4 h-4" />}
              >
                Chỉnh sửa bài viết
              </DropdownItem>
            )}

            {(isOwner || isAdmin) && (
              <DropdownItem
                onClick={() => openDeleteModal(isOwner ? 'soft' : 'force')}
                icon={<Trash2 className="w-4 h-4" />}
                className="text-red-600 hover:bg-red-50"
              >
                {isOwner ? "Xóa bài viết" : "Xóa vĩnh viễn"}
              </DropdownItem>
            )}

            <DropdownItem
              onClick={handleSave}
              icon={<Bookmark className="w-4 h-4" />}
            >
              Lưu bài viết
            </DropdownItem>
            
            {!isOwner && (
              <DropdownItem
                onClick={handleReport}
                icon={<Flag className="w-4 h-4" />}
              >
                Báo cáo bài viết
              </DropdownItem>
            )}
          </div>
        </div>
      )}

      {/* Delete Confirmation Modal */}
      {showDeleteModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 max-w-md w-full mx-4">
            <h3 className="text-lg font-semibold mb-4">
              {deleteType === 'force' ? 'Xóa vĩnh viễn bài viết' : 'Xóa bài viết'}
            </h3>
            
            <p className="text-gray-600 mb-6">
              {deleteType === 'force'
                ? 'Bạn có chắc muốn xóa vĩnh viễn bài viết này? Hành động này không thể hoàn tác.'
                : 'Bài viết sẽ được ẩn khỏi hệ thống nhưng có thể khôi phục sau.'}
            </p>
            
            <div className="flex justify-end space-x-3">
              <button
                onClick={() => setShowDeleteModal(false)}
                disabled={isDeleting}
                className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 transition-colors disabled:opacity-50"
              >
                Hủy bỏ
              </button>
              
              <button
                onClick={handleDelete}
                disabled={isDeleting}
                className={`px-4 py-2 rounded-md text-white transition-colors disabled:opacity-50 ${
                  deleteType === 'force' 
                    ? 'bg-red-600 hover:bg-red-700' 
                    : 'bg-blue-600 hover:bg-blue-700'
                }`}
              >
                {isDeleting ? (
                  <span className="flex items-center">
                    <Loader2 className="animate-spin mr-2 h-4 w-4" />
                    Đang xóa...
                  </span>
                ) : (
                  deleteType === 'force' ? 'Xóa vĩnh viễn' : 'Xóa bài viết'
                )}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

const DropdownItem = ({ icon, children, onClick, className = "" }) => (
  <button
    onClick={onClick}
    className={`flex items-center w-full px-4 py-2.5 text-sm hover:bg-gray-50 transition-colors duration-150 ${className}`}
  >
    <span className="mr-3">{icon}</span>
    <span className="text-left">{children}</span>
  </button>
);

export default PostOptionsDropdown;