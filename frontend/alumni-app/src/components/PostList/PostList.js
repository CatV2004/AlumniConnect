import React, { useEffect, useState } from 'react';
import PostItem from './PostItem';
import { useDispatch, useSelector } from 'react-redux';
import { createPost, fetchPosts } from '../../features/posts/postSlice';
import axios from 'axios';

const dummyPosts = [
  { id: 1, name: 'Nguyễn Văn A', userId: 1, content: 'Chào mọi người, lâu quá mới gặp lại!', time: '2 giờ trước', commentCount: 10, likeCount: 10, avatar: "https://res.cloudinary.com/dohsfqs6d/image/upload/v1745916239/AlumniSocialNetwork/gfzchaq3udjwwtpmagll.png", images: ["https://res.cloudinary.com/dohsfqs6d/image/upload/v1745916239/AlumniSocialNetwork/gfzchaq3udjwwtpmagll.png"] },
  { id: 2, name: 'Trần Thị B', userId: 3, content: 'Ai còn nhớ lớp K18A không nhỉ?', time: '1 ngày trước', commentCount: 9, likeCount: 11, avatar: ["https://res.cloudinary.com/dohsfqs6d/image/upload/v1745916239/AlumniSocialNetwork/gfzchaq3udjwwtpmagll.png"], images: ["https://res.cloudinary.com/dohsfqs6d/image/upload/v1745916239/AlumniSocialNetwork/gfzchaq3udjwwtpmagll.png", "https://res.cloudinary.com/dohsfqs6d/image/upload/v1745916239/AlumniSocialNetwork/gfzchaq3udjwwtpmagll.png", "https://res.cloudinary.com/dohsfqs6d/image/upload/v1745916239/AlumniSocialNetwork/gfzchaq3udjwwtpmagll.png", "https://res.cloudinary.com/dohsfqs6d/image/upload/v1745916239/AlumniSocialNetwork/gfzchaq3udjwwtpmagll.png"] },
];

// const PostForm = () => {
//   const [content, setContent] = useState("");
//   const [image, setImage] = useState("");

//   const handleSubmit = async (e) => {
//     e.preventDefault();
//     try {
//       const postData = {
//         content: content,
//         image: image
//       };

//       const response = await axios.post("http://localhost:8080/AlumniConnect/api/posts", postData, {
//         headers: {
//           "Content-Type": "application/json"
//         }
//       });

//       alert("Đăng bài thành công!");
//       setContent("");
//       setImage("");
//     } catch (error) {
//       alert("Đăng bài thất bại!");
//       console.error(error);
//     }
//   };
// }

const PostList = () => {
  const dispatch = useDispatch();
  // const { posts, loading, error } = useSelector((state) => state.posts);
  const [content, setContent] = useState('');
  const [image, setImage] = useState([]);


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

  return (
    <div className="space-y-4">
      {posts?.map((post) => (
        <PostItem key={post.id} post={post} />
      ))}
    </div>
  );

};

export default PostList;
