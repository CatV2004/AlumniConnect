import { useEffect, useState } from "react";
import axios from "axios";
import moment from "moment";
import "moment/locale/vi";
import CommentItem from "./CommentItem";
import CommentHasmore from "./CommentHasmore";
import CommentCreated from "./CommentCreated";
import CountComment from "./CountComment";

moment.locale("vi");

const CommentList = ({ post, showComment, setCountComment }) => {
  // console.log("p[ost: ", post);
  const BASE_URL = "http://localhost:8080/AlumniConnect/api";
  const [comments, setComments] = useState([]);
  const [page, setPage] = useState(0);
  const [size] = useState(5);
  const [hasMore, setHasMore] = useState(true);
  // console.log("post.lockComment: ", post.lockComment);

  useEffect(() => {
    setComments([]);
    setPage(0);
    setHasMore(true);
  }, [post.id]);

  useEffect(() => {
    if (hasMore) {
      fetchComments();
    }
  }, [page]);

  useEffect(() => {
    fetchCommentByPost();
  }, [comments]);

  const fetchComments = async () => {
    try {
      const response = await axios.get(
        `${BASE_URL}/comment/${post.id}/post?page=${page}&size=${size}`,
        {
          headers: {
            Authorization: localStorage.getItem("token") || null,
          },
        }
      );
      setComments((prev) => {
        const filtered = response.data.content
          .filter((newC) => !prev.some((c) => c.id === newC.id))
          .map((comment) => ({
            ...comment,
            children: [],
            hasMoreReplies: response.data.last,
          }));

        return [...prev, ...filtered];
      });

      setHasMore(!response.data.last);
    } catch (error) {
      console.error("Lỗi khi tải bình luận:", error);
    }
  };

  const fetchCommentByPost = async () => {
    const countC = await CountComment(post.id);
    setCountComment(countC);
  };

  const updateCommentTree = (comments, parentId, newReplies, hasMore) => {
    return comments.map((comment) => {
      if (comment.id === parentId) {
        const existingIds = comment.children?.map((c) => c.id) || [];
        const uniqueReplies = newReplies.filter(
          (r) => !existingIds.includes(r.id)
        );

        return {
          ...comment,
          children: [...(comment.children || []), ...uniqueReplies],
          hasMoreReplies: hasMore,
        };
      } else if (comment.children && comment.children.length > 0) {
        return {
          ...comment,
          children: updateCommentTree(
            comment.children,
            parentId,
            newReplies,
            hasMore
          ),
        };
      }
      return comment;
    });
  };

  const fetchReplies = async (parentId) => {
    try {
      const res = await axios.get(`${BASE_URL}/comment/${parentId}/replies`, {
        headers: {
          Authorization: localStorage.getItem("token") || null,
        },
      });
      const newReplies = res.data.content.map((reply) => ({
        ...reply,
        hasMoreReplies: true,
      }));

      setComments((prevComments) =>
        updateCommentTree(prevComments, parentId, newReplies, !res.data.last)
      );
    } catch (err) {
      console.error("Lỗi khi tải replies:", err);
    }
  };

  const handleNewComment = (comment, parentId = null) => {
    if (parentId === null) {
      setComments((prev) => [comment, ...prev]);
    } else {
      const addReply = (comments) => {
        return comments.map((c) => {
          if (c.id === parentId) {
            return {
              ...c,
              children: [comment, ...(c.children || [])],
            };
          }
          if (c.children) {
            return {
              ...c,
              children: addReply(c.children),
            };
          }
          return c;
        });
      };
      setComments((prev) => addReply(prev));
    }
  };

  const handleDeleteComment = (commentId) => {
    const deleteRecursive = (comments) => {
      return comments
        .filter((c) => c.id !== commentId)
        .map((c) => ({
          ...c,
          children: c.children ? deleteRecursive(c.children) : [],
        }));
    };

    setComments((prev) => deleteRecursive(prev));
  };

  const handleCommentUpdated = (updatedComment) => {
    const updateRecursive = (comments) => {
      return comments.map((c) => {
        if (c.id === updatedComment.id) return updatedComment;
        if (c.children) {
          return {
            ...c,
            children: updateRecursive(c.children),
          };
        }
        return c;
      });
    };
    setComments((prev) => updateRecursive(prev));
  };

  const renderComments = (commentsList) => {
    return commentsList.map((comment) => (
      <div key={comment.id} className="ml-4 mt-4">
        <div className="flex gap-3">
          <CommentItem
            userId={post.userId.id}
            comment={comment}
            post={post}
            onCommentAdded={(c) => handleNewComment(c, comment.id)}
            handleCommentUpdated={handleCommentUpdated}
            showComment={showComment}
            handleDeleteComment={handleDeleteComment}
            isComment={post.lockComment}
          />
        </div>

        {comment.children && comment.children.length > 0 && (
          <div className="ml-6 pl-6 border-l border-gray-200 space-y-4">
            {renderComments(comment.children)}
          </div>
        )}

        {comment.hasMoreReplies && (
          <div className="pl-6 mt-2">
            <button
              className="text-sm text-blue-600 font-medium hover:underline"
              onClick={() => fetchReplies(comment.id)}
            >
              Xem thêm trả lời
            </button>
          </div>
        )}
      </div>
    ));
  };


  return (
    <div className="space-y-2 gap-3">
      {renderComments(comments)}

      {hasMore && <CommentHasmore setPage={setPage} />}
      {post.lockComment === false && (
        <CommentCreated post={post} onCommentAdded={handleNewComment} />
      )}
    </div>
  );
};

export default CommentList;
