/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.components;
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

    public String authorization(String auth) {
        String token = auth.replace("Bearer ", "");
        return jwtService.getUsernameFromToken(token);
    }
    
    @Scheduled(fixedRate = 86400000)
    public void autoDeletedPost() {
        LocalDateTime deadline = LocalDateTime.now().minusDays(30);
        postService.autoDeletedPost(deadline);
    }

}
