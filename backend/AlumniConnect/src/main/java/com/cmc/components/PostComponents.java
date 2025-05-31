/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.components;
import com.cmc.pojo.Post;
import com.cmc.repository.CommentRepository;
import com.cmc.repository.PostRepository;
import com.cmc.service.PostService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author PHAT
 */
@Component
public class PostComponents {

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private PostRepository postRepo;
    
    @Autowired
    private CommentRepository commentRepository;

    public String authorization(String auth) {
        String token = auth.replace("Bearer ", "");
        return jwtService.getUsernameFromToken(token);
    }
    
    @Scheduled(fixedRate = 86400000)
    public void autoDeletedPost() {
        LocalDateTime deadline = LocalDateTime.now().minusDays(30);
        postService.autoDeletedPost(deadline);
    }
    
    
    public void checkAndLockCommentsIfNegative(Long postId) {
    Long negativeCount = commentRepository.countByPostIdAndLabel(postId, "NEGATIVE");

    if (negativeCount >= 5) {
        Post post = this.postRepo.getPostId(postId);
            if (Boolean.TRUE.equals(post.getLockComment())) {
                post.setLockComment(Boolean.TRUE);
                post.setUpdatedDate(LocalDateTime.now());
                this.postRepo.saveOrUpdate(post);
            }
        }
    }
}


