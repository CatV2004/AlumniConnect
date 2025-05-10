import React, { useEffect, useState } from 'react';
import PostItem from './PostItem';
import axios from 'axios';

const PostList = () => {

  const BASE_URL = "http://localhost:8080/AlumniConnect/api";
  const [posts, setPosts] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const pageSize = 5;

  const loadPosts = async () => {
    try {
      const res = await axios.get(`${BASE_URL}/posts?page=${page}&size=${pageSize}`);
      const newPosts = res.data.content;
      setPosts((prev) => [...prev, ...newPosts]);
      setHasMore(!res.data.last);
    } catch (err) {
      console.error("Lỗi khi tải bài viết:", err);
    }
  };

  useEffect(() => {
    loadPosts();
  }, [page]);

  useEffect(() => {
    const handleScroll = () => {
      if (window.innerHeight + window.scrollY >= document.body.offsetHeight -100 && hasMore){
        setPage((prev) => prev + 1)
      }
    }

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasMore]);

  return (
    <div className="space-y-4">
      {posts?.map((post) => (
        <PostItem key={post.id} post={post} />
      ))}
      {!hasMore && <p className='text-center text-gray-500'>không còn bài viết nào.</p>}
    </div>
  );

};

export default PostList;
