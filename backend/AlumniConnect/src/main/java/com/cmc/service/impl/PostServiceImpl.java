/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.dtos.PostDTO;
import com.cmc.pojo.Post;
import com.cmc.repository.PostRepository;
import com.cmc.service.PostService;
import java.util.List;
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
    public Iterable<Post> getPosts(Integer pageSize, Integer offset) {
        return postRepository.getPosts("", PageRequest.of(offset, pageSize));
    }

    @Override
    public int addPost(Post post) {
        return postRepository.addPost(post);
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
        List<Post> posts = postRepository.getPosts(kw, pageable);

        long total = postRepository.countTotalPosts(kw);

        return new PageImpl<>(posts, pageable, total);
    }

}
