import React from 'react';

const ProfileHeader = ({ user }) => {
  return (
    <div className="bg-white shadow rounded-xl p-4 mb-4">
      <div className="flex items-center gap-4">
        <img
          src={user.avatarUrl || '/default-avatar.png'}
          alt="Avatar"
          className="w-20 h-20 rounded-full object-cover border-2 border-blue-500"
        />
        <div>
          <h2 className="text-xl font-bold">{user.firstName} {user.lastName}</h2>
          <p className="text-gray-600">{user.email}</p>
        </div>
      </div>
    </div>
  );
};

export default ProfileHeader;
