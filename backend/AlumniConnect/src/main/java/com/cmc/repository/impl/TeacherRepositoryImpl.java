/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.dtos.TeacherDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.Alumni;
import com.cmc.pojo.Teacher;
import com.cmc.pojo.User;
import com.cmc.repository.TeacherRepository;
import com.cmc.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private BCryptPasswordEncoder passEncoder;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public boolean createTeacherAccount(TeacherDTO teacherDTO) {
        UserDTO userDTO = teacherDTO.getUser();
        User user = modelMapper.map(userDTO, User.class);
        Teacher teacher = modelMapper.map(teacherDTO, Teacher.class);

        if (user != null) {
            if (user.getPassword() == null) {
                user.setPassword("ou@123");
            }
            user.setPassword(this.passEncoder.encode(user.getPassword()));
            user.setRole("TEACHER");
            user.setActive(true);

            getCurrentSession().persist(user);

            teacher.setMustChangePassword(true);
            teacher.setPasswordResetTime(LocalDateTime.now().plusHours(24));
            teacher.setUserId(user);

            getCurrentSession().persist(teacher);

            return true;
        }

        return false;
    }

    @Override
    public boolean isPasswordChangeDeadlineExceeded(String username) {
        User user = getCurrentSession()
                .createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", username)
                .uniqueResult();

        if (user == null) {
            return false;
        }

        TeacherDTO teacherDTO = modelMapper.map(user, TeacherDTO.class);
        return teacherDTO.getPasswordResetTime() != null
                && teacherDTO.getPasswordResetTime().isBefore(LocalDateTime.now());
    }

    @Override
    public boolean resetPasswordChangeDeadline(Long id) {
        Teacher teacher = this.getTeacherById(id);

        if (teacher != null) {
            User user = teacher.getUserId();
            teacher.setMustChangePassword(Boolean.TRUE);
            teacher.setPasswordResetTime(LocalDateTime.now().plusHours(24));
            user.setActive(Boolean.TRUE);

            this.userRepository.saveOrUpdate(user);
            this.saveOrUpdate(teacher);

            return true;
        }
        return false;
    }

    @Override
    public List<Teacher> getTeachers(Map<String, String> params) {
        Session session = this.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Teacher> query = builder.createQuery(Teacher.class);
        Root<Teacher> root = query.from(Teacher.class);

        Join<Teacher, User> userJoin = root.join("userId", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null && params.containsKey("username")) {
            String username = params.get("username");
            Predicate usernamePredicate = builder.like(userJoin.get("username"), "%" + username + "%");
            predicates.add(usernamePredicate);
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        query.select(root).orderBy(builder.desc(root.get("id")));

        Query<Teacher> q = session.createQuery(query);

        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.get("page"));
            int size = Integer.parseInt(params.getOrDefault("size", "10"));
            int start = (page - 1) * size;

            q.setFirstResult(start);
            q.setMaxResults(size);
        }

        return q.getResultList();
    }

    @Override
    public Long countTeachers() {
        Session session = this.getCurrentSession();
        Query<Long> query = session.createQuery("SELECT count(*) FROM Teacher", Long.class);
        return query.uniqueResult();
    }

    @Override
    public void saveOrUpdate(Teacher teacher) {
        getCurrentSession().merge(teacher);
        getCurrentSession().flush();
    }

    @Override
    public Teacher getTeacherById(Long id) {
        Session session = this.getCurrentSession();
        Query<Teacher> query = session.createQuery("FROM Teacher WHERE id = :id", Teacher.class);
        query.setParameter("id", id);
        return query.uniqueResult();
    }
    
    @Override
    public List<Teacher> findAllByMustChangePassword(LocalDateTime deadline) {
        Session session = this.getCurrentSession();
        String hql = "FROM Teacher t WHERE t.mustChangePassword = true AND t.passwordResetTime < :deadline";
        return session.createQuery(hql, Teacher.class)
                .setParameter("deadline", deadline)
                .getResultList();
    }
}
