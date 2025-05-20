/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.Ugroup;
import com.cmc.pojo.User;
import com.cmc.repository.UgroupRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

/**
 *
 * @author FPTSHOP
 */
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
            session.persist(ugroup);
        } else {
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
    public List<Ugroup> findAllActiveGroups() {
        Session session = this.getSession();
        Query<Ugroup> query = session.createQuery(
                "FROM Ugroup ug WHERE ug.active = true",
                Ugroup.class);
        return query.list();
    }

    @Override
    public List<Ugroup> findAll() {
        Session session = this.getSession();
        Query<Ugroup> query = session.createQuery("FROM Ugroup", Ugroup.class);
        return query.getResultList();
    }

    @Override
    public void addUserToGroup(Long groupId, Long userId) {
        Session session = getSession();
        Ugroup group = session.get(Ugroup.class, groupId);
        User user = session.get(User.class, userId);

        if (group != null && user != null) {
            group.getUserSet().add(user);
            user.getUgroupSet().add(group);
            session.saveOrUpdate(group);
            session.saveOrUpdate(user);
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
            session.saveOrUpdate(group);
            session.saveOrUpdate(user);
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
                "SELECT count(g) > 0 FROM Ugroup g JOIN g.userSet u " +
                "WHERE g.id = :groupId AND u.id = :userId", 
                Boolean.class)
                .setParameter("groupId", groupId)
                .setParameter("userId", userId)
                .uniqueResult();
    }

}
