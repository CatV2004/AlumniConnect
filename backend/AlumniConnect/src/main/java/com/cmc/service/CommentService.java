/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.pojo.Comment;
import java.util.Map;
import org.springframework.data.domain.Page;

/**
 *
 * @author PHAT
 */
public interface CommentService {

    boolean deleteComment(Long commentId, Long currentUserId);

    Comment updateComment(Long commentId, Long userId, String newContent, String pathFile);

    Comment createComment(Map<String, String> pagram, String file, String username);

    Page<Comment> getCommentByPosts(Long postId, Integer page, Integer size);

    Page<Comment> getCommentByComments(Long parentId, Integer page, Integer size);
    
    Long totalCommentByPost(Long postId);
    
    public Comment getCommentById(Long id);
}
