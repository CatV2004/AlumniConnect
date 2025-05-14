import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import CreatePostModal from "./CreatePostModal";
import defaultAvatar from "../../assets/image/default-user.png";

const CreatePostBar = ({ user }) => {
  const [showModal, setShowModal] = useState(false);
  const navigate = useNavigate();

  const handleClick = () => {
    if (!user) {
      navigate("/login");
    } else {
      setShowModal(true);
    }
  };

  return (
    <>
      <div className="bg-white p-4 rounded-xl shadow">
        <div className="flex items-center space-x-4">
          <img
            src={user?.avatar || defaultAvatar}
            alt="avatar"
            className="w-10 h-10 rounded-full object-cover"
          />
          <button
            onClick={handleClick}
            className="bg-gray-100 hover:bg-gray-200 text-gray-700 flex-1 text-left px-4 py-2 rounded-full"
          >
            {user
              ? `${
                  user.lastName ? user.lastName : "ADMIN"
                } ơi, bạn đang nghĩ gì thế?`
              : "Bạn đang nghĩ gì thế?"}
          </button>
        </div>
        <div className="flex justify-between mt-4 px-2 text-sm text-gray-600">
          <button className="flex items-center space-x-1 hover:bg-gray-100 p-2 rounded">
            <span className="text-red-500">📹</span>
            <span>Video trực tiếp</span>
          </button>
          <button className="flex items-center space-x-1 hover:bg-gray-100 p-2 rounded">
            <span className="text-green-500">🖼️</span>
            <span>Ảnh/video</span>
          </button>
          <button className="flex items-center space-x-1 hover:bg-gray-100 p-2 rounded">
            <span className="text-yellow-500">😊</span>
            <span>Cảm xúc/hoạt động</span>
          </button>
        </div>
      </div>

      {showModal && (
        <CreatePostModal onClose={() => setShowModal(false)} user={user} />
      )}
    </>
  );
};

export default CreatePostBar;
