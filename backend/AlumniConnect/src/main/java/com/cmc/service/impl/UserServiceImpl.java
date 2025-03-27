/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cmc.components.CloudinaryService;
import com.cmc.dtos.ChangePasswordDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.User;
import com.cmc.repository.UserRepository;
import com.cmc.service.UserService;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author FPTSHOP
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder passEncoder;
    
    private final CloudinaryService cloudinaryService = new CloudinaryService();

    @Override
    public UserDTO getUserByUsername(String username) {
        return this.userRepo.getUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = this.userRepo.getUserByUsername(username);
        if (userDTO == null) {
            throw new UsernameNotFoundException("Không tồn tại!");
        }
        
        User user = modelMapper.map(userDTO, User.class);

        Set<GrantedAuthority> authorities = new HashSet<>(); 
        authorities.add(new SimpleGrantedAuthority(userDTO.getRole()));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public void addUser(User user) {
        if (userRepo.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username đã tồn tại!");
        }

        if (user.getAvatarFile() != null && !user.getAvatarFile().isEmpty()) {
            String avatarUrl = cloudinaryService.uploadFile(user.getAvatarFile());
            user.setAvatar(avatarUrl);
        }

        if (user.getCoverFile() != null && !user.getCoverFile().isEmpty()) {
            String coverUrl = cloudinaryService.uploadFile(user.getCoverFile());
            user.setCover(coverUrl);
        }

        user.setPassword(passEncoder.encode(user.getPassword()));

        userRepo.addUser(user);
    }

    @Override
    public boolean authUser(String username, String password) {
        return this.userRepo.authUser(username, password);
    }
}
