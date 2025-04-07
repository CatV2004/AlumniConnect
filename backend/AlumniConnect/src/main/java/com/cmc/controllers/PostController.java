/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.pojo.Post;
import com.cmc.pojo.PostImage;
import com.cmc.service.PostService;
import com.cmc.service.UserService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author PHAT
 */
@Controller
public class PostController {

    @Autowired
    private PostService postService;
    
    @Autowired
    private UserService userService;

    @RequestMapping("/admin/posts")
    public String getPosts(
            @RequestParam(required=false, defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", value = "page", name = "page") int page,
            @RequestParam(defaultValue = "10",value = "size", name = "size") int size,
            Model model) {
        Page<Post> pages = postService.getPosts(page, size);
        model.addAttribute("posts", pages.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pages.getTotalPages());
        return "admin_posts";
    }
    
    
    @GetMapping("/admin/add-posts")
    public String createPost(Model model){
        model.addAttribute("post", new Post());
        model.addAttribute("userList", userService.getUsers());
        return "posts";
    }
    
   
    
    @GetMapping("/admin/posts/{postId}")
    public String updatePost(Model model, @PathVariable(value = "postId") Long postId){
        model.addAttribute("post", this.postService.getPostById(postId));
        model.addAttribute("userList", userService.getUsers());
        return "posts";
    }
    
    @PostMapping("/admin/add-posts")
    public String addPost(@ModelAttribute(value = "post") Post post,
                    @RequestParam("images") MultipartFile[] images){
        this.postService.saveOrUpdate(post, images);
        return "redirect:/admin/posts";
    }
   
}
