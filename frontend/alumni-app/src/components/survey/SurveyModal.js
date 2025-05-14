import React, { useState } from "react";
import { X, Plus, Trash2, Check, ChevronDown } from "react-feather";
import SurveyTypeSelector from "./SurveyTypeSelector";

const SurveyModal = ({ show, onClose, onSubmit, loading, message }) => {
  const [surveyData, setSurveyData] = useState({
    content: "",
    surveyType: "TRAINING_PROGRAM",
    endTime: "",
    questions: [
      {
        question: "",
        multiChoice: false,
        options: [{ optionText: "" }, { optionText: "" }],
      },
    ],
  });

  if (!show) return null;

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setSurveyData({ ...surveyData, [name]: value });
  };

  const handleSurveyTypeChange = (type) => {
    setSurveyData({ ...surveyData, surveyType: type });
  };

  const handleQuestionChange = (index, field, value) => {
    const updatedQuestions = [...surveyData.questions];
    updatedQuestions[index][field] = value;
    setSurveyData({ ...surveyData, questions: updatedQuestions });
  };

  const handleOptionChange = (qIndex, oIndex, value) => {
    const updatedQuestions = [...surveyData.questions];
    updatedQuestions[qIndex].options[oIndex].optionText = value;
    setSurveyData({ ...surveyData, questions: updatedQuestions });
  };

  const addQuestion = () => {
    setSurveyData({
      ...surveyData,
      questions: [
        ...surveyData.questions,
        {
          question: "",
          multiChoice: false,
          options: [{ optionText: "" }, { optionText: "" }],
        },
      ],
    });
  };

  const removeQuestion = (index) => {
    if (surveyData.questions.length <= 1) return;
    const updatedQuestions = [...surveyData.questions];
    updatedQuestions.splice(index, 1);
    setSurveyData({ ...surveyData, questions: updatedQuestions });
  };

  const addOption = (qIndex) => {
    const updatedQuestions = [...surveyData.questions];
    updatedQuestions[qIndex].options.push({ optionText: "" });
    setSurveyData({ ...surveyData, questions: updatedQuestions });
  };

  const removeOption = (qIndex, oIndex) => {
    const updatedQuestions = [...surveyData.questions];
    if (updatedQuestions[qIndex].options.length <= 2) return;
    updatedQuestions[qIndex].options.splice(oIndex, 1);
    setSurveyData({ ...surveyData, questions: updatedQuestions });
  };

  const validateSurvey = () => {
    if (!surveyData.content.trim()) {
      return "Vui lòng nhập tiêu đề khảo sát";
    }

    if (!surveyData.endTime) {
      return "Vui lòng chọn thời gian kết thúc";
    }

    for (const q of surveyData.questions) {
      if (!q.question.trim()) {
        return "Vui lòng nhập nội dung cho tất cả câu hỏi";
      }

      for (const opt of q.options) {
        if (!opt.optionText.trim()) {
          return "Vui lòng nhập nội dung cho tất cả lựa chọn";
        }
      }
    }

    return null;
  };

  const handleSubmit = (e) => {
    if (e && typeof e.preventDefault === "function") {
      e.preventDefault();
    }
    const error = validateSurvey();
    if (error) {
      return onSubmit({ error });
    }
    onSubmit(surveyData);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 z-50 flex justify-center items-center p-4">
      <div className="bg-white rounded-xl shadow-xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        <div className="sticky top-0 bg-white p-6 pb-4 border-b flex justify-between items-center">
          <h2 className="text-2xl font-bold text-gray-800">Tạo khảo sát mới</h2>
          <button
            onClick={onClose}
            className="text-gray-500 hover:text-gray-700 transition"
            aria-label="Đóng"
          >
            <X size={24} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-6">
          {/* Survey Content */}
          <div className="space-y-2">
            <label className="block text-sm font-medium text-gray-700">
              Tiêu đề khảo sát <span className="text-red-500">*</span>
            </label>
            <input
              type="text"
              name="content"
              value={surveyData.content}
              onChange={handleInputChange}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              placeholder="Ví dụ: Khảo sát sở thích hoạt động ngoại khóa"
              required
            />
          </div>

          {/* Survey Type - Sử dụng component đã có */}
          <SurveyTypeSelector
            value={surveyData.surveyType}
            onChange={handleSurveyTypeChange}
          />

          {/* End Time */}
          <div className="space-y-2">
            <label className="block text-sm font-medium text-gray-700">
              Thời gian kết thúc <span className="text-red-500">*</span>
            </label>
            <input
              type="datetime-local"
              name="endTime"
              value={surveyData.endTime}
              onChange={handleInputChange}
              min={new Date().toISOString().slice(0, 16)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              required
            />
          </div>

          {/* Questions Section */}
          <div className="space-y-6">
            <div className="flex justify-between items-center">
              <h3 className="text-lg font-semibold text-gray-800">
                Danh sách câu hỏi <span className="text-red-500">*</span>
              </h3>
              <button
                type="button"
                onClick={addQuestion}
                className="flex items-center text-blue-600 hover:text-blue-800 text-sm font-medium"
              >
                <Plus size={16} className="mr-1" /> Thêm câu hỏi
              </button>
            </div>

            {surveyData.questions.map((q, qIndex) => (
              <div
                key={qIndex}
                className="bg-gray-50 p-4 rounded-lg border border-gray-200 transition-all"
              >
                <div className="flex justify-between items-start mb-3">
                  <h4 className="font-medium text-gray-700">
                    Câu hỏi {qIndex + 1}
                  </h4>
                  {surveyData.questions.length > 1 && (
                    <button
                      type="button"
                      onClick={() => removeQuestion(qIndex)}
                      className="text-red-500 hover:text-red-700 p-1 transition"
                      title="Xóa câu hỏi"
                      aria-label={`Xóa câu hỏi ${qIndex + 1}`}
                    >
                      <Trash2 size={16} />
                    </button>
                  )}
                </div>

                {/* Question Text */}
                <div className="mb-4">
                  <input
                    type="text"
                    value={q.question}
                    onChange={(e) =>
                      handleQuestionChange(qIndex, "question", e.target.value)
                    }
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    placeholder="Nhập nội dung câu hỏi"
                    required
                  />
                </div>

                {/* Multi-choice Toggle */}
                <div className="flex items-center mb-4">
                  <label className="inline-flex items-center cursor-pointer space-x-3">
                    <div className="relative">
                      <input
                        type="checkbox"
                        checked={q.multiChoice}
                        onChange={(e) =>
                          handleQuestionChange(
                            qIndex,
                            "multiChoice",
                            e.target.checked
                          )
                        }
                        className="sr-only peer"
                      />
                      <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full rtl:peer-checked:after:-translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:start-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
                    </div>
                    <span className="text-sm font-medium text-gray-700">
                      Cho phép chọn nhiều đáp án
                    </span>
                  </label>
                </div>

                {/* Options */}
                <div className="space-y-2">
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Các lựa chọn <span className="text-red-500">*</span>
                  </label>
                  {q.options.map((opt, oIndex) => (
                    <div key={oIndex} className="flex items-center group">
                      <div className="flex-1 relative flex items-center">
                        <div className="absolute left-3 text-gray-400">
                          {q.multiChoice ? (
                            <Check size={16} />
                          ) : (
                            <div className="w-4 h-4 rounded-full border border-gray-400"></div>
                          )}
                        </div>
                        <input
                          type="text"
                          value={opt.optionText}
                          onChange={(e) =>
                            handleOptionChange(qIndex, oIndex, e.target.value)
                          }
                          className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                          placeholder={`Lựa chọn ${oIndex + 1}`}
                          required
                        />
                      </div>
                      {q.options.length > 2 && (
                        <button
                          type="button"
                          onClick={() => removeOption(qIndex, oIndex)}
                          className="ml-2 text-red-500 hover:text-red-700 p-1 opacity-0 group-hover:opacity-100 transition"
                          title="Xóa lựa chọn"
                          aria-label={`Xóa lựa chọn ${oIndex + 1}`}
                        >
                          <Trash2 size={16} />
                        </button>
                      )}
                    </div>
                  ))}
                  <button
                    type="button"
                    onClick={() => addOption(qIndex)}
                    className="flex items-center text-blue-600 hover:text-blue-800 text-sm mt-2 transition"
                  >
                    <Plus size={16} className="mr-1" /> Thêm lựa chọn
                  </button>
                </div>
              </div>
            ))}
          </div>

          {/* Message & Submit */}
          <div className="pt-4 border-t">
            {message && (
              <p
                className={`text-sm text-center font-medium mb-4 ${
                  message.error ? "text-red-500" : "text-green-500"
                }`}
              >
                {message.error || message.success}
              </p>
            )}
            <div className="flex justify-end space-x-3">
              <button
                type="button"
                onClick={onClose}
                className="px-5 py-2.5 rounded-lg border border-gray-300 text-gray-700 hover:bg-gray-50 font-medium transition"
                disabled={loading}
              >
                Hủy bỏ
              </button>
              <button
                type="submit"
                className="px-5 py-2.5 rounded-lg bg-blue-600 text-white hover:bg-blue-700 font-medium transition flex items-center justify-center min-w-24 disabled:opacity-70"
                disabled={loading}
              >
                {loading ? (
                  <>
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
                    Đang tạo...
                  </>
                ) : (
                  "Tạo khảo sát"
                )}
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default SurveyModal;
