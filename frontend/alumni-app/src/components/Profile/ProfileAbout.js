import React from 'react';
import { formatDateFromArray } from '../../app/utils/dateUtils';

const ProfileAbout = ({ user }) => {
  const infoSections = [
    { title: 'Thông tin cơ bản', items: [
      { label: 'Vai trò', value: user.role },
      { label: 'Ngày tham gia', value: formatDateFromArray(user.createdDate) }
    ]},
    { title: 'Liên hệ', items: [
      { label: 'Email', value: user.email },
      { label: 'Điện thoại', value: user.phone }
    ]}
  ];

  return (
    <div className="bg-white p-4 rounded-xl shadow">
      <h2 className="text-xl font-bold mb-4">Giới thiệu</h2>
      
      {infoSections.map((section, index) => (
        <div key={index} className="mb-6">
          <h3 className="font-semibold text-lg mb-2">{section.title}</h3>
          <div className="space-y-2">
            {section.items.map((item, idx) => (
              item.value && (
                <div key={idx} className="flex gap-4">
                  <span className="text-gray-600 w-24">{item.label}:</span>
                  <span className="font-medium">{item.value}</span>
                </div>
              )
            ))}
          </div>
        </div>
      ))}
    </div>
  );
};

export default ProfileAbout;