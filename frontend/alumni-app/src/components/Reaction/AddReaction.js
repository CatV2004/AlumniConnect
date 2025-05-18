import axios from "axios";

const BASE_URL = 'http://localhost:8080/AlumniConnect/api';

const addReaction = async (postId) => {
    try {
        const formData = new FormData();
        formData.append("postId", postId);
        formData.append("reactionType", "LIKE")

        const res = await axios.post(`${BASE_URL}/reactions`, formData, {
            headers: {
                'Authorization': localStorage.getItem('token') || ''
            }
        });
    } catch (err) {
        console.error("Lỗi khi thêm phản ứng:", err);
    }
};

export default addReaction;