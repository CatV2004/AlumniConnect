/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.Post;
import com.cmc.repository.PostRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

/**
 *
 * @author FPTSHOP
 */
@Repository
public class PostRepositoryImpl implements PostRepository{

    private static final int SIZE = 10;
    
    @Autowired
    private LocalSessionFactoryBean Factory;
    
    private Session getSession(){
        return Factory.getObject().getCurrentSession();
    }
    
    @Override
    @Transactional
    public List<Post> getPostByUserId(Long id) {
        List<Post> p = new ArrayList<>();
        String sql = "SELECT * FROM Post p" +
            "JOIN Users u ON p.userId = u.id" +
            "LEFT JOIN PostImages pi ON p.id = pi.postId" +
            "WHERE p.userId = :userId"
                + "ORDER BY p.id DESC";
        Query<Post> q = getSession().createQuery(sql, Post.class);
        q.setParameter("userId", id);
        p = q.getResultList();
        return p;
    }

    @Override
    @Transactional
    public List<Post> getPosts(String kw, Pageable pageable) {
        String hql = "FROM Post p WHERE p.content LIKE :kw ORDER BY p.id DESC";
        Query<Post> query = getSession().createQuery(hql, Post.class);
        query.setParameter("kw", "%" + kw + "%");

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    @Override
    @Transactional
    public int addPost(Post post) {
        try{
            getSession().persist(post); 
        }catch(Exception ex){
            return -1;
        }
        return 1;
    }

    @Override
    @Transactional
    public int deletePost(Long id) {
        Query q = getSession().createQuery("UPDATE Post p SET p.active = 0 WHERE p.id = :id", Post.class);
        q.setParameter("id", id);
        return q.executeUpdate();
    }

    @Override
    @Transactional
    public int updateContent(Long id, String content) {
        Query q = getSession().createNamedQuery("UPDATE Post p SET p.content = :content WHERE p.id = :id", Post.class);
        q.setParameter("id", id);
        q.setParameter("content", content);
        return q.executeUpdate();
    }

    @Override
    public Iterable<Post> findAll(Sort sort) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    @Transactional
    public List<Post> getPostId(Long id) {
        List<Post> ps = new ArrayList<>();
        String sql = "SELECT p FROM Post p WHERE p.active = true, p.id = :id";
        Query<Post> q = getSession().createQuery(sql, Post.class);
        q.setParameter("id", id);
        ps = q.getResultList();
        return ps;
    }
    
    @Override
    public long countTotalPosts(String kw) {
        String hql = "SELECT COUNT(p) FROM Post p WHERE p.content LIKE :kw";
        Query<Long> query = getSession().createQuery(hql, Long.class);
        query.setParameter("kw", "%" + kw + "%");
        return query.getSingleResult();
    }
    
}
