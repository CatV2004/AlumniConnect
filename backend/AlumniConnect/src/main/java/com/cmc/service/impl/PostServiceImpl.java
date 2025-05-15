/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.components.CloudinaryService;
import com.cmc.components.PostMapper;
import com.cmc.dtos.PageResponse;
import com.cmc.dtos.PostResponseDTO;

import com.cmc.pojo.Post;
import com.cmc.pojo.PostImage;
import com.cmc.repository.PostRepository;
import com.cmc.service.PostImageService;
import com.cmc.service.PostService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author FPTSHOP
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostImageService postImageService;

    @Autowired
    private CloudinaryService CloudinaryService;

    @Override
    public Page<Post> getPosts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Post> posts = postRepository.getPosts(pageable);
        long total = postRepository.countTotalPosts();
        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public void saveOrUpdate(Post post, String[] images) {
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
        }
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
    public PostResponseDTO getPostResponseById(Long postId) {
        Post post = postRepository.getPostId(postId);
        return postMapper.toPostResponseDTO(post);
    }

    @Override
    public Post getPostIdOfDL(Long id) {
        return this.postRepository.getPostIdOfDL(id);
    }

    @Override
    public Post addOrUpdatePost(Post post, List<MultipartFile> images, List<Long> existingImageIds, boolean isCreate) {
        if (isCreate) {
            post.setCreatedDate(LocalDateTime.now());
            post.setActive(true);
//            post.setLockComment(false);
        } else {
            post.setUpdatedDate(LocalDateTime.now());
            this.deleteImagesNotInList(post, existingImageIds);
        }

        Post savedPost = postRepository.addPost(post);
        postImageService.uploadAndSaveImages(post, images);
        return savedPost;
    }

    @Override
    public void deleteImagesNotInList(Post post, List<Long> keepImageIds) {
        this.postRepository.deleteImagesNotInList(post, keepImageIds);
    }

    @Override
    public boolean updatePostContent(Long postId, String newContent) {
        return postRepository.updateContent(postId, newContent) == 1;
    }

    @Override
    public boolean deletePost(Long postId) {
        return postRepository.deletePost(postId);
    }

    @Override
    public boolean restorePost(Long postId) {
        return postRepository.restorePost(postId);
    }

    @Override
    public boolean deletePostPermanently(Long postId) {
        return postRepository.deletePostPermanently(postId);
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
    public long countPosts() {
        return postRepository.countTotalPosts();
    }

    @Override
    public long countPosts(String keyword) {
        return postRepository.countTotalPosts(keyword);
    }

    @Override
    public PostImage getImagePostById(Long idImage) {
        return this.postRepository.getPostImageById(idImage);
    }

    @Override
    public PageResponse<PostResponseDTO> getPostsByUser(Map<String, Object> params) {
        int page = params.get("page") != null ? (int) params.get("page") : 1;
        int size = params.get("size") != null ? (int) params.get("size") : 10;

        List<Post> posts = postRepository.getPostsByUserId(params);
        long totalItems = postRepository.countTotalPostsByUser(params);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        List<PostResponseDTO> postDTOs = posts.stream()
                .map(postMapper::toPostResponseDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(postDTOs, page, size, totalItems, totalPages);
    }

    @Override
    public PageResponse<PostResponseDTO> getPosts(Map<String, Object> params) {
        int page = params.get("page") != null ? (int) params.get("page") : 1;
        int size = params.get("size") != null ? (int) params.get("size") : 10;

        List<Post> posts = postRepository.findPosts(params);
        long totalItems = postRepository.countPosts(params);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        List<PostResponseDTO> postDTOs = posts.stream()
                .map(postMapper::toPostResponseDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(postDTOs, page, size, totalItems, totalPages);
    }

//    @Override
//    public List<Post> getDeletedPostsByUser(Long userId) {
//        return postRepository.getDeletedPostsByUser(userId);
//    }
    @Override
    public PageResponse<PostResponseDTO> getDeletedPostsByUser(Map<String, Object> params) {
        int page = params.get("page") != null ? (int) params.get("page") : 1;
        int size = params.get("size") != null ? (int) params.get("size") : 10;

        List<Post> deletedPosts = postRepository.getDeletedPostsByUser(params);
        long totalItems = postRepository.countDeletedPostsByUser(params);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        List<PostResponseDTO> postDTOs = deletedPosts.stream()
                .map(postMapper::toPostResponseDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(postDTOs, page, size, totalItems, totalPages);
    }

    @Override
    public void createImagePost(Long postId, List<MultipartFile> files) {
        for (MultipartFile file : files) {
            try {
                String imageUrl = CloudinaryService.uploadFile(file);
                this.postRepository.createImagePost(postId, imageUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
