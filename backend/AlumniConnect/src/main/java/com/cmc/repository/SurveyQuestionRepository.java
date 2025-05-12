/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.pojo.SurveyQuestion;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public interface SurveyQuestionRepository {

    void saveOrUpdate(SurveyQuestion q);

    SurveyQuestion findById(Long id);

    List<SurveyQuestion> findBySurveyPostId(Long surveyPostId);

}
