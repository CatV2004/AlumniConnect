/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.dtos;

/**
 *
 * @author FPTSHOP
 */
public class SurveyOptionStatisticDTO {
    private Long questionId;
    private String question;
    private Long optionId;
    private String optionText;
    private Long totalSelected;

    public SurveyOptionStatisticDTO(Long questionId, String question,
                                    Long optionId, String optionText, Long totalSelected) {
        this.questionId = questionId;
        this.question = question;
        this.optionId = optionId;
        this.optionText = optionText;
        this.totalSelected = totalSelected;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Long getTotalSelected() {
        return totalSelected;
    }

    public void setTotalSelected(Long totalSelected) {
        this.totalSelected = totalSelected;
    }
    
    

}

