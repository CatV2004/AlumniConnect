import { useState, useRef } from "react";
import { useSelector } from "react-redux";
import { FiCamera } from "react-icons/fi";
import ReactCrop from 'react-image-crop';
import 'react-image-crop/dist/ReactCrop.css';
import { updateUserAvatarOrCover } from "../../services/userService";

const ProfileHeader = ({ user }) => {
  const { token } = useSelector((state) => state.auth);
  const avatarInputRef = useRef(null);
  const coverInputRef = useRef(null);
  
  // State cho crop ảnh
  const [showModal, setShowModal] = useState(false);
  const [selectedImage, setSelectedImage] = useState(null);
  const [crop, setCrop] = useState({ aspect: 1/1 });
  const [type, setType] = useState('avatar'); // 'avatar' hoặc 'cover'

  
  const handleImageChange = async (e, imageType) => {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      setSelectedImage(reader.result);
      setType(imageType);
      setShowModal(true);
      
      // Reset crop settings theo từng loại
      if(imageType === 'cover') {
        setCrop({ aspect: 16/9, width: 100, unit: '%' });
      } else {
        setCrop({ aspect: 1/1, width: 100, unit: '%' });
      }
    };
    reader.readAsDataURL(file);
  };

  const getCroppedImg = async (image, crop) => {
    const canvas = document.createElement('canvas');
    const scaleX = image.naturalWidth / image.width;
    const scaleY = image.naturalHeight / image.height;
    
    canvas.width = crop.width * scaleX;
    canvas.height = crop.height * scaleY;
    
    const ctx = canvas.getContext('2d');
    ctx.drawImage(
      image,
      crop.x * scaleX,
      crop.y * scaleY,
      crop.width * scaleX,
      crop.height * scaleY,
      0,
      0,
      crop.width * scaleX,
      crop.height * scaleY
    );

    return new Promise((resolve) => {
      canvas.toBlob((blob) => {
        resolve(blob);
      }, 'image/jpeg');
    });
  };

  const handleCrop = async () => {
    const image = document.getElementById('crop-image');
    const blob = await getCroppedImg(image, crop);
    
    // Tạo file từ blob
    const file = new File([blob], `${type}.jpg`, { type: 'image/jpeg' });
    
    // Tạo formData và gọi API
    const formData = new FormData();
    formData.append(type, file);

    try {
      await updateUserAvatarOrCover(formData, token);
      window.location.reload();
    } catch (err) {
      console.error("Cập nhật ảnh thất bại", err);
    } finally {
      setShowModal(false);
    }
  };

  return (
    <div className="bg-white shadow rounded-xl overflow-hidden mb-4">
      {/* Cover Photo */}
      <div className="h-48 bg-gray-200 relative group">
        {user.cover && (
          <img
            src={user.cover}
            alt="Cover"
            className="w-full h-full object-cover"
          />
        )}
        
        {/* Upload cover */}
        <button
          className="absolute bottom-4 right-4 bg-white px-3 py-2 rounded-md shadow flex items-center gap-2 text-sm opacity-0 group-hover:opacity-100 transition-opacity"
          onClick={() => coverInputRef.current.click()}
        >
          <FiCamera className="w-4 h-4 text-gray-700" />
          Chỉnh sửa ảnh bìa
        </button>

        <input
          type="file"
          accept="image/*"
          hidden
          ref={coverInputRef}
          onChange={(e) => handleImageChange(e, "cover")}
        />

        {/* Profile Picture */}
        <div className="absolute -bottom-16 left-4 group">
          <img
            src={user.avatar || "/default-avatar.png"}
            alt="Avatar"
            className="w-32 h-32 rounded-full object-cover border-4 border-white shadow-lg"
          />
          <button
            className="absolute bottom-0 right-0 bg-white p-2 rounded-full shadow opacity-0 group-hover:opacity-100 transition-opacity"
            onClick={() => avatarInputRef.current.click()}
          >
            <FiCamera className="text-gray-700 w-5 h-5" />
          </button>

          <input
            type="file"
            accept="image/*"
            hidden
            ref={avatarInputRef}
            onChange={(e) => handleImageChange(e, "avatar")}
          />
        </div>
      </div>

      {/* Crop Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg w-full max-w-2xl">
            <h3 className="text-xl font-semibold mb-4">Chỉnh sửa ảnh</h3>
            
            <div className="relative max-h-[70vh] overflow-auto">
              {selectedImage && (
                <ReactCrop
                  crop={crop}
                  onChange={(c) => setCrop(c)}
                  onComplete={(c) => setCrop(c)}
                  aspect={type === 'cover' ? 16/9 : 1}
                >
                  <img 
                    id="crop-image" 
                    src={selectedImage} 
                    alt="Crop preview" 
                  />
                </ReactCrop>
              )}
            </div>

            <div className="flex justify-end gap-3 mt-4">
              <button
                onClick={() => setShowModal(false)}
                className="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded"
              >
                Hủy
              </button>
              <button
                onClick={handleCrop}
                className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
              >
                Lưu thay đổi
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Profile Info */}
      <div className="pt-20 px-4 pb-4">{/* thông tin người dùng */}</div>
    </div>
  );
};

export default ProfileHeader;