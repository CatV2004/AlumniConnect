/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.dtos.TeacherDTO;
import com.cmc.pojo.Teacher;
import com.cmc.pojo.User;
import com.cmc.repository.TeacherRepository;
import com.cmc.service.MailServices;
import com.cmc.service.TeacherService;
import com.cmc.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author FPTSHOP
 */
@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private MailServices mailServices;
    
    @Autowired
    private UserService userService;

    @Override
    public boolean createTeacherAccount(TeacherDTO teacherDTO) {

        boolean isCreated = teacherRepository.createTeacherAccount(teacherDTO);

        if (isCreated) {
            mailServices.notifyTeacherAccountCreation(
                    teacherDTO.getUser().getEmail(),
                    teacherDTO.getUser().getFirstName() + " " + teacherDTO.getUser().getLastName(),
                    teacherDTO.getUser().getUsername(),
                    "ou@123"
            );
            return true;
        }
        return false;

    }

    @Override
    public boolean isPasswordChangeDeadlineExceeded(String username) {
        return teacherRepository.isPasswordChangeDeadlineExceeded(username);
    }

    @Override
    public void resetPasswordChangeDeadline(Long id) {
        boolean isReset = teacherRepository.resetPasswordChangeDeadline(id);
        if (isReset) {
            String teacherEmail = teacherRepository.getTeacherById(id).getUserId().getEmail();
            String teacherName = teacherRepository.getTeacherById(id).getUserId().toString();
            LocalDateTime passwordChangeDeadline = teacherRepository.getTeacherById(id).getPasswordResetTime();

            mailServices.notifyTeacherPasswordReset(teacherEmail, teacherName, passwordChangeDeadline);
        }
    }

    @Override
    public List<Teacher> getTeachers(Map<String, String> params) {
        return this.teacherRepository.getTeachers(params);
    }

    @Override
    public Long countTeachers() {
        return this.teacherRepository.countTeachers();
    }
    
    @Override
    public void lockExpiredAccounts(LocalDateTime dateTime){
        List<Teacher> expiredTeachers = this.teacherRepository.findAllByMustChangePassword(dateTime);
        for (Teacher teacher : expiredTeachers) {
            User user = teacher.getUserId();
            if (Boolean.TRUE.equals(user.getActive())) {
                teacher.setMustChangePassword(Boolean.FALSE);
                user.setActive(Boolean.FALSE);
                this.teacherRepository.saveOrUpdate(teacher);
                this.userService.Save(user);
            }
    }
    }
}
