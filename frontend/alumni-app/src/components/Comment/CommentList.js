import { useEffect, useState } from "react";
import axios from "axios";
import moment from "moment";
import 'moment/locale/vi';
import CommentItem from "./CommentItem";
import CommentHasmore from "./CommentHasmore";
import CommentCreated from "./CommentCreated";


moment.locale('vi');

const CommentList = ({ post, showComment, setCountComment }) => {
    const BASE_URL = 'http://localhost:8080/AlumniConnect/api';
    const [comments, setComments] = useState([]);
    const [page, setPage] = useState(0);
    const [size,] = useState(5)
    const [hasMore, setHasMore] = useState(true);
    const [repliesMap, setRepliesMap] = useState({});
    const [replyPages, setReplyPages] = useState({});
    const [hasMoreReplies, setHasMoreReplies] = useState({});

    const fetchComments = async () => {
        try {
            const response = await axios.get(`${BASE_URL}/comment/${post.id}/post?page=${page}&size=${size}`, {
                headers: {
                    Authorization: localStorage.getItem('token') || null
                }
            });
            const newComments = response.data.content;
            console.log(newComments)
            setComments([...newComments, ...comments]);
            setHasMore(!response.data.last);
        } catch (error) {
            console.error("Lỗi khi tải bình luận:", error);
        }
    };
    const fetchReplies = async (parentId) => {
        const page = replyPages[parentId] || 0;
        try {
            const res = await axios.get(`${BASE_URL}/comment/${parentId}/replies?page=${page}&size=${size}`, {
                headers: {
                    Authorization: localStorage.getItem('token') || null
                }
            });
            const newReplies = res.data.content;
            setRepliesMap(prev => {
                const existing = prev[parentId] || [];
                const uniqueReplies = newReplies.filter(
                    r => !existing.some(e => e.id === r.id)
                );
                return {
                    ...prev,
                    [parentId]: [...existing, ...uniqueReplies]
                };
            });
            setReplyPages(prev => ({
                ...prev,
                [parentId]: page + 1
            }));
            setHasMoreReplies(prev => ({
                ...prev,
                [parentId]: !res.data.last
            }));
        } catch (err) {
            console.error("Lỗi khi tải replies:", err);
        }
    };

    useEffect(() => {
        fetchComments();
    }, [post.id, page, size]);


    const handleNewComment = (comment) => {
        setCountComment(prev => prev + 1);
        setComments(prev => [comment, ...prev]);
    };

    const handleDeleteComment = (commentId = null) => {
        setComments(prev => prev.filter(c => c.id !== commentId));
        setCountComment(prev => prev - 1);
        if (commentId !== null) {
            setRepliesMap(prev => {
                const updatedMap = { ...prev };
                delete updatedMap[commentId];
                return updatedMap;
            });

        }
    };

    const handleCommentUpdated = (updated, parentComment = null) => {
        setComments(prev =>
            prev.map(c => c.id === updated.id ? updated : c)
        );
        if (updated !== null && parentComment !== null) {
            setRepliesMap(prev => ({
                ...prev,
                [parentComment]: prev[parentComment].map(reply =>
                    reply.id === updated.id ? updated : reply
                )
            }));
        }
    };

    const handleReplies = (comment, parentId) => {
        if (comment !== null) {
            setRepliesMap(prev => ({
                ...prev,
                [parentId]: [comment, ...(prev[parentId] || [])]
            }));
        }
    }

    return (
        <div className=" space-y-2 gap-3">
            {comments.map(comment => (
                <div key={comment.id}>
                    <div className="flex gap-3">
                        <CommentItem userId={post.userId.id} comment={comment} post={post} onCommentAdded={handleReplies}
                            handleCommentUpdated={handleCommentUpdated} showComment={showComment} handleDeleteComment={handleDeleteComment} />
                    </div>

                    {/* Replies */}
                    {repliesMap[comment.id]?.length > 0 && (
                        <div className="mt-4 ml-7 pl-6 space-y-4 border-l border-gray-200">
                            {repliesMap[comment.id].map(reply => (
                                <CommentItem userId={post.userId.id} key={reply.id} parentComment={comment.id} comment={reply} post={post} onCommentAdded={handleReplies}
                                    handleCommentUpdated={handleCommentUpdated} showComment={showComment} handleDeleteComment={handleDeleteComment} />
                            ))}
                        </div>
                    )}

                    {/* Nút "Xem thêm trả lời" */}
                    {hasMoreReplies[comment.id] !== false && (
                        <div className="pl-6 mt-2">
                            <button
                                className="text-sm text-blue-600 font-medium hover:underline"
                                onClick={() => fetchReplies(comment.id)}
                            >
                                Xem thêm trả lời
                            </button>
                        </div>
                    )}
                </div>
            ))}
            {hasMore && (
                <CommentHasmore setPage={setPage} />
            )}

            <CommentCreated
                post={post}
                onCommentAdded={handleNewComment}
            />
        </div>
    );
};

export default CommentList;