import axios from "axios";

const BASE_URL = "http://localhost:8080/AlumniConnect/api";

const getAuthHeader = () => {
  const token = localStorage.getItem("token");
  return {
    headers: {
      Authorization: token,
    },
  };
};
const NotificationService = {
  getUserNotifications: async () => {
    try {
      const response = await axios.get(
        `${BASE_URL}/notifications`,
        getAuthHeader()
      );
      return response.data;
    } catch (error) {
      console.error("Error fetching notifications:", error);
      throw error;
    }
  },

  getUnreadNotifications: async () => {
    try {
      const response = await axios.get(
        `${BASE_URL}/notifications/unread`,
        getAuthHeader()
      );
      return response.data;
    } catch (error) {
      console.error("Error fetching unread notifications:", error);
      throw error;
    }
  },

  markAsRead: async (id) => {
    try {
      const response = await axios.post(
        `${BASE_URL}/notifications/${id}/read`,
        null,
        getAuthHeader()
      );
      return response.data;
    } catch (error) {
      console.error(`Error marking notification ${id} as read:`, error);
      throw error;
    }
  },

  markAllAsRead: async () => {
    try {
      const response = await axios.post(
        `${BASE_URL}/notifications/read-all`,
        null,
        getAuthHeader()
      );
      return response.data;
    } catch (error) {
      console.error("Error marking all notifications as read:", error);
      throw error;
    }
  },
};

export default NotificationService;
