/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author FPTSHOP
 */
@Entity
@Table(name = "survey_question")
public class SurveyQuestion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "question")
    private String question;
    @Column(name = "multi_choice")
    private Boolean multiChoice;
    @JoinColumn(name = "survey_post_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SurveyPost surveyPostId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionId")
    private Set<SurveyDraftAnswer> surveyDraftAnswerSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "surveyQuestionId")
    private Set<SurveyOption> surveyOptionSet;

    public SurveyQuestion() {
    }

    public SurveyQuestion(Long id) {
        this.id = id;
    }

    public SurveyQuestion(Long id, String question) {
        this.id = id;
        this.question = question;
    }

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

    public SurveyPost getSurveyPostId() {
        return surveyPostId;
    }

    public void setSurveyPostId(SurveyPost surveyPostId) {
        this.surveyPostId = surveyPostId;
    }

    public Set<SurveyDraftAnswer> getSurveyDraftAnswerSet() {
        return surveyDraftAnswerSet;
    }

    public void setSurveyDraftAnswerSet(Set<SurveyDraftAnswer> surveyDraftAnswerSet) {
        this.surveyDraftAnswerSet = surveyDraftAnswerSet;
    }

    public Set<SurveyOption> getSurveyOptionSet() {
        return surveyOptionSet;
    }

    public void setSurveyOptionSet(Set<SurveyOption> surveyOptionSet) {
        this.surveyOptionSet = surveyOptionSet;
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
        if (!(object instanceof SurveyQuestion)) {
            return false;
        }
        SurveyQuestion other = (SurveyQuestion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SurveyQuestion{"
                + "id=" + id
                + ", question='" + question + '\''
                + ", multiChoice=" + multiChoice
                + ", surveyOptions=" + (surveyOptionSet != null
                        ? surveyOptionSet.stream().map(SurveyOption::toString).toList() : "[]")
                + '}';
    }

}
