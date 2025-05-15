/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.MultiSelectOptionRequest;
import com.cmc.pojo.SurveyQuestion;
import com.cmc.pojo.User;
import com.cmc.pojo.UserSurveyOption;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public interface UserSurveyOptionService {

    void selectOptions(User user, SurveyQuestion question, List<Long> optionIds);

    List<UserSurveyOption> getSelectedOptions(User user, SurveyQuestion question);

    void unselectOptions(User user, SurveyQuestion question);

    void selectMultipleOptions(User user, List<MultiSelectOptionRequest.SelectOptionRequest> answers);
    
    boolean hasUserAnsweredSurvey(Long userId, Long surveyPostId);
}
