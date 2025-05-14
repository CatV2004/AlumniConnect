import React, { useState, useEffect } from "react";
import { 
  Modal, 
  Backdrop, 
  Fade, 
  CircularProgress, 
  Typography,
  Button,
  IconButton,
  Tooltip
} from "@mui/material";
import { Fullscreen, Close } from "@mui/icons-material"; // Thêm icon Fullscreen
import { useNavigate } from "react-router-dom"; // Thêm hook navigate
import { getSurveyStatistics } from "../../services/surveyPostService";
import SurveyStatsCard from "./SurveyStatistics/SurveyStatsCard";
import QuestionStats from "./SurveyStatistics/QuestionStats";

const SurveyStatsModal = ({ open, onClose, surveyPostId, token }) => {
  const [statsData, setStatsData] = useState({
    statistics: [],
    participantCount: 0
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate(); // Sử dụng hook navigate

  useEffect(() => {
    if (open && surveyPostId) {
      fetchStats();
    }
  }, [open, surveyPostId]);

  const fetchStats = async () => {
    try {
      setLoading(true);
      const data = await getSurveyStatistics(surveyPostId, token);
      setStatsData(data);
      setError(null);
    } catch (err) {
      setError("Không thể tải thống kê. Vui lòng thử lại sau.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleViewFullPage = () => {
    navigate(`/surveys/stats/${surveyPostId}`);
    onClose(); // Đóng modal sau khi chuyển trang
  };

  return (
    <Modal
      open={open}
      onClose={onClose}
      closeAfterTransition
      style={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        zIndex: 1300,
      }}
    >
      <Fade in={open}>
        <div className="bg-white rounded-lg shadow-xl max-w-4xl w-full max-h-[90vh] overflow-y-auto relative">
          {/* Thêm nút mở rộng ở góc trên bên phải */}
          <div className="absolute top-4 right-4 flex space-x-2">
            <Tooltip title="Xem trang đầy đủ">
              <IconButton
                onClick={handleViewFullPage}
                color="primary"
                aria-label="view-full-page"
              >
                <Fullscreen />
              </IconButton>
            </Tooltip>
            <Tooltip title="Đóng">
              <IconButton
                onClick={onClose}
                color="inherit"
                aria-label="close"
              >
                <Close />
              </IconButton>
            </Tooltip>
          </div>

          <div className="p-6 pt-16"> {/* Thêm padding-top để tránh bị che bởi nút */}
            <Typography variant="h5" component="h2" className="mb-4">
              Thống kê khảo sát
            </Typography>

            {loading ? (
              <div className="flex justify-center py-8">
                <CircularProgress />
              </div>
            ) : error ? (
              <div className="text-red-500 text-center py-4">{error}</div>
            ) : (
              <>
                <SurveyStatsCard 
                  statistics={statsData.statistics} 
                  participantCount={statsData.participantCount} 
                />
                <div className="mt-6 space-y-8">
                  {groupByQuestion(statsData.statistics).map((question) => (
                    <QuestionStats
                      key={question.questionId}
                      question={question}
                      participantCount={statsData.participantCount}
                    />
                  ))}
                </div>
                
                {/* Thêm nút ở cuối modal */}
                <div className="mt-6 flex justify-end">
                  <Button
                    variant="outlined"
                    startIcon={<Fullscreen />}
                    onClick={handleViewFullPage}
                  >
                    Xem trang đầy đủ
                  </Button>
                </div>
              </>
            )}
          </div>
        </div>
      </Fade>
    </Modal>
  );
};

// Hàm nhóm các option theo question (giữ nguyên)
const groupByQuestion = (stats) => {
  const questionsMap = new Map();

  stats.forEach((item) => {
    if (!questionsMap.has(item.questionId)) {
      questionsMap.set(item.questionId, {
        questionId: item.questionId,
        questionText: item.question,
        options: [],
      });
    }
    questionsMap.get(item.questionId).options.push({
      optionId: item.optionId,
      optionText: item.optionText,
      totalSelected: item.totalSelected,
    });
  });

  return Array.from(questionsMap.values());
};

export default SurveyStatsModal;