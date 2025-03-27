/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author FPTSHOP
 */
@Entity
@Table(name = "survey_draft_answer")
@NamedQueries({
    @NamedQuery(name = "SurveyDraftAnswer.findAll", query = "SELECT s FROM SurveyDraftAnswer s"),
    @NamedQuery(name = "SurveyDraftAnswer.findById", query = "SELECT s FROM SurveyDraftAnswer s WHERE s.id = :id")})
public class SurveyDraftAnswer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "draft_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SurveyDraft draftId;
    @JoinColumn(name = "selected_option_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SurveyOption selectedOptionId;
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SurveyQuestion questionId;

    public SurveyDraftAnswer() {
    }

    public SurveyDraftAnswer(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SurveyDraft getDraftId() {
        return draftId;
    }

    public void setDraftId(SurveyDraft draftId) {
        this.draftId = draftId;
    }

    public SurveyOption getSelectedOptionId() {
        return selectedOptionId;
    }

    public void setSelectedOptionId(SurveyOption selectedOptionId) {
        this.selectedOptionId = selectedOptionId;
    }

    public SurveyQuestion getQuestionId() {
        return questionId;
    }

    public void setQuestionId(SurveyQuestion questionId) {
        this.questionId = questionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SurveyDraftAnswer)) {
            return false;
        }
        SurveyDraftAnswer other = (SurveyDraftAnswer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cmc.pojo.SurveyDraftAnswer[ id=" + id + " ]";
    }
    
}
