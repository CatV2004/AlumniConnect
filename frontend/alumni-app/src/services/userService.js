import axios from 'axios';
const BASE_URL = 'http://localhost:8080/AlumniConnect/api';

export const getUserProfile = async (userId, token) => {
  const response = await axios.get(`${BASE_URL}/users/${userId}`, {
    headers: { Authorization: token },
  });
  return response.data;
};

export const getUserPosts = async (userId, token) => {
  const response = await axios.get(`${BASE_URL}/users/${userId}/posts`, {
    headers: { Authorization: token },
  });
  return response.data;
};
