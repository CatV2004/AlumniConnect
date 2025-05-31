/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.Post;
import com.cmc.pojo.SurveyPost;
import com.cmc.pojo.SurveyStatus;
import com.cmc.repository.SurveyPostRepository;
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
public class SurveyPostRepositoryImpl implements SurveyPostRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public void saveOrUpdate(SurveyPost s) {
        if (s.getId() == null) {
            this.getCurrentSession().persist(s);
        } else {
            this.getCurrentSession().merge(s);
        }
    }

    @Override
    public SurveyPost findById(Long id) {
        return this.getCurrentSession().get(SurveyPost.class, id);
    }

    @Override
    public List<SurveyPost> findAll() {
        String hql = "FROM SurveyPost s WHERE s.post.deletedDate IS NULL AND s.post.active = true";
        Query<SurveyPost> query = this.getCurrentSession().createQuery(hql, SurveyPost.class);
        return query.getResultList();
    }

    @Override
    public boolean exitSurveyContent(String content) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "SELECT count(u.id) FROM Post u WHERE u.content = :content";
        Long count = (Long) session.createQuery(hql)
                .setParameter("content", content)
                .uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public void updateExpiredStatus() {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "UPDATE SurveyPost s SET s.status = :expiredStatus "
                + "WHERE s.endTime < :now AND s.status = :activeStatus";
        session.createQuery(hql)
                .setParameter("expiredStatus", "EXPIRED")
                .setParameter("activeStatus", "ACTIVE")
                .setParameter("now", LocalDateTime.now())
                .executeUpdate();
    }

    @Override
    public List<Post> findExpiredSurveyPosts(Map<String, Object> params) {
        Session session = this.factory.getObject().getCurrentSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> root = cq.from(Post.class);

        root.fetch("surveyPost", JoinType.LEFT);
        root.fetch("postImageSet", JoinType.LEFT);
        root.fetch("invitationPost", JoinType.LEFT);

        Join<Post, SurveyPost> surveyPostJoin = root.join("surveyPost", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.isNull(root.get("deletedDate")));
        predicates.add(cb.isTrue(root.get("active")));

        predicates.add(cb.equal(surveyPostJoin.get("status"), SurveyStatus.EXPIRED));

        if (params.containsKey("kw")) {
            String kw = params.get("kw").toString();
            if (!kw.isBlank()) {
                predicates.add(cb.like(root.get("content"), "%" + kw.trim() + "%"));
            }
        }

        cq.select(root).distinct(true)
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .orderBy(cb.desc(root.get("createdDate")));

        Query<Post> query = session.createQuery(cq);

        int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;

        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

}
