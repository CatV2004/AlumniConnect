import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getPostById } from "../services/postService";
import { useSelector } from "react-redux";
import { formatDateFromArray } from "../app/utils/dateUtils";
import { motion } from "framer-motion";
import { FiArrowLeft, FiCheck, FiClock } from "react-icons/fi";
import ProgressBar from "../components/ProgressBar";

const SurveyDetailPage = () => {
  const { postId } = useParams();
  const navigate = useNavigate();
  const token = useSelector((state) => state.auth.token);
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [responses, setResponses] = useState({});
  const [submitting, setSubmitting] = useState(false);
  const [progress, setProgress] = useState(0);

  useEffect(() => {
    const fetchSurvey = async () => {
      try {
        const data = await getPostById(postId, token);
        setPost(data);

        if (data.surveyPost && data.surveyPost.questions) {
          const initialResponses = {};
          data.surveyPost.questions.forEach((q, qIndex) => {
            initialResponses[qIndex] = q.multiChoice ? [] : null;
          });
          setResponses(initialResponses);
          calculateProgress(initialResponses, data.surveyPost.questions.length);
        } else {
          throw new Error("Survey data is incomplete");
        }
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchSurvey();
  }, [postId, token]);

  const calculateProgress = (currentResponses, totalQuestions) => {
    const answeredQuestions = Object.values(currentResponses).filter(
      (response) => response !== null && (Array.isArray(response) ? response.length > 0 : true)
    ).length;
    const newProgress = (answeredQuestions / totalQuestions) * 100;
    setProgress(newProgress);
  };

  const handleResponseChange = (qIndex, optionIndex, isMulti, checked) => {
    setResponses((prev) => {
      let newResponses;
      if (isMulti) {
        const newArray = [...(prev[qIndex] || [])];
        if (checked) {
          newArray.push(optionIndex);
        } else {
          const index = newArray.indexOf(optionIndex);
          if (index > -1) {
            newArray.splice(index, 1);
          }
        }
        newResponses = { ...prev, [qIndex]: newArray };
      } else {
        newResponses = { ...prev, [qIndex]: optionIndex };
      }
      
      // Calculate progress after state update
      setTimeout(() => {
        calculateProgress(newResponses, post.surveyPost.questions.length);
      }, 0);
      
      return newResponses;
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      // await submitSurveyResponse(postId, responses, token);
      navigate(`/post/${postId}`, { state: { surveySubmitted: true } });
    } catch (err) {
      alert("Failed to submit survey: " + err.message);
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="text-center">
        <div className="w-16 h-16 border-4 border-blue-500 border-t-transparent rounded-full animate-spin mx-auto"></div>
        <p className="mt-4 text-lg font-medium text-gray-700">Đang tải khảo sát...</p>
      </div>
    </div>
  );

  if (error) return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="text-center p-6 max-w-md bg-white rounded-lg shadow-md">
        <div className="text-red-500 text-4xl mb-4">⚠️</div>
        <h2 className="text-xl font-bold text-gray-800 mb-2">Đã xảy ra lỗi</h2>
        <p className="text-gray-600 mb-4">{error}</p>
        <button
          onClick={() => window.location.reload()}
          className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition"
        >
          Thử lại
        </button>
      </div>
    </div>
  );

  if (!post || !post.surveyPost) return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="text-center p-6 max-w-md bg-white rounded-lg shadow-md">
        <div className="text-gray-500 text-4xl mb-4">🔍</div>
        <h2 className="text-xl font-bold text-gray-800 mb-2">Không tìm thấy khảo sát</h2>
        <p className="text-gray-600 mb-4">Khảo sát bạn đang tìm kiếm không tồn tại hoặc đã bị xóa.</p>
        <button
          onClick={() => navigate("home")}
          className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition"
        >
          Quay lại
        </button>
      </div>
    </div>
  );

  const survey = post.surveyPost;
  const endTimeFormatted = formatDateFromArray(survey.endTime, { includeTime: true });
  const [dateStr, timeStr] = endTimeFormatted.split(", ");

  return (
    <div className="min-h-screen bg-gray-50 py-8 px-4 sm:px-6 lg:px-8">
      <div className="max-w-3xl mx-auto">
        {/* Back button */}
        <button
          onClick={() => navigate(-1)}
          className="flex items-center text-blue-600 hover:text-blue-800 mb-6 transition"
        >
          <FiArrowLeft className="mr-2" />
          Quay lại
        </button>

        {/* Survey header */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.3 }}
          className="bg-white rounded-xl shadow-sm p-6 mb-6"
        >
          <h1 className="text-2xl sm:text-3xl font-bold text-gray-800 mb-3">{post.content}</h1>
          
          <div className="flex flex-wrap items-center gap-4 text-sm text-gray-600">
            <div className="flex items-center bg-blue-50 px-3 py-1 rounded-full">
              <FiClock className="mr-2 text-blue-600" />
              <span className="font-medium">Kết thúc: {dateStr} {/* lúc {timeStr} */}</span>
            </div>
            
            <div className="flex items-center bg-green-50 px-3 py-1 rounded-full">
              <FiCheck className="mr-2 text-green-600" />
              <span className="font-medium">
                {survey.questions.length} câu hỏi
              </span>
            </div>
          </div>
        </motion.div>

        {/* Progress bar */}
        <div className="mb-8">
          <div className="flex justify-between items-center mb-2">
            <span className="text-sm font-medium text-gray-700">
              Tiến độ hoàn thành
            </span>
            <span className="text-sm font-medium text-blue-600">
              {Math.round(progress)}%
            </span>
          </div>
          <ProgressBar value={progress} />
        </div>

        {/* Survey form */}
        <motion.form
          onSubmit={handleSubmit}
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.1, duration: 0.3 }}
          className="space-y-6"
        >
          {survey.questions.map((question, qIndex) => (
            <motion.div
              key={qIndex}
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.1 + qIndex * 0.05 }}
              className="bg-white rounded-xl shadow-sm overflow-hidden"
            >
              <div className="p-6">
                <div className="flex justify-between items-start">
                  <h3 className="text-lg font-medium text-gray-800 mb-4">
                    <span className="text-blue-600 mr-2">Câu {qIndex + 1}:</span>
                    {question.question}
                  </h3>
                  {question.multiChoice && (
                    <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-purple-100 text-purple-800">
                      Nhiều lựa chọn
                    </span>
                  )}
                </div>

                <div className="space-y-3">
                  {question.options.map((option, oIndex) => (
                    <div
                      key={oIndex}
                      className={`p-3 rounded-lg border transition ${
                        (question.multiChoice
                          ? responses[qIndex]?.includes(oIndex)
                          : responses[qIndex] === oIndex)
                          ? "border-blue-500 bg-blue-50"
                          : "border-gray-200 hover:border-gray-300"
                      }`}
                    >
                      <label className="flex items-center cursor-pointer">
                        <input
                          type={question.multiChoice ? "checkbox" : "radio"}
                          className={`form-${question.multiChoice ? "checkbox" : "radio"} text-blue-600 focus:ring-blue-500 h-4 w-4`}
                          checked={
                            question.multiChoice
                              ? responses[qIndex]?.includes(oIndex)
                              : responses[qIndex] === oIndex
                          }
                          onChange={(e) =>
                            handleResponseChange(
                              qIndex,
                              oIndex,
                              question.multiChoice,
                              e.target.checked
                            )
                          }
                        />
                        <span className="ml-3 text-gray-700">{option.optionText}</span>
                      </label>
                    </div>
                  ))}
                </div>
              </div>
            </motion.div>
          ))}

          <div className="sticky bottom-0 bg-white py-4 border-t border-gray-200">
            <div className="flex justify-end">
              <button
                type="submit"
                disabled={submitting || progress < 100}
                className={`px-6 py-3 rounded-lg font-medium transition ${
                  submitting
                    ? "bg-blue-400 cursor-not-allowed"
                    : progress < 100
                    ? "bg-gray-300 text-gray-500 cursor-not-allowed"
                    : "bg-blue-600 hover:bg-blue-700 text-white shadow-md"
                }`}
              >
                {submitting ? (
                  <span className="flex items-center">
                    <svg
                      className="animate-spin -ml-1 mr-2 h-4 w-4 text-white"
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                    >
                      <circle
                        className="opacity-25"
                        cx="12"
                        cy="12"
                        r="10"
                        stroke="currentColor"
                        strokeWidth="4"
                      ></circle>
                      <path
                        className="opacity-75"
                        fill="currentColor"
                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                      ></path>
                    </svg>
                    Đang gửi...
                  </span>
                ) : (
                  "Gửi khảo sát"
                )}
              </button>
            </div>
          </div>
        </motion.form>
      </div>
    </div>
  );
};

export default SurveyDetailPage;