/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.components.CloudinaryService;
import com.cmc.dtos.ChangePasswordDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.User;
import com.cmc.repository.UserRepository;
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
    public UserDTO getUserByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM User WHERE username = :username", User.class);
        q.setParameter("username", username);

        User user = (User) q.uniqueResult();
        return user != null ? modelMapper.map(user, UserDTO.class) : null;
    }

    @Override
    public void addUser(User user) {
        getCurrentSession().persist(user);
    }

    @Override
    public void changePassword(String username, ChangePasswordDTO dto) {
        User user = getCurrentSession()
                .createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", username)
                .uniqueResult();

        if (user != null) {
            user.setPassword(passEncoder.encode(dto.getNewPassword()));
            getCurrentSession().flush();
        }
    }

    @Override
    public boolean authUser(String username, String password) {
        UserDTO uDTO = this.getUserByUsername(username);
        User user = modelMapper.map(uDTO, User.class);
        return this.passEncoder.matches(password, user.getPassword());
    }

    @Override
    public boolean existsByUsername(String username) {
        return getCurrentSession()
                .createQuery("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username",  Boolean.class)
                .uniqueResultOptional()
                .orElse(false);
    }
}
