/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.pojo.Comment;
import com.cmc.pojo.Post;
import com.cmc.pojo.User;
import com.cmc.repository.CommentRepository;
import com.cmc.repository.PostRepository;
import com.cmc.repository.UserRepository;
import com.cmc.service.CommentService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author PHAT
 */
@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private PostRepository postRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    
    @Override
    public Comment createComment(Map<String, String> pagram, String file, String username){
        String content = pagram.get("content");
        User userId = this.userRepo.getUserByUsername(username);
        Post postId = this.postRepo.getPostId(Long.valueOf(pagram.get("postId")));
        String parentId = pagram.get("parentId");
        
        Comment comment = new Comment();
        comment.setContent(content);
        if (file != null)
            comment.setImage(file);
        if(parentId != null )
            comment.setParentId(Long.valueOf(parentId));
        comment.setCreatedDate(LocalDateTime.now());
        comment.setUpdatedDate(LocalDateTime.now());
        comment.setUserId(userId);
        comment.setPostId(postId);
        comment.setActive(Boolean.TRUE);
        
        return this.commentRepo.saveOrUpdate(comment);
    }
    

    @Override
    public Comment updateComment(Long commentId, Long userId, String newContent, String pathFile) {
        Comment cmt = commentRepo.getCommentById(commentId);
        if (cmt == null || !cmt.getUserId().getId().equals(userId)) {
            return null;
        }
        cmt.setContent(newContent);
        cmt.setImage(pathFile);
        cmt.setUpdatedDate(LocalDateTime.now());
        return commentRepo.saveOrUpdate(cmt);
    }
    
    
    @Override
    public Comment getCommentById(Long id){
        return this.commentRepo.getCommentById(id);
    }

    @Override
    public boolean deleteComment(Long commentId, Long currentUserId) {
        Comment cmt = commentRepo.getCommentById(commentId);
        if (cmt == null) return false;

        Long postOwnerId = cmt.getPostId().getUserId().getId();
        Long commentAuthorId = cmt.getUserId().getId();

        if (currentUserId.equals(postOwnerId) || currentUserId.equals(commentAuthorId)) {
            cmt.setActive(false);
            cmt.setDeletedDate(LocalDateTime.now());
            commentRepo.saveOrUpdate(cmt);
            
            List<Comment> replies = commentRepo.getRepliesByParentId(commentId);

            for (Comment reply : replies) {
                reply.setActive(false);
                reply.setDeletedDate(LocalDateTime.now());
                commentRepo.saveOrUpdate(reply);
            }
            return true;
        }
        return false;
    }

    @Override
    public Page<Comment> getCommentByPosts(Long postId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Comment> comments = this.commentRepo.findByPostIdAndActiveTrueOrderByCreatedDateAsc(postId, page, size);
        long totalCommentByPost = this.commentRepo.totalCommentByPost(postId);
        return new PageImpl<>(comments, pageable, totalCommentByPost);
    }
    
    @Override
    public Long totalCommentByPost(Long postId){
        return this.commentRepo.countByPostId(postId);
    }
    
    @Override
    public Page<Comment> getCommentByComments(Long parentId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Comment> comments = this.commentRepo.getCommentByComment(parentId, page, size);
        long totalCommentByPost = this.commentRepo.totalCommentByComment(parentId);
        return new PageImpl<>(comments, pageable, totalCommentByPost);
    }
}
