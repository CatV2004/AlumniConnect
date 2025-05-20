/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.InvitationPost;
import com.cmc.repository.InvitationPostRepository;
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
public class InvitationPostRepositoryImpl implements InvitationPostRepository {

    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    private Session getSession() {
        return sessionFactory.getObject().getCurrentSession();
    }

    @Override
    public InvitationPost findById(Long id) {
        return getSession().get(InvitationPost.class, id);
    }

    @Override
    public void save(InvitationPost invitationPost) {
        Session session = this.getSession();
        if (invitationPost.getId() == null) {
            session.persist(invitationPost);
        }
        else {
            session.merge(invitationPost);
        }
    }

    @Override
    public List<InvitationPost> findByEventName(String eventName) {
        String hql = "FROM InvitationPost ip WHERE ip.eventName LIKE :eventName";
        Query<InvitationPost> query = getSession().createQuery(hql, InvitationPost.class);
        query.setParameter("eventName", "%" + eventName + "%");
        return query.list();
    }

    @Override
    public List<InvitationPost> findByGroupId(Long groupId) {
        String hql = "SELECT ip FROM InvitationPost ip JOIN ip.ugroupSet ug WHERE ug.id = :groupId";
        Query<InvitationPost> query = getSession().createQuery(hql, InvitationPost.class);
        query.setParameter("groupId", groupId);
        return query.list();
    }

    @Override
    public List<InvitationPost> findByInvitedUserId(Long userId) {
        String hql = "SELECT ip FROM InvitationPost ip JOIN ip.userSet u WHERE u.id = :userId";
        Query<InvitationPost> query = getSession().createQuery(hql, InvitationPost.class);
        query.setParameter("userId", userId);
        return query.list();
    }

    @Override
    public void delete(Long id) {
        InvitationPost invitationPost = getSession().get(InvitationPost.class, id);
        if (invitationPost != null) {
            getSession().remove(invitationPost);
        }
    }
}
