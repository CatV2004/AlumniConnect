/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.InvitationPost;
import com.cmc.repository.InvitationPostRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        } else {
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

    @Override
    public List<InvitationPost> findInvitationPosts(Map<String, String> params) {
        Session session = this.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<InvitationPost> cq = cb.createQuery(InvitationPost.class);
        Root<InvitationPost> root = cq.from(InvitationPost.class);

        root.fetch("ugroupSet", JoinType.LEFT);
        root.fetch("userSet", JoinType.LEFT);
        root.fetch("post", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();

        if (params.containsKey("eventName")) {
            String keyword = "%" + params.get("eventName").trim().toLowerCase() + "%";
            predicates.add(cb.like(cb.lower(root.get("eventName")), keyword));
        }

        if (params.containsKey("fromTime")) {
            LocalDateTime fromTime = LocalDateTime.parse(params.get("fromTime"));
            predicates.add(cb.greaterThanOrEqualTo(root.get("eventTime"), fromTime));
        }

        if (params.containsKey("toTime")) {
            LocalDateTime toTime = LocalDateTime.parse(params.get("toTime"));
            predicates.add(cb.lessThanOrEqualTo(root.get("eventTime"), toTime));
        }

        cq.select(root).where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(root.get("eventTime")));
        cq.distinct(true);

        Query<InvitationPost> query = session.createQuery(cq);

        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int size = Integer.parseInt(params.getOrDefault("size", "10"));
        int offset = (page - 1) * size;

        query.setFirstResult(offset);
        query.setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public long countInvitationPosts(Map<String, String> params) {
        Session session = this.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<InvitationPost> root = cq.from(InvitationPost.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params.containsKey("eventName")) {
            String keyword = "%" + params.get("eventName").trim().toLowerCase() + "%";
            predicates.add(cb.like(cb.lower(root.get("eventName")), keyword));
        }

        if (params.containsKey("fromTime")) {
            LocalDateTime fromTime = LocalDateTime.parse(params.get("fromTime"));
            predicates.add(cb.greaterThanOrEqualTo(root.get("eventTime"), fromTime));
        }

        if (params.containsKey("toTime")) {
            LocalDateTime toTime = LocalDateTime.parse(params.get("toTime"));
            predicates.add(cb.lessThanOrEqualTo(root.get("eventTime"), toTime));
        }

        cq.select(cb.count(root)).where(predicates.toArray(new Predicate[0]));

        return session.createQuery(cq).getSingleResult();
    }
}
