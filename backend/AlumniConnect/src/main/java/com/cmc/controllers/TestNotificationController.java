/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.pojo.User;
import com.cmc.repository.UserRepository;
import com.cmc.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author FPTSHOP
 */
@RestController
@RequestMapping("/test")
public class TestNotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/send-notification")
    public ResponseEntity<String> sendTestNotification() {
        User sender = userRepository.getUserByUsername("admin");  
        User recipient = userRepository.getUserByUsername("cuong");  

        if (sender == null || recipient == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        notificationService.createNotification(
                recipient,
                sender,
                "Thông báo test từ backend",
                "/invitation/details",
                "INVITATION"
        );

        return ResponseEntity.ok("Notification sent");
    }
}
