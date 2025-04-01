/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.pojo.Post;
import com.cmc.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author PHAT
 */
@Controller
public class PostController {

    @Autowired
    private PostService postService;

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
}
