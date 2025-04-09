/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
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
@Table(name = "invitation_post")
@NamedQueries({
    @NamedQuery(name = "InvitationPost.findAll", query = "SELECT i FROM InvitationPost i"),
    @NamedQuery(name = "InvitationPost.findById", query = "SELECT i FROM InvitationPost i WHERE i.id = :id"),
    @NamedQuery(name = "InvitationPost.findByEventName", query = "SELECT i FROM InvitationPost i WHERE i.eventName = :eventName")})
public class InvitationPost implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "event_name")
    private String eventName;
    @JoinTable(name = "invitation_post_group", joinColumns = {
        @JoinColumn(name = "invitation_post_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "group_id", referencedColumnName = "id")})
    @ManyToMany
    @JsonIgnore
    private Set<Ugroup> ugroupSet;
    
    @JoinTable(name = "invitation_post_user", joinColumns = {
        @JoinColumn(name = "invitation_post_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")})
    @ManyToMany
    private Set<User> userSet;
    
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Post post;

    public InvitationPost() {
    }

    public InvitationPost(Long id) {
        this.id = id;
    }

    public InvitationPost(Long id, String eventName) {
        this.id = id;
        this.eventName = eventName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Set<Ugroup> getUgroupSet() {
        return ugroupSet;
    }

    public void setUgroupSet(Set<Ugroup> ugroupSet) {
        this.ugroupSet = ugroupSet;
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
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
        if (!(object instanceof InvitationPost)) {
            return false;
        }
        InvitationPost other = (InvitationPost) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cmc.pojo.InvitationPost[ id=" + id + " ]";
    }
    
}
