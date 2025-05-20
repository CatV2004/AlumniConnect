import moment from "moment";
import 'moment/locale/vi';
import { useEffect, useRef, useState } from "react";
import CommentCreated from "./CommentCreated";
import { useSelector } from "react-redux";
import { Flag, MoreVertical, Trash2 } from "lucide-react";
import axios from "axios";
import { toast } from "react-toastify";
import { FaImage, FaPaperPlane, FaSpinner } from "react-icons/fa";


const DropdownItem = ({ icon, children, onClick }) => (
    <button
        onClick={onClick}
        className="flex items-center w-full px-4 py-2 text-sm hover:bg-gray-100 transition-colors duration-150"
    >
        <span className="mr-3 text-gray-500">{icon}</span>
        <span>{children}</span>
    </button>
);

const CommentItem = ({ comment, post, userId, onCommentAdded, handleCommentUpdated, showComment }) => {
    const [isOpen, setIsOpen] = useState(false);
    const [replyTo, setReplyTo] = useState(null);
    const [content, setContent] = useState(comment.content);
    const [file, setFile] = useState(null);
    const [update, setUpdate] = useState(false);
    const dropdownRef = useRef(null);
    const currentUser = useSelector((state) => state.auth.user);
    const [openComment, setOpenComment] = useState(true);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const formRef = useRef(null);
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
                if (res.status === 200) {
                    toast.success("Xóa bình luận thành công");
                    setIsOpen(false);
                    setOpenComment(false);
                } else
                    toast.error("Lỗi không thể xóa bình luận!!!")

            } catch (error) {
                toast.error("Xóa bình luận thất bại: " + error.message);
                setIsOpen(false);
            }
        }
    }

    const updateComment = async () => {
        try {
            setIsSubmitting(true);
            const formData = new FormData();
            formData.append("content", content);
            if (file) formData.append("file", file);

            let res = await axios.patch(`${BASE_URL}/comment/${comment.id}`, formData, {
                headers: {
                    Authorization: localStorage.getItem('token') || null
                }
            });
            if (res.status === 200) {
                toast.success("Sửa bình luận thành công");
                setUpdate(false);
                setIsOpen(false);
                setOpenComment(true);
                handleCommentUpdated(res.data);
            } else if (res.status > 500)
                toast.error("Lỗi server không thẻ sửa bình luận!!!")
            else {
                toast.warning("Không có quyền sửa bình luận!!!")
            }
        } catch (error) {
            toast.error("Xóa bài viết thất bại: " + error.message);
            setIsOpen(false);
        } finally {
            setIsSubmitting(false);

        }
    }

    useEffect(() => {
        const handleClickOutside = (e) =>
         dropdownRef.current && !dropdownRef.current.contains(e.target) && setIsOpen(false);
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);


    useEffect(() => {
        if (!update) return;

        const handleClickOutside = (e) => {
            if (formRef.current && !formRef.current.contains(e.target)) {
                setUpdate(false);
                setContent(comment.content);
                setFile(null);
                showComment(false);
            }
        };

        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, [update, comment]);

    return (
        <div className="flex">
            {openComment && (
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
                                        {currentUser && currentUser.id === comment.userId.id && (
                                            <DropdownItem
                                                onClick={() => {
                                                    setUpdate(true);
                                                    setIsOpen(false);
                                                    setOpenComment(false);
                                                }}
                                                icon={<Flag className="w-4 h-4" />}
                                            >
                                                Sửa bình luận
                                            </DropdownItem>
                                        )}
                                        {currentUser && (currentUser.id === userId || comment.userId.id === currentUser.id) && (
                                            <DropdownItem icon={<Trash2 className="w-4 h-4" />}
                                                onClick={deleteComent}>
                                                Xóa bình luận
                                            </DropdownItem>
                                        )}
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            )}

            <div onDoubleClick={() => setUpdate(true)}>
          {/* nội dung comment */}
        </div>

            {update && (
                <form onSubmit={(e) => {
                    e.preventDefault();
                    updateComment();
                }}
                    ref={formRef}
                    className="flex items-start gap-4 p-2 border rounded-lg" >
                    <img
                        src={comment.userId.avatar || '/default-avatar.png'}
                        alt="Avatar"
                        className="w-10 h-10 rounded-full object-cover"
                    />
                    {/* Row: input + send */}
                    <div className="flex-1 flex flex-col gap-2">
                        <div className="flex items-center gap-2">
                            <input
                                type="text"
                                className="flex-1 w-full border border-gray-300 rounded-full px-4 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
                                placeholder="Viết bình luận..."
                                value={content}
                                onChange={e => setContent(e.target.value)}
                                disabled={isSubmitting}
                            />
                            <button
                                type="submit"
                                className={`p-2 bg-blue-500 text-white rounded-full hover:bg-blue-600 transition
                                                    ${isSubmitting
                                        ? 'bg-gray-300 cursor-not-allowed'
                                        : 'bg-blue-500 hover:bg-blue-600 text-white'}`}
                            >
                                {isSubmitting
                                    ? <FaSpinner className="w-4 h-4 animate-spin text-gray-600" />
                                    : <FaPaperPlane className="w-4 h-4 text-white" />
                                }
                            </button>
                        </div>
                        {/* Row: image picker */}
                        <label className="flex items-center text-gray-500 hover:text-gray-700 cursor-pointer">
                            <FaImage className="w-5 h-5 mr-1 text-blue" />
                            <span className="text-sm">Chọn hình</span>
                            <input
                                type="file"
                                accept="image/*"
                                className="hidden"
                                onChange={e => setFile(e.target.files[0])}
                                disabled={isSubmitting}
                            />
                        </label>
                        {file !== null ? <>
                            <img
                                src={URL.createObjectURL(file)}
                                alt="preview"
                                style={{ width: "350px" }}
                                className="mt-2 rounded"
                            />
                        </> : <>
                            {comment.image && (
                                <img
                                    src={comment.image}
                                    alt="comment"
                                    style={{ width: "350px" }}
                                    className="mt-2 rounded" />
                            )}

                        </>}
                    </div>
                </form>

            )}

        </div >
    )
}


export default CommentItem;