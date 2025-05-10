import { useEffect, useState } from "react";
import CommentCreated from "./CommentCreated";
import axios from "axios";
import moment from "moment";
import 'moment/locale/vi';
moment.locale('vi');


const CommentList = ({ post }) => {
    const BASE_URL = 'http://localhost:8080/AlumniConnect/api';
    const [comments, setComments] = useState([]);
    const [page, setPage] = useState(0);
    const [size,] = useState(3)
    const [hasMore, setHasMore] = useState(true);
    const [replyTo, setReplyTo] = useState(null);

    const fetchComments = async () => {
        try {
            const response = await axios.get(`${BASE_URL}/comment/${post}/post?page=${page}&size=${size}`);
            const newComments = response.data.content;
            console.log(newComments)
            setComments([...newComments, ...comments]);
            setHasMore(!response.data.last);
        } catch (error) {
            console.error("Lỗi khi tải bình luận:", error);
        }
    };

    useEffect(() => {
        fetchComments();
    }, [post, page, size]);

    const parents = comments.filter(c => c.parentId === null);
    const children = comments.filter(c => c.parentId !== null);


    return (
        <div className="mt-3 space-y-6 gap-3">
            {parents.map(parent => (
                <div key={parent.id} className="border-b  pb-4">
                    <div className="flex gap-3 p-3">
                        <div className="mt-2">
                            <img
                                src={parent.userId.avatar || '/default-avatar.png'}
                                alt="avatar"
                                className="w-7 h-7 rounded-full"
                            />
                        </div>
                        <div className="flex-1">
                            <div className="inline-block rounded-[20px] bg-gray-100 p-2 pl-4">
                                <p className="font-semibold">{parent.userId.username}</p>
                                <p className="text-sm text-gray-900">{parent.content}</p>
                            </div>

                            {parent.image && (
                                <img src={parent.image} alt="comment" className="w-41 mt-2 rounded" />
                            )}
                            <div className="flex items-center justify-content-start gap-3 mt-1 px-1">
                                <div>
                                    <p className="text-sm text-gray-800 italic">
                                        {Array.isArray(parent.createdDate)
                                            ? moment(parent.createdDate).format('DD-MM-YYYY HH:mm')
                                            : moment(parent.createdDate, 'DD-MM-YYYY HH:mm').fromNow()}
                                    </p>
                                </div>
                                <div>
                                    <button
                                        className="text-sm text-gray-900 "
                                        onClick={() => setReplyTo(replyTo === parent.id ? null : parent.id)}
                                    ><p className="text-sm text-gray-500 font-bold">Trả lời</p>

                                    </button>
                                </div>
                            </div>

                            {replyTo === parent.id && (
                                <div className="mt-2">
                                    <CommentCreated post={post} parentComment={parent.id} />
                                </div>
                            )}

                            {/* Comment con */}
                            <div className="mt-4 pl-6 space-y-4 border-l border-gray-200">
                                {children
                                    .filter(child => child.parentId === parent.id)
                                    .map(child => (
                                        <div key={child.id} className="flex gap-3">
                                            <img
                                                src={child.userId.avatar || '/default-avatar.png'}
                                                alt="avatar"
                                                className="w-8 h-8 rounded-full"
                                            />
                                            <div className="flex-1">
                                                <p className="font-semibold">{child.userId.username}</p>
                                                <p className="text-sm text-gray-700">{child.content}</p>
                                                {child.image && (
                                                    <img src={child.image} alt="comment" className="w-32 mt-2 rounded" />
                                                )}
                                            </div>
                                        </div>
                                    ))}
                            </div>
                        </div>
                    </div>
                </div>
            ))}

            {hasMore && (
                <div className="text-center mt-6">
                    <button
                        className="text-blue-600 font-medium hover:underline"
                        onClick={() => setPage((prev) => prev + 1)}
                    >
                        Xem thêm bình luận
                    </button>
                </div>
            )}
        </div>
    );
};

export default CommentList;