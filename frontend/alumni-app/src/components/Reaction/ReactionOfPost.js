import axios from "axios";

const ReactionOfPost = async (postId) => {
    const BASE_URL = 'http://localhost:8080/AlumniConnect/api';

    try {
        const res = await axios.get(`${BASE_URL}/liked/${postId}`, {
            headers: {
                'Authorization': localStorage.getItem('token') || ''
            }
        });
        return res.data;
    } catch (err) {
        console.error("Lỗi khi thêm phản ứng:", err);
    }
}

export default ReactionOfPost;