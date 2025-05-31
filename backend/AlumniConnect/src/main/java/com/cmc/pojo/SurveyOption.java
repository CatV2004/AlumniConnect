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
@Table(name = "survey_option")
public class SurveyOption implements Serializable {

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
    @Column(name = "option_text")
    private String optionText;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "selectedOptionId")
    private Set<SurveyDraftAnswer> surveyDraftAnswerSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "surveyOptionId")
    private Set<UserSurveyOption> userSurveyOptionSet;
    @JoinColumn(name = "survey_question_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SurveyQuestion surveyQuestionId;

    public SurveyOption() {
    }

    public SurveyOption(Long id) {
        this.id = id;
    }

    public SurveyOption(Long id, String optionText) {
        this.id = id;
        this.optionText = optionText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Set<SurveyDraftAnswer> getSurveyDraftAnswerSet() {
        return surveyDraftAnswerSet;
    }

    public void setSurveyDraftAnswerSet(Set<SurveyDraftAnswer> surveyDraftAnswerSet) {
        this.surveyDraftAnswerSet = surveyDraftAnswerSet;
    }

    public Set<UserSurveyOption> getUserSurveyOptionSet() {
        return userSurveyOptionSet;
    }

    public void setUserSurveyOptionSet(Set<UserSurveyOption> userSurveyOptionSet) {
        this.userSurveyOptionSet = userSurveyOptionSet;
    }

    public SurveyQuestion getSurveyQuestionId() {
        return surveyQuestionId;
    }

    public void setSurveyQuestionId(SurveyQuestion surveyQuestionId) {
        this.surveyQuestionId = surveyQuestionId;
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
        if (!(object instanceof SurveyOption)) {
            return false;
        }
        SurveyOption other = (SurveyOption) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SurveyOption{"
                + "id=" + id
                + ", optionText='" + optionText + '\''
                + '}';
    }

}
