/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.dtos.SurveyOptionStatisticDTO;
import com.cmc.dtos.SurveyStatisticsResponseDTO;
import com.cmc.repository.SurveyOptionStatistic;
import java.util.ArrayList;
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
public class SurveyOptionStatisticImpl implements SurveyOptionStatistic {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public SurveyStatisticsResponseDTO getSurveyStatisticsWithParticipants(Long surveyPostId) {
        List<SurveyOptionStatisticDTO> statistics = new ArrayList<>();
        Long participantCount = 0L;

        try {
            Session session = getSession();

            String hqlStats = """
            SELECT new com.cmc.dtos.SurveyOptionStatisticDTO(
                q.id, q.question, o.id, o.optionText, COUNT(u.id)
            )
            FROM UserSurveyOption u
            JOIN u.surveyOptionId o
            JOIN o.surveyQuestionId q
            JOIN q.surveyPostId p
            WHERE p.id = :surveyPostId
            GROUP BY q.id, q.question, o.id, o.optionText
            ORDER BY q.id, o.id
        """;
            Query<SurveyOptionStatisticDTO> statsQuery = session.createQuery(hqlStats, SurveyOptionStatisticDTO.class);
            statsQuery.setParameter("surveyPostId", surveyPostId);
            statistics = statsQuery.getResultList();

            String hqlParticipant = """
            SELECT COUNT(DISTINCT u.userId.id)
            FROM UserSurveyOption u
            JOIN u.surveyOptionId o
            JOIN o.surveyQuestionId q
            JOIN q.surveyPostId p
            WHERE p.id = :surveyPostId
        """;
            Query<Long> countQuery = session.createQuery(hqlParticipant, Long.class);
            countQuery.setParameter("surveyPostId", surveyPostId);
            participantCount = countQuery.uniqueResult();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new SurveyStatisticsResponseDTO(statistics, participantCount);
    }

}
