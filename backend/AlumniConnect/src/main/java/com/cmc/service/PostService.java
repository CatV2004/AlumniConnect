/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.PostDTO;
import com.cmc.pojo.Post;
import com.cmc.pojo.PostImage;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author FPTSHOP
 */
public interface PostService {

    List<PostDTO> getPostByUserId(Long id);

    public Page<Post> getPosts(Integer page, Integer size);

    Post saveOrUpdate(Post post, String[] images);

    boolean deletePost(Long postId);

    Page<Post> searchPosts(String kw, int page, int size);

    int lockComment(Long id);

    Post getPostById(Long id);

    boolean restorePost(Long postId);

    Post addPost(Post post, List<String> imageUrls);

    long countPosts(String keyword);

    long countPosts();

    boolean updatePostContent(Long postId, String newContent);

    boolean deleteImage(Long imageId);

    PostImage addImageToPost(Long postId, String imageUrl);

    List<PostImage> getImagesOfPost(Long postId);
    
    List<Post> getPostsByUser(Long userId);
}
