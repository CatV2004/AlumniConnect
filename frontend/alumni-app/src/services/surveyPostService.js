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
    if(error.response)
      throw error.response?.data;
    throw error
  }
};

export const getSurveyStatistics = async (surveyPostId, token) => {
  try {
    const response = await axios.get(
      `${BASE_URL}/statistics/${surveyPostId}`,
      {
        headers: {
          Authorization: token,
        },
      }
    );
    return response.data; 
  } catch (error) {
    console.error("Error fetching survey statistics:", error);
    throw error;
  }
};

export const checkUserHasAnsweredSurvey = async (surveyPostId, token) => {
  try {
    const response = await axios.get(
      `${BASE_URL}/user-survey-options/has-answered`,
      {
        params: {
          surveyPostId,
        },
        headers: {
          Authorization: token,
        },
      }
    );

    return response.data; //  true hoặc false
  } catch (error) {
    console.error("Error checking survey answer status:", error);
    throw error;
  }
};

export const selectMultipleSurveyOptions = async (answers, token) => {
  try {
    const response = await axios.post(
      `${BASE_URL}/user-survey-options/select-multiple`,
      {
        answers: answers,
      },
      {
        headers: {
          Authorization: token,
          "Content-Type": "application/json",
        },
      }
    );

    return response.data;
  } catch (error) {
    console.error(
      "Error selecting multiple survey options:",
      error.response ? error.response.data : error.message
    );
    throw new Error("Có lỗi xảy ra khi gửi lựa chọn khảo sát.");
  }
};
