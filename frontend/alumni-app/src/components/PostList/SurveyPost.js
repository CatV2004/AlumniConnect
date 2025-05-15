import React from "react";
import { Link } from "react-router-dom";
import { formatDateFromArray } from "../../app/utils/dateUtils";

const SurveyPost = ({ survey, postId }) => {
  return (
    <div className="p-4 border-t border-gray-100 bg-blue-50 rounded-lg mt-2">
      <a
        href={`/survey/${postId}`}
        target="_blank"
        rel="noopener noreferrer"
        className="block hover:bg-blue-100 p-3 rounded transition"
      >
        <div className="flex items-center justify-between">
          <div>
            <h3 className="font-semibold text-blue-700">{survey.content}</h3>
            <p className="text-sm text-gray-600 mt-1">
              Survey • Ends on{" "}
              {formatDateFromArray(survey.endTime, { includeTime: true })}
            </p>
          </div>
          <div className="text-blue-600 font-medium">Take Survey →</div>
        </div>
      </a>
    </div>
  );
};

export default SurveyPost;
