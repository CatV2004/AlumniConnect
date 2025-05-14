import React, { useState, useRef, useEffect } from "react";
import defaultAvatar from "../../assets/image/default-user.png";
import { createPost, getPostById, updatePost } from "../../services/postService";
import { toast } from "react-toastify";
import {
  FaTimes,
  FaImage,
  FaUserLock,
  FaSmile,
  FaGlobeAmericas,
} from "react-icons/fa";
import { IoMdClose } from "react-icons/io";
import { useDispatch } from "react-redux";
import { fetchPosts } from "../../features/posts/postSlice";

const PostForm = ({ onClose, user, postId = null }) => {
  const dispatch = useDispatch();
  const [content, setContent] = useState("");
  const [images, setImages] = useState([]);
  const [imagePreviews, setImagePreviews] = useState([]);
  const [existingImages, setExistingImages] = useState([]);
  const [lockComment, setLockComment] = useState(false);
  const [loading, setLoading] = useState(false);
  const [isLoadingPost, setIsLoadingPost] = useState(false);
  const [privacy, setPrivacy] = useState("public");
  const fileInputRef = useRef(null);

  // Load post data if in edit mode
  useEffect(() => {
    if (postId) {
      const loadPostData = async () => {
        setIsLoadingPost(true);
        try {
          const postData = await getPostById(postId);
          
          setContent(postData.content || "");
          setLockComment(postData.lockComment || false);
          setPrivacy(postData.privacy || "public");
          
          if (postData.postImageSet && postData.postImageSet.length > 0) {
            setExistingImages(postData.postImageSet.map(img => ({
              id: img.id,
              imageUrl: img.image
            })));
          }
        } catch (error) {
          toast.error("Failed to load post data");
          console.error("Error loading post:", error);
        } finally {
          setIsLoadingPost(false);
        }
      };
      
      loadPostData();
    }
  }, [postId]);

  const handleImageChange = (e) => {
    const files = Array.from(e.target.files);
    if (files.length + images.length > 10) {
      toast.warning("You can only upload up to 10 images");
      return;
    }

    setImages([...images, ...files]);
    const previews = files.map((file) => URL.createObjectURL(file));
    setImagePreviews([...imagePreviews, ...previews]);
  };

  const removeImage = (index) => {
    const newImages = [...images];
    const newPreviews = [...imagePreviews];
    newImages.splice(index, 1);
    newPreviews.splice(index, 1);
    setImages(newImages);
    setImagePreviews(newPreviews);
  };

  const removeExistingImage = (index) => {
    const newExistingImages = [...existingImages];
    newExistingImages.splice(index, 1);
    setExistingImages(newExistingImages);
  };

  const triggerFileInput = () => {
    fileInputRef.current.click();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!content.trim() && images.length === 0 && existingImages.length === 0) {
      toast.warning("Please enter content or add images");
      return;
    }

    const formData = new FormData();
    formData.append("content", content);
    formData.append("lockComment", lockComment);
    formData.append("privacy", privacy);
    
    // Append new images
    images.forEach((img) => formData.append("images", img));
    
    // Append existing image IDs to keep
    existingImages.forEach((img) => {
      formData.append("existingImages", img.id);
    });

    try {
      setLoading(true);
      
      if (postId) {
        // Update existing post
        await updatePost(postId, formData, localStorage.getItem("token"));
        toast.success("Post updated successfully!");
      } else {
        // Create new post
        await createPost(formData, localStorage.getItem("token"));
        toast.success("Post created successfully!");
      }
      
      dispatch(fetchPosts({ page: 1, size: 3, refresh: true }));
      onClose();
    } catch (err) {
      console.error("Error:", err);
      toast.error(postId ? "Failed to update post" : "Failed to create post");
    } finally {
      setLoading(false);
    }
  };

  const privacyOptions = {
    public: { icon: <FaGlobeAmericas />, label: "Public" },
    friends: { icon: <FaUserLock />, label: "Friends" },
    private: { icon: <FaUserLock />, label: "Only me" },
  };

  if (isLoadingPost) {
    return (
      <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur-sm">
        <div className="bg-white rounded-lg p-8 text-center">
          <p>Loading post data...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur-sm">
      <div
        className="bg-white rounded-lg w-full max-w-2xl mx-4 shadow-xl overflow-hidden flex flex-col"
        style={{ maxHeight: "90vh" }}
      >
        {/* Header */}
        <div className="border-b border-gray-200 p-4 relative flex-shrink-0">
          <h2 className="text-xl font-bold text-center">
            {postId ? "Edit Post" : "Create Post"}
          </h2>
          <button
            onClick={onClose}
            className="absolute right-4 top-4 text-gray-500 hover:text-gray-700 p-1 rounded-full hover:bg-gray-100"
          >
            <FaTimes size={20} />
          </button>
        </div>

        {/* Body */}
        <div className="overflow-y-auto flex-grow">
          <div className="p-4 border-b border-gray-200">
            <div className="flex items-center gap-3">
              <img
                src={user?.avatar || defaultAvatar}
                alt="avatar"
                className="w-10 h-10 rounded-full object-cover"
              />
              <div>
                <div className="font-semibold">{user?.username}</div>
                <div className="flex items-center gap-1 text-xs text-gray-500">
                  <button
                    onClick={() =>
                      setPrivacy(
                        privacy === "public"
                          ? "friends"
                          : privacy === "friends"
                          ? "private"
                          : "public"
                      )
                    }
                    className="flex items-center gap-1 px-2 py-1 rounded bg-gray-100 hover:bg-gray-200"
                  >
                    {privacyOptions[privacy].icon}
                    {privacyOptions[privacy].label}
                  </button>
                </div>
              </div>
            </div>
          </div>

          <form onSubmit={handleSubmit} className="p-4">
            <textarea
              className="w-full p-3 text-lg resize-none outline-none placeholder-gray-500"
              placeholder={`What's on your mind, ${user?.username}?`}
              value={content}
              onChange={(e) => setContent(e.target.value)}
              rows={4}
              autoFocus
            />

            {/* Existing Images */}
            {existingImages.length > 0 && (
              <div
                className={`mt-3 rounded-lg overflow-hidden ${
                  existingImages.length === 1
                    ? "max-h-96"
                    : "grid grid-cols-2 gap-1"
                }`}
              >
                {existingImages.map((img, i) => (
                  <div key={`existing-${img.id}`} className="relative group">
                    <img
                      src={img.imageUrl}
                      alt="existing"
                      className="w-full h-48 object-cover"
                    />
                    <button
                      onClick={(e) => {
                        e.preventDefault();
                        removeExistingImage(i);
                      }}
                      className="absolute top-2 right-2 bg-black bg-opacity-60 text-white rounded-full p-1 hover:bg-opacity-80"
                    >
                      <IoMdClose size={18} />
                    </button>
                  </div>
                ))}
              </div>
            )}

            {/* New Image Previews */}
            {imagePreviews.length > 0 && (
              <div
                className={`mt-3 rounded-lg overflow-hidden ${
                  imagePreviews.length === 1
                    ? "max-h-96"
                    : "grid grid-cols-2 gap-1"
                }`}
              >
                {imagePreviews.map((src, i) => (
                  <div key={`new-${i}`} className="relative group">
                    <img
                      src={src}
                      alt="preview"
                      className="w-full h-48 object-cover"
                    />
                    <button
                      onClick={(e) => {
                        e.preventDefault();
                        removeImage(i);
                      }}
                      className="absolute top-2 right-2 bg-black bg-opacity-60 text-white rounded-full p-1 hover:bg-opacity-80"
                    >
                      <IoMdClose size={18} />
                    </button>
                  </div>
                ))}
              </div>
            )}

            {/* Add to post */}
            <div className="mt-4 border border-gray-200 rounded-lg p-3">
              <div className="text-sm font-semibold mb-2">
                Add to your post
              </div>
              <div className="flex items-center gap-4">
                <button
                  type="button"
                  onClick={triggerFileInput}
                  className="flex items-center gap-2 text-green-600 hover:bg-gray-100 p-2 rounded-lg"
                >
                  <div className="bg-green-100 p-2 rounded-full">
                    <FaImage className="text-green-600" />
                  </div>
                  <span>Photo/Video</span>
                </button>

                <button
                  type="button"
                  className="flex items-center gap-2 text-yellow-600 hover:bg-gray-100 p-2 rounded-lg"
                >
                  <div className="bg-yellow-100 p-2 rounded-full">
                    <FaSmile className="text-yellow-600" />
                  </div>
                  <span>Feeling</span>
                </button>

                <input
                  type="file"
                  ref={fileInputRef}
                  accept="image/*,video/*"
                  multiple
                  onChange={handleImageChange}
                  className="hidden"
                />
              </div>
            </div>

            {/* Comment toggle */}
            <div className="mt-3 flex items-center justify-between text-sm">
              <div className="flex items-center gap-2 text-gray-600">
                <label htmlFor="lockComment" className="font-semibold">
                  Allow comments
                </label>
                <label className="relative inline-flex items-center cursor-pointer">
                  <input
                    type="checkbox"
                    id="lockComment"
                    checked={!lockComment}
                    onChange={(e) => setLockComment(!e.target.checked)}
                    className="sr-only peer"
                  />
                  <div
                    className={`w-11 h-6 rounded-full transition-colors duration-300 ${
                      !lockComment ? "bg-blue-500" : "bg-gray-300"
                    } relative peer-focus:outline-none peer-checked:after:translate-x-full after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all`}
                  ></div>
                  <span className="ml-2 text-xs text-gray-500">
                    {!lockComment ? "Comments enabled" : "Comments disabled"}
                  </span>
                </label>
              </div>
            </div>
          </form>
        </div>

        {/* Footer */}
        <div className="p-4 border-t border-gray-200 flex-shrink-0">
          <button
            type="submit"
            onClick={handleSubmit}
            className={`w-full py-3 rounded-lg text-white font-semibold ${
              loading
                ? "bg-blue-300 cursor-not-allowed"
                : "bg-blue-500 hover:bg-blue-600"
            } ${
              !content.trim() && images.length === 0 && existingImages.length === 0
                ? "opacity-50 cursor-not-allowed"
                : ""
            }`}
            disabled={loading || (!content.trim() && images.length === 0 && existingImages.length === 0)}
          >
            {loading 
              ? (postId ? "Updating..." : "Posting...") 
              : (postId ? "Update" : "Post")}
          </button>
        </div>
      </div>
    </div>
  );
};

export default PostForm;