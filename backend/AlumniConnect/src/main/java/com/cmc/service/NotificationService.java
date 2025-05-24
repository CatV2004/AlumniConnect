/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.NotificationDTO;
import com.cmc.pojo.Notification;
import com.cmc.pojo.User;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public interface NotificationService {

    void createNotification(User recipient, User sender, String message, String link, String type);

    List<NotificationDTO> getUserNotifications(Long userId);

    List<Notification> getUnreadNotifications(Long userId);

    void markNotificationAsRead(Long notificationId);

    void markAllNotificationsAsRead(Long userId);
}
