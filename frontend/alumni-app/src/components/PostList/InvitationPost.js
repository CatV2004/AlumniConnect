import React from "react";
import moment from "moment";
import "moment/locale/vi";
import {
  Calendar as CalendarIcon,
  MapPin as LocationIcon,
  Users as UsersIcon,
} from "lucide-react";
import { Link } from "react-router-dom";

moment.locale("vi");

const InvitationPost = ({ invitation, postId }) => {
  // console.log("invitation: ", invitation);
  if (!invitation) return null;
  const formatDate = (dateArray) => {
    if (!dateArray) return "";
    const date = new Date(...dateArray);
    return moment(date).format("llll");
  };

  return (
    <div className="p-4 border-t border-gray-100 bg-blue-50">
      <div className="mb-3">
        <h3 className="font-semibold text-blue-700 flex items-center gap-2">
          <CalendarIcon className="w-5 h-5 text-blue-600 hover:scale-110 transition-transform" />
          Event Invitation
        </h3>
      </div>

      <div className="space-y-3">
        <div>
          <h4 className="font-medium text-lg">
            <Link
              to={`/invitations/${postId}`}
              className="hover:underline"
            >
              {invitation.eventName}
            </Link>
          </h4>
          <p className="text-gray-600">{invitation.description}</p>
        </div>

        <div className="flex items-start gap-2">
          <CalendarIcon className="w-5 h-5 mt-1 text-gray-500 hover:text-blue-600 transition-colors" />
          <div>
            <p className="font-medium">Event Time</p>
            <p>{formatDate(invitation.eventTime)}</p>
            {invitation.endTime && <p>to {formatDate(invitation.endTime)}</p>}
          </div>
        </div>

        {invitation.location && (
          <div className="flex items-start gap-2">
            <LocationIcon className="w-5 h-5 mt-1 text-gray-500 hover:text-blue-600 transition-colors" />
            <div>
              <p className="font-medium">Location</p>
              <p>{invitation.location}</p>
            </div>
          </div>
        )}

        {invitation.participants && (
          <div className="flex items-start gap-2">
            <UsersIcon className="w-5 h-5 mt-1 text-gray-500 hover:text-blue-600 transition-colors" />
            <div>
              <p className="font-medium">Participants</p>
              <p>{invitation.participants.join(", ")}</p>
            </div>
          </div>
        )}

        <div className="flex gap-2 mt-4">
          <button className="flex-1 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors">
            Going
          </button>
          <button className="flex-1 bg-gray-200 text-gray-800 px-4 py-2 rounded-md hover:bg-gray-300 transition-colors">
            Maybe
          </button>
          <button className="flex-1 bg-gray-200 text-gray-800 px-4 py-2 rounded-md hover:bg-gray-300 transition-colors">
            Can't Go
          </button>
        </div>

        {invitation.rsvpDeadline && (
          <p className="text-sm text-gray-500 mt-2">
            Please respond by {formatDate(invitation.rsvpDeadline)}
          </p>
        )}
      </div>
    </div>
  );
};

export default InvitationPost;
