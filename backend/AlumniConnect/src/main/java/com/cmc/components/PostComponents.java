/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.components;

import com.cmc.dtos.PostDTO;
import com.cmc.pojo.Post;
import com.cmc.pojo.PostImage;
import com.cmc.service.UserService;
import java.util.HashSet;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author PHAT
 */
@Component
public class PostComponents {

    @Autowired
    private JwtService jwtService;

    public PostDTO toPostDTO(Post post) {
        List<String> images = post.getPostImageSet().stream().map(PostImage::getImage).collect(toList());
        return new PostDTO(post.getId(), post.getContent(), post.getLockComment(), post.getCreatedDate(),
                post.getUpdatedDate(), post.getDeletedDate(), post.getActive(),
                post.getUserId(), images);
    }

    public Post toPost(PostDTO postDTO) {
        return new Post(postDTO.getId(), postDTO.getContent(), postDTO.getLockComment(),
                postDTO.getCreatedDate(), postDTO.getUpdatedDate(),
                postDTO.getDeletedDate(), postDTO.getActive(),
                postDTO.getUserId(), new HashSet<>());
    }

    public String authorization(String auth) {
        String token = auth.replace("Bearer ", "");
        return jwtService.getUsernameFromToken(token);
    }

}
