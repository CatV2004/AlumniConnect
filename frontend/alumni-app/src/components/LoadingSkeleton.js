import React from "react";

const LoadingSkeleton = ({ type }) => {
  if (type === "invitation-detail") {
    return (
      <div className="animate-pulse space-y-6 p-6 bg-white rounded shadow-md">
        {/* Skeleton for Title */}
        <div className="h-6 bg-gray-200 rounded w-2/3"></div>

        {/* Skeleton for author and date */}
        <div className="flex space-x-4 items-center">
          <div className="h-4 bg-gray-200 rounded w-24"></div>
          <div className="h-4 bg-gray-200 rounded w-16"></div>
        </div>

        {/* Skeleton for event time */}
        <div className="h-4 bg-gray-200 rounded w-40"></div>

        {/* Skeleton for content */}
        <div className="space-y-2">
          <div className="h-4 bg-gray-200 rounded w-full"></div>
          <div className="h-4 bg-gray-200 rounded w-5/6"></div>
          <div className="h-4 bg-gray-200 rounded w-3/4"></div>
        </div>

        {/* Skeleton for invited users */}
        <div className="space-y-2 pt-4">
          <div className="h-5 bg-gray-300 rounded w-32"></div>
          <div className="flex space-x-3">
            <div className="h-6 w-20 bg-gray-200 rounded"></div>
            <div className="h-6 w-20 bg-gray-200 rounded"></div>
            <div className="h-6 w-20 bg-gray-200 rounded"></div>
          </div>
        </div>
      </div>
    );
  }

  return null;
};

export default LoadingSkeleton;
