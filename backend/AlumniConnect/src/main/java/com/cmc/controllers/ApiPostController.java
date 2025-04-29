/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.components.CloudinaryService;
import com.cmc.components.PostComponents;
import com.cmc.dtos.PostDTO;
import com.cmc.pojo.Post;
import com.cmc.pojo.User;
import com.cmc.service.PostService;
import com.cmc.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author PHAT
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiPostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostComponents postComponents;

    @Autowired
    private CloudinaryService CloudinaryService;


    @GetMapping("/posts")
    public ResponseEntity<Page<Post>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ResponseEntity<>(
                this.postService.getPosts(page, size),
                HttpStatus.OK
        );
    }

    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<Page<Post>> getPostsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ResponseEntity<>(
                this.postService.getPostsByUser(userId, page, size),
                HttpStatus.OK
        );
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Post> getPostById(
            @PathVariable(value = "postId") Long postId
    ) {
        return new ResponseEntity<>(this.postService.getPostById(postId), HttpStatus.OK);
    }

    @PostMapping(path = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(
            @RequestParam("content") String content,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestPart(name = "images", required = false) List<MultipartFile> images) {

        String username = this.postComponents.authorization(authorizationHeader);

        Post post = new Post();
        post.setContent(content);

        User user = this.userService.getUserByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(post, HttpStatus.BAD_REQUEST);
        }
        post.setUserId(user);

        List<String> imageUrls = new ArrayList<>();
        if (images != null) {
            for (MultipartFile file : images) {
                String url = CloudinaryService.uploadFile(file);
                imageUrls.add(url);
            }
        }

        Post created = postService.addPost(post, imageUrls);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PatchMapping("/posts/{postId}/content")
    public ResponseEntity<String> updateContent(@PathVariable Long postId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(value = "content") String content) {

        String username = this.postComponents.authorization(authorizationHeader);

        User u = this.userService.getUserByUsername(username);
        Post p = this.postService.getPostById(postId);
        if (u == null) {
            return ResponseEntity.badRequest().body("User không tồn tại!!!");
        }
        if (!p.getUserId().getUsername().equals(username)) {
            return ResponseEntity.badRequest().body("Bạn không thể sửa bài đăng!!!");
        }

        boolean flag = this.postService.updatePostContent(postId, content);
        return flag ? ResponseEntity.ok("Cập nhật thành công!") : ResponseEntity.badRequest().body("Cập nhật thất bại!");
    }

    @PatchMapping("/posts/{postId}/toggle-comment")
    public ResponseEntity<String> updateLockComment(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long postId) {

        String username = this.postComponents.authorization(authorizationHeader);

        User u = this.userService.getUserByUsername(username);
        Post p = this.postService.getPostById(postId);

        if (!p.getUserId().getUsername().equals(username) && !u.getRole().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền Khóa comment bài viết này.");
        }

        int updated = postService.lockComment(postId);
        return updated > 0 ? ResponseEntity.ok("Cập nhật thành công!") : ResponseEntity.badRequest().body("Cập nhật thất bại!");
    }

    @DeleteMapping("/posts/{postId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
            @PathVariable(value = "postId") Long postId
    ) {
        this.postService.deletePost(postId);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable(value = "postId") Long postId,
            @RequestHeader("Authorization") String authorizationHeader) {

        String username = this.postComponents.authorization(authorizationHeader);

        Post post = postService.getPostById(postId);
        User user = userService.getUserByUsername(username);

        if (post == null || user == null) {
            return ResponseEntity.badRequest().body("Bài viết hoặc người dùng không tồn tại.");
        }

        if (!post.getUserId().getUsername().equals(username) && !user.getRole().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền xóa bài viết này.");
        }

        boolean success = postService.deletePost(postId);
        return success
                ? ResponseEntity.ok("Xoá bài viết thành công.")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xoá thất bại.");
    }

    @PatchMapping("/posts/{postId}/restore-posts")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> restorePost(Model model, @PathVariable(value = "postId") Long postId,
            @RequestHeader("Authorization") String authorizationHeader) {

        String username = this.postComponents.authorization(authorizationHeader);

        Post post = postService.getPostById(postId);
        User user = userService.getUserByUsername(username);

        if (post == null || user == null) {
            return ResponseEntity.badRequest().body("Bài viết hoặc người dùng không tồn tại.");
        }

        if (!post.getUserId().getUsername().equals(username) && !user.getRole().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền Khôi bài viết này.");
        }
        boolean success = this.postService.restorePost(postId);
        return success
                ? ResponseEntity.ok("Khôi phục bài viết thành công.")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Khôi phục thất bại.");
    }

    @DeleteMapping("/posts/images/{imageId}")
    public ResponseEntity<?> deleteImage(
            @PathVariable Long imageId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(value = "postId") Long postId) {
        String username = this.postComponents.authorization(authorizationHeader);
        Post p = this.postService.getPostById(postId);

        if (this.postService.getImagePostById(imageId).getPostId().getId().equals(postId)
                && p.getUserId().getUsername().equals(username)) {
            if (postService.deleteImage(imageId)) {
                return ResponseEntity.ok("Xóa thành Công");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy ảnh");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi kết nối server!!!");
    }

}
