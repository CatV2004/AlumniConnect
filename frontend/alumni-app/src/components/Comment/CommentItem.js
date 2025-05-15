import moment from "moment";
import 'moment/locale/vi';
import { useState } from "react";
import CommentCreated from "./CommentCreated";


const CommentItem = ({ comment, postId, onCommentAdded }) => {
    const [replyTo, setReplyTo] = useState(null);
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
                </div>

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
                        <CommentCreated postId={postId} parentComment={comment.id} onCommentAdded={onCommentAdded} />
                    </div>
                )}
            </div>
        </div>
    )
}


export default CommentItem;