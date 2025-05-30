/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.components;

import com.cmc.pojo.Comment;
import com.cmc.repository.CommentRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author PHAT
 */
@Component
public class CommentComponentScheduled {
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private PostComponents postComponents;

    @Scheduled(fixedRate = 3600000)
    public void analyzeUnlabeledComments() {
        List<Comment> unlabeledComments = this.commentRepository.getUnlabeledComments();

        for (Comment comment : unlabeledComments) {
            try {
                Map<String, String> payload = new HashMap<>();
                payload.put("id", comment.getId().toString());
                payload.put("text", comment.getContent());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

                ResponseEntity<Map> response = restTemplate.postForEntity(
                        "http://localhost:5001/analyze", request, Map.class);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    String label = (String) response.getBody().get("label");
                    comment.setLabel(label);
                    if (label.equals("NEGATIVE")) {
                        comment.setActive(Boolean.FALSE);
                    }
                    commentRepository.saveOrUpdate(comment);
                    postComponents.checkAndLockCommentsIfNegative(comment.getPostId().getId());
                }

            } catch (Exception ex) {
                System.err.println("Lỗi phân tích cảm xúc: " + ex.getMessage());
            }
        }
    }
}
