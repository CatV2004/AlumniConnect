/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.dtos.ChangePasswordDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.User;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public interface UserRepository {

    User  getUserByUsername(String username);
    
    User getUserById(long id);

    void saveOrUpdate(User User);

    void changePassword(String username, ChangePasswordDTO dto);
    
    boolean authUser(String username, String password );
    
    boolean existsByUsername(String username);

}
