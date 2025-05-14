import React from 'react';
import { Button } from '@material-ui/core';
import { BarChartOutlined } from '@material-ui/icons';

const SurveyStatsButton = ({ onClick, disabled }) => {
  return (
    <Button
      variant="outlined"
      color="primary"
      startIcon={<BarChartOutlined />}
      onClick={onClick}
      disabled={disabled}
    >
      Xem thống kê
    </Button>
  );
};

export default SurveyStatsButton;