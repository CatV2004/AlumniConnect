/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.pojo.Reaction;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PHAT
 */
public interface ReactionService {

    Reaction addReaction(Long postId, Long userId, String reactionType);

    List<Reaction> getReactionsByPost(Long PostId);

    int deleteReaction(Long reactionId, Long userId, Long postId);

    Reaction addOrUpdateReaction(Long postId, Long userId, String reactionType);

    List<Reaction> getByReactionType(Long postId, String reactionType);

    boolean hasUserLikedPost(Long postId, Long userId);

    Map<String, Long> countLikesByPost(Long postId);
    
    Reaction findByPostIdAndUserId(Long postId, Long userId);
}
