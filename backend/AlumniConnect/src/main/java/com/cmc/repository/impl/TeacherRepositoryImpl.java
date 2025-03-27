/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.dtos.TeacherDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.User;
import com.cmc.repository.TeacherRepository;
import java.time.LocalDateTime;
import java.util.Date;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author FPTSHOP
 */
@Repository
@Transactional
public class TeacherRepositoryImpl implements TeacherRepository {
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private ModelMapper modelMapper;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public void createTeacherAccount(TeacherDTO teacherDTO) {
        User user = modelMapper.map(teacherDTO.getUser(), User.class);
        user.setRole("TEACHER");

        TeacherDTO newTeacherDTO = new TeacherDTO();
        newTeacherDTO.setUser(modelMapper.map(user, UserDTO.class));
        newTeacherDTO.setMustChangePassword(true);
        newTeacherDTO.setPasswordResetTime(new Date());

        getCurrentSession().persist(modelMapper.map(newTeacherDTO, User.class));
    }

    @Override
    public boolean isPasswordChangeDeadlineExceeded(String username) {
        User user = getCurrentSession()
            .createQuery("FROM User WHERE username = :username", User.class)
            .setParameter("username", username)
            .uniqueResult();

        if (user == null) return false;

        TeacherDTO teacherDTO = modelMapper.map(user, TeacherDTO.class);
        return teacherDTO.getPasswordResetTime() != null &&
                teacherDTO.getPasswordResetTime().before(new Date());
    }

    @Override
    public void resetPasswordChangeDeadline(String username) {
        User user = getCurrentSession()
            .createQuery("FROM User WHERE username = :username", User.class)
            .setParameter("username", username)
            .uniqueResult();

        if (user != null) {
            TeacherDTO teacherDTO = modelMapper.map(user, TeacherDTO.class);
            teacherDTO.setPasswordResetTime(new Date());
            teacherDTO.setMustChangePassword(true);
            getCurrentSession().merge(modelMapper.map(teacherDTO, User.class));
        }
    }
}
