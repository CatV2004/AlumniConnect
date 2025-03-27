/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.ChangePasswordDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.User;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author FPTSHOP
 */
public interface UserService extends UserDetailsService {

    UserDTO getUserByUsername(String username);

    void addUser(UserDTO user);

    boolean authUser(String username, String password);
}
