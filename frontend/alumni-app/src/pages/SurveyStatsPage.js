import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { 
  Container, 
  CircularProgress, 
  Typography, 
  Button,
  Box,
  Alert
} from '@mui/material';
import { FaArrowLeft } from 'react-icons/fa';
import SurveyStatsCard from '../components/survey/SurveyStatistics/SurveyStatsCard';
import QuestionStats from '../components/survey/SurveyStatistics/QuestionStats';
import { getSurveyStatistics } from '../services/surveyPostService';

const SurveyStatsPage = () => {
  const { surveyPostId } = useParams();
  const navigate = useNavigate();
  const { token, role } = useSelector(state => state.auth);
  const [statsData, setStatsData] = useState({
    statistics: [],
    participantCount: 0
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Chỉ cho phép Admin truy cập
  useEffect(() => {
    if (role !== 'ADMIN') {
      navigate('/unauthorized', { replace: true });
    }
  }, [role, navigate]);

  useEffect(() => {
    if (role === 'ADMIN' && surveyPostId) {
      fetchStats();
    }
  }, [surveyPostId, role]);

  const fetchStats = async () => {
    try {
      setLoading(true);
      const data = await getSurveyStatistics(surveyPostId, token);
      setStatsData(data);
      setError(null);
    } catch (err) {
      setError(err.response?.data?.message || 'Không thể tải thống kê. Vui lòng thử lại sau.');
    } finally {
      setLoading(false);
    }
  };

  const handleBack = () => {
    navigate(-1);
  };

  if (role !== 'ADMIN') {
    return null; // Hoặc có thể redirect ngay trong useEffect
  }

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Button 
        startIcon={<FaArrowLeft />} 
        onClick={handleBack}
        sx={{ mb: 3 }}
        variant="outlined"
      >
        Quay lại
      </Button>

      <Typography variant="h4" component="h1" sx={{ mb: 4 }}>
        Thống kê khảo sát chi tiết
      </Typography>

      {loading ? (
        <Box display="flex" justifyContent="center" py={8}>
          <CircularProgress size={60} />
        </Box>
      ) : error ? (
        <Alert severity="error" sx={{ mb: 4 }}>
          {error}
        </Alert>
      ) : (
        <>
          <SurveyStatsCard 
            statistics={statsData.statistics} 
            participantCount={statsData.participantCount} 
          />
          
          <Box sx={{ mt: 4, display: 'flex', flexDirection: 'column', gap: 4 }}>
            {groupByQuestion(statsData.statistics).map((question) => (
              <QuestionStats 
                key={question.questionId} 
                question={question}
                participantCount={statsData.participantCount}
              />
            ))}
          </Box>
        </>
      )}
    </Container>
  );
};

// Hàm nhóm các option theo question
const groupByQuestion = (stats) => {
  if (!stats) return [];
  
  const questionsMap = new Map();
  
  stats.forEach((item) => {
    if (!questionsMap.has(item.questionId)) {
      questionsMap.set(item.questionId, {
        questionId: item.questionId,
        questionText: item.question,
        options: []
      });
    }
    questionsMap.get(item.questionId).options.push({
      optionId: item.optionId,
      optionText: item.optionText,
      totalSelected: item.totalSelected
    });
  });
  
  return Array.from(questionsMap.values());
};

export default SurveyStatsPage;