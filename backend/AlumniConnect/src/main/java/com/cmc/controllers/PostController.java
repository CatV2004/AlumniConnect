/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.components.CloudinaryService;
import com.cmc.pojo.Post;
import com.cmc.service.PostService;
import com.cmc.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Autowired
    private CloudinaryService cloudianryService;

    @RequestMapping("/admin/posts")
    public String getPosts(
            @RequestParam(required = false, defaultValue = "", value = "kw") String keyword,
            @RequestParam(defaultValue = "0", value = "page", name = "page") int page,
            @RequestParam(defaultValue = "10", value = "size", name = "size") int size,
            Model model) {
        
        Page<Post> pages = this.postService.searchPosts(keyword, page, size);
        model.addAttribute("posts", pages.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pages.getTotalPages());
        return "admin_posts";
    }

    @GetMapping("/admin/add-posts")
    public String createPost(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("userList", userService.getUsers());
        return "posts";
    }

    @GetMapping("/admin/posts/{postId}")
    public String updatePost(Model model, @PathVariable(value = "postId") Long postId) {
        model.addAttribute("post", this.postService.getPostById(postId));
        model.addAttribute("userList", userService.getUsers());
        return "posts";
    }

    @PostMapping(value = "/admin/add-posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addPost(@ModelAttribute(value = "post") Post post,
            @RequestParam(value = "images") MultipartFile[] images) {
        
        List<String> urlImage = new ArrayList<>();
        if (images != null) {
            for (MultipartFile file : images) {
                urlImage.add(this.cloudianryService.uploadFile(file));
            }
        }
        this.postService.saveOrUpdate(post, urlImage.toArray(new String[0]));
        return "redirect:/admin/posts";
    }
    
}
