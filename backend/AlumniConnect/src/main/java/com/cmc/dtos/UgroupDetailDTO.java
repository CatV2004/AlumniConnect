/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.dtos;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public class UgroupDetailDTO extends UgroupDTO {

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<UserSimpleDTO> members;

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

    public List<UserSimpleDTO> getMembers() {
        return members;
    }

    public void setMembers(List<UserSimpleDTO> members) {
        this.members = members;
    }
}
