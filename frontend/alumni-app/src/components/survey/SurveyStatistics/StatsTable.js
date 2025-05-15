import React from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';  // Dùng MUI v5
import { CheckCircle, Cancel } from '@mui/icons-material';  // Ví dụ các icon từ MUI

const StatsTable = ({ options }) => {
  const totalResponses = options.reduce((sum, option) => sum + option.totalSelected, 0);

  return (
    <TableContainer component={Paper} variant="outlined">
      <Table size="small">
        <TableHead>
          <TableRow>
            <TableCell>Lựa chọn</TableCell>
            <TableCell align="right">Số lượt chọn</TableCell>
            <TableCell align="right">Tỷ lệ</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {options.map((option) => (
            <TableRow key={option.optionId}>
              <TableCell>{option.optionText}</TableCell>
              <TableCell align="right">{option.totalSelected}</TableCell>
              <TableCell align="right">
                {totalResponses > 0 
                  ? `${Math.round((option.totalSelected / totalResponses) * 100)}%` 
                  : '0%'}
              </TableCell>
              {/* Bạn có thể thêm icon để minh họa cho lựa chọn */}
              <TableCell align="right">
                {option.totalSelected > 0 ? <CheckCircle color="success" /> : <Cancel color="error" />}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default StatsTable;
