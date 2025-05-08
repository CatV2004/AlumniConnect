import React, { useEffect, useState } from 'react';
import PostItem from './PostItem';
import { useDispatch, useSelector } from 'react-redux';
import { createPost, fetchPosts } from '../../features/posts/postSlice';

const dummyPosts = [
    { id: 1, name: 'Nguyễn Văn A', userId: 1, content: 'Chào mọi người, lâu quá mới gặp lại!', time: '2 giờ trước', commentCount: 10, likeCount: 10, avatar: "https://res.cloudinary.com/dohsfqs6d/image/upload/v1745916239/AlumniSocialNetwork/gfzchaq3udjwwtpmagll.png", images: ["https://res.cloudinary.com/dohsfqs6d/image/upload/v1745916239/AlumniSocialNetwork/gfzchaq3udjwwtpmagll.png"] },
    { id: 2, name: 'Trần Thị B', userId: 3, content: 'Ai còn nhớ lớp K18A không nhỉ?', time: '1 ngày trước', commentCount: 9, likeCount: 11, avatar: ["https://res.cloudinary.com/dohsfqs6d/image/upload/v1745916239/AlumniSocialNetwork/gfzchaq3udjwwtpmagll.png"], images: ["https://res.cloudinary.com/dohsfqs6d/image/upload/v1745916239/AlumniSocialNetwork/gfzchaq3udjwwtpmagll.png", "https://res.cloudinary.com/dohsfqs6d/image/upload/v1745916239/AlumniSocialNetwork/gfzchaq3udjwwtpmagll.png", "https://res.cloudinary.com/dohsfqs6d/image/upload/v1745916239/AlumniSocialNetwork/gfzchaq3udjwwtpmagll.png", "https://res.cloudinary.com/dohsfqs6d/image/upload/v1745916239/AlumniSocialNetwork/gfzchaq3udjwwtpmagll.png"] },
];


const postCreated = () => {
    const dispatch = useDispatch();
    const { posts, loading, error } = useSelector((state) => state.posts);
    const [content, setContent] = useState('');
    const [image, setImage] = useState([]);


    useEffect(() => {
        dispatch(fetchPosts());
    }, [dispatch]);

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!content.trim()) return;

        dispatch(createPost({ content, image }));
        setContent('');
        setImage('');
    };

    return (
        <div className="space-y-4">
            {dummyPosts.map((post) => (
                <PostItem key={post.id} post={post} />
            ))}
        </div>
    );
}