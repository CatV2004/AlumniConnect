/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.SurveyOption;
import com.cmc.pojo.SurveyQuestion;
import com.cmc.pojo.User;
import com.cmc.pojo.UserSurveyOption;
import com.cmc.repository.UserSurveyOptionRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
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
public class UserSurveyOptionRepositoryImpl implements UserSurveyOptionRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public UserSurveyOption save(UserSurveyOption userSurveyOption) {
        if (userSurveyOption.getId() == null) {
            this.getSession().persist(userSurveyOption);
            return userSurveyOption;
        } else {
            return this.getSession().merge(userSurveyOption);
        }
    }

    @Override
    public List<UserSurveyOption> findByUserAndSurveyOption(User user, SurveyOption surveyOption) {
        String hql = "FROM UserSurveyOption uso WHERE uso.userId = :user AND uso.surveyOptionId = :option";
        return getSession()
                .createQuery(hql, UserSurveyOption.class)
                .setParameter("user", user)
                .setParameter("option", surveyOption)
                .getResultList();
    }

    @Override
    public List<UserSurveyOption> findByUserAndQuestion(User user, SurveyQuestion question) {
        String hql = "FROM UserSurveyOption uso "
                + "WHERE uso.userId = :user "
                + "AND uso.surveyOptionId.surveyQuestionId = :question";

        return getSession()
                .createQuery(hql, UserSurveyOption.class)
                .setParameter("user", user)
                .setParameter("question", question)
                .getResultList();
    }

    @Override
    public void deleteByUserAndQuestion(User user, SurveyQuestion question) {
        String hql = """
        delete from UserSurveyOption uso
        where uso.userId  = :user
          and uso.surveyOptionId.id in (
              select so.id from SurveyOption so
              where so.surveyQuestionId = :question
          )
    """;

        Query query = getSession().createQuery(hql);
        query.setParameter("user", user);
        query.setParameter("question", question);
        query.executeUpdate();
    }

    @Override
    public SurveyOption getOptionById(Long id) {
        return this.getSession().get(SurveyOption.class, id);
    }

    public boolean hasUserAnsweredSurvey(Long userId, Long surveyPostId) {
        String sql = """
        SELECT EXISTS (
            SELECT 1
            FROM user_survey_option uo
            JOIN survey_option so ON uo.survey_option_id = so.id
            JOIN survey_question sq ON so.survey_question_id = sq.id
            WHERE uo.user_id = :userId
              AND sq.survey_post_id = :surveyPostId
        )
    """;

        Object result = this.getSession()
                .createNativeQuery(sql)
                .setParameter("userId", userId)
                .setParameter("surveyPostId", surveyPostId)
                .getSingleResult();

        // Ép kiểu đúng và chuyển sang boolean
        return ((Number) result).intValue() == 1;
    }

}
