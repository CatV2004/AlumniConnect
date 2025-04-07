/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.PostDTO;
import com.cmc.pojo.Post;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author FPTSHOP
 */
public interface PostService {
    List<PostDTO> getPostByUserId(Long id);
    public Page<Post> getPosts(Integer pageSize,Integer offset) ;
    int saveOrUpdate(Post post, MultipartFile[] images);
    int deletePost(Long id);
    int updateContent(Long id, String content);
    Page<Post> searchPosts(String kw, int page, int size);
    int lockComment(Long id);
    Post getPostById(Long id);
    int restorePost(Long id);
    Post createPost(Map<String,String> pagram, MultipartFile[] images);
}
