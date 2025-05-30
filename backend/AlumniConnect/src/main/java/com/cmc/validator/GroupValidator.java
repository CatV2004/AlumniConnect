/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.validator;

import com.cmc.dtos.GroupDTO;
import com.cmc.dtos.UgroupDetailDTO;
import com.cmc.service.UgroupService;
import com.cmc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author PHAT
 */
@Component
public class GroupValidator implements Validator{
    
    @Autowired
    private UgroupService uGroupService;

    @Override
    public boolean supports(Class<?> clazz) {
        return GroupDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GroupDTO groupDTO = (GroupDTO) target;
        
        if(groupDTO.getGroupName()!= null && this.uGroupService.isByGroupName(groupDTO.getGroupName())){
            errors.rejectValue("groupName", "groupName", "Tên Nhóm đã tồn tại !!!");
        }
        
    }
    
}
