import moment from "moment";
import 'moment/locale/vi';
import { useState } from "react";
import CommentCreated from "./CommentCreated";
import { useSelector } from "react-redux";
import { Flag, MoreVertical, Trash2 } from "lucide-react";
import axios from "axios";
import { toast } from "react-toastify";


const DropdownItem = ({ icon, children, onClick }) => (
    <button
        onClick={onClick}
        className="flex items-center w-full px-4 py-2 text-sm hover:bg-gray-100 transition-colors duration-150"
    >
        <span className="mr-3 text-gray-500">{icon}</span>
        <span>{children}</span>
    </button>
);

const CommentItem = ({ comment, post, userId, onCommentAdded }) => {
    const [isOpen, setIsOpen] = useState(false);
    const [replyTo, setReplyTo] = useState(null);
    const currentUser = useSelector((state) => state.auth.user);
    const BASE_URL = "http://localhost:8080/AlumniConnect/api"

    console.log(userId);
    console.log("=====> ", currentUser);

    const toggleDropdown = () => {
        setIsOpen(!isOpen);
    };


    const deleteComent = async () => {
        if (window.confirm("Bạn có chắc chắn muốn xóa bài viết này?")) {
            try {

               let res = await axios.delete(`${BASE_URL}/comment/${comment.id}`, {
                headers: {
                    Authorization: localStorage.getItem('token') || null
                }
               });
                if (res.status === 200){
                    toast.success("Xóa bài viết thành công");
                    setIsOpen(false);
                }else 
                toast.error("Lỗi không thể xóa bình luận!!!")
                
            } catch (error) {
                toast.error("Xóa bài viết thất bại: " + error.message);
                setIsOpen(false);
            }
        }
    }


    return (
        <div className="flex">
            <div className="mt-2">
                <img
                    src={comment.userId.avatar || '/default-avatar.png'}
                    alt="avatar"
                    className="w-7 h-7 rounded-full"
                />
            </div>
            <div className="flex-1">
                <div className="inline-block rounded-[20px] bg-gray-100 p-2 pl-4">

                    <p className="font-semibold">{comment.userId.username}</p>
                    <p className="text-sm text-gray-900">{comment.content}</p>


                    {comment.image && (
                        <img src={comment.image} alt="comment" style={{ width: "350px" }} className="mt-2 rounded" />
                    )}
                    <div className="flex items-center justify-content-start gap-3 mt-1 px-1">
                        <div>
                            <p className="text-sm text-gray-800 italic">
                                {Array.isArray(comment.createdDate)
                                    ? moment(comment.createdDate).format('DD-MM-YYYY HH:mm')
                                    : moment(comment.createdDate, 'DD-MM-YYYY HH:mm').fromNow()}
                            </p>
                        </div>
                        <div>
                            <button
                                className="text-sm text-gray-900 "
                                onClick={() => setReplyTo(replyTo === comment.id ? null : comment.id)}
                            ><p className="text-sm text-gray-500 font-bold">Trả lời</p>

                            </button>
                        </div>
                    </div>


                    {replyTo === comment.id && (
                        <div className="mt-2">
                            <CommentCreated post={post} parentComment={comment.id} onCommentAdded={onCommentAdded} />
                        </div>
                    )}
                </div>

            </div>

            {/* Dấu 3 chấm ở góc dưới phải */}
            <div className="relative">
                <div className="absolute bottom-1 left-0">
                    <button
                        onClick={toggleDropdown}
                        className="p-1 rounded-full hover:bg-gray-200"
                    >
                        <MoreVertical className="w-5 h-5 text-gray-500" />
                    </button>

                    {/* Dropdown bên phải dấu 3 chấm */}
                    {isOpen && (
                        <div className="absolute left-10 bottom-2 mb-2 w-48 bg-white rounded-md shadow-lg z-10 border border-gray-200">
                            <div className="py-1">

                                <DropdownItem
                                    // onClick={handleReport}
                                    icon={<Flag className="w-4 h-4" />}
                                >
                                    Báo cáo
                                </DropdownItem>
                                {(currentUser && currentUser.id === userId || comment.userId.id === currentUser.id) && (
                                    <DropdownItem icon={<Trash2 className="w-4 h-4" />} 
                                    onClick={deleteComent}>
                                        Xóa
                                    </DropdownItem>
                                )}
                            </div>
                        </div>
                    )}
                </div>

            </div>
        </div>
    )
}


export default CommentItem;