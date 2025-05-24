/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.Post;
import com.cmc.pojo.User;
import com.cmc.pojo.Reaction;
import com.cmc.repository.ReactionRepository;
import com.cmc.service.PostService;
import com.cmc.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author PHAT
 */
@Repository
@Transactional
public class ReactionRepositoryImpl implements ReactionRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public Reaction findById(Long id) {
        return getSession().get(Reaction.class, id);
    }

    @Override
    public List<Reaction> findByPostId(Long postId) {
        String sql = "From Reaction r WHERE r.postId.id = :postId Order by -id";
        Query q = getSession().createQuery(sql, Reaction.class);
        q.setParameter("postId", postId);
        return q.getResultList();
    }

//    @Override
//    public Long countLikesByPostId(Long postId) {
//        String hql = "SELECT COUNT(r.id) FROM Reaction r WHERE r.postId.id = :postId";
//        Query<Long> query = getSession().createQuery(hql, Long.class);
//        query.setParameter("postId", postId);
//        return query.uniqueResult();
//    }
//    
    @Override
    public Map<String, Long> countReactionsByPostId(Long postId) {
        String hql = "SELECT r.reaction, COUNT(r.id) FROM Reaction r "
                + "WHERE r.postId.id = :postId GROUP BY r.reaction";
        Query<Object[]> query = getSession().createQuery(hql, Object[].class);
        query.setParameter("postId", postId);

        List<Object[]> results = query.getResultList();
        Map<String, Long> reactionCounts = new HashMap<>();

        for (Object[] row : results) {
            String type = (String) row[0];
            Long count = (Long) row[1];
            reactionCounts.put(type, count);
        }

        return reactionCounts;
    }

    @Override
    public boolean hasUserLikedPost(Long postId, Long userId) {
        String hql = "SELECT COUNT(r.id) FROM Reaction r WHERE r.postId.id = :postId AND r.userId.id = :userId";
        Query<Long> query = getSession().createQuery(hql, Long.class);
        query.setParameter("postId", postId);
        query.setParameter("userId", userId);
        Long count = query.uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public List<Reaction> findByReactionType(String reactionType, Long postId) {
        String sql = "From Reaction r WHERE r.reaction = :reactionType AND r.postId.id = :postId Order by -id";
        Query q = getSession().createQuery(sql, Reaction.class);
        q.setParameter("reactionType", reactionType);
        q.setParameter("postId", postId);
        return q.getResultList();
    }

    @Override
    public Reaction findByPostIdAndUserId(Long postId, Long userId) {
        String sql = "From Reaction r WHERE r.postId.id = :postId AND r.userId.id = :userId AND r.postId.active = true";
        Query q = getSession().createQuery(sql, Reaction.class);
        q.setParameter("postId", postId);
        q.setParameter("userId", userId);
        List<Reaction> results = q.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public Reaction saveOrUpdate(Reaction reaction) {
        if (reaction.getId() == null) {
            getSession().persist(reaction);
        } else {
            getSession().merge(reaction);
        }
        return reaction;
    }

    @Override
    public void deleteReaction(Reaction reaction) {
        this.getSession().remove(reaction);
    }
}
