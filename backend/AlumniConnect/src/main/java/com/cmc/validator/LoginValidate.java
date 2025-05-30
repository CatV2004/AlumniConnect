/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.validator;

import com.cmc.dtos.LoginRequestDTO;
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
public class LoginValidate implements Validator{
    
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return LoginRequestDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginRequestDTO loginDTO = (LoginRequestDTO) target;
        
        if(loginDTO.getUsername() != null && !this.userService.existsByUsername(loginDTO.getUsername())){
            errors.rejectValue("username", "username", "Tên đăng nhập không tồn tại !!!");
        }
    }
    
}
