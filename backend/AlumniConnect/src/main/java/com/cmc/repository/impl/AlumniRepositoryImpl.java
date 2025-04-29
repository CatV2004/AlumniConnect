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
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public Alumni getAlumniById(Long id) {
        Session s = this.getCurrentSession();
        Query q = s.createQuery("FROM Alumni WHERE id = :id", Alumni.class);
        q.setParameter("id", id);

        Alumni alumni = (Alumni) q.uniqueResult();
        return alumni != null ? alumni : null;
    }

    @Override
    public void saveOrUpdateAlumni(Alumni alumni) {
        if (alumni.getId() == null) {
            getCurrentSession().persist(alumni);
        } else {
            getCurrentSession().merge(alumni);
        }
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
    public boolean approveAlumni(Long id) {
        Session session = factory.getObject().getCurrentSession();
        Alumni alumni = this.getAlumniById(id);
        User user = this.userRepository.getUserById(alumni.getUserId().getId());

        if (user == null) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }

        if (alumni == null) {
            throw new EntityNotFoundException("Alumni not found with id: " + id);
        }
        alumni.setIsVerified(true); 
        user.setActive(true);

        session.merge(alumni);
        session.merge(user);

        return true;
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
        Boolean exists = getCurrentSession()
                .createQuery("SELECT EXISTS(SELECT 1 FROM Alumni a WHERE a.studentCode = :studentId)", Boolean.class)
                .setParameter("studentId", studentId)
                .uniqueResult();
        return exists != null && exists;
    }

//    @Override
//    public boolean checkStudentCode(String studentId) {
//        return getCurrentSession()
//                .createQuery("SELECT 1 FROM Alumni a WHERE a.studentCode = :studentId", Integer.class)
//                .setParameter("studentId", studentId)
//                .setMaxResults(1)
//                .uniqueResult() != null;
//    }

    @Override
    public List<Alumni> getAlumnis(Map<String, String> params) {
        Session session = this.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Alumni> q = builder.createQuery(Alumni.class);
        Root<Alumni> root = q.from(Alumni.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String studentCode = params.get("studentCode");
            if (studentCode != null && !studentCode.isEmpty()) {
                predicates.add(builder.like(root.get("studentCode"), "%" + studentCode + "%"));
            }

            if (!predicates.isEmpty()) {
                q.where(predicates.toArray(new Predicate[0]));
            }

            Query<Alumni> query = session.createQuery(q);

            if (params.containsKey("page")) {
                int page = Integer.parseInt(params.get("page"));
                int size = Integer.parseInt(params.getOrDefault("size", "10"));
                int start = (page - 1) * size;
                query.setFirstResult(start);
                query.setMaxResults(size);
            }

            return query.getResultList();
        }

        return session.createQuery(q).getResultList();
    }

    @Override
    public long countAlumnis() {
        Session session = this.getCurrentSession();
        Query<Long> query = session.createQuery("SELECT count(*) FROM Alumni", Long.class);
        return query.uniqueResult();
    }

}
