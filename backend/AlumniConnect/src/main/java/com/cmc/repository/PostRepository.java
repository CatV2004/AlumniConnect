/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.pojo.Post;
import com.cmc.pojo.PostImage;
import com.cmc.pojo.SurveyPost;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author FPTSHOP
 */
public interface PostRepository {

    void saveOrUpdate(Post post);
    
    long countPosts(Map<String, Object> params);

    long countTotalPosts(String kw);

    List<Post> findPosts(Map<String, Object> params);

    List<Post> getPostsByUserId(Map<String, Object> params);

    List<Post> getPostByKeywords(String kw, Pageable pageable);

    List<Post> getPosts(Pageable pageable);

    Post getPostId(Long id);
    
    Post getPostIdOfDL(Long id);

    Post addPost(Post post);

    boolean deletePost(Long id);
    
    boolean deletePostPermanently(Long postId);
    
    List<Post> getDeletedPostsByUser(Map<String, Object> params);
    
    long countDeletedPostsByUser(Map<String, Object> params);

    int updateContent(Long id, String content);

    long countTotalPosts();

    int lockComment(Long id);

    boolean restorePost(Long id);

    PostImage addPostImage(Long postId, String imageUrl);

    List<PostImage> getImagesByPostId(Long postId);

    PostImage getPostImageById(Long id);

    long countTotalPostsByUser(Map<String, Object> params);
    
    void createImagePost(Long postId, String url);
    
    void deleteImagesByPost(Post post);
    
    void deleteImagesNotInList(Post post, List<Long> keepImageIds);
    
    List<Post> getPostsDelete(LocalDateTime dateTime);
    
    void deletePost(Post p);
    
    boolean updateStatus(long postId, boolean active);
    
    Post getPostUnActiveById(Long id);
}
