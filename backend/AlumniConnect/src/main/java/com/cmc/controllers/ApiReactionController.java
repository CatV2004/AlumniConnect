/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.pojo.Post;
import com.cmc.pojo.Reaction;
import com.cmc.service.ReactionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author PHAT
 */
@RestController
@RequestMapping("/api")
public class ApiReactionController {
    
    @Autowired
    private ReactionService reactionService;
    
    @PostMapping("/reactions")
    public ResponseEntity<Reaction> createOrUpdateReaction(
            @RequestParam Long postId,
            @RequestParam Long userId,
            @RequestParam String reactionType
    ) {
        Reaction reaction = reactionService.addOrUpdateReaction(postId, userId, reactionType);
        return ResponseEntity.ok(reaction);
    }
    
    @DeleteMapping("/reactions/{reactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReaction(
            @PathVariable(value = "reactionId") Long reactionId,
            @RequestParam Long userId){
        int status = this.reactionService.deleteReaction(reactionId, userId);
        if (status < 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Deletion not allowed!!!");
        }
    }
    
    @GetMapping("/reactions/{postId}/posts")
    public ResponseEntity<List<Reaction>> getReactions(@PathVariable(value = "postId") Long postId){
        return new ResponseEntity(this.reactionService.getReactionsByPost(postId), HttpStatus.OK);
    }
    
}
