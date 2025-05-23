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
public class UgroupDetailDTO extends GroupDTO {

    private List<UserSimpleDTO> members;

    public List<UserSimpleDTO> getMembers() {
        return members;
    }

    public void setMembers(List<UserSimpleDTO> members) {
        this.members = members;
    }
}
