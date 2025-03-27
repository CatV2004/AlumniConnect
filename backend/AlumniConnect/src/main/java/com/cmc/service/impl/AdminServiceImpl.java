/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.repository.AdminRepository;
import com.cmc.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author FPTSHOP
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void confirmAlumniRegistration(String username) {
        adminRepository.confirmAlumniRegistration(username);
    }

    @Override
    public void resetTeacherPasswordDeadline(String username) {
        adminRepository.resetTeacherPasswordDeadline(username);
    }
}
