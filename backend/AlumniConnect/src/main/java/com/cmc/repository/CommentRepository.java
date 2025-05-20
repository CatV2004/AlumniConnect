/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.pojo.Comment;
import java.util.List;

/**
 *
 * @author PHAT
 */
public interface CommentRepository {

    List<Comment> findByPostIdAndActiveTrueOrderByCreatedDateAsc(Long postId, Integer page, Integer size);

    List<Comment> findByUserId(Long userId);

    long countByPostId(Long postId);

    long totalCommentByPost(Long postId);

    long totalCommentByComment(Long parentId);

    List<Comment> getCommentByComment(Long parentId, Integer page, Integer size);
    
    Comment saveOrUpdate(Comment c);
    
    Comment getCommentById(Long id);
    
    List<Comment> getRepliesByParentId(Long parentId);
}
