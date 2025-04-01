/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.dtos.PostDTO;
import com.cmc.pojo.Post;
import com.cmc.service.PostService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author PHAT
 */
@RestController
public class ApiPostController {
    
    @Autowired
    private PostService postService;
    
    @GetMapping("/api/posts")
    public ResponseEntity<Iterable<Post>> getPosts(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        return new ResponseEntity<>(
                this.postService.getPosts(offset,pageSize),
                HttpStatus.OK
        );
    }
    
    @PatchMapping("/admin/posts/{postId}/toggle-comment")
    public ResponseEntity<String> updateLockComment(
            @PathVariable Long postId) {
        int updated = postService.lockComment(postId);
        return updated > 0 ? ResponseEntity.ok("Cập nhật thành công!") : ResponseEntity.badRequest().body("Cập nhật thất bại!");

        
    }
}
