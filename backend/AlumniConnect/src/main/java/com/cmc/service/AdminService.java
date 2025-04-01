/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.UserDTO;
import com.cmc.pojo.User;

/**
 *
 * @author FPTSHOP
 */
public interface AdminService {
    void registerAdmin(UserDTO admin);
    void confirmAlumniRegistration(String username);
    void resetTeacherPasswordDeadline(String username);
}

