import axios from "axios";
const BASE_URL = "http://localhost:8080/AlumniConnect/api";

export const fetchPosts = async (params = {}) => {
  try {
    const response = await axios.get(`${BASE_URL}/posts`, {
      params,
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching posts:", error);
    throw error;
  }
};

export const createPost = async (formData, token) => {
  try {
    const response = await axios.post(`${BASE_URL}/posts`, formData, {
      headers: {
        Authorization: token,
        "Content-Type": "multipart/form-data",
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error creating post:", error);
    throw error;
  }
};

export const updatePost = async (postId, formData, token) => {
  try {
    const response = await axios.put(`${BASE_URL}/posts/${postId}`, formData, {
      headers: {
        Authorization: token,
        "Content-Type": "multipart/form-data",
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error updating post:", error);
    throw error;
  }
};

export const getPostById = async (postId) => {
  try {
    const response = await axios.get(`${BASE_URL}/posts/${postId}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching post by ID:", error);
    throw error;
  }
};

// Method để cập nhật ảnh
export const updateImage = async (imageId, imageFile, token) => {
  try {
    const formData = new FormData();
    formData.append("image", imageFile); // append file vào form data

    const response = await axios.put(
      `${BASE_URL}/post-images/${imageId}`,
      formData,
      {
        headers: {
          Authorization: token,
          "Content-Type": "multipart/form-data",
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error updating image:", error);
    throw error;
  }
};

//upload ảnh post lên
export const uploadPostImages = async (postId, imageFiles, token) => {
  try {
    const formData = new FormData();
    imageFiles.forEach((file) => {
      formData.append("images", file);
    });

    const response = await axios.post(
      `${BASE_URL}/posts/${postId}/images`,
      formData,
      {
        headers: {
          Authorization: token,
          "Content-Type": "multipart/form-data",
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error uploading post images:", error);
    throw error;
  }
};

// Method để xóa ảnh
export const deleteImage = async (imageId, token) => {
  try {
    const response = await axios.delete(`${BASE_URL}/post-images/${imageId}`, {
      headers: {
        Authorization: token,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error deleting image:", error);
    throw error;
  }
};

// Xóa mềm bài viết
export const softDeletePost = async (postId, token) => {
  try {
    const response = await axios.put(
      `${BASE_URL}/posts/${postId}`,
      {},
      {
        headers: {
          Authorization: token,
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error soft deleting post:", error);
    throw error;
  }
};

// Khôi phục bài viết
export const restorePost = async (postId, token) => {
  try {
    const response = await axios.put(
      `${BASE_URL}/posts/${postId}/restore`,
      {},
      {
        headers: {
          Authorization: token,
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error restoring post:", error);
    throw error;
  }
};

// Xóa cứng bài viết
export const forceDeletePost = async (postId, token) => {
  try {
    const response = await axios.put(
      `${BASE_URL}/posts/${postId}/force`,
      {},
      {
        headers: {
          Authorization: token,
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error force deleting post:", error);
    throw error;
  }
};

// Lấy danh sách các bài viết đã bị xóa mềm
export const getDeletedPosts = async (token) => {
  try {
    const response = await axios.get(
      `${BASE_URL}/posts/deleted`,
      {
        headers: {
          Authorization: token,
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error fetching deleted posts:", error);
    throw error;
  }
};
