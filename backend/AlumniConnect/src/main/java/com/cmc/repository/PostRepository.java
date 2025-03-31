/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.pojo.Post;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


/**
 *
 * @author FPTSHOP
 */
public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {
    List<Post> getPostByUserId(Long id);
    List<Post> getPosts(String kw, Pageable pageable);
    List<Post> getPostId(Long id);
    int addPost(Post post);
    int deletePost(Long id);
    int updateContent(Long id,String content);
    long countTotalPosts(String kw);
}
