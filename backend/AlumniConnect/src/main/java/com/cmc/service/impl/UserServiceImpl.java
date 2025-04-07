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

    private final CloudinaryService cloudinaryService = new CloudinaryService();

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
    public User addUser(Map<String, String> params, MultipartFile avatar, MultipartFile cover) {
        User u = new User();
        u.setFirstName(params.get("firstName"));
        u.setLastName(params.get("lastName"));
        u.setPhone(params.getOrDefault("phone", "113"));
        u.setEmail(params.getOrDefault("email", "admin@gmail.com"));
        u.setUsername(params.get("username"));
        u.setPassword(this.passEncoder.encode(params.get("password")));
        u.setRole(params.get("role"));

        u.setAvatar(cloudinaryService.uploadFile(avatar));
        u.setCover(cloudinaryService.uploadFile(cover));
        u.setCreatedDate(LocalDateTime.now());
        u.setUpdatedDate(LocalDateTime.now());

        u.setActive(true);


        userRepo.saveOrUpdate(u);

        return u;
    }

    @Override
    public boolean authUser(String username, String password) {
        User u = this.getUserByUsername(username);
        return u.getActive() && this.userRepo.authUser(username, password);
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

}
