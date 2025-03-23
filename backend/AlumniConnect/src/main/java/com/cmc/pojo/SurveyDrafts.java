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
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author FPTSHOP
 */
@Entity
@Table(name = "survey_drafts")
@NamedQueries({
    @NamedQuery(name = "SurveyDrafts.findAll", query = "SELECT s FROM SurveyDrafts s"),
    @NamedQuery(name = "SurveyDrafts.findById", query = "SELECT s FROM SurveyDrafts s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyDrafts.findByDraftedAt", query = "SELECT s FROM SurveyDrafts s WHERE s.draftedAt = :draftedAt")})
public class SurveyDrafts implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "drafted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date draftedAt;
    @JoinColumn(name = "survey_post_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SurveyPosts surveyPostId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "draftId")
    private Collection<SurveyDraftAnswers> surveyDraftAnswersCollection;

    public SurveyDrafts() {
    }

    public SurveyDrafts(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDraftedAt() {
        return draftedAt;
    }

    public void setDraftedAt(Date draftedAt) {
        this.draftedAt = draftedAt;
    }

    public SurveyPosts getSurveyPostId() {
        return surveyPostId;
    }

    public void setSurveyPostId(SurveyPosts surveyPostId) {
        this.surveyPostId = surveyPostId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public Collection<SurveyDraftAnswers> getSurveyDraftAnswersCollection() {
        return surveyDraftAnswersCollection;
    }

    public void setSurveyDraftAnswersCollection(Collection<SurveyDraftAnswers> surveyDraftAnswersCollection) {
        this.surveyDraftAnswersCollection = surveyDraftAnswersCollection;
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
        if (!(object instanceof SurveyDrafts)) {
            return false;
        }
        SurveyDrafts other = (SurveyDrafts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cmc.pojo.SurveyDrafts[ id=" + id + " ]";
    }
    
}
