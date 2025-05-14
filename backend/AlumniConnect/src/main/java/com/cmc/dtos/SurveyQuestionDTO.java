/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.dtos;

import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public class SurveyQuestionDTO {

    private Long id;
    private String question;
    private Boolean multiChoice;
    private List<SurveyOptionDTO> options;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getMultiChoice() {
        return multiChoice;
    }

    public void setMultiChoice(Boolean multiChoice) {
        this.multiChoice = multiChoice;
    }

    public List<SurveyOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<SurveyOptionDTO> options) {
        this.options = options;
    }

}
