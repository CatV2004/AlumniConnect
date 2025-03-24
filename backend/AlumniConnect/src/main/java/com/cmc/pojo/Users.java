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
import jakarta.persistence.ManyToMany;
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
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findById", query = "SELECT u FROM Users u WHERE u.id = :id"),
    @NamedQuery(name = "Users.findByUsername", query = "SELECT u FROM Users u WHERE u.username = :username"),
    @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password"),
    @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email"),
    @NamedQuery(name = "Users.findByAvatar", query = "SELECT u FROM Users u WHERE u.avatar = :avatar"),
    @NamedQuery(name = "Users.findByCover", query = "SELECT u FROM Users u WHERE u.cover = :cover"),
    @NamedQuery(name = "Users.findByRole", query = "SELECT u FROM Users u WHERE u.role = :role"),
    @NamedQuery(name = "Users.findByCreatedDate", query = "SELECT u FROM Users u WHERE u.createdDate = :createdDate"),
    @NamedQuery(name = "Users.findByUpdatedDate", query = "SELECT u FROM Users u WHERE u.updatedDate = :updatedDate"),
    @NamedQuery(name = "Users.findByDeletedDate", query = "SELECT u FROM Users u WHERE u.deletedDate = :deletedDate"),
    @NamedQuery(name = "Users.findByActive", query = "SELECT u FROM Users u WHERE u.active = :active")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "password")
    private String password;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 255)
    @Column(name = "avatar")
    private String avatar;
    @Size(max = 500)
    @Column(name = "cover")
    private String cover;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "role")
    private String role;
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
    @ManyToMany(mappedBy = "usersSet")
    private Set<Ugroups> ugroupsSet;
    @ManyToMany(mappedBy = "usersSet")
    private Set<InvitationPosts> invitationPostsSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Set<Comments> commentsSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Set<SurveyDrafts> surveyDraftsSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Set<Posts> postsSet;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "users")
    private Alumni alumni;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Set<UserSurveyOptions> userSurveyOptionsSet;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "users")
    private Teachers teachers;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Set<Reactions> reactionsSet;

    public Users() {
    }

    public Users(Long id) {
        this.id = id;
    }

    public Users(Long id, String username, String password, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public Set<Ugroups> getUgroupsSet() {
        return ugroupsSet;
    }

    public void setUgroupsSet(Set<Ugroups> ugroupsSet) {
        this.ugroupsSet = ugroupsSet;
    }

    public Set<InvitationPosts> getInvitationPostsSet() {
        return invitationPostsSet;
    }

    public void setInvitationPostsSet(Set<InvitationPosts> invitationPostsSet) {
        this.invitationPostsSet = invitationPostsSet;
    }

    public Set<Comments> getCommentsSet() {
        return commentsSet;
    }

    public void setCommentsSet(Set<Comments> commentsSet) {
        this.commentsSet = commentsSet;
    }

    public Set<SurveyDrafts> getSurveyDraftsSet() {
        return surveyDraftsSet;
    }

    public void setSurveyDraftsSet(Set<SurveyDrafts> surveyDraftsSet) {
        this.surveyDraftsSet = surveyDraftsSet;
    }

    public Set<Posts> getPostsSet() {
        return postsSet;
    }

    public void setPostsSet(Set<Posts> postsSet) {
        this.postsSet = postsSet;
    }

    public Alumni getAlumni() {
        return alumni;
    }

    public void setAlumni(Alumni alumni) {
        this.alumni = alumni;
    }

    public Set<UserSurveyOptions> getUserSurveyOptionsSet() {
        return userSurveyOptionsSet;
    }

    public void setUserSurveyOptionsSet(Set<UserSurveyOptions> userSurveyOptionsSet) {
        this.userSurveyOptionsSet = userSurveyOptionsSet;
    }

    public Teachers getTeachers() {
        return teachers;
    }

    public void setTeachers(Teachers teachers) {
        this.teachers = teachers;
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
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cmc.pojo.Users[ id=" + id + " ]";
    }
    
}
