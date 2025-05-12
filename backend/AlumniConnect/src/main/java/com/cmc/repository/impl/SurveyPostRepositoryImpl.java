/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.SurveyPost;
import com.cmc.repository.SurveyPostRepository;
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

}
