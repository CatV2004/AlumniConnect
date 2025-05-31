
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.validator;

import com.cmc.dtos.TeacherRequestDTO;
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
public class TeacherRequestValidator implements Validator{
    @Autowired
    private UserRepository userRepo;

    @Override
    public boolean supports(Class<?> clazz) {
        return TeacherRequestDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TeacherRequestDTO teacher = (TeacherRequestDTO) target;
        
        if(teacher.getEmail() != null && this.userRepo.existsByEmail(teacher.getEmail())){
            errors.rejectValue("email", "email", "Email đã được sử dụng!!!");
        }
        
        if (teacher.getUsername() != null && this.userRepo.existsByUsername(teacher.getUsername())){
            errors.rejectValue("username", "username", "Đã tồn tại tài khoản giảng viên!!!");
        }
    }
    
}
