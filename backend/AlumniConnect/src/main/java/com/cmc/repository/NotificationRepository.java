/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.pojo.Notification;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public interface NotificationRepository {

    void save(Notification notification);

    List<Notification> findByRecipientId(Long userId);

    List<Notification> findUnreadByRecipientId(Long userId);

    void markAsRead(Long notificationId);

    void markAllAsRead(Long userId);
}
