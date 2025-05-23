/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.components.CloudinaryService;
import com.cmc.dtos.ChangePasswordDTO;
import com.cmc.dtos.PageResponse;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.Alumni;
import com.cmc.pojo.User;
import com.cmc.repository.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder passEncoder;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public User getUserByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM User WHERE username = :username", User.class);
        q.setParameter("username", username);

        User user = (User) q.uniqueResult();
        return user != null ? user : null;
    }

    @Override
    public void saveOrUpdate(User user) {
        if (user.getId() != null) {
            getCurrentSession().merge(user);
        } else {
            getCurrentSession().persist(user);
        }
        getCurrentSession().flush();
    }

    @Override
    public void changePassword(String username, ChangePasswordDTO dto) {
        User user = this.getUserByUsername(username);

        if (user != null) {
            user.setPassword(passEncoder.encode(dto.getNewPassword()));
            getCurrentSession().flush();
        }
    }

    @Override
    public boolean authUser(String username, String password, String role) {
        User user = this.getUserByUsername(username);
        return user != null && this.passEncoder.matches(password, user.getPassword()) && user.getRole().equals(role);
    }

    @Override
    public boolean existsByUsername(String username) {
        Boolean exists = getCurrentSession()
                .createQuery("SELECT EXISTS(SELECT 1 FROM User u WHERE u.username = :username)", Boolean.class)
                .setParameter("username", username)
                .uniqueResult();
        return exists != null && exists;
    }

    @Override
    public User getUserById(long id) {
        User user = getCurrentSession()
                .createQuery("FROM User WHERE id = :id", User.class)
                .setParameter("id", id)
                .uniqueResult();
        return user;
    }

    @Override
    public List<User> getUsers() {
        Session s = this.factory.getObject().getCurrentSession();
        Query<User> q = s.createQuery("FROM User WHERE active = true", User.class);
        return q.getResultList();
    }

    @Override
    public PageResponse<User> getAllUsers(Map<String, Object> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<User> dataQuery = cb.createQuery(User.class);
        Root<User> dataRoot = dataQuery.from(User.class);

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);

        List<Predicate> dataPredicates = new ArrayList<>();
        List<Predicate> countPredicates = new ArrayList<>();

        if (params.containsKey("excludeUsername")) {
            String excludeUsername = (String) params.get("excludeUsername");
            dataPredicates.add(cb.notEqual(dataRoot.get("username"), excludeUsername));
            countPredicates.add(cb.notEqual(countRoot.get("username"), excludeUsername));
        }

        dataPredicates.add(cb.or(
                cb.isNull(dataRoot.get("deletedDate")),
                cb.equal(dataRoot.get("active"), true)
        ));
        countPredicates.add(cb.or(
                cb.isNull(countRoot.get("deletedDate")),
                cb.equal(countRoot.get("active"), true)
        ));

        if (params.containsKey("keyword")) {
            String keyword = "%" + params.get("keyword").toString().trim().toLowerCase() + "%";

            Predicate dataKeywordPredicate = cb.or(
                    cb.like(cb.lower(dataRoot.get("firstName")), keyword),
                    cb.like(cb.lower(dataRoot.get("lastName")), keyword)
            );
            dataPredicates.add(dataKeywordPredicate);

            Predicate countKeywordPredicate = cb.or(
                    cb.like(cb.lower(countRoot.get("firstName")), keyword),
                    cb.like(cb.lower(countRoot.get("lastName")), keyword)
            );
            countPredicates.add(countKeywordPredicate);
        }

        dataQuery.where(dataPredicates.toArray(new Predicate[0]));
        countQuery.select(cb.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));

        int page = (int) params.getOrDefault("page", 1);
        int size = (int) params.getOrDefault("size", 10);
        int firstResult = (page - 1) * size;

        List<User> users = session.createQuery(dataQuery)
                .setFirstResult(firstResult)
                .setMaxResults(size)
                .getResultList();

        Long totalItems = session.createQuery(countQuery).getSingleResult();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        return new PageResponse<>(users, page, size, totalItems, totalPages);
    }

    @Override
    public List<User> findAllActiveUsers() {
        Session session = this.factory.getObject().getCurrentSession();
        Query<User> query = session.createQuery(
                "FROM User u WHERE u.active = true",
                User.class);
        return query.list();
    }

    @Override
    public List<User> findUsersInGroup(Long groupId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<User> query = session.createQuery(
                "SELECT u FROM User u JOIN u.ugroupSet ug WHERE ug.id = :groupId",
                User.class);
        query.setParameter("groupId", groupId);
        return query.list();
    }

    @Override
    public List<User> findUsersNotInGroup(Long groupId) {
        Session session = this.factory.getObject().getCurrentSession();

        Query<User> query = session.createQuery(
                "SELECT u FROM User u "
                + "WHERE u.active = true AND u.id NOT IN ("
                + "SELECT u2.id FROM Ugroup g JOIN g.userSet u2 WHERE g.id = :groupId"
                + ")",
                User.class
        );

        query.setParameter("groupId", groupId);
        return query.getResultList();
    }

}
