/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public class SurveyDTO {
    private Long id;
    @NotBlank(message = "Không thể bỏ trống nội dung khảo sát!!!")
    private String content;
    private UserDTO userId;
    private String surveyType;
    @Future(message = "Thời gian kết thúc phải nằm trong tương lai")
    private LocalDateTime endTime;
    private List<SurveyQuestionDTO> questions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<SurveyQuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<SurveyQuestionDTO> questions) {
        this.questions = questions;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserDTO getUserId() {
        return userId;
    }

    public void setUserId(UserDTO userId) {
        this.userId = userId;
    }
    
    
    
}

