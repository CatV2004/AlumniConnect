/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.pojo.Reaction;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PHAT
 */
public interface ReactionRepository {

    Reaction findById(Long id);

    List<Reaction> findByPostId(Long postId);

    List<Reaction> findByReactionType(String reactionType, Long postId);

    Reaction findByPostIdAndUserId(Long postId, Long userId);
    
    Reaction saveOrUpdate(Reaction reaction);
    
    void deleteReaction(Reaction reaction);
    
    Map<String, Long> countReactionsByPostId(Long postId);
    
    boolean hasUserLikedPost(Long postId, Long userId);
    
}
