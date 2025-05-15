import axios from "axios";

const CountComment = async (postId) => {
    const BASE_URL = 'http://localhost:8080/AlumniConnect/api';

    try {
        const res = await axios.get(`${BASE_URL}/comment/${postId}/total-post`);
        return res.data
    } catch (err) {
        console.error("Lỗi đếm comment:", err);
    }
}
export default CountComment;