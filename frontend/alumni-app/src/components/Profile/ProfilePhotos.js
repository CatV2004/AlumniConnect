import React from 'react';

const ProfilePhotos = ({ posts }) => {
  const allPhotos = posts.reduce((acc, post) => {
    return [...acc, ...(post.postImageSet || [])];
  }, []);

  if (allPhotos.length === 0) {
    return <div className="text-center text-gray-500 py-4">Chưa có ảnh nào được đăng</div>;
  }

  return (
    <div className="grid grid-cols-3 gap-2">
      {allPhotos.map((photo, index) => (
        <div key={index} className="aspect-square bg-gray-200 rounded overflow-hidden">
          <img
            src={photo}
            alt={`Post ${index}`}
            className="w-full h-full object-cover hover:scale-105 transition-transform cursor-pointer"
          />
        </div>
      ))}
    </div>
  );
};

export default ProfilePhotos;