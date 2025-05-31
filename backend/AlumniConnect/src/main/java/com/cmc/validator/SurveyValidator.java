/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.validator;

import com.cmc.dtos.SurveyDTO;
import com.cmc.dtos.SurveyOptionDTO;
import com.cmc.dtos.SurveyQuestionDTO;
import com.cmc.repository.SurveyPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author PHAT
 */
@Component
public class SurveyValidator implements Validator{
    
    @Autowired
    private SurveyPostRepository surveyRepo;

    @Override
    public boolean supports(Class<?> clazz) {
        return SurveyDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SurveyDTO s = (SurveyDTO) target;
        
        if(s.getContent() != null && this.surveyRepo.exitSurveyContent(s.getContent())){
            errors.rejectValue("content", "content", "Nội dung khảo sát bị trùng !!!");
        }
        
        if (s.getQuestions()!= null && s.getQuestions().size() <= 0){
            errors.rejectValue("question", "question", "Khảo sát phải có trên 1 câu hỏi!!!");
        }
        
        if (s.getQuestions() != null){
            for (SurveyQuestionDTO question : s.getQuestions()){
                if(question.getOptions().size() < 2){
                    errors.rejectValue("option", "option", "Khảo sát phải có từ 2 lựa chọn!!!");
                }
            }
            
        }
    }
    
}
