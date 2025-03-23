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
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author FPTSHOP
 */
@Entity
@Table(name = "survey_posts")
@NamedQueries({
    @NamedQuery(name = "SurveyPosts.findAll", query = "SELECT s FROM SurveyPosts s"),
    @NamedQuery(name = "SurveyPosts.findById", query = "SELECT s FROM SurveyPosts s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyPosts.findByEndTime", query = "SELECT s FROM SurveyPosts s WHERE s.endTime = :endTime"),
    @NamedQuery(name = "SurveyPosts.findBySurveyType", query = "SELECT s FROM SurveyPosts s WHERE s.surveyType = :surveyType")})
public class SurveyPosts implements Serializable {

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
    private Date endTime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 23)
    @Column(name = "survey_type")
    private String surveyType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "surveyPostId")
    private Collection<SurveyDrafts> surveyDraftsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "surveyPostId")
    private Collection<SurveyQuestions> surveyQuestionsCollection;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Posts posts;

    public SurveyPosts() {
    }

    public SurveyPosts(Long id) {
        this.id = id;
    }

    public SurveyPosts(Long id, Date endTime, String surveyType) {
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

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }

    public Collection<SurveyDrafts> getSurveyDraftsCollection() {
        return surveyDraftsCollection;
    }

    public void setSurveyDraftsCollection(Collection<SurveyDrafts> surveyDraftsCollection) {
        this.surveyDraftsCollection = surveyDraftsCollection;
    }

    public Collection<SurveyQuestions> getSurveyQuestionsCollection() {
        return surveyQuestionsCollection;
    }

    public void setSurveyQuestionsCollection(Collection<SurveyQuestions> surveyQuestionsCollection) {
        this.surveyQuestionsCollection = surveyQuestionsCollection;
    }

    public Posts getPosts() {
        return posts;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
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
        if (!(object instanceof SurveyPosts)) {
            return false;
        }
        SurveyPosts other = (SurveyPosts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cmc.pojo.SurveyPosts[ id=" + id + " ]";
    }
    
}
