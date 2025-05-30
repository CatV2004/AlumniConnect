/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.ChangePasswordDTO;
import com.cmc.dtos.PageResponse;
import com.cmc.dtos.ResponseUserDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.User;
import java.util.List;
import java.util.Map;
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
    
    void Save(User user);

    public PageResponse<ResponseUserDTO> getAllUsers(Map<String, Object> params);

    public void updateCurrentUser(String username, String email, String phone, MultipartFile avatar, MultipartFile cover);
    
    List<User> findUsersNotInGroup(Long groupId);
    
    List<User> findUsersInGroup(Long groupId);
    
    void changePassword(User user, ChangePasswordDTO dto);
    
    boolean existsByEmail(String email);
    
    boolean existsByStudentCode(String code);

    boolean existsByUsername(String username);
}
