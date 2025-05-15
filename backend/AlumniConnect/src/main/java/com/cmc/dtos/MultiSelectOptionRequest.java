/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.dtos;

import java.util.List;
import com.cmc.dtos.SelectOptionRequest;

/**
 *
 * @author FPTSHOP
 */
public class MultiSelectOptionRequest {

    private List<SelectOptionRequest> answers;

    public List<SelectOptionRequest> getAnswers() {
        return answers;
    }

    public void setAnswers(List<SelectOptionRequest> answers) {
        this.answers = answers;
    }

    public static class SelectOptionRequest {
        private Long questionId;
        private List<Long> optionIds;

        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public List<Long> getOptionIds() {
            return optionIds;
        }

        public void setOptionIds(List<Long> optionIds) {
            this.optionIds = optionIds;
        }
    }
}

