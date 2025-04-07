/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.dtos.UserDTO;
import com.cmc.pojo.User;
import com.cmc.repository.AdminRepository;
import com.cmc.repository.UserRepository;
import com.cmc.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author FPTSHOP
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void registerAdmin(UserDTO admin) {
        if (userRepository.existsByUsername(admin.getUsername())) {
            throw new RuntimeException("Tài khoản đã tồn tại!");
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole("ADMIN");
        User u = modelMapper.map(admin, User.class);
        userRepository.saveOrUpdate(u);
    }

    @Override
    public void confirmAlumniRegistration(String username) {
        adminRepository.confirmAlumniRegistration(username);
    }

    @Override
    public void resetTeacherPasswordDeadline(String username) {
        adminRepository.resetTeacherPasswordDeadline(username);
    }
}
