import React from 'react';
import { Card, CardContent, Typography, Grid } from '@mui/material';
import { QuestionAnswer, People, BarChart } from '@mui/icons-material';

const SurveyStatsCard = ({ statistics, participantCount }) => {
  const totalQuestions = new Set(statistics.map(item => item.questionId)).size;
  const averagePerQuestion = totalQuestions > 0 
    ? Math.round(participantCount / totalQuestions) 
    : 0;

  return (
    <Card className="mb-6">
      <CardContent>
        <Typography variant="h6" className="mb-4">
          Tổng quan khảo sát
        </Typography>
        <Grid container spacing={3}>
          <Grid item xs={12} sm={4}>
            <div className="flex items-center">
              <QuestionAnswer className="text-blue-500 mr-2" fontSize="large" />
              <div>
                <Typography variant="body2" color="textSecondary">
                  Số câu hỏi
                </Typography>
                <Typography variant="h5">{totalQuestions}</Typography>
              </div>
            </div>
          </Grid>
          <Grid item xs={12} sm={4}>
            <div className="flex items-center">
              <People className="text-green-500 mr-2" fontSize="large" />
              <div>
                <Typography variant="body2" color="textSecondary">
                  Số người tham gia
                </Typography>
                <Typography variant="h5">{participantCount}</Typography>
              </div>
            </div>
          </Grid>
          <Grid item xs={12} sm={4}>
            <div className="flex items-center">
              <BarChart className="text-purple-500 mr-2" fontSize="large" />
              <div>
                <Typography variant="body2" color="textSecondary">
                  Trung bình mỗi câu
                </Typography>
                <Typography variant="h5">{averagePerQuestion}</Typography>
              </div>
            </div>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
  );
};

export default SurveyStatsCard;