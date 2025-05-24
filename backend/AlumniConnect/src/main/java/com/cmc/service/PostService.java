/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.PageResponse;
import com.cmc.dtos.PostResponseDTO;
import com.cmc.pojo.Post;
import com.cmc.pojo.PostImage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author FPTSHOP
 */
public interface PostService {

    public Page<Post> getPosts(Integer page, Integer size);
    
    PageResponse<PostResponseDTO> getPosts(Map<String, Object> params);
    
    PageResponse<PostResponseDTO> getPostsByUser(Map<String, Object> params);

    void saveOrUpdate(Post post, String[] images);

    boolean deletePost(Long postId);
    
    boolean deletePostPermanently(Long postId);
    
    PageResponse<PostResponseDTO> getDeletedPostsByUser(Map<String, Object> params);

    Page<Post> searchPosts(String kw, int page, int size);

    int lockComment(Long id);
    
    PostResponseDTO getPostResponseById(Long postId);

    Post getPostById(Long id);
    
    Post getPostIdOfDL(Long id);

    boolean restorePost(Long postId);

    public Post addOrUpdatePost(Post post, List<MultipartFile> images, List<Long> existingImageIds, boolean isCreate);

    void deleteImagesNotInList(Post post, List<Long> keepImageIds);
        
    long countPosts(String keyword);

    long countPosts();

    boolean updatePostContent(Long postId, String newContent);

    PostImage addImageToPost(Long postId, String imageUrl);

    List<PostImage> getImagesOfPost(Long postId);
    
    PostImage getImagePostById(Long idImage);
    
    void createImagePost(Long postId, List<MultipartFile> files);
    
    void autoDeletedPost(LocalDateTime dateTime);
}
