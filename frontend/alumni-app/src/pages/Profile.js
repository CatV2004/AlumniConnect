import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getUserProfile, getUserPosts } from '../services/userService';
import ProfileHeader from '../components/Profile/ProfileHeader';
import ProfileTabs from '../components/Profile/ProfileTabs';
import ProfileTimeline from '../components/Profile/ProfileTimeline';
import { useSelector } from 'react-redux';

const Profile = () => {
  const { id } = useParams(); 
  const { token } = useSelector((state) => state.auth);
  const [user, setUser] = useState(null);
  const [posts, setPosts] = useState([]);
  const [activeTab, setActiveTab] = useState('timeline');
  
  useEffect(() => {
    if (token) {
      getUserProfile(id, token).then(setUser);
      getUserPosts(id, token).then(setPosts);
    }
  }, [id, token]);

  if (!user) return <div className="text-center mt-10">Đang tải dữ liệu...</div>;

  return (
    <div className="max-w-4xl mx-auto mt-6">
      <ProfileHeader user={user} />
      <ProfileTabs activeTab={activeTab} setActiveTab={setActiveTab} />
      {activeTab === 'timeline' && <ProfileTimeline posts={posts} />}
    </div>
  );
};

export default Profile;
