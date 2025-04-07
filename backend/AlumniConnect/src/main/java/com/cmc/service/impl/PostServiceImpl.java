/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cmc.components.CloudinaryService;
import com.cmc.dtos.PostDTO;
import com.cmc.pojo.Post;
import com.cmc.pojo.PostImage;
import com.cmc.repository.PostRepository;
import com.cmc.repository.UserRepository;
import com.cmc.service.PostService;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UserRepository userRepository;

    public Page<Post> getPosts(Integer pageSize, Integer offset) {
        Pageable pageable = PageRequest.of(pageSize, offset);
        List<Post> posts = postRepository.getPosts(pageable);
        long total = postRepository.countTotalPosts();
        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public int saveOrUpdate(Post post, MultipartFile[] images) {
        if (images != null) {
            Set<PostImage> imageSet = new HashSet<>();
            for (MultipartFile file : images) {
                if (file != null) {

                    try {
                        Map res = cloudinary.uploader().upload(file, ObjectUtils.asMap("resource_type", "auto"));
                        PostImage img = new PostImage();
                        img.setImage(res.get("secure_url").toString());
                        imageSet.add(img);
                    } catch (IOException ex) {
                        Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            post.setPostImageSet(imageSet);
            this.postRepository.addPost(post);
        }
        return 0;
    }

    @Override
    public int deletePost(Long id) {
        return postRepository.deletePost(id);
    }

    @Override
    public int updateContent(Long id, String content) {
        return postRepository.updateContent(id, content);
    }

    @Override
    public List<PostDTO> getPostByUserId(Long id) {
        List<Post> list = postRepository.getPostByUserId(id);
        return list.stream().map(m -> modelMapper.map(m, PostDTO.class)).collect(Collectors.toList());
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
    public int restorePost(Long id) {
        return this.postRepository.restorePost(id);
    }

    private String uploadImage(MultipartFile file) throws IOException {
        Map res = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
        return res.get("secure_url").toString();
    }

    @Override
    public Post createPost(Map<String, String> pagram, MultipartFile[] images) {
        Post p = new Post();
        p.setContent(pagram.get("content"));
        p.setUserId(this.userRepository.getUserById(Long.parseLong(pagram.get("userId"))));
        Set<PostImage> imageSet = new HashSet<>();
        if (images != null) {
            for (MultipartFile file : images) {
                if (file != null) {
                    try {
                        PostImage img = new PostImage();
                        img.setImage(uploadImage(file));
                        img.setPostId(p);
                        imageSet.add(img);
                    } catch (IOException ex) {
                        Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        p.setPostImageSet(imageSet);

        return this.postRepository.addPost(p);
    }
}
