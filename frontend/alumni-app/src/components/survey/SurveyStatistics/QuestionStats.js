import React from 'react';
import { Card, CardContent, Typography, Divider } from '@mui/material'; 
import { Info } from '@mui/icons-material';  
import StatsChart from './StatsChart';
import StatsTable from './StatsTable';

const QuestionStats = ({ question }) => {
  const totalResponses = question.options.reduce((sum, option) => sum + option.totalSelected, 0);

  return (
    <Card>
      <CardContent>
        <Typography variant="subtitle1" className="font-semibold mb-2">
          {question.questionText}
        </Typography>
        <Typography variant="body2" color="textSecondary" className="mb-4">
          Tổng câu trả lời: {totalResponses}
        </Typography>
        
        <div className="mb-4">
          <StatsChart options={question.options} totalResponses={totalResponses} />
        </div>
        
        <Divider className="my-4" />
        
        <StatsTable options={question.options} />
        
        <div className="mt-4">
          <Info fontSize="large" />  {/* Example icon */}
        </div>
      </CardContent>
    </Card>
  );
};

export default QuestionStats;
