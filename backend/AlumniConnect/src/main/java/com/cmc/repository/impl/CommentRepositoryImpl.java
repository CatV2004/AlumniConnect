/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.Comment;
import com.cmc.repository.CommentRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author PHAT
 */
@Repository
@Transactional
public class CommentRepositoryImpl implements CommentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Comment> findByPostIdAndActiveTrueOrderByCreatedDateAsc(Long postId, Integer page, Integer size) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "From Comment c Where c.postId.id = :postId AND c.active = TRUE and c.parentId IS NULL ORDER BY c.id DESC";
        Query q = s.createQuery(hql, Comment.class);
        q.setParameter("postId", postId);
        int start = page * size;
        q.setFirstResult(start);
        q.setMaxResults(size);
        return q.getResultList();
    }

    @Override
    public long totalCommentByPost(Long postId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(*) FROM Comment WHERE postId.id = :postId AND active = TRUE AND parentId IS NULL";
        Query query = s.createQuery(hql, Long.class);
        query.setParameter("postId", postId);
        return (long) query.getSingleResult();
    }

    @Override
    public long totalCommentByComment(Long parentId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(*) FROM Comment WHERE parentId = :parentId AND active = TRUE";
        Query query = s.createQuery(hql, Long.class);
        query.setParameter("parentId", parentId);
        return (long) query.getSingleResult();
    }

    @Override
    public List<Comment> getCommentByComment(Long parentId, Integer page, Integer size) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "Comment.findByParentId";
        Query query = s.createNamedQuery(hql, Comment.class);
        query.setParameter("parentId", parentId);
        int start = page * size;
        query.setFirstResult(start);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public List<Comment> findByUserId(Long userId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "From Comment c Where c.userId.id :userId AND c.active = TRUE";
        Query q = s.createQuery(hql, Comment.class);
        q.setParameter("userId", userId);
        return q.getResultList();
    }

    @Override
    public long countByPostId(Long postId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(*) FROM Comment WHERE postId.id = :postId AND active = TRUE";
        Query query = s.createQuery(hql, Long.class);
        query.setParameter("postId", postId);
        return (long) query.getSingleResult();
    }

    @Override
    public Comment saveOrUpdate(Comment c) {
        Session s = this.factory.getObject().getCurrentSession();
        if (c.getId() == null) {
            s.persist(c);
        }

        s.merge(c);
        s.refresh(c);
        return c;
    }

    @Override
    public Comment getCommentById(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.createNamedQuery("Comment.findById", Comment.class).setParameter("id", id).getSingleResult();
    }

    @Override
    public List<Comment> getRepliesByParentId(Long parentId) {
        String hql = "FROM Comment c WHERE c.parentId = :parentId AND c.active = TRUE";
        return this.factory.getObject().getCurrentSession()
                .createQuery(hql, Comment.class)
                .setParameter("parentId", parentId)
                .getResultList();
    }
    
    @Override
    public List<Comment> getUnlabeledComments(){
        Session ss = this.factory.getObject().getCurrentSession();
        String hql = "FROM Comment c WHERE c.label IS NULL";
        return ss.createQuery(hql, Comment.class).getResultList();
    }
    
    @Override
    public Long countByPostIdAndLabel(Long postId, String label){
        Session ss = this.factory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(*) FROM Comment c WHERE c.label = :label AND c.postId = :postId";
        Query query = ss.createQuery(hql, Long.class);
        query.setParameter("postId", postId);
        query.setParameter("label", label);
        return (Long) query.getSingleResultOrNull();
    }
}
