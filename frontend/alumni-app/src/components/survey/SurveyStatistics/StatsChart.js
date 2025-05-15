import React from 'react';
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const StatsChart = ({ options, totalResponses }) => {
  const data = {
    labels: options.map(option => option.optionText),
    datasets: [
      {
        label: 'Số lượt chọn',
        data: options.map(option => option.totalSelected),
        backgroundColor: 'rgba(54, 162, 235, 0.6)',
        borderColor: 'rgba(54, 162, 235, 1)',
        borderWidth: 1,
      },
      {
        label: 'Tỷ lệ %',
        data: options.map(option => Math.round((option.totalSelected / totalResponses) * 100)),
        backgroundColor: 'rgba(75, 192, 192, 0.6)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1,
        yAxisID: 'y1',
      }
    ]
  };

  const chartOptions = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
      },
      tooltip: {
        callbacks: {
          label: function(context) {
            let label = context.dataset.label || '';
            if (label) {
              label += ': ';
            }
            if (context.datasetIndex === 1) {
              label += context.raw + '%';
            } else {
              label += context.raw;
            }
            return label;
          }
        }
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        title: {
          display: true,
          text: 'Số lượt chọn'
        }
      },
      y1: {
        position: 'right',
        beginAtZero: true,
        max: 100,
        title: {
          display: true,
          text: 'Tỷ lệ %'
        },
        grid: {
          drawOnChartArea: false,
        },
      }
    }
  };

  return <Bar data={data} options={chartOptions} />;
};

export default StatsChart;