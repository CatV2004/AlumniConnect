import { useEffect, useState } from "react";
import CommentCreated from "./CommentCreated";
import axios from "axios";


const CommentList = ({ post }) => {
    const BASE_URL = 'http://localhost:8080/AlumniConnect/api';
    const [comments, setComments] = useState([]);
    const [page, setPage] = useState(0);
    const [size,] = useState(3)
    const [hasMore, setHasMore] = useState(true);

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

    return parents.map(parent => (
        <div>

            <div key={parent.id} className="mb-4 border-b pb-2">
                <div className="flex gap-2 items-start">
                    <img
                        src={parent.userId.avatar}
                        alt="avatar"
                        className="w-8 h-8 rounded-full"
                    />
                    <div className="flex-1">
                        <div className="font-semibold">{parent.userId.username}</div>
                        <div>{parent.content}</div>
                        {parent.image && (
                            <img
                                src={parent.image}
                                alt="comment"
                                className="w-32 mt-1 rounded"
                            />
                        )}
                        <button
                            className="text-blue-500 text-sm"
                            onClick={() => <CommentCreated post={post} parentComment={parent.id} />}>
                            Trả lời
                        </button>
                    </div>
                </div>

                {/* Hiển thị comment con */}
                <div className="ml-6 mt-2">
                    {children
                        .filter(child => child.parentId === parent.id)
                        .map(child => (
                            <div key={child.id} className="mb-2">
                                <img
                                    src={parent.userId.avatar}
                                    alt="avatar"
                                    className="w-8 h-8 rounded-full"
                                />
                                <div className="flex-1">
                                    <div className="font-semibold">{parent.userId.username}</div>
                                    <div>{parent.content}</div>
                                    {parent.image && (
                                        <img
                                            src={parent.image}
                                            alt="comment"
                                            className="w-32 mt-1 rounded"
                                        />
                                    )}
                                    <button
                                        className="text-blue-500 text-sm"
                                        onClick={() => <CommentCreated post={post} parentComment={parent.id} />}>
                                        Trả lời
                                    </button>
                                </div>
                            </div>
                        ))}
                </div>
            </div>
            {hasMore && (
                <button
                    className="mt-4 text-blue-500 hover:underline"
                    onClick={() => {
                        setPage((prev) => prev + 1);
                    }}
                >
                    Xem thêm bình luận
                </button>
            )}
        </div>
    ));

};

export default CommentList;