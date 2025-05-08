import axios from "axios";
import { useReducer, useState } from "react";
import cookie from 'react-cookies'


const CommentCreated = ({ post, parentComment = null }) => {
    const BASE_URL = 'http://localhost:8080/AlumniConnect/api';
    const [content, setContent] = useState('');
    const [postId, setPostId] = useState(post);
    const [parentId, setParentId] = useState(parentComment);
    const [file, setFile] = useState(null);


    const getTokenFromCookie = () => {
        const match = cookie.load('token') || null;
        return match ? match : '';
    };

    const handleSubmitAddComment = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append("content", content);
        formData.append("postId", postId);
        if (parentId) formData.append("parentId", parentId);
        if (file) formData.append("file", file);

        const token = getTokenFromCookie();

        try {
            const response = await axios.post(`${BASE_URL}/comment`, formData, {
                headers: { 'Content-Type': 'multipart/form-data', Authorization: `Bearer ${token}`, }
            });
            if (response.ok) {
                const result = await response.json();
                alert('Bình luận đã được gửi!');
                console.log(result);
            } else {
                alert('Lỗi khi gửi bình luận');
            }
        } catch (error) {
            console.error('Lỗi:', error);
            alert('Lỗi khi gửi bình luận');
        }
    }


    return (<form onSubmit={handleSubmitAddComment} className="flex mt-3 gap-2">
        <input
            type="text"
            className="flex-1 border rounded px-3 py-1 text-sm"
            placeholder="Viết bình luận..."
            value={content}
            onChange={(e) => setContent(e.target.value)}
        />
        <input
            type="file"
            className="flex-1 border rounded px-3 py-1 text-sm"
            onChange={(e) => setFile(e.target.files[0])} />
        <button
            type="submit"
            className="text-sm text-blue-500 hover:underline">
            Gửi
        </button>
    </form>
    )
}

export default CommentCreated;