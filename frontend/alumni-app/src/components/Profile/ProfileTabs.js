import React from 'react';

const tabs = [
  { key: 'timeline', label: 'Bài viết' },
  { key: 'about', label: 'Giới thiệu' },
  { key: 'photos', label: 'Ảnh' },
];

const ProfileTabs = ({ activeTab, setActiveTab }) => {
  return (
    <div className="flex gap-4 border-b mb-4">
      {tabs.map(tab => (
        <button
          key={tab.key}
          onClick={() => setActiveTab(tab.key)}
          className={`pb-2 px-4 font-medium ${
            activeTab === tab.key
              ? 'border-b-2 border-blue-600 text-blue-600'
              : 'text-gray-600'
          }`}
        >
          {tab.label}
        </button>
      ))}
    </div>
  );
};

export default ProfileTabs;
