/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.UserDTO;
import com.cmc.pojo.Teacher;
import com.cmc.pojo.User;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

/**
 */
public interface UserService extends UserDetailsService {

    User getUserByUsername(String username);

    User addUser(Map<String, String> params, MultipartFile avatar, MultipartFile cover);

    boolean authUser(String username, String password);
    
    boolean registerAdmin(String username, String password);
    
    User getUserById(Long id);
    
    List<User> getUsers();
    
    
}
