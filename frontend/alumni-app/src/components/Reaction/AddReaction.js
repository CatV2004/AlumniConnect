import axios from "axios";

const BASE_URL = 'http://localhost:8080/AlumniConnect/api';

const addReaction = async (postId, typeReaction) => {
    try {
        const formData = new FormData();
        formData.append("postId", postId);
        formData.append("reactionType", typeReaction);
        console.log(typeReaction);

        const res = await axios.post(`${BASE_URL}/reactions`, formData, {
            headers: {
                'Authorization': localStorage.getItem('token') || ''
            }
        });
        console.log(res);
        console.log(res.status);
    } catch (err) {
        console.error("Lỗi khi thêm phản ứng:", err);
    }
};

export default addReaction;