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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author FPTSHOP
 */
@Entity
@Table(name = "ugroup")
@NamedQueries({
    @NamedQuery(name = "Ugroup.findAll", query = "SELECT u FROM Ugroup u"),
    @NamedQuery(name = "Ugroup.findById", query = "SELECT u FROM Ugroup u WHERE u.id = :id"),
    @NamedQuery(name = "Ugroup.findByGroupName", query = "SELECT u FROM Ugroup u WHERE u.groupName = :groupName"),
    @NamedQuery(name = "Ugroup.findByCreatedDate", query = "SELECT u FROM Ugroup u WHERE u.createdDate = :createdDate"),
    @NamedQuery(name = "Ugroup.findByUpdatedDate", query = "SELECT u FROM Ugroup u WHERE u.updatedDate = :updatedDate"),
    @NamedQuery(name = "Ugroup.findByDeletedDate", query = "SELECT u FROM Ugroup u WHERE u.deletedDate = :deletedDate"),
    @NamedQuery(name = "Ugroup.findByActive", query = "SELECT u FROM Ugroup u WHERE u.active = :active")})
public class Ugroup implements Serializable {

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
    private LocalDateTime createdDate;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;
    @Column(name = "active")
    private Boolean active;
    @ManyToMany(mappedBy = "ugroupSet")
    private Set<InvitationPost> invitationPostSet;
    @JoinTable(name = "group_user", joinColumns = {
        @JoinColumn(name = "group_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")})
    @ManyToMany
    private Set<User> userSet;

    public Ugroup() {
    }

    public Ugroup(Long id) {
        this.id = id;
    }

    public Ugroup(Long id, String groupName) {
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public LocalDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<InvitationPost> getInvitationPostSet() {
        return invitationPostSet;
    }

    public void setInvitationPostSet(Set<InvitationPost> invitationPostSet) {
        this.invitationPostSet = invitationPostSet;
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
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
        if (!(object instanceof Ugroup)) {
            return false;
        }
        Ugroup other = (Ugroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cmc.pojo.Ugroup[ id=" + id + " ]";
    }
    
}
