/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.dtos.PostDTO;
import com.cmc.pojo.Post;
import com.cmc.pojo.PostImage;
import com.cmc.repository.PostRepository;
import com.cmc.service.PostService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author FPTSHOP
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<Post> getPosts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Post> posts = postRepository.getPosts(pageable);
        long total = postRepository.countTotalPosts();
        return new PageImpl<>(posts, pageable, total);
    }
    

    @Override
    public Post saveOrUpdate(Post post, String[] images) {
        if (images != null) {
            Set<PostImage> currentImages = post.getPostImageSet();

            Set<String> newImageNames = new HashSet<>();
            if (images != null) {
                newImageNames.addAll(Arrays.asList(images));
            }
            if (currentImages != null) {
                currentImages.removeIf(img -> !newImageNames.contains(img.getImage()));
            } else {
                currentImages = new HashSet<>();
            }

            for (String file : newImageNames) {
                boolean exists = currentImages.stream().anyMatch(i -> i.getImage().equals(file));
                if (!exists) {
                    PostImage img = new PostImage();
                    img.setImage(file);
                    img.setPostId(post);
                    currentImages.add(img);
                }
            }
            post.setPostImageSet(currentImages);
            return this.postRepository.saveOrUpdate(post);
        }
        return this.postRepository.saveOrUpdate(post);
    }

    @Override
    public Page<Post> searchPosts(String kw, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Post> posts = postRepository.getPostByKeywords(kw, pageable);

        long total = postRepository.countTotalPosts(kw);

        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public int lockComment(Long id) {
        return this.postRepository.lockComment(id);
    }

    @Override
    public Post getPostById(Long id) {
        return this.postRepository.getPostId(id);
    }

    @Override
    public Post addPost(Post post, List<String> imageUrls) {
        post.setCreatedDate(LocalDateTime.now());
        post.setActive(true);
        post.setLockComment(false);

        Post savedPost = postRepository.addPost(post);
        Set<PostImage> images = new HashSet<>();
        if (imageUrls != null) {
            for (String url : imageUrls) {
                images.add(postRepository.addPostImage(savedPost.getId(), url));
            }
        }
        savedPost.setPostImageSet(images);
        return savedPost;
    }

    @Override
    public boolean updatePostContent(Long postId, String newContent) {
        return postRepository.updateContent(postId, newContent) == 1;
    }

    @Override
    public boolean deletePost(Long postId) {
        return postRepository.deletePost(postId) == 1;
    }

    @Override
    public boolean restorePost(Long postId) {
        return postRepository.restorePost(postId) == 1;
    }

    @Override
    public List<PostImage> getImagesOfPost(Long postId) {
        return postRepository.getImagesByPostId(postId);
    }

    @Override
    public PostImage addImageToPost(Long postId, String imageUrl) {
        return postRepository.addPostImage(postId, imageUrl);
    }

    @Override
    public boolean deleteImage(Long imageId) {
        return postRepository.deletePostImage(imageId) == 1;
    }

    @Override
    public long countPosts() {
        return postRepository.countTotalPosts();
    }

    @Override
    public long countPosts(String keyword) {
        return postRepository.countTotalPosts(keyword);
    }
    
    @Override
    public PostImage getImagePostById(Long idImage){
        return this.postRepository.getPostImageById(idImage);
    }

    @Override
    public Page<Post> getPostsByUser(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Post> posts = postRepository.getPostByUserId(userId, page, size);
        long total = postRepository.countTotalPostsByUser(userId);
        return new PageImpl<>(posts, pageable, total);
    }
}
