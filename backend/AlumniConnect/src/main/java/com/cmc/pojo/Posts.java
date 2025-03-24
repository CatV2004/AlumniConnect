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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author FPTSHOP
 */
@Entity
@Table(name = "posts")
@NamedQueries({
    @NamedQuery(name = "Posts.findAll", query = "SELECT p FROM Posts p"),
    @NamedQuery(name = "Posts.findById", query = "SELECT p FROM Posts p WHERE p.id = :id"),
    @NamedQuery(name = "Posts.findByLockComment", query = "SELECT p FROM Posts p WHERE p.lockComment = :lockComment"),
    @NamedQuery(name = "Posts.findByCreatedDate", query = "SELECT p FROM Posts p WHERE p.createdDate = :createdDate"),
    @NamedQuery(name = "Posts.findByUpdatedDate", query = "SELECT p FROM Posts p WHERE p.updatedDate = :updatedDate"),
    @NamedQuery(name = "Posts.findByDeletedDate", query = "SELECT p FROM Posts p WHERE p.deletedDate = :deletedDate"),
    @NamedQuery(name = "Posts.findByActive", query = "SELECT p FROM Posts p WHERE p.active = :active")})
public class Posts implements Serializable {

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
    @Column(name = "content")
    private String content;
    @Column(name = "lock_comment")
    private Boolean lockComment;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Column(name = "deleted_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedDate;
    @Column(name = "active")
    private Boolean active;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postId")
    private Set<PostImages> postImagesSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postId")
    private Set<Comments> commentsSet;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users userId;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "posts")
    private InvitationPosts invitationPosts;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "posts")
    private SurveyPosts surveyPosts;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postId")
    private Set<Reactions> reactionsSet;

    public Posts() {
    }

    public Posts(Long id) {
        this.id = id;
    }

    public Posts(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getLockComment() {
        return lockComment;
    }

    public void setLockComment(Boolean lockComment) {
        this.lockComment = lockComment;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<PostImages> getPostImagesSet() {
        return postImagesSet;
    }

    public void setPostImagesSet(Set<PostImages> postImagesSet) {
        this.postImagesSet = postImagesSet;
    }

    public Set<Comments> getCommentsSet() {
        return commentsSet;
    }

    public void setCommentsSet(Set<Comments> commentsSet) {
        this.commentsSet = commentsSet;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public InvitationPosts getInvitationPosts() {
        return invitationPosts;
    }

    public void setInvitationPosts(InvitationPosts invitationPosts) {
        this.invitationPosts = invitationPosts;
    }

    public SurveyPosts getSurveyPosts() {
        return surveyPosts;
    }

    public void setSurveyPosts(SurveyPosts surveyPosts) {
        this.surveyPosts = surveyPosts;
    }

    public Set<Reactions> getReactionsSet() {
        return reactionsSet;
    }

    public void setReactionsSet(Set<Reactions> reactionsSet) {
        this.reactionsSet = reactionsSet;
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
        if (!(object instanceof Posts)) {
            return false;
        }
        Posts other = (Posts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cmc.pojo.Posts[ id=" + id + " ]";
    }
    
}
