/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.components.CloudinaryService;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.Teacher;
import com.cmc.pojo.User;
import com.cmc.repository.UserRepository;
import com.cmc.service.UserService;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author FPTSHOP
 */
@Service("UserDetailsService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder passEncoder;

    private final CloudinaryService cloudinaryService;
    
    @Autowired
    public UserServiceImpl(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public User getUserByUsername(String username) {
        return this.userRepo.getUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepo.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Không tồn tại!");
        }

        if (user.getUsername() == null || user.getPassword() == null) {
            throw new UsernameNotFoundException("Lỗi ánh xạ dữ liệu từ UserDTO sang User!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User saveOrUpdate(UserDTO userDTO, MultipartFile avatar, MultipartFile cover) {
        User user = new User();
        user = modelMapper.map(userDTO, User.class);

        user.setPassword(this.passEncoder.encode(user.getPassword()));
        user.setRole("ALUMNI");

        user.setAvatar(cloudinaryService.uploadFile(avatar,"avatar"));
        user.setCover(cloudinaryService.uploadFile(cover, "cover"));

        user.setActive(true);
        
        this.userRepo.saveOrUpdate(user);

        return user;
    }
  
    @Override
    public boolean authUser(String username, String password, String role) {
        User u = this.getUserByUsername(username);
        return u.getActive() && this.userRepo.authUser(username, password, role);
    }

    @Override
    public boolean registerAdmin(String username, String password) {
        if (userRepo.getUserByUsername(username) != null) {
            return false;
        }

        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(passEncoder.encode(password));
        admin.setRole("ADMIN");
        userRepo.saveOrUpdate(admin);
        return true;
    }

    @Override
    public User getUserById(Long id) {
        return this.userRepo.getUserById(id);
    }

    @Override
    public List<User> getUsers() {
        return this.userRepo.getUsers();
    }

}
