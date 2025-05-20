/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.components.JwtService;
import com.cmc.components.PostComponents;
import com.cmc.pojo.Post;
import com.cmc.pojo.Reaction;
import com.cmc.service.ReactionService;
import com.cmc.service.UserService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
@CrossOrigin
@RequestMapping("/api")
public class ApiReactionController {

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostComponents PostComponents;

    @PostMapping("/reactions")
    public ResponseEntity<?> createOrUpdateReaction(
            @RequestParam Long postId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String reactionType
    ) {
//        try{
            String username = this.PostComponents.authorization(authorizationHeader);
            Long userId = this.userService.getUserByUsername(username).getId();
            Reaction reactionOdd = this.reactionService.findByPostIdAndUserId(postId, userId);
            if (reactionOdd != null){
                if (!reactionOdd.getReaction().equals(reactionType)){
                    return ResponseEntity.ok(this.reactionService.addOrUpdateReaction(postId, userId, reactionType));
                }else{
                    this.reactionService.deleteReaction(reactionOdd.getId(), userId, postId);
                    return ResponseEntity.ok("DELETED");
                }
            }
            Reaction reaction = reactionService.addOrUpdateReaction(postId, userId, reactionType);
            return ResponseEntity.ok(reaction);
//        }catch (Exception ex){
//            return ResponseEntity.ok("ERR_SERVER");
//        }
       
    }

    @DeleteMapping("/reactions/{reactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReaction(
            @PathVariable(value = "reactionId") Long reactionId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam Long postId) {
        
        String username = this.PostComponents.authorization(authorizationHeader);
        int status = this.reactionService.deleteReaction(reactionId, this.userService.getUserByUsername(username).getId(), postId);
        if (status < 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Deletion not allowed!!!");
        }
    }

    @GetMapping("/reactions/{postId}/posts")
    public ResponseEntity<List<Reaction>> getReactions(@PathVariable(value = "postId") Long postId) {
        return new ResponseEntity(this.reactionService.getReactionsByPost(postId), HttpStatus.OK);
    }
    
    
    @GetMapping("/reactions/{postId}/user")
    public ResponseEntity<Reaction> getReactionUserByPost(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable(value = "postId") Long postId) {
        String username = this.PostComponents.authorization(authorizationHeader);
        Long userId = this.userService.getUserByUsername(username).getId();
        return new ResponseEntity(this.reactionService.findByPostIdAndUserId(postId, userId), HttpStatus.OK);
    }
    
    @GetMapping("/liked/{postId}")
    public ResponseEntity<Boolean> hasUserLikedPost(
           @RequestHeader("Authorization") String authorizationHeader,
           @PathVariable(value = "postId") Long postId) {
        String username = this.PostComponents.authorization(authorizationHeader);
        boolean liked = reactionService.hasUserLikedPost(postId, this.userService.getUserByUsername(username).getId());
        return ResponseEntity.ok(liked);
    }
    
    
    @GetMapping("/like-count/{postId}")
    public ResponseEntity<Long> countLikes(@PathVariable(value = "postId") Long postId) {
        Long count = reactionService.countLikesByPost(postId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/reactions/{postId}/type-reaction")
    public ResponseEntity<List<Reaction>> getReactionsByTypeReaction(@PathVariable(value = "postId") Long postId,
            @RequestParam Map<String, String> params) {
        return new ResponseEntity(this.reactionService.getByReactionType(postId, params.get("type")), HttpStatus.OK);
    }

}
