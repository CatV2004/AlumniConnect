import { useState } from "react";
import CommentList from "./CommentList";
import CommentCreated from "./CommentCreated";


const Comment = ({ post }) => {
    const [showComment, setShowCommnet] = useState(false);
    return (
        <>
            <button className="flex items-center gap-1 hover:text-blue-600" onClick={() => { setShowCommnet(true) }}>
                💬 <span className='text-l font-bold drop-shadow-sm'>Bình luận</span>
            </button>

            {showComment && <>
                <CommentList post={post}/>
                <CommentCreated post={post} parentComment={null}/>
            </>}
        </>

    )
}


export default Comment;