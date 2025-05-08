import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import ReactionButton from '../Reaction/Reaction';
import Comment from '../Comment/Comment';
import axios from 'axios';
import moment from 'moment';
import 'moment/locale/vi';
import CommentList from '../Comment/CommentList';
import CommentCreated from '../Comment/CommentCreated';


const PostItem = ({ post }) => {

  const [likeCount, setLikeCount] = useState(0);
  const [commentCount, setCommentCount] = useState(0);
  const [showComment, setShowCommnet] = useState(false);
  const BASE_URL = 'http://localhost:8080/AlumniConnect/api';
  moment.locale('vi');

  const loadLike = async () => {
    const response = axios.get(BASE_URL)
  }


  return (
    <div className="p-4 bg-white rounded-xl shadow-md max-w-xl mx-auto mb-4">
      <div className="flex items-center mb-2">
        <Link to={`/profile/${post.userId.id}`} className="flex items-center gap-2 hover:opacity-80">
          <img src={post.userId.avatar || '/default-avatar.png'} alt="avatar" className="w-10 h-10 rounded-full mr-2" />
        </Link>
        <div>
          <Link to={`/profile/${post.userId.id}`} className="flex items-center gap-2 hover:opacity-80">
            <p className="font-semibold">{post.userId.username}</p>
          </Link>
          <Link to={`/post/${post.id}`}>
            <p className="text-sm text-gray-500">{
              Array.isArray(post.createdDate)
                ? moment(post.createdDate).format('DD-MM-YYYY HH:mm')
                : moment(post.createdDate, 'DD-MM-YYYY HH:mm').fromNow()
            }</p>
          </Link>
        </div>
      </div>
      <p className="mb-2">{post.content}</p>
      <div className="grid grid-cols-3 gap-2">
        {post.postImageSet.map((img, index) => (
          <Link to={`/post/${post.id}`} key={index}>
            <img
              src={img.image}
              alt={`img-${index}`}
              className="w-full h-32 object-cover rounded hover:opacity-80 cursor-pointer transition duration-200"
            />
          </Link>
          // <img key={index} src={img} alt={`img-${index}`} className="w-full h-32 object-cover rounded" />
        ))}
      </div>
      <div className=" mt-2 flex justify-between text-sm text-gray-500 mb-1">
        <span>👍 {likeCount} lượt thích</span>
        <span>💬 {commentCount} bình luận</span>
      </div>
      <div className="mt-2 flex justify-around border-t pt-2 text-gray-600 text-sm">
        <ReactionButton />
        <button className="flex items-center gap-1 hover:text-blue-600" onClick={() => { setShowCommnet(true) }}>
          💬 <span className='text-l font-bold drop-shadow-sm'>Bình luận</span>
        </button>
        {/* <Comment post={post.id} /> */}
      </div>
      <div>
        {showComment && <>
          <CommentList post={post.id} />
          <CommentCreated post={post.id} parentComment={null} />
        </>}
      </div>
    </div>
  );
};

export default PostItem;
