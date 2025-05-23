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
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin
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

    @PatchMapping(value = "/comment/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateComment(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String content) {
        try {
            String username = this.postComponents.authorization(authorizationHeader);
            Comment c = this.commentService.getCommentById(id);
            if (c == null) {
                return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
            }
            String urlImage = null;
            if (file != null && !file.isEmpty()) {
                urlImage = this.cloudianryService.uploadFile(file);
            }
            Comment updated = commentService.updateComment(id, this.userService.getUserByUsername(username).getId(), content, urlImage);
            if (updated == null) {
                return (ResponseEntity<Comment>) ResponseEntity.status(HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity(updated, HttpStatus.OK);
        } catch (Exception ex) {
            return (ResponseEntity<Comment>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/comment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addComment(
            @RequestParam Map<String, String> param,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String username = this.postComponents.authorization(authorizationHeader);

            String urlImage = null;
            if (file != null && !file.isEmpty()) {
                urlImage = this.cloudianryService.uploadFile(file);
            }

            Comment created = commentService.createComment(param, urlImage, username);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mess", "Server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String username = this.postComponents.authorization(authorizationHeader);

            boolean deleted = commentService.deleteComment(id, this.userService.getUserByUsername(username).getId());
            if (deleted) {
                return ResponseEntity.ok("Comment deleted");
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed to delete");
        } catch (Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mess", "Server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
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

    @GetMapping("/comment/{postId}/total-post")
    public ResponseEntity<Long> getCommentsByPost(
            @PathVariable(value = "postId") Long postId
    ) {
        try {
            Long totalComment = this.commentService.totalCommentByPost(postId);
            return ResponseEntity.ok(totalComment);
        } catch (Exception ex) {
            return ResponseEntity.ok(Long.parseLong("0"));
        }

    }
}
