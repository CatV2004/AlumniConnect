/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.dtos.TeacherDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.Teacher;
import com.cmc.pojo.User;
import com.cmc.repository.TeacherRepository;
import com.cmc.repository.UserRepository;
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
    
    @Autowired
    private UserRepository userRepository;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public void createTeacherAccount(TeacherDTO teacherDTO) {
        UserDTO userDTO = userRepository.getUserById(teacherDTO.getId());
        User user = modelMapper.map(userDTO, User.class);
        if (user != null) {

            user.setRole("TEACHER");
            getCurrentSession().merge(user);

            Teacher teacher = new Teacher();
            teacher.setId(user.getId());
            teacher.setMustChangePassword(teacherDTO.getMustChangePassword());
            teacher.setPasswordResetTime(new Date());

            getCurrentSession().persist(teacher);
        }
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
