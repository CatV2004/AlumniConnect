/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.pojo.Post;
import com.cmc.pojo.PostImage;
import com.cmc.pojo.SurveyPost;
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

    List<Post> getPostsByUserId(Long id, int page, int size);

    List<Post> getPostByKeywords(String kw, Pageable pageable);

    List<Post> getPosts(Pageable pageable);

    Post getPostId(Long id);

    Post addPost(Post post);

    int deletePost(Long id);

    int updateContent(Long id, String content);

    long countTotalPosts();

    int lockComment(Long id);

    int restorePost(Long id);

    PostImage addPostImage(Long postId, String imageUrl);

    int deletePostImage(Long imageId);

    List<PostImage> getImagesByPostId(Long postId);

    PostImage getPostImageById(Long id);

    long countTotalPostsByUser(Long userId);

}
