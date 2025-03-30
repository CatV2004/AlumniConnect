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
import com.cmc.repository.UserRepository;
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
    
    @Autowired
    private UserRepository userRepository;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public void registerAlumni(AlumniDTO alumniDTO) {
        User user = userRepository.getUserById(alumniDTO.getId());

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
        long count = getCurrentSession()
                .createQuery("SELECT COUNT(a) FROM Alumni a WHERE a.studentCode = :studentCode", Long.class)
                .setParameter("studentCode", studentCode)
                .uniqueResult();
        return count > 0;
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
