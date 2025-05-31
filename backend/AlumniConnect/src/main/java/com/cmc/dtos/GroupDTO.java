/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.dtos;

import com.cmc.pojo.Ugroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author FPTSHOP
 */
public class GroupDTO {

    private Long id;
    @NotBlank(message = "Tên nhóm không được để trốngs")
    private String groupName;
    private String createdDate;
    private String updatedDate;
    private Boolean active;
    private long memberCount;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Constructors
    public GroupDTO() {
    }

    public GroupDTO(Ugroup group) {
        this.id = group.getId();
        this.groupName = group.getGroupName();
        this.active = group.getActive();
        this.memberCount = group.getUserSet() != null ? group.getUserSet().size() : 0;

        this.createdDate = group.getCreatedDate() != null
                ? group.getCreatedDate().format(DATE_FORMATTER)
                : null;

        this.updatedDate = group.getUpdatedDate() != null
                ? group.getUpdatedDate().format(DATE_FORMATTER)
                : null;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public long getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(long memberCount) {
        this.memberCount = memberCount;
    }

}
