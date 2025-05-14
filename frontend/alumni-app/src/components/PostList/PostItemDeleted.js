import { useState } from "react";
import { Link } from "react-router-dom";
import CommentList from "../Comment/CommentList";
import CommentCreated from "../Comment/CommentCreated";
import moment from "moment";
import "moment/locale/vi";
import { FaThumbsUp, FaRegCommentDots, FaShare } from "react-icons/fa";
import { ImSpinner8 } from "react-icons/im";

import PostOptionsDropdown from "./PostOptionsDropdown";
import PostImagesGallery from "./PostImagesGallery";
import SurveyPost from "./SurveyPost";
import InvitationPost from "./InvitationPost";
import { formatDate } from "../../app/utils/dateUtils";
import PostForm from "../PostForm/PostForm";

moment.locale("vi");

const PostItem = ({ post }) => {
  const [showComment, setShowComment] = useState(false);
  const [likeCount, setLikeCount] = useState(post.likeCount || 0);
  const [commentCount, setCommentCount] = useState(post.commentCount || 0);
  const [isLiked, setIsLiked] = useState(false);
  const [isExpanded, setIsExpanded] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);

  const MAX_CONTENT_LENGTH = 300;

  const toggleComments = () => {
    setShowComment(!showComment);
  };

  const toggleLike = () => {
    setIsLiked(!isLiked);
    setLikeCount(isLiked ? likeCount - 1 : likeCount + 1);
    // TODO: Call API to update like status
  };

  const handleEditClick = () => {
    setIsEditModalOpen(true);
  };

  const renderContent = () => {
    if (!post.content) return null;

    if (post.content.length > MAX_CONTENT_LENGTH && !isExpanded) {
      return (
        <>
          <p className="mb-2 whitespace-pre-line">
            {post.content.substring(0, MAX_CONTENT_LENGTH)}...
          </p>
          <button
            onClick={() => setIsExpanded(true)}
            className="text-blue-500 text-sm hover:underline"
          >
            See more
          </button>
        </>
      );
    }
    return <p className="mb-2 whitespace-pre-line">{post.content}</p>;
  };

  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden mb-4">
      {/* Post Header */}
      <div className="p-4">
        <div className="flex justify-between items-start">
          <div className="flex items-center">
            <Link
              to={`/profile/${post.userId.id}`}
              className="flex items-center gap-2 hover:opacity-80"
            >
              <img
                src={post.userId.avatar || "/default-avatar.png"}
                alt="avatar"
                className="w-10 h-10 rounded-full object-cover"
              />
            </Link>
            <div className="ml-2">
              <Link
                to={`/profile/${post.userId.id}`}
                className="flex items-center gap-2 hover:opacity-80"
              >
                <p className="font-semibold">{post.userId.username}</p>
              </Link>
              <Link to={`/post/${post.id}`}>
                <p className="text-xs text-gray-500">
                  {formatDate(post.createdDate)}
                </p>
              </Link>
            </div>
          </div>
        </div>

        {/* Post Content */}
        <div className="mt-3">{renderContent()}</div>
      </div>

      {/* Post Media */}
      {post.postImages && post.postImages.length > 0 && (
        <PostImagesGallery images={post.postImages} />
      )}


      {/* Post Stats */}
      <div className="px-4 pt-2 pb-1 border-t border-gray-100">
        <div className="flex justify-between text-sm text-gray-500">
          <span>{likeCount} likes</span>
          <span>{commentCount} comments</span>
        </div>
      </div>

      {/* Post Actions */}
      <div className="px-4 py-2 border-t border-gray-100 flex justify-between text-sm font-medium">
        <button
          onClick={toggleLike}
          className={`flex items-center justify-center gap-2 w-full py-2 rounded-md 
                transition-all duration-150 ease-in-out 
                ${
                  isLiked
                    ? "text-blue-600 font-semibold"
                    : "text-gray-600 hover:text-blue-500 hover:bg-gray-100"
                }`}
        >
          <FaThumbsUp className={`w-5 h-5 ${isLiked ? "fill-blue-600" : ""}`} />
          Like
        </button>

        <button
          onClick={toggleComments}
          className="flex items-center justify-center gap-2 w-full py-2 rounded-md 
               text-gray-600 hover:text-blue-500 hover:bg-gray-100 
               transition-all duration-150 ease-in-out"
        >
          <FaRegCommentDots className="w-5 h-5" />
          Comment
        </button>

        <button
          className="flex items-center justify-center gap-2 w-full py-2 rounded-md 
               text-gray-600 hover:text-blue-500 hover:bg-gray-100 
               transition-all duration-150 ease-in-out"
        >
          <FaShare className="w-5 h-5" />
          Share
        </button>
      </div>

    </div>
  );
};

export default PostItem;
