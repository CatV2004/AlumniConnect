/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.components.CloudinaryService;
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
import org.springframework.boot.web.server.Http2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
public class ApiPostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

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

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Post> getPostById(
            @PathVariable(value = "postId") Long postId
    ) {
        return new ResponseEntity<>(this.postService.getPostById(postId), HttpStatus.OK);
    }

    @PostMapping(path = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(
            @RequestParam("content") String content,
            @RequestParam("userId") Long userId,
            @RequestPart(name = "images", required = false) List<MultipartFile> images) {

        Post post = new Post();
        post.setContent(content);

        User user = this.userService.getUserById(userId);
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
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "content") String content) {
        User u = this.userService.getUserById(userId);
        Post p = this.postService.getPostById(postId);
        if (u == null) {
            return ResponseEntity.badRequest().body("User không tồn tại!!!");
        }
        if (!Objects.equals(u.getId(), p.getUserId())) {
            return ResponseEntity.badRequest().body("Bạn không thể sửa bài đăng!!!");
        }

        boolean flag = this.postService.updatePostContent(postId, content);
        return flag ? ResponseEntity.ok("Cập nhật thành công!") : ResponseEntity.badRequest().body("Cập nhật thất bại!");
    }

    @PatchMapping("/posts/{postId}/toggle-comment")
    public ResponseEntity<String> updateLockComment(
            @PathVariable Long postId) {
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

    @PatchMapping("/posts/{postId}/restore-posts")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void restorePost(Model model, @PathVariable(value = "postId") Long postId) {
        this.postService.restorePost(postId);
    }

    @DeleteMapping("/posts/images/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable Long imageId) {
        if (postService.deleteImage(imageId)) {
            return ResponseEntity.ok("Xóa thành Công");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy ảnh");
    }

}
