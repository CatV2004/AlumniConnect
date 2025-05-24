/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.Notification;
import com.cmc.repository.NotificationRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author FPTSHOP
 */
@Repository
@Transactional
public class NotificationRepositoryImpl implements NotificationRepository {

    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    private Session getSession() {
        return sessionFactory.getObject().getCurrentSession();
    }

    @Override
    public void save(Notification notification) {
        Session session = this.getSession();
        session.persist(notification);
    }

    @Override
    public List<Notification> findByRecipientId(Long userId) {
        Session session = this.getSession();
        Query<Notification> query = session.createQuery(
                "SELECT n FROM Notification n JOIN FETCH n.recipient WHERE n.recipient.id = :userId ORDER BY n.createdAt DESC",
                Notification.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<Notification> findUnreadByRecipientId(Long userId) {
        Session session = this.getSession();
        Query<Notification> query = session.createQuery(
                "FROM Notification n WHERE n.recipient.id = :userId AND n.read = false ORDER BY n.createdAt DESC",
                Notification.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public void markAsRead(Long notificationId) {
        Session session = this.getSession();
        Query<?> query = session.createQuery(
                "UPDATE Notification n SET n.read = true WHERE n.id = :notificationId");
        query.setParameter("notificationId", notificationId);
        query.executeUpdate();
    }

    @Override
    public void markAllAsRead(Long userId) {
        Session session = this.getSession();
        Query<?> query = session.createQuery(
                "UPDATE Notification n SET n.read = true WHERE n.recipient.id = :userId AND n.read = false");
        query.setParameter("userId", userId);
        query.executeUpdate();
    }
}
