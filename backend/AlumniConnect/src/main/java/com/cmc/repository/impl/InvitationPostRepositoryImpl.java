///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.cmc.repository.impl;
//
//import com.cmc.pojo.InvitationPost;
//import com.cmc.repository.InvitationPostRepository;
//import java.util.List;
//import org.hibernate.Session;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// *
// * @author FPTSHOP
// */
//@Repository
//@Transactional
//public class InvitationPostRepositoryImpl implements InvitationPostRepository {
//
//    @Autowired
//    private LocalSessionFactoryBean factory;
//
//    private Session getSession() {
//        return factory.getObject().getCurrentSession();
//    }
//
//    @Override
//    public InvitationPost findById(Long id) {
//        return this.getSession().get(InvitationPost.class, id);
//    }
//
//    @Override
//    public List<InvitationPost> findAll() {
//        return this.getSession().createQuery("FROM InvitationPost", InvitationPost.class).list();
//    }
//
//    @Override
//    public void save(InvitationPost post) {
//        Transaction tx = null;
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            tx = session.beginTransaction();
//            session.save(post);
//            tx.commit();
//        } catch (Exception e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            throw e;
//        }
//    }
//
//    @Override
//    public void update(InvitationPost post) {
//        Transaction tx = null;
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            tx = session.beginTransaction();
//            session.update(post);
//            tx.commit();
//        } catch (Exception e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            throw e;
//        }
//    }
//
//    @Override
//    public void delete(Long id) {
//        Transaction tx = null;
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            tx = session.beginTransaction();
//            InvitationPost post = session.get(InvitationPost.class, id);
//            if (post != null) {
//                session.delete(post);
//            }
//            tx.commit();
//        } catch (Exception e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            throw e;
//        }
//    }
//
//    @Override
//    public List<InvitationPost> findByGroupId(Long groupId) {
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            String hql = "SELECT ip FROM InvitationPost ip JOIN ip.groups g WHERE g.id = :groupId";
//            return session.createQuery(hql, InvitationPost.class)
//                    .setParameter("groupId", groupId)
//                    .list();
//        }
//    }
//
//    @Override
//    public List<InvitationPost> findByUserId(Long userId) {
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            String hql = "SELECT ip FROM InvitationPost ip JOIN ip.users u WHERE u.id = :userId";
//            return session.createQuery(hql, InvitationPost.class)
//                    .setParameter("userId", userId)
//                    .list();
//        }
//    }
//}
