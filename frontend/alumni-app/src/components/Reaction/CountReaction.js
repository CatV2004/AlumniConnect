import axios from "axios";

const CountReaction = async (postId) => {
    const BASE_URL = 'http://localhost:8080/AlumniConnect/api';

    try {
        const res = await axios.get(`${BASE_URL}/like-count/${postId}`, {
            headers: {
                'Authorization': localStorage.getItem('token') || ''
            }
        });
        return res.data
        console.log("So luogn like:", res.data);
    } catch (err) {
        console.error("Lỗi khi thêm phản ứng:", err);
    }
}
export default CountReaction;