/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.SurveyOption;
import com.cmc.repository.SurveyOptionRepository;
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
public class SurveyOptionRepositoryImpl implements SurveyOptionRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public void saveOrUpdate(SurveyOption o) {
        if (o.getId() == null) {
            this.getCurrentSession().persist(o);
        } else {
            this.getCurrentSession().merge(o);
        }
    }

    @Override
    public SurveyOption findById(Long id) {
        return this.getCurrentSession().get(SurveyOption.class, id);
    }

    @Override
    public List<SurveyOption> findByQuestionId(Long questionId) {
        String hql = "FROM SurveyOption o WHERE o.surveyQuestionId.id = :questionId";
        Query<SurveyOption> query = this.getCurrentSession().createQuery(hql, SurveyOption.class);
        query.setParameter("questionId", questionId);
        return query.getResultList();
    }
}
