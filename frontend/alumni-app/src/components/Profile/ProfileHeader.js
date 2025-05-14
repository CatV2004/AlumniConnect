import React from 'react';

const ProfileHeader = ({ user }) => {
  return (
    <div className="bg-white shadow rounded-xl overflow-hidden mb-4">
      {/* Cover Photo */}
      <div className="h-48 bg-gray-200 relative">
        {user.cover && (
          <img
            src={user.cover}
            alt="Cover"
            className="w-full h-full object-cover"
          />
        )}
        
        {/* Profile Picture */}
        <div className="absolute -bottom-16 left-4">
          <img
            src={user.avatar || '/default-avatar.png'}
            alt="Avatar"
            className="w-32 h-32 rounded-full object-cover border-4 border-white shadow-lg"
          />
        </div>
      </div>

      {/* Profile Info */}
      <div className="pt-20 px-4 pb-4">
        <div className="flex justify-between items-start">
          <div>
            <h1 className="text-2xl font-bold">{user.firstName} {user.lastName}</h1>
            <p className="text-gray-600">{user.email}</p>
            
            {/* Basic Info */}
            <div className="mt-2 flex gap-4 text-sm text-gray-600">
              {user.phone && <span>📱 {user.phone}</span>}
              {user.role && <span>🎓 {user.role}</span>}
            </div>
          </div>
          
          {/* Action Buttons */}
          <div className="flex gap-2">
            <button className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">
              Thêm liên lạc
            </button>
            <button className="bg-gray-200 px-4 py-2 rounded-md hover:bg-gray-300">
              Nhắn tin
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProfileHeader;