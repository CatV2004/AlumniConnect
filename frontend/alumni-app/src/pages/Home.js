import React from 'react';
import Navbar from '../components/layout/Navbar';
import PostList from '../components/PostList/PostList';
import CreatePostBar from '../components/PostForm';
import { useSelector } from 'react-redux';

const Home = () => {
  const { user } = useSelector((state) => state.auth); 

  return (
    <div className="bg-gray-100 min-h-screen">
      <Navbar />
      <div className="flex justify-center max-w-7xl mx-auto px-4 pt-6">
        {/* Left Sidebar */}
        <aside className="w-1/5 hidden lg:block pr-4">
          <div className="bg-white rounded-xl shadow p-4">
            <h2 className="font-semibold text-gray-700 mb-2">Danh mục</h2>
            <ul className="text-sm text-gray-600 space-y-1">
              <li>Tin mới</li>
              <li>Nhóm</li>
              <li>Khảo sát</li>
              <li>Sự kiện</li>
            </ul>
          </div>
        </aside>

        {/* Main content */}
        <main className="w-full lg:w-3/5 space-y-6">
        <CreatePostBar user={user} />
        <PostList />
        </main>

        {/* Right Sidebar */}
        <aside className="w-1/5 hidden lg:block pl-4">
          <div className="bg-white rounded-xl shadow p-4">
            <h2 className="font-semibold text-gray-700 mb-2">Gợi ý</h2>
            <p className="text-sm text-gray-600">Tính năng mới sắp ra mắt...</p>
          </div>
        </aside>
      </div>
    </div>
  );
};

export default Home;
