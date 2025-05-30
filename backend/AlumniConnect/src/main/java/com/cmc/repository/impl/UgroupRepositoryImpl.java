/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.Ugroup;
import com.cmc.pojo.User;
import com.cmc.repository.UgroupRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
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
public class UgroupRepositoryImpl implements UgroupRepository {

    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    private Session getSession() {
        return sessionFactory.getObject().getCurrentSession();
    }

    @Override
    public Ugroup save(Ugroup ugroup) {
        Session session = this.getSession();
        if (ugroup.getId() == null) {
            ugroup.setCreatedDate(LocalDateTime.now());
            ugroup.setActive(true);
            session.persist(ugroup);
        } else {
            ugroup.setUpdatedDate(LocalDateTime.now());
            ugroup = (Ugroup) session.merge(ugroup);
        }
        return ugroup;
    }

    @Override
    public Ugroup findById(Long id) {
        Session session = this.getSession();
        return session.get(Ugroup.class, id);
    }

    @Override
    public Ugroup findByGroupName(String groupName) {
        Session session = this.getSession();
        Query<Ugroup> query = session.createQuery(
                "FROM Ugroup ug WHERE ug.groupName = :groupName",
                Ugroup.class);
        query.setParameter("groupName", groupName);
        return query.uniqueResult();
    }

    @Override
    public List<Ugroup> findGroups() {
        Session session = this.getSession();
        Query<Ugroup> query = session.createQuery(
                "FROM Ugroup WHERE active = true",
                Ugroup.class);
        return query.getResultList();
    }

    @Override
    public List<Ugroup> findGroups(Map<String, String> params) {
        Session session = this.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Ugroup> cq = cb.createQuery(Ugroup.class);
        Root<Ugroup> root = cq.from(Ugroup.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params.containsKey("active")) {
            Boolean active = Boolean.parseBoolean(params.get("active"));
            predicates.add(cb.equal(root.get("active"), active));
        }

        if (params.containsKey("groupName")) {
            String keyword = "%" + params.get("groupName").trim() + "%";
            predicates.add(cb.like(cb.lower(root.get("groupName")), keyword.toLowerCase()));
        }

        cq.select(root).where(predicates.toArray(new Predicate[0]));
        Query<Ugroup> query = session.createQuery(cq);

        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int size = Integer.parseInt(params.getOrDefault("size", "10"));
        int offset = (page - 1) * size;

        query.setFirstResult(offset);
        query.setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public long countGroups(Map<String, String> params) {
        Session session = this.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Ugroup> root = cq.from(Ugroup.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params.containsKey("active")) {
            Boolean active = Boolean.parseBoolean(params.get("active"));
            predicates.add(cb.equal(root.get("active"), active));
        }

        cq.select(cb.count(root)).where(predicates.toArray(new Predicate[0]));

        return session.createQuery(cq).getSingleResult();
    }

    @Override
    public void addUserToGroup(Long groupId, Long userId) {
        Session session = getSession();
        Ugroup group = session.get(Ugroup.class, groupId);
        User user = session.get(User.class, userId);

        if (group != null && user != null) {
            group.getUserSet().add(user);
            user.getUgroupSet().add(group);
            session.persist(group);
            session.persist(user);
        }
    }

    @Override
    public void removeUserFromGroup(Long groupId, Long userId) {
        Session session = getSession();
        Ugroup group = session.get(Ugroup.class, groupId);
        User user = session.get(User.class, userId);

        if (group != null && user != null) {
            group.getUserSet().remove(user);
            user.getUgroupSet().remove(group);
            session.persist(group);
            session.persist(user);
        }
    }

    @Override
    public boolean existsById(Long id) {
        return this.getSession().createQuery(
                "SELECT count(g) > 0 FROM Ugroup g WHERE g.id = :id",
                Boolean.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public boolean isUserInGroup(Long userId, Long groupId) {
        return this.getSession().createQuery(
                "SELECT count(g) > 0 FROM Ugroup g JOIN g.userSet u "
                + "WHERE g.id = :groupId AND u.id = :userId",
                Boolean.class)
                .setParameter("groupId", groupId)
                .setParameter("userId", userId)
                .uniqueResult();
    }

    @Override
    public long countUsersInGroup(Long groupId) {
        Session session = this.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Ugroup> root = cq.from(Ugroup.class);
        Join<Object, Object> users = root.join("userSet", JoinType.INNER);

        cq.select(cb.count(users));
        cq.where(cb.equal(root.get("id"), groupId));

        return session.createQuery(cq).getSingleResult();
    }

    @Override
    public void deleteById(Long id) {
        Session session = this.getSession();
        Ugroup ugroup = session.get(Ugroup.class, id);
        if (ugroup != null) {
            session.remove(ugroup);
        }
    }
    
    @Override
    public boolean isByGroupName(String name){
        Session session = this.getSession();
        String hql = "SELECT count(u.id) FROM Ugroup u WHERE u.groupName = :name";
        Long count = (Long) session.createQuery(hql)
            .setParameter("name", name)
            .uniqueResult();
        return count != null && count > 0;
    }

}
