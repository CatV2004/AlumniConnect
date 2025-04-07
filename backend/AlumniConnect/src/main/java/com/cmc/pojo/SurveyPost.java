/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 *
 * @author FPTSHOP
 */
@Entity
@Table(name = "survey_post")
@NamedQueries({
    @NamedQuery(name = "SurveyPost.findAll", query = "SELECT s FROM SurveyPost s"),
    @NamedQuery(name = "SurveyPost.findById", query = "SELECT s FROM SurveyPost s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyPost.findByEndTime", query = "SELECT s FROM SurveyPost s WHERE s.endTime = :endTime"),
    @NamedQuery(name = "SurveyPost.findBySurveyType", query = "SELECT s FROM SurveyPost s WHERE s.surveyType = :surveyType")})
public class SurveyPost implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endTime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 23)
    @Column(name = "survey_type")
    private String surveyType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "surveyPostId")
    private Set<SurveyQuestion> surveyQuestionSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "surveyPostId")
    private Set<SurveyDraft> surveyDraftSet;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Post post;

    public SurveyPost() {
    }

    public SurveyPost(Long id) {
        this.id = id;
    }

    public SurveyPost(Long id, LocalDateTime endTime, String surveyType) {
        this.id = id;
        this.endTime = endTime;
        this.surveyType = surveyType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }

    public Set<SurveyQuestion> getSurveyQuestionSet() {
        return surveyQuestionSet;
    }

    public void setSurveyQuestionSet(Set<SurveyQuestion> surveyQuestionSet) {
        this.surveyQuestionSet = surveyQuestionSet;
    }

    public Set<SurveyDraft> getSurveyDraftSet() {
        return surveyDraftSet;
    }

    public void setSurveyDraftSet(Set<SurveyDraft> surveyDraftSet) {
        this.surveyDraftSet = surveyDraftSet;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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
        if (!(object instanceof SurveyPost)) {
            return false;
        }
        SurveyPost other = (SurveyPost) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cmc.pojo.SurveyPost[ id=" + id + " ]";
    }
    
}
