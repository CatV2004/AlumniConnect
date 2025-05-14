/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.dtos.MultiSelectOptionRequest;
import com.cmc.pojo.SurveyOption;
import com.cmc.pojo.SurveyQuestion;
import com.cmc.pojo.User;
import com.cmc.pojo.UserSurveyOption;
import com.cmc.repository.SurveyOptionRepository;
import com.cmc.repository.SurveyQuestionRepository;
import com.cmc.repository.UserSurveyOptionRepository;
import com.cmc.service.UserSurveyOptionService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author FPTSHOP
 */
@Service
@Transactional
public class UserSurveyOptionServiceImpl implements UserSurveyOptionService {

    @Autowired
    private UserSurveyOptionRepository userSurveyOptionRepository;

    @Autowired
    SurveyQuestionRepository surveyQuestionRepository;

    @Override
    public void selectOptions(User user, SurveyQuestion question, List<Long> optionIds) {
        if (optionIds == null || optionIds.isEmpty()) {
            throw new IllegalArgumentException("Danh sách lựa chọn không được để trống");
        }
        userSurveyOptionRepository.deleteByUserAndQuestion(user, question);

        Set<SurveyOption> selectedOptions = new HashSet<>();
        optionIds.forEach(o -> {
            selectedOptions.add(this.userSurveyOptionRepository.getOptionById(o));
        });

        for (SurveyOption option : selectedOptions) {
            if (!option.getSurveyQuestionId().getId().equals(question.getId())) {
                throw new IllegalArgumentException("Option không thuộc câu hỏi này");
            }
        }

        if (!Boolean.TRUE.equals(question.getMultiChoice()) && selectedOptions.size() > 1) {
            throw new IllegalArgumentException("Câu hỏi này chỉ cho phép chọn một lựa chọn");
        }

        for (SurveyOption option : selectedOptions) {
            UserSurveyOption uso = new UserSurveyOption();
            uso.setUserId(user);
            uso.setSurveyOptionId(option);
            userSurveyOptionRepository.save(uso);
        }
    }

    @Override
    public void selectMultipleOptions(User user, List<MultiSelectOptionRequest.SelectOptionRequest> answers) {
        for (MultiSelectOptionRequest.SelectOptionRequest answer : answers) {
            SurveyQuestion question = surveyQuestionRepository.findById(answer.getQuestionId());

            this.selectOptions(user, question, answer.getOptionIds());
        }
    }

    @Override
    public List<UserSurveyOption> getSelectedOptions(User user, SurveyQuestion question) {
        return userSurveyOptionRepository.findByUserAndQuestion(user, question);
    }

    @Override
    public void unselectOptions(User user, SurveyQuestion question) {
        userSurveyOptionRepository.deleteByUserAndQuestion(user, question);
    }

    @Override
    public boolean hasUserAnsweredSurvey(Long userId, Long surveyPostId) {
        return userSurveyOptionRepository.hasUserAnsweredSurvey(userId, surveyPostId);
    }
}
