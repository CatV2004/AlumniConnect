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
@Table(name = "survey_draft_answers")
@NamedQueries({
    @NamedQuery(name = "SurveyDraftAnswers.findAll", query = "SELECT s FROM SurveyDraftAnswers s"),
    @NamedQuery(name = "SurveyDraftAnswers.findById", query = "SELECT s FROM SurveyDraftAnswers s WHERE s.id = :id")})
public class SurveyDraftAnswers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "draft_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SurveyDrafts draftId;
    @JoinColumn(name = "selected_option_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SurveyOptions selectedOptionId;
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SurveyQuestions questionId;

    public SurveyDraftAnswers() {
    }

    public SurveyDraftAnswers(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SurveyDrafts getDraftId() {
        return draftId;
    }

    public void setDraftId(SurveyDrafts draftId) {
        this.draftId = draftId;
    }

    public SurveyOptions getSelectedOptionId() {
        return selectedOptionId;
    }

    public void setSelectedOptionId(SurveyOptions selectedOptionId) {
        this.selectedOptionId = selectedOptionId;
    }

    public SurveyQuestions getQuestionId() {
        return questionId;
    }

    public void setQuestionId(SurveyQuestions questionId) {
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
        if (!(object instanceof SurveyDraftAnswers)) {
            return false;
        }
        SurveyDraftAnswers other = (SurveyDraftAnswers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cmc.pojo.SurveyDraftAnswers[ id=" + id + " ]";
    }
    
}
