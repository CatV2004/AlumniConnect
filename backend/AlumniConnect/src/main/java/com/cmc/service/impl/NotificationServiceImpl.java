/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.dtos.NotificationDTO;
import com.cmc.pojo.Notification;
import com.cmc.pojo.User;
import com.cmc.repository.NotificationRepository;
import com.cmc.service.NotificationService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author FPTSHOP
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepo;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    @Async
    public void createNotification(User recipient, User sender, String message, String link, String type) {
        try {
            Notification notification = new Notification();
            notification.setRecipient(recipient);
            notification.setSender(sender);
            notification.setMessage(message);
            notification.setLink(link);
            notification.setType(type);
            notificationRepo.save(notification);

            System.out.println("recipient.getId().toString(): " + recipient.getId().toString());
            System.out.println("username: " + recipient.getUsername());

            messagingTemplate.convertAndSendToUser(
                    recipient.getUsername(),
                    "/queue/notifications",
                    new NotificationDTO(
                            notification.getId(),
                            message,
                            link,
                            sender.getUsername(),
                            sender.getAvatar(),
                            LocalDateTime.now().toString(),
                            false,
                            type
                    )
            );
        } catch (Exception e) {
            System.err.println("Error while creating notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<NotificationDTO> getUserNotifications(Long userId) {
        List<Notification> notifications = notificationRepo.findByRecipientId(userId);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepo.findUnreadByRecipientId(userId);
    }

    @Override
    public void markNotificationAsRead(Long notificationId) {
        notificationRepo.markAsRead(notificationId);
    }

    @Override
    public void markAllNotificationsAsRead(Long userId) {
        notificationRepo.markAllAsRead(userId);
    }

    private NotificationDTO convertToDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getMessage(),
                notification.getLink(),
                notification.getSender().getUsername(),
                notification.getSender().getAvatar(),
                notification.getCreatedAt().toString(),
                notification.isRead(),
                notification.getType()
        );
    }

}
