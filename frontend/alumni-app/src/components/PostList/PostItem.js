import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import CommentList from "../Comment/CommentList";
import CommentCreated from "../Comment/CommentCreated";
import moment from "moment";
import "moment/locale/vi";
import {
  FaThumbsUp,
  FaRegCommentDots,
  FaShare,
  FaChartBar,
  FaLaugh,
  FaHeart,
} from "react-icons/fa";
import { ImSpinner8 } from "react-icons/im";
import SurveyStatsModal from "../survey/SurveyStatsModal";
import PostOptionsDropdown from "./PostOptionsDropdown";
import PostImagesGallery from "./PostImagesGallery";
import SurveyPost from "./SurveyPost";
import InvitationPost from "./InvitationPost";
import { formatDate } from "../../app/utils/dateUtils";
import PostForm from "../PostForm/PostForm";
import addReaction from "../Reaction/AddReaction";
import CountReaction from "../Reaction/CountReaction";
import ReactionOfPost from "../Reaction/ReactionOfPost";
import { useSelector } from "react-redux";
import CountComment from "../Comment/CountComment";
import FindReaction from "../Reaction/FindReaction";

moment.locale("vi");

const PostItem = ({ post }) => {
  const role = useSelector((state) => state.auth.role);
  // console.log("post: ", post);
  const user = useSelector((state) => state.auth);

  const [showComment, setShowComment] = useState(false);
  const [likeCount, setLikeCount] = useState(post.likeCount || 0);
  const [commentCount, setCommentCount] = useState(post.commentCount || 0);
  const [isLiked, setIsLiked] = useState(false);
  const [isExpanded, setIsExpanded] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [statsModalOpen, setStatsModalOpen] = useState(false);
  const [reactionType, setReactionType] = useState(null);
  const [reactionPost, setReactionPost] = useState([]);

  const MAX_CONTENT_LENGTH = 300;

  useEffect(() => {
    const fetchReactions = async () => {
      const liked = await ReactionOfPost(post.id);
      const reactions = await CountReaction(post.id);
      setIsLiked(liked);
      const totalCount = Object.values(reactions).reduce(
        (sum, count) => sum + count,
        0
      );
      const types = Object.entries(reactions)
        .filter(([_, count]) => count > 0)
        .map(([type, _]) => type);
      setLikeCount(totalCount);
      setReactionPost(types);
    };

    const fetchReactionUser = async () => {
      try {
        const reaction = await FindReaction(post.id);
        if (reaction && reaction.reaction) {
          setReactionType(reaction.reaction);
        } else {
          setReactionType(null);
        }
      } catch (err) {
        console.error("Lỗi khi fetch reaction của user:", err);
        setReactionType(null);
      }
    };

    const fetchCommentByPost = async () => {
      const countC = await CountComment(post.id);
      setCommentCount(countC);
    };

    if (user !== null) {
      fetchReactions();
      fetchCommentByPost();
      fetchReactionUser();
    }
  }, [post.id]);

  const toggleComments = () => {
    setShowComment(!showComment);
  };

  const toggleLike = async (typeReaction) => {
    const wasLiked = !!reactionType;
    const isSameReaction = reactionType === typeReaction;

    let newLikeCount = likeCount;
    let newReactionPost = [...(reactionPost || [])];

    if (!wasLiked) {
      newLikeCount += 1;
    } else if (wasLiked && isSameReaction) {
      newLikeCount = Math.max(newLikeCount - 1, 0);

      if (likeCount === 1) {
        newReactionPost = newReactionPost.filter((r) => r !== typeReaction);
      }
    }

    if (!newReactionPost.includes(typeReaction)) {
      newReactionPost.push(typeReaction);
    }

    setLikeCount(newLikeCount);
    setReactionType(isSameReaction ? null : typeReaction);
    setIsLiked(!isSameReaction);
    setReactionPost(newReactionPost);

    try {
      await addReaction(post.id, typeReaction);
    } catch (error) {
      setLikeCount(likeCount);
      setReactionType(reactionType);
      setIsLiked(!!reactionType);
      console.error("Failed to toggle like", error);
    }
  };

  const ReactionIcon = ({ reaction }) => {
    switch (reaction) {
      case "LOVE":
        return <FaHeart className="w-5 h-5 fill-red-500" />;
      case "HAHA":
        return <FaLaugh className="w-5 h-5 fill-yellow-500" />;
      default:
        return <FaThumbsUp className="w-5 h-5 fill-blue-600" />;
    }
  };
  const CurrentUserReactionIcon = () => {
    switch (reactionType) {
      case "LOVE":
        return (
          <FaHeart className={`w-5 h-5 ${isLiked ? "fill-red-500" : ""}`} />
        );
      case "HAHA":
        return (
          <FaLaugh className={`w-5 h-5 ${isLiked ? "fill-yellow-500" : ""}`} />
        );
      default:
        return (
          <FaThumbsUp className={`w-5 h-5 ${isLiked ? "fill-blue-600" : ""}`} />
        );
    }
  };

  const ReactionLabel = () => {
    switch (reactionType) {
      case "LOVE":
        return "Love";
      case "HAHA":
        return "Haha";
      default:
        return "Like";
    }
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
          <PostOptionsDropdown post={post} onEdit={handleEditClick} />
        </div>

        {/* Post Content */}
        <div className="mt-3">{renderContent()}</div>
      </div>

      {/* Post Media */}
      {post.postImages && post.postImages.length > 0 && (
        <PostImagesGallery images={post.postImages} />
      )}

      {/* Survey Post */}
      {post.surveyPost && (
        <SurveyPost survey={post.surveyPost} postId={post.id} />
      )}

      {/* Invitation Post */}
      {post.invitationPost && (
        <InvitationPost invitation={post.invitationPost} postId={post.id} />
      )}

      {/* Post Stats */}
      <div className="px-4 pt-2 pb-1 border-t border-gray-100">
        <div className="flex justify-between text-sm text-gray-500">
          <span className="flex items-center gap-1">
            {likeCount}
            {reactionPost &&
              reactionPost.map((r, idx) => (
                <ReactionIcon key={idx} reaction={r} />
              ))}
            {reactionPost.length === 0 && " reaction"}
          </span>
          <span>{commentCount} comments</span>
        </div>
      </div>

      {/* Post Actions */}
      <div className="px-4 relative group py-2 border-t border-gray-100 flex justify-between text-sm font-medium">
        <button
          onClick={() => toggleLike("LIKE")}
          className={`relative flex group items-center justify-center gap-2 w-full py-2 rounded-md 
                transition-all duration-150 ease-in-out 
                ${
                  reactionType === "LOVE"
                    ? "text-red-500/80 font-semibold"
                    : reactionType === "HAHA"
                    ? "text-yellow-500/80 font-semibold"
                    : reactionType === "LIKE"
                    ? "text-blue-600 font-semibold"
                    : "text-gray-600 hover:text-blue-500 hover:bg-gray-100"
                }`}
        >
          {/* <FaThumbsUp className={`w-5 h-5 ${isLiked ? "fill-blue-600" : ""}`} />
          Like */}
          <CurrentUserReactionIcon />
          <ReactionLabel />
        </button>
        {/* <div className="relative inline-block group w-max"> */}

        {/* Menu reaction (hiện khi hover) */}
        <div
          className="absolute -top-10 left-20 translate-x-2 flex space-x-2 p-1 bg-white rounded-full shadow-lg
                    opacity-0 invisible group-hover:opacity-100 group-hover:visible
                    transition-all duration-200"
        >
          <button
            onClick={() => toggleLike("LIKE")}
            className="p-1 rounded-full hover:bg-gray-300"
          >
            <FaThumbsUp className="w-6 h-6 text-blue-600" />
          </button>
          <button
            onClick={() => toggleLike("LOVE")}
            className="p-1 rounded-full hover:bg-gray-300"
          >
            <FaHeart className="w-6 h-6 text-red-500" />
          </button>
          <button
            onClick={() => toggleLike("HAHA")}
            className="p-1 rounded-full hover:bg-gray-300"
          >
            <FaLaugh className="w-6 h-6 text-yellow-500" />
          </button>
        </div>

        {/* {!post.lockComment && ( */}
          <button
            onClick={toggleComments}
            className="flex items-center justify-center gap-2 w-full py-2 rounded-md 
               text-gray-600 hover:text-blue-500 hover:bg-gray-100 
               transition-all duration-150 ease-in-out"
          >
            <FaRegCommentDots className="w-5 h-5" />
            Comment
          </button>
        {/* )} */}
        
        {post.surveyPost && role === "ADMIN" && (
          <button
            onClick={() => setStatsModalOpen(true)}
            className="flex items-center justify-center gap-2 w-full py-2 rounded-md 
                 text-gray-600 hover:text-blue-500 hover:bg-gray-100 
                 transition-all duration-150 ease-in-out"
          >
            <FaChartBar className="w-5 h-5" />
            Stats
          </button>
        )}

        {!post.surveyPost && (
          <button
            className="flex items-center justify-center gap-2 w-full py-2 rounded-md 
               text-gray-600 hover:text-blue-500 hover:bg-gray-100 
               transition-all duration-150 ease-in-out"
          >
            <FaShare className="w-5 h-5" />
            Share
          </button>
        )}
      </div>

      {/* Comments Section */}
      {showComment && (
        <div className="bg-gray-50 p-4 border-t border-gray-100">
          <CommentList
            post={post}
            showComment={setShowComment}
            setCountComment={setCommentCount}
          />
          {/* <CommentCreated post={post} /> */}
        </div>
      )}
      {isEditModalOpen && (
        <PostForm
          onClose={() => setIsEditModalOpen(false)}
          user={post.userId}
          postId={post.id}
        />
      )}
      {post.surveyPost && (
        <SurveyStatsModal
          open={statsModalOpen}
          onClose={() => setStatsModalOpen(false)}
          surveyPostId={post.surveyPost.id}
          token={localStorage.getItem("token")}
        />
      )}
    </div>
  );
};
export default PostItem;
