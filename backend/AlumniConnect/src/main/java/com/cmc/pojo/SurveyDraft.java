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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 *
 * @author FPTSHOP
 */
@Entity
@Table(name = "survey_draft")
@NamedQueries({
    @NamedQuery(name = "SurveyDraft.findAll", query = "SELECT s FROM SurveyDraft s"),
    @NamedQuery(name = "SurveyDraft.findById", query = "SELECT s FROM SurveyDraft s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyDraft.findByDraftedAt", query = "SELECT s FROM SurveyDraft s WHERE s.draftedAt = :draftedAt")})
public class SurveyDraft implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "drafted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime draftedAt;
    @JoinColumn(name = "survey_post_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SurveyPost surveyPostId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "draftId")
    private Set<SurveyDraftAnswer> surveyDraftAnswerSet;

    public SurveyDraft() {
    }

    public SurveyDraft(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDraftedAt() {
        return draftedAt;
    }

    public void setDraftedAt(LocalDateTime draftedAt) {
        this.draftedAt = draftedAt;
    }

    public SurveyPost getSurveyPostId() {
        return surveyPostId;
    }

    public void setSurveyPostId(SurveyPost surveyPostId) {
        this.surveyPostId = surveyPostId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Set<SurveyDraftAnswer> getSurveyDraftAnswerSet() {
        return surveyDraftAnswerSet;
    }

    public void setSurveyDraftAnswerSet(Set<SurveyDraftAnswer> surveyDraftAnswerSet) {
        this.surveyDraftAnswerSet = surveyDraftAnswerSet;
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
        if (!(object instanceof SurveyDraft)) {
            return false;
        }
        SurveyDraft other = (SurveyDraft) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cmc.pojo.SurveyDraft[ id=" + id + " ]";
    }
    
}
