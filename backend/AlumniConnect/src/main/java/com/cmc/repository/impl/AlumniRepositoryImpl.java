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
import com.cmc.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
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

    private final Session getCurrentSession() {
        Session session = factory.getObject().getCurrentSession();
        if (session == null || !session.isOpen()) {
            session = factory.getObject().openSession();
        }
        return session;
    }

    @Override
    public void registerAlumni(Alumni alumni) {
        getCurrentSession().persist(alumni);
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
    public void approveAlumni(Long id) {
        Session session = getCurrentSession();  
        Transaction tx = session.beginTransaction(); 
        try {
            User user = this.userRepository.getUserById(id);
            System.out.println("user confirm: " + user);
            if (user == null) {
                throw new EntityNotFoundException("User not found with id: " + id);
            }

            Alumni alumni = session.get(Alumni.class, id);
            if (alumni == null) {
                throw new EntityNotFoundException("Alumni not found with id: " + id);
            }

            alumni.setIsVerified(true);
            user.setActive(true);

            session.merge(alumni);
            session.merge(user);
            session.flush();

            tx.commit(); 
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public Alumni findByStudentCode(String studentCode) {
        Session s = this.getCurrentSession();
        Query q = s.createQuery("FROM Alumni WHERE studentCode = :studentCode", User.class);
        q.setParameter("studentCode", studentCode);

        Alumni alumni = (Alumni) q.uniqueResult();
        return alumni != null ? alumni : null;
    }

    @Override
    public boolean checkStudentCode(String studentId) {
        Long count = getCurrentSession()
                .createQuery("SELECT COUNT(a) FROM Alumni a WHERE a.studentCode = :studentId", Long.class)
                .setParameter("studentId", studentId)
                .uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public List<Alumni> getAlumnis() {
        String hql = "FROM Alumni";
        return getCurrentSession().createQuery(hql, Alumni.class).getResultList();

    }

}
