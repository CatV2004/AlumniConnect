const CommentHasmore = ({ setPage }) => {
    return (
        <div>
            <div className="text-center mt-6">
                <button
                    className="text-blue-600 font-medium hover:underline"
                    onClick={() => setPage((prev) => prev + 1)}
                >
                    Xem thêm bình luận
                </button>
            </div>
        </div>
    )
}

export default CommentHasmore;