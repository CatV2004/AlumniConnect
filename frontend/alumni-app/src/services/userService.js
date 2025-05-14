import axios from "axios";
const BASE_URL = "http://localhost:8080/AlumniConnect/api";

export const getUserProfile = async (userId, token) => {
  const response = await axios.get(`${BASE_URL}/users/${userId}`, {
    headers: { Authorization: token },
  });
  return response.data;
};


export const getUserPosts = async (userId, token, page = 0, size = 10) => {
  try {
    const response = await axios.get(`${BASE_URL}/user/${userId}/posts`, {
      headers: {
        Authorization: token,
      },
      params: {
        page,
        size,
      },
    });
    console.log("data posts of user: ", response.data);
    return response.data;
  } catch (error) {
    console.error("Error fetching user posts:", error);
    throw error;
  }
};


export const getAllUsers = async ({ page = 1, size = 10, keyword = "" }, token) => {
  try {
    const params = { page, size };
    if (keyword.trim() !== "") {
      params.keyword = keyword;
    }

    const response = await axios.get(`${BASE_URL}/users`, {
      params,
      headers: {
        Authorization: token
      }
    });

    return response.data;
  } catch (error) {
    console.error("Lỗi API getAllUsers:", {
      message: error.message,
      response: error.response?.data,
      config: error.config
    });
    throw error;
  }
};

