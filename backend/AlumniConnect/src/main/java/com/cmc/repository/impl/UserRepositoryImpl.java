/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.components.CloudinaryService;
import com.cmc.dtos.ChangePasswordDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.Alumni;
import com.cmc.pojo.User;
import com.cmc.repository.UserRepository;
import java.util.List;
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
        Query q = s.createQuery("FROM User", User.class);
        return q.getResultList();
    }

}
