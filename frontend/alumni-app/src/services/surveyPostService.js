import axios from "axios";

const BASE_URL = "http://localhost:8080/AlumniConnect/api";

export const createSurvey = async (payload, token) => {
  try {
    const response = await axios.post(`${BASE_URL}/survey-posts`, payload, {
      headers: {
        Authorization: token,
        "Content-Type": "application/json",
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error creating survey:", error);
    throw error;
  }
};