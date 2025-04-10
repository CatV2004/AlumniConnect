/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.UserDTO;
import com.cmc.pojo.User;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

/**
 */
public interface UserService extends UserDetailsService {

    User getUserByUsername(String username);

    User saveOrUpdate(UserDTO userDTO, MultipartFile avatar, MultipartFile cover);

    boolean authUser(String username, String password, String role);
    
    boolean registerAdmin(String username, String password);
    
    User getUserById(Long id);
    
    List<User> getUsers();
    
    
}
