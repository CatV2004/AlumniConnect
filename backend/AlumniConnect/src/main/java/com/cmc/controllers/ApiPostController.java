/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.components.CloudinaryService;
import com.cmc.components.PostComponents;
import com.cmc.dtos.PageResponse;
import com.cmc.dtos.PostResponseDTO;
import com.cmc.pojo.Post;
import com.cmc.pojo.User;
import com.cmc.service.PostImageService;
import com.cmc.service.PostService;
import com.cmc.service.UserService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<PageResponse<PostResponseDTO>> getPosts(@RequestParam Map<String, Object> params) {
        Map<String, Object> filterParams = new HashMap<>();

        int page = params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1;
        filterParams.put("page", page);

        int size = params.containsKey("size") ? Integer.parseInt(params.get("size").toString()) : 10;
        filterParams.put("size", size);

        String kw = params.containsKey("kw") ? params.get("kw").toString().trim() : "";
        if (!kw.isEmpty()) {
            filterParams.put("kw", kw);
        }

        boolean hasSurvey = params.containsKey("hasSurvey") && Boolean.parseBoolean(params.get("hasSurvey").toString());
        filterParams.put("hasSurvey", hasSurvey);

        boolean hasImage = params.containsKey("hasImage") && Boolean.parseBoolean(params.get("hasImage").toString());
        filterParams.put("hasImage", hasImage);

        boolean hasInvitation = params.containsKey("hasInvitation") && Boolean.parseBoolean(params.get("hasInvitation").toString());
        filterParams.put("hasInvitation", hasInvitation);

        PageResponse<PostResponseDTO> response = postService.getPosts(filterParams);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable("postId") Long postId) {
        PostResponseDTO postDto = postService.getPostResponseById(postId);
        return ResponseEntity.ok(postDto);
    }

    @PostMapping(path = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(
            @RequestParam("content") String content,
            @RequestParam(name = "lockComment", required = false) Boolean lockComment,
            Principal principal,
            @RequestPart(name = "images", required = false) List<MultipartFile> images) {

        String username = principal.getName();

        Post post = new Post();
        post.setContent(content);
        post.setLockComment(lockComment != null ? lockComment : false);

        User user = this.userService.getUserByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(post, HttpStatus.BAD_REQUEST);
        }
        post.setUserId(user);

        Post created = postService.addOrUpdatePost(post, images, null, true);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping(path = "/posts/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> updatePost(
            @PathVariable Long id,
            @RequestParam(name = "content", required = true) String content,
            @RequestParam(name = "lockComment", required = false) Boolean lockComment,
            @RequestPart(name = "images", required = false) List<MultipartFile> images,
            @RequestParam(name = "existingImages", required = false) List<Long> existingImageIds,
            Principal principal) {

        String username = principal.getName();
        User user = userService.getUserByUsername(username);

        Post post = postService.getPostById(id);
        if (post == null || !post.getUserId().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        post.setContent(content);
        post.setLockComment(lockComment != null ? lockComment : false);
        Post updatedPost = postService.addOrUpdatePost(post, images, existingImageIds, false);
        return ResponseEntity.ok(updatedPost);
    }

    @PostMapping("/posts/{postId}/images")
    public ResponseEntity<?> uploadImagesToPost(
            @PathVariable("postId") Long postId,
            @RequestParam("images") List<MultipartFile> images
    ) {
        if (images == null || images.isEmpty()) {
            return ResponseEntity.badRequest().body("No files selected.");
        }

        try {
            postService.createImagePost(postId, images);
            return ResponseEntity.ok("Upload multiple images success.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload images: " + e.getMessage());
        }
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

//    @DeleteMapping("/posts/{postId}/delete")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deletePost(
//            @PathVariable(value = "postId") Long postId
//    ) {
//        this.postService.deletePost(postId);
//    }
    @PutMapping("/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") Long postId, Principal principal) {
        try {
            String username = principal.getName();
            Post post = postService.getPostById(postId);
            User user = userService.getUserByUsername(username);

            if (post == null || user == null) {
                return ResponseEntity.badRequest().body("Bài viết hoặc người dùng không tồn tại.");
            }

            boolean isOwner = post.getUserId().getUsername().equals(username);
            boolean isAdmin = user.getRole().equalsIgnoreCase("ADMIN");

            if (!isOwner && !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền xóa bài viết này.");
            }

            boolean success = postService.deletePost(postId);
            return success
                    ? ResponseEntity.ok("Xoá bài viết thành công.")
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xoá thất bại.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi xoá bài viết.");
        }
    }

    @PutMapping("/posts/{postId}/restore")
    public ResponseEntity<String> restorePost(@PathVariable("postId") Long postId, Principal principal) {
        try {
            String username = principal.getName();
            Post post = postService.getPostIdOfDL(postId);
            User user = userService.getUserByUsername(username);

            if (post == null || user == null) {
                return ResponseEntity.badRequest().body("Bài viết hoặc người dùng không tồn tại.");
            }

            boolean isOwner = post.getUserId().getUsername().equals(username);
            boolean isAdmin = user.getRole().equalsIgnoreCase("ADMIN");

            if (!isOwner && !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền khôi phục bài viết này.");
            }

            boolean success = postService.restorePost(postId);
            return success
                    ? ResponseEntity.ok("Khôi phục bài viết thành công.")
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Khôi phục thất bại.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi khôi phục bài viết.");
        }
    }

    @DeleteMapping("/posts/{postId}/force")
    public ResponseEntity<String> forceDeletePost(
            @PathVariable("postId") Long postId,
            Principal principal) {
        
        String username = principal.getName();
        Post post = postService.getPostIdOfDL(postId);
        User user = userService.getUserByUsername(username);

        if (post == null || user == null) {
            return ResponseEntity.badRequest().body("Bài viết hoặc người dùng không tồn tại.");
        }

        boolean isOwner = post.getUserId().getUsername().equals(username);
        boolean isAdmin = user.getRole().equalsIgnoreCase("ADMIN");

        if (!isOwner && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền xóa vĩnh viễn bài viết này.");
        }

        boolean deleted = postService.deletePostPermanently(postId);

        return deleted
                ? ResponseEntity.ok("Xoá vĩnh viễn thành công.")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bài viết không tồn tại hoặc chưa bị xoá mềm.");
    }

    @GetMapping("/posts/deleted")
    public ResponseEntity<?> getDeletedPostsByUser(
            Principal principal,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        User user = userService.getUserByUsername(principal.getName());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getId());
        params.put("page", page);
        params.put("size", size);

        PageResponse<PostResponseDTO> response = postService.getDeletedPostsByUser(params);

        return ResponseEntity.ok(response);
    }

}
