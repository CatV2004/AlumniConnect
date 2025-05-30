/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.validator;

import com.cmc.dtos.UserDTO;
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
public class UserValidator implements Validator{
    
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO user = (UserDTO) target;
        
        if (user.getEmail() != null && userService.existsByEmail(user.getEmail())) {
            errors.rejectValue("email", "email", "Email đã được sử dụng");
        }
        
        if (user.getUsername() != null && userService.existsByUsername(user.getUsername())) {
            errors.rejectValue("username", "username", "Tên đăng nhập đã được sử dụng");
        }
        
    }
    
}
