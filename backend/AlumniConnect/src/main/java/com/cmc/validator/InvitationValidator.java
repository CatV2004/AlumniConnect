/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.validator;

import com.cmc.dtos.InvitationRequestDTO;
import com.cmc.pojo.Ugroup;
import com.cmc.pojo.User;
import com.cmc.repository.InvitationPostRepository;
import com.cmc.repository.UgroupRepository;
import com.cmc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author PHAT
 */
@Component
public class InvitationValidator implements Validator {

    @Autowired
    private InvitationPostRepository invitationRepo;

    @Autowired
    private UgroupRepository groupRepo;
    
    @Autowired
    private UserRepository userRepo;

    @Override
    public boolean supports(Class<?> clazz) {
        return InvitationRequestDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        InvitationRequestDTO invitation = (InvitationRequestDTO) target;

        if (invitation.getEventName() != null && this.invitationRepo.exitNameEvent(invitation.getEventName())) {
            errors.rejectValue("eventName", "eventName", "Tên sự kiện bị trùng!!!");
        }

        if (invitation.getGroupIds() != null) {
            for (Long groupId : invitation.getGroupIds()) {
                Ugroup group = this.groupRepo.findById(groupId);
                if (group == null) {
                    errors.rejectValue("groupIds", "groupIds", "Nhóm với ID " + groupId + " không tồn tại!!!");
                }
            }
        }
        
        if (invitation.getUserIds() != null)
            for(Long u :invitation.getUserIds()){
                User user = this.userRepo.getUserById(u);
                if(user == null){
                    errors.rejectValue("userIds", "userIds", "User có ID " + u + " không tồn tại!!!");
                }
                if(user != null && user.getActive() == Boolean.FALSE){
                    errors.rejectValue("userIds", "userIds", "User có ID " + u + " Chưa được duyệt!!!");
                }
            }
    }

}
