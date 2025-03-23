/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
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
@Table(name = "ugroups")
@NamedQueries({
    @NamedQuery(name = "Ugroups.findAll", query = "SELECT u FROM Ugroups u"),
    @NamedQuery(name = "Ugroups.findById", query = "SELECT u FROM Ugroups u WHERE u.id = :id"),
    @NamedQuery(name = "Ugroups.findByGroupName", query = "SELECT u FROM Ugroups u WHERE u.groupName = :groupName"),
    @NamedQuery(name = "Ugroups.findByCreatedDate", query = "SELECT u FROM Ugroups u WHERE u.createdDate = :createdDate"),
    @NamedQuery(name = "Ugroups.findByUpdatedDate", query = "SELECT u FROM Ugroups u WHERE u.updatedDate = :updatedDate"),
    @NamedQuery(name = "Ugroups.findByDeletedDate", query = "SELECT u FROM Ugroups u WHERE u.deletedDate = :deletedDate"),
    @NamedQuery(name = "Ugroups.findByActive", query = "SELECT u FROM Ugroups u WHERE u.active = :active")})
public class Ugroups implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "group_name")
    private String groupName;
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
    @JoinTable(name = "group_users", joinColumns = {
        @JoinColumn(name = "group_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Users> usersCollection;
    @ManyToMany(mappedBy = "ugroupsCollection")
    private Collection<InvitationPosts> invitationPostsCollection;

    public Ugroups() {
    }

    public Ugroups(Long id) {
        this.id = id;
    }

    public Ugroups(Long id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    public Collection<InvitationPosts> getInvitationPostsCollection() {
        return invitationPostsCollection;
    }

    public void setInvitationPostsCollection(Collection<InvitationPosts> invitationPostsCollection) {
        this.invitationPostsCollection = invitationPostsCollection;
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
        if (!(object instanceof Ugroups)) {
            return false;
        }
        Ugroups other = (Ugroups) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cmc.pojo.Ugroups[ id=" + id + " ]";
    }
    
}
