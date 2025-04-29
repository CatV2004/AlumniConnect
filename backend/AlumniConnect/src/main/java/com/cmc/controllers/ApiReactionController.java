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
@RequestMapping("/api")
@CrossOrigin
public class ApiReactionController {

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostComponents PostComponents;

    @PostMapping("/reactions")
    public ResponseEntity<Reaction> createOrUpdateReaction(
            @RequestParam Long postId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String reactionType
    ) {
        String username = this.PostComponents.authorization(authorizationHeader);
        Reaction reaction = reactionService.addOrUpdateReaction(postId, this.userService.getUserByUsername(username).getId(), reactionType);
        return ResponseEntity.ok(reaction);
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

    @GetMapping("/reactions/{postId}/type-reaction")
    public ResponseEntity<List<Reaction>> getReactionsByTypeReaction(@PathVariable(value = "postId") Long postId,
            @RequestParam Map<String, String> params) {
        return new ResponseEntity(this.reactionService.getByReactionType(postId, params.get("type")), HttpStatus.OK);
    }

}
