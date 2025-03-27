/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.dtos.AlumniDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.Alumni;
import com.cmc.pojo.User;
import com.cmc.repository.AlumniRepository;
import java.util.List;
import java.util.stream.Collectors;
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
public class AlumniRepositoryImpl implements AlumniRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private ModelMapper modelMapper;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    public UserDTO getUserById(long id) {
        User user = getCurrentSession()
                .createQuery("FROM User WHERE id = :id", User.class)
                .setParameter("id", id)
                .uniqueResult();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

    @Override
    public void registerAlumni(AlumniDTO alumniDTO) {
        UserDTO userDTO = this.getUserById(alumniDTO.getId());

        User user = modelMapper.map(userDTO, User.class);
        if (user != null) {

            user.setRole("ALUMNI");
            getCurrentSession().merge(user);

            Alumni alumni = new Alumni();
            alumni.setId(user.getId());
            alumni.setStudentCode(alumniDTO.getStudentCode());
            alumni.setIsVerified(false);

            getCurrentSession().persist(alumni);
        }
    }

    @Override
    public boolean existsByStudentId(String studentCode) {
        return getCurrentSession()
                .createQuery("SELECT COUNT(a) > 0 FROM Alumni a WHERE a.studentCode = :studentCode", Boolean.class)
                .setParameter("studentCode", studentCode)
                .uniqueResultOptional()
                .orElse(false);
    }

    @Override
    public List<AlumniDTO> getPendingAlumniRegistrations() {
        return getCurrentSession()
                .createQuery("FROM Alumni a WHERE a.isVerified = false", Alumni.class)
                .list()
                .stream()
                .map(alumni -> modelMapper.map(alumni, AlumniDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void approveAlumni(String username) {
        Session session = getCurrentSession();
        User user = session
                .createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", username)
                .uniqueResult();

        if (user != null) {
            Alumni alumni = session
                    .createQuery("FROM Alumni WHERE user.id = :userId", Alumni.class)
                    .setParameter("userId", user.getId())
                    .uniqueResult();

            if (alumni != null) {
                alumni.setIsVerified(true);
                getCurrentSession().merge(alumni); 
            }
        }
    }

}
