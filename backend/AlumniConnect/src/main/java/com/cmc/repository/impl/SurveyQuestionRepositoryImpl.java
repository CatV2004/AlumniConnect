/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.SurveyQuestion;
import com.cmc.repository.SurveyQuestionRepository;
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
public class SurveyQuestionRepositoryImpl implements SurveyQuestionRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public void saveOrUpdate(SurveyQuestion q) {
        if (q.getId() == null) {
            this.getCurrentSession().persist(q);
        } else {
            this.getCurrentSession().merge(q);
        }
    }

    @Override
    public SurveyQuestion findById(Long id) {
        return this.getCurrentSession().get(SurveyQuestion.class, id);
    }

    @Override
    public List<SurveyQuestion> findBySurveyPostId(Long surveyPostId) {
        String hql = "FROM SurveyQuestion q WHERE q.surveyPostId.id = :postId";
        Query<SurveyQuestion> query = this.getCurrentSession().createQuery(hql, SurveyQuestion.class);
        query.setParameter("postId", surveyPostId);
        return query.getResultList();
    }
}
