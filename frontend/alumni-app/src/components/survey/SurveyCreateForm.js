import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { createSurvey } from "../../services/surveyPostService";
import SurveyModal from "./SurveyModal";
import { fetchSurveyPosts } from "../../features/posts/postSlice";

const SurveyCreateForm = () => {
  const dispatch = useDispatch();
  const token = useSelector((state) => state.auth.token);
  const user = useSelector((state) => state.auth.user);
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState({});

  const handleSubmit = async (surveyData) => {
    setLoading(true);
    setMessage({});

    try {
      // Kiểm tra nếu surveyData có error (từ validate)
      // if (surveyData.error) {
      //   setMessage({ error: surveyData.error });
      //   return;
      // }

      await createSurvey(surveyData, token);

      setMessage({ success: "✅ Tạo khảo sát thành công!" });
      dispatch(fetchSurveyPosts({ page: 1, size: 5, refresh: true }));
      setTimeout(() => setShowModal(false), 2000); // Tự động đóng sau 1.5s
    } catch (err) {
      setMessage({ error: "❌ Tạo khảo sát thất bại: " + err.message });
      console.log("Lỗi tại: ",err)
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="mb-8">
      <div className="flex justify-between items-center bg-white px-4 py-3 shadow rounded-md">
        {/* Avatar + Name */}
        <div className="flex items-center space-x-3">
          <img
            src={user?.avatar || "/default-avatar.png"}
            alt="Avatar"
            className="w-10 h-10 rounded-full border object-cover"
          />
          <div>
            <p className="font-medium text-gray-800">
              {user?.firstName && user?.lastName
                ? `${user.firstName} ${user.lastName}`
                : "Admin"}
            </p>
            <p className="text-sm text-gray-500">Quản trị viên</p>
          </div>
        </div>

        {/* Create Survey Button */}
        <button
          onClick={() => setShowModal(true)}
          className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded shadow transition"
        >
          + Tạo khảo sát mới
        </button>
      </div>

      <SurveyModal
        show={showModal}
        onClose={() => {
          setShowModal(false);
          setMessage({});
        }}
        onSubmit={handleSubmit}
        loading={loading}
        message={message}
      />
    </div>
  );
};

export default SurveyCreateForm;
