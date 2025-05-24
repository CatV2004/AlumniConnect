/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.dtos.ChangePasswordDTO;
import com.cmc.dtos.PageResponse;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author FPTSHOP
 */
public interface UserRepository {

    User getUserByUsername(String username);

    User getUserById(long id);
    
    void saveOrUpdate(User User);

    void changePassword(User user);

    boolean authUser(String username, String password, String role);

    boolean existsByUsername(String username);

    List<User> getUsers();

    public PageResponse<User> getAllUsers(Map<String, Object> params);

    List<User> findAllActiveUsers();

    List<User> findUsersInGroup(Long groupId);

    List<User> findUsersNotInGroup(Long groupId);
    
}
