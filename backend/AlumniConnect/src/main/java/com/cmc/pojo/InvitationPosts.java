/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.pojo;

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
@Table(name = "invitation_posts")
@NamedQueries({
    @NamedQuery(name = "InvitationPosts.findAll", query = "SELECT i FROM InvitationPosts i"),
    @NamedQuery(name = "InvitationPosts.findById", query = "SELECT i FROM InvitationPosts i WHERE i.id = :id"),
    @NamedQuery(name = "InvitationPosts.findByEventName", query = "SELECT i FROM InvitationPosts i WHERE i.eventName = :eventName")})
public class InvitationPosts implements Serializable {

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
    @JoinTable(name = "invitation_post_groups", joinColumns = {
        @JoinColumn(name = "invitation_post_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "group_id", referencedColumnName = "id")})
    @ManyToMany
    private Set<Ugroups> ugroupsSet;
    @JoinTable(name = "invitation_post_users", joinColumns = {
        @JoinColumn(name = "invitation_post_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")})
    @ManyToMany
    private Set<Users> usersSet;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Posts posts;

    public InvitationPosts() {
    }

    public InvitationPosts(Long id) {
        this.id = id;
    }

    public InvitationPosts(Long id, String eventName) {
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

    public Set<Ugroups> getUgroupsSet() {
        return ugroupsSet;
    }

    public void setUgroupsSet(Set<Ugroups> ugroupsSet) {
        this.ugroupsSet = ugroupsSet;
    }

    public Set<Users> getUsersSet() {
        return usersSet;
    }

    public void setUsersSet(Set<Users> usersSet) {
        this.usersSet = usersSet;
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
        if (!(object instanceof InvitationPosts)) {
            return false;
        }
        InvitationPosts other = (InvitationPosts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cmc.pojo.InvitationPosts[ id=" + id + " ]";
    }
    
}
