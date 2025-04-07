/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.dtos.PostDTO;
import com.cmc.pojo.Post;
import com.cmc.service.PostService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @GetMapping("/posts")
    public ResponseEntity<Iterable<Post>> getPosts(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        return new ResponseEntity<>(
                this.postService.getPosts(offset,pageSize),
                HttpStatus.OK
        );
    }
    
    
    @PostMapping(path = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(@RequestParam Map<String, String> pagram, 
            @RequestParam(value = "images") MultipartFile[] images){
        return new ResponseEntity<>(this.postService.createPost(pagram, images), HttpStatus.CREATED);
    }
    
    
    @PatchMapping("/posts/{postId}/toggle-comment")
    public ResponseEntity<String> updateLockComment(
            @PathVariable Long postId) {
        int updated = postService.lockComment(postId);
        return updated > 0 ? ResponseEntity.ok("Cập nhật thành công!") : ResponseEntity.badRequest().body("Cập nhật thất bại!");
    }
    
    @DeleteMapping("/posts/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
            @PathVariable(value = "postId") Long postId
    ){
        this.postService.deletePost(postId);
    }
    
    
    @PatchMapping("/posts/restore-posts/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void restorePost(Model model, @PathVariable(value = "postId") Long postId){
        this.postService.restorePost(postId);
    }
}
