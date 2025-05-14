/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.pojo.SurveyOption;
import com.cmc.pojo.SurveyQuestion;
import com.cmc.pojo.User;
import com.cmc.pojo.UserSurveyOption;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public interface UserSurveyOptionRepository {

    UserSurveyOption save(UserSurveyOption userSurveyOption);

    List<UserSurveyOption> findByUserAndSurveyOption(User user, SurveyOption surveyOption);

    List<UserSurveyOption> findByUserAndQuestion(User user, SurveyQuestion question);

    void deleteByUserAndQuestion(User user, SurveyQuestion question);
    
    SurveyOption getOptionById(Long id);
    
    boolean hasUserAnsweredSurvey(Long userId, Long surveyPostId);
}
