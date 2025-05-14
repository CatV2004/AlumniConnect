import React from "react";

const PostSkeleton = () => {
  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden mb-4">
      <div className="p-4">
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 bg-gray-200 rounded-full animate-pulse"></div>
          <div className="space-y-2">
            <div className="w-32 h-4 bg-gray-200 rounded animate-pulse"></div>
            <div className="w-24 h-3 bg-gray-200 rounded animate-pulse"></div>
          </div>
        </div>
        <div className="mt-4 space-y-2">
          <div className="w-full h-4 bg-gray-200 rounded animate-pulse"></div>
          <div className="w-5/6 h-4 bg-gray-200 rounded animate-pulse"></div>
          <div className="w-3/4 h-4 bg-gray-200 rounded animate-pulse"></div>
        </div>
      </div>
      <div className="w-full h-64 bg-gray-200 animate-pulse"></div>
      <div className="p-4 space-y-2">
        <div className="w-1/2 h-4 bg-gray-200 rounded animate-pulse"></div>
        <div className="flex space-x-4 pt-2">
          <div className="w-1/3 h-8 bg-gray-200 rounded animate-pulse"></div>
          <div className="w-1/3 h-8 bg-gray-200 rounded animate-pulse"></div>
          <div className="w-1/3 h-8 bg-gray-200 rounded animate-pulse"></div>
        </div>
      </div>
    </div>
  );
};

export default PostSkeleton;
