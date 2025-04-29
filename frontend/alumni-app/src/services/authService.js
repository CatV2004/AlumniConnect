import axios from 'axios';

const BASE_URL = 'http://localhost:8080/AlumniConnect/api';

export const login = async ({ username, password, role }) => {
  const response = await axios.post(`${BASE_URL}/login`, { username, password, role });
  return response.data;
};

export const register = async (userData) => {
  const response = await axios.post(`${BASE_URL}/users`, userData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return response.data;
};

export const getCurrentUser = async (token) => {
  const response = await axios.get(`${BASE_URL}/current-user`, {
    headers: { Authorization: `${token}` },
  });
  return response.data;
};
