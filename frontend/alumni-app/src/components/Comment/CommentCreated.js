import axios from "axios";
import { useReducer, useState } from "react";
import cookie from 'react-cookies'


const CommentCreated = ({ post, parentComment = null, onCommentAdded  }) => {
    const BASE_URL = 'http://localhost:8080/AlumniConnect/api';
    const [content, setContent] = useState('');
    const [postId, setPostId] = useState(post);
    const [parentId, setParentId] = useState(parentComment);
    const [file, setFile] = useState(null);

    const handleSubmitAddComment = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append("content", content);
        formData.append("postId", postId);
        if (parentId) formData.append("parentId", parentId);
        if (file) formData.append("file", file);

        try {
            const response = await axios.post(`${BASE_URL}/comment`, formData, {
                headers: { 
                    'Content-Type': 'multipart/form-data',
                    Authorization: localStorage.getItem('token') || null }
            });
            if (response.status === 201) {
                const result = await response.json();
                alert('Bình luận đã được gửi!');
                console.log(result);
                setFile(null);
                setContent('');
                if (onCommentAdded) onCommentAdded();
            } else {
                alert('Lỗi khi gửi bình luận');
            }
        } catch (error) {
            console.error('Lỗi:', JSON.stringify(error));
            alert('Lỗi khi gửi bình luận');
        }
    }


    return (
        <form onSubmit={handleSubmitAddComment} className="flex flex-col mt-3 gap-3 items-start border p-2 rounded-lg" >
            <div className="w-full">
                <input
                    type="text"
                    className="w-full border border-gray-300 rounded-lg px-4 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="Viết bình luận..."
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                />
            </div>
            <div className="flex justify-between items-center">
                <div>
                    <input
                        type="file"
                        className="text-sm file:mr-3 file:px-3 file:py-1 file:rounded-md file:border-0 file:bg-gray-200 hover:file:bg-gray-300"
                        onChange={(e) => setFile(e.target.files[0])}
                    />
                </div>
                <div>
                    <button
                    type="submit"
                    className="bg-blue-500 text-white text-sm px-4 py-2 rounded-lg hover:bg-blue-600 transition">
                    Gửi
                </button>
                </div>
            </div>

        </form>
    )
}

export default CommentCreated;