import { useEffect, useState } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import { useSelector } from "react-redux";
import moment from "moment";
import "moment/locale/vi";
import {
  Calendar as CalendarIcon,
  MapPin as LocationIcon,
  Users as UsersIcon,
  ArrowLeft as BackIcon,
  Clock as ClockIcon,
  CheckCircle as CheckIcon,
  XCircle as DeclineIcon,
  HelpCircle as MaybeIcon,
  MessageCircle as CommentIcon,
  Share2 as ShareIcon
} from "lucide-react";
import {
  FaThumbsUp,
  FaHeart,
  FaLaugh
} from "react-icons/fa";

import { getPostById } from "../services/postService";
import CommentList from "../components/Comment/CommentList";
import NotFoundPage from "./NotFoundPage";
import LoadingSkeleton from "../components/LoadingSkeleton";
import PostOptionsDropdown from "../components/PostList/PostOptionsDropdown";
import { formatDate } from "../app/utils/dateUtils";
import PostItem from "../components/PostList/PostItem";

moment.locale("vi");

const InvitationDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useSelector((state) => state.auth);
  const [invitation, setInvitation] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [rsvpStatus, setRsvpStatus] = useState(null);
  const [showComment, setShowComment] = useState(false);
  const [commentCount, setCommentCount] = useState(0);
  const [likeCount, setLikeCount] = useState(0);
  const [isLiked, setIsLiked] = useState(false);
  const [reactionType, setReactionType] = useState(null);
  const [reactionPost, setReactionPost] = useState([]);

  useEffect(() => {
    const loadInvitation = async () => {
      try {
        setLoading(true);
        const data = await getPostById(id);
        setInvitation(data);
        setLikeCount(data.likeCount || 0);
        setCommentCount(data.commentCount || 0);
        
        if (data.invitationPost?.responses && user) {
          const userResponse = data.invitationPost.responses.find(
            (r) => r.userId === user.id
          );
          if (userResponse) {
            setRsvpStatus(userResponse.status);
          }
        }
      } catch (err) {
        console.error("Failed to load invitation:", err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    loadInvitation();
  }, [id, user]);

  const handleRSVP = async (status) => {
    try {
      // Call API to update RSVP status
      // await updateInvitationResponse(id, { userId: user.id, status });
      setRsvpStatus(status);
      // Show success feedback
    } catch (err) {
      console.error("Failed to update RSVP:", err);
    }
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
    }

    setLikeCount(newLikeCount);
    setReactionType(isSameReaction ? null : typeReaction);
    setIsLiked(!isSameReaction);

    try {
      // await addReaction(invitation.id, typeReaction);
    } catch (error) {
      console.error("Failed to toggle like", error);
    }
  };

  const ReactionIcon = ({ reaction }) => {
    switch (reaction) {
      case "LOVE": return <FaHeart className="w-4 h-4 fill-red-500" />;
      case "HAHA": return <FaLaugh className="w-4 h-4 fill-yellow-500" />;
      default: return <FaThumbsUp className="w-4 h-4 fill-blue-600" />;
    }
  };

  const CurrentUserReactionIcon = () => {
    switch (reactionType) {
      case "LOVE": return <FaHeart className="w-5 h-5 fill-red-500" />;
      case "HAHA": return <FaLaugh className="w-5 h-5 fill-yellow-500" />;
      default: return <FaThumbsUp className="w-5 h-5 fill-blue-600" />;
    }
  };

  const ReactionLabel = () => {
    switch (reactionType) {
      case "LOVE": return "Love";
      case "HAHA": return "Haha";
      default: return "Like";
    }
  };

  if (loading) {
    return <LoadingSkeleton type="invitation-detail" />;
  }

  if (error || !invitation) {
    return <NotFoundPage />;
  }

  const invitationPost = invitation.invitationPost || {};
  const eventTime = invitationPost.eventTime || [];
  const endTime = invitationPost.endTime || [];
  const rsvpDeadline = invitationPost.rsvpDeadline || [];
  const participants = invitationPost.invitedUserIds || [];

  return (
    <div className="max-w-2xl mx-auto bg-white rounded-lg shadow-sm overflow-hidden">
      {/* Header */}
      <div className="sticky top-0 z-10 bg-white border-b border-gray-200 p-4 flex items-center justify-between">
        <button
          onClick={() => navigate(-1)}
          className="p-2 rounded-full hover:bg-gray-100"
        >
          <BackIcon className="w-5 h-5" />
        </button>
        <h1 className="text-xl font-bold text-gray-900 flex-1 text-center">
          Event Details
        </h1>
        <PostOptionsDropdown post={invitation} />
      </div>

      {/* Main Content */}
      <div className="p-4">
        {/* Original Post */}
        <PostItem post={invitation} />

        {/* Event Details Card */}
        <div className="bg-blue-50 rounded-xl p-5 mb-6 border border-blue-100">
          <div className="space-y-4">
            {/* Time */}
            <div className="flex gap-3">
              <div className="bg-blue-100 p-2 rounded-lg">
                <CalendarIcon className="w-5 h-5 text-blue-600" />
              </div>
              <div>
                <h3 className="font-medium text-gray-900">Event Time</h3>
                <p className="text-gray-700">{formatDate(eventTime)}</p>
                {endTime.length > 0 && (
                  <p className="text-gray-600">Until {formatDate(endTime)}</p>
                )}
              </div>
            </div>

            {/* Location */}
            {invitationPost.location && (
              <div className="flex gap-3">
                <div className="bg-blue-100 p-2 rounded-lg">
                  <LocationIcon className="w-5 h-5 text-blue-600" />
                </div>
                <div>
                  <h3 className="font-medium text-gray-900">Location</h3>
                  <p className="text-gray-700">{invitationPost.location}</p>
                  {invitationPost.locationLink && (
                    <a
                      href={invitationPost.locationLink}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-blue-600 text-sm hover:underline"
                    >
                      View on map
                    </a>
                  )}
                </div>
              </div>
            )}

            {/* Participants */}
            {participants.length > 0 && (
              <div className="flex gap-3">
                <div className="bg-blue-100 p-2 rounded-lg">
                  <UsersIcon className="w-5 h-5 text-blue-600" />
                </div>
                <div>
                  <h3 className="font-medium text-gray-900">Invited Guests</h3>
                  <div className="flex flex-wrap gap-1">
                    {participants.map((participant, index) => (
                      <span
                        key={index}
                        className="text-blue-600 hover:underline text-sm"
                      >
                        {participant}
                        {index < participants.length - 1 ? "," : ""}
                      </span>
                    ))}
                  </div>
                </div>
              </div>
            )}

            {/* RSVP Deadline */}
            {rsvpDeadline.length > 0 && (
              <div className="flex gap-3">
                <div className="bg-blue-100 p-2 rounded-lg">
                  <ClockIcon className="w-5 h-5 text-blue-600" />
                </div>
                <div>
                  <h3 className="font-medium text-gray-900">RSVP Deadline</h3>
                  <p className="text-gray-700">{formatDate(rsvpDeadline)}</p>
                </div>
              </div>
            )}
          </div>
        </div>

        {/* RSVP Section */}
        <div className="bg-white border border-gray-200 rounded-xl p-5 mb-6">
          <h3 className="font-semibold text-gray-900 mb-4 text-center">
            Will you attend this event?
          </h3>
          <div className="grid grid-cols-3 gap-3">
            <button
              onClick={() => handleRSVP("going")}
              className={`flex flex-col items-center justify-center p-3 rounded-lg transition-all ${
                rsvpStatus === "going"
                  ? "bg-green-50 border-2 border-green-400"
                  : "bg-gray-50 hover:bg-gray-100"
              }`}
            >
              <CheckIcon className={`w-6 h-6 ${
                rsvpStatus === "going" ? "text-green-600" : "text-gray-500"
              }`}
              />
              <span className="mt-1 text-sm font-medium">Going</span>
            </button>
            <button
              onClick={() => handleRSVP("maybe")}
              className={`flex flex-col items-center justify-center p-3 rounded-lg transition-all ${
                rsvpStatus === "maybe"
                  ? "bg-yellow-50 border-2 border-yellow-400"
                  : "bg-gray-50 hover:bg-gray-100"
              }`}
            >
              <MaybeIcon className={`w-6 h-6 ${
                rsvpStatus === "maybe" ? "text-yellow-600" : "text-gray-500"
              }`}
              />
              <span className="mt-1 text-sm font-medium">Maybe</span>
            </button>
            <button
              onClick={() => handleRSVP("declined")}
              className={`flex flex-col items-center justify-center p-3 rounded-lg transition-all ${
                rsvpStatus === "declined"
                  ? "bg-red-50 border-2 border-red-400"
                  : "bg-gray-50 hover:bg-gray-100"
              }`}
            >
              <DeclineIcon className={`w-6 h-6 ${
                rsvpStatus === "declined" ? "text-red-600" : "text-gray-500"
              }`}
              />
              <span className="mt-1 text-sm font-medium">Can't Go</span>
            </button>
          </div>
        </div>

        {/* Responses Summary */}
        {invitationPost.responses && invitationPost.responses.length > 0 && (
          <div className="bg-white border border-gray-200 rounded-xl p-5 mb-6">
            <h3 className="font-semibold text-gray-900 mb-4 text-center">
              Responses Summary
            </h3>
            <div className="grid grid-cols-3 gap-4">
              <div className="text-center">
                <div className="text-2xl font-bold text-green-600">
                  {invitationPost.responses.filter((r) => r.status === "going").length}
                </div>
                <div className="text-sm text-gray-600">Going</div>
              </div>
              <div className="text-center">
                <div className="text-2xl font-bold text-yellow-600">
                  {invitationPost.responses.filter((r) => r.status === "maybe").length}
                </div>
                <div className="text-sm text-gray-600">Maybe</div>
              </div>
              <div className="text-center">
                <div className="text-2xl font-bold text-red-600">
                  {invitationPost.responses.filter((r) => r.status === "declined").length}
                </div>
                <div className="text-sm text-gray-600">Declined</div>
              </div>
            </div>
          </div>
        )}

        {/* Comments Section */}
        {showComment && (
          <div className="mt-4">
            <CommentList 
              post={invitation} 
              showComment={showComment} 
              setCountComment={setCommentCount} 
            />
          </div>
        )}
      </div>
    </div>
  );
};

export default InvitationDetailPage;