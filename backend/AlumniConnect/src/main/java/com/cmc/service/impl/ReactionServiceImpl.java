/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.pojo.Post;
import com.cmc.pojo.Reaction;
import com.cmc.pojo.User;
import com.cmc.repository.ReactionRepository;
import com.cmc.service.PostService;
import com.cmc.service.ReactionService;
import com.cmc.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author PHAT
 */
@Service
public class ReactionServiceImpl implements ReactionService {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private ReactionRepository reactionRepo;

    @Override
    public Reaction addReaction(Long postId, Long userId, String reactionType) {
        Post p = this.postService.getPostById(postId);
        User u = this.userService.getUserById(userId);
        
        if (p != null && u != null){
            Reaction re = new Reaction();
            re.setReaction(reactionType);
            re.setUserId(u);
            re.setPostId(p);
            return this.reactionRepo.saveOrUpdate(re);
        }
        return null;
    }

    @Override
    public List<Reaction> getReactionsByPost(Long PostId) {
        return this.reactionRepo.findByPostId(PostId);
    }

    @Override
    public int deleteReaction(Long reactionId, Long userId) {
        Reaction reaction = this.reactionRepo.findById(reactionId);
        System.out.print(reaction.getUserId());
        if (reaction.getUserId().getId().equals(userId)){
            this.reactionRepo.deleteReaction(reaction);
            return 1;
        }
        return -1;
    }

    @Override
    public Reaction addOrUpdateReaction(Long postId, Long userId, String reactionType) {
        Reaction reaction = this.reactionRepo.findByPostIdAndUserId(postId, userId);
        if (reaction == null){
            reaction = new Reaction();
            reaction.setPostId(this.postService.getPostById(postId));
            reaction.setUserId(this.userService.getUserById(userId));
            reaction.setCreatedDate(LocalDateTime.now());
            reaction.setActive(Boolean.TRUE);
        }
        reaction.setUpdatedDate(LocalDateTime.now());
        reaction.setReaction(reactionType);
        return this.reactionRepo.saveOrUpdate(reaction);
    }
}
