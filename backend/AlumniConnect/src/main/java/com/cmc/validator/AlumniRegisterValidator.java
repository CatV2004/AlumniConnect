/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.validator;

import com.cmc.dtos.AlumniRegisterDTO;
import com.cmc.repository.UserRepository;
import com.cmc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author PHAT
 */
@Component
public class AlumniRegisterValidator implements Validator {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepo;

    @Override
    public boolean supports(Class<?> clazz) {
        return AlumniRegisterDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AlumniRegisterDTO dto = (AlumniRegisterDTO) target;

        if (dto.getEmail() != null && userService.existsByEmail(dto.getEmail())) {
            errors.rejectValue("email", "email", "Email đã được sử dụng");
        }
        
        if (dto.getStudentCode() != null && userService.existsByStudentCode(dto.getStudentCode())){
            errors.rejectValue("studentCode", "studentCode", "Trùng mã sinh viên!!!");
        }
        
        if (dto.getUsername() != null && userRepo.existsByUsername(dto.getUsername())){
            errors.rejectValue("username", "username", "Tên đăng nhập đã được sử dụng!!!");
        }
        
        

        MultipartFile avatar = dto.getAvatar();
        if (avatar != null && avatar.isEmpty()) {
            String contentType = avatar.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                errors.rejectValue("avatar", "avatar", "Avatar phải là file ảnh (jpg, png,...)");
            } else if (avatar.getSize() > 2 * 1024 * 1024) {
                errors.rejectValue("avatar", "avatar", "Kích thước ảnh không vượt quá 5MB");

            }
        }

        MultipartFile cover = dto.getCover();
        if (cover != null && cover.isEmpty()) {
            String contentType = cover.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                errors.rejectValue("cover", "cover", "Cover phải là file ảnh (jpg, png,...)");
            } else if (cover.getSize() > 5 * 1024 * 1024) {
                errors.rejectValue("cover", "cover", "Kích thước ảnh không vượt quá 5MB");

            }
        }

    }

}
