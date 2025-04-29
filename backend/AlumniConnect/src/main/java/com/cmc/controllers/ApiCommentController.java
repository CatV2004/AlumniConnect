/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.components.CloudinaryService;
import com.cmc.components.PostComponents;
import com.cmc.pojo.Comment;
import com.cmc.service.CommentService;
import com.cmc.service.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author PHAT
 */
@RestController
@RequestMapping("/api")
public class ApiCommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CloudinaryService cloudianryService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostComponents postComponents;

    @PatchMapping("/comment/{id}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String content) {
        
        String username = this.postComponents.authorization(authorizationHeader);

        Comment updated = commentService.updateComment(id, this.userService.getUserByUsername(username).getId(), content);
        if (updated == null) {
            return (ResponseEntity<Comment>) ResponseEntity.status(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(updated, HttpStatus.OK);
    }

    @PostMapping(value = "/comment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Comment> addComment(
            @RequestParam Map<String, String> param,
            @RequestParam(value = "file", required = false) MultipartFile file, 
            @RequestHeader("Authorization") String authorizationHeader) {
        
        String username = this.postComponents.authorization(authorizationHeader);

        String urlImage = null;
        if (file != null && !file.isEmpty()) {
            urlImage = this.cloudianryService.uploadFile(file);
        }

        Comment created = commentService.createComment(param, urlImage, username);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader) {
        
        String username = this.postComponents.authorization(authorizationHeader);

        boolean deleted = commentService.deleteComment(id, this.userService.getUserByUsername(username).getId());
        if (deleted) {
            return ResponseEntity.ok("Comment deleted");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed to delete");
    }

    @GetMapping("/comment/{postId}/post")
    public ResponseEntity<Page<Comment>> getCommentsByPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(commentService.getCommentByPosts(postId, page, size));
    }

    @GetMapping("/comment/{parentId}/replies")
    public ResponseEntity<Page<Comment>> getReplies(
            @PathVariable Long parentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(commentService.getCommentByComments(parentId, page, size));
    }
}
