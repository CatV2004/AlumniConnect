/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.dtos.AlumniDTO;
import com.cmc.dtos.TeacherDTO;
import com.cmc.pojo.Alumni;
import com.cmc.pojo.User;
import com.cmc.repository.AdminRepository;
import java.time.LocalDateTime;
import java.util.List;
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
public class AdminRepositoryImpl implements AdminRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private ModelMapper modelMapper;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public void confirmAlumniRegistration(String username) {
        User user = getCurrentSession()
                .createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", username)
                .uniqueResult();

        if (user != null && "ALUMNI".equalsIgnoreCase(user.getRole())) {
            AlumniDTO alumniDTO = modelMapper.map(user, AlumniDTO.class);
            alumniDTO.setIsVerified(true);
            getCurrentSession().merge(modelMapper.map(alumniDTO, User.class));
        }
    }

    @Override
    public void resetTeacherPasswordDeadline(String username) {
        User user = getCurrentSession()
                .createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", username)
                .uniqueResult();

        if (user != null && "TEACHER".equalsIgnoreCase(user.getRole())) {
            TeacherDTO teacherDTO = modelMapper.map(user, TeacherDTO.class);
            teacherDTO.setMustChangePassword(true);
            teacherDTO.setPasswordResetTime(LocalDateTime.now().plusHours(24));
            getCurrentSession().merge(modelMapper.map(teacherDTO, User.class));
        }
    }


}
