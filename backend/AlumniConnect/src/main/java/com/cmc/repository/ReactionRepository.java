/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.pojo.Reaction;
import java.util.List;

/**
 *
 * @author PHAT
 */
public interface ReactionRepository {

    Reaction findById(Long id);

    List<Reaction> findByPostId(Long postId);

    List<Reaction> findByReactionType(String reactionType);

    Reaction findByPostIdAndUserId(Long postId, Long userId);
    
    Reaction saveOrUpdate(Reaction reaction);
    
    void deleteReaction(Reaction reaction);
}
