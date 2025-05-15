/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.components.CloudinaryService;
import com.cmc.dtos.PageResponse;
import com.cmc.dtos.ResponseUserDTO;
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
import java.util.stream.Collectors;
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
        
        String role = user.getRole();
        if (role == null || role.isEmpty()) {
            throw new UsernameNotFoundException("User role is missing for: " + username);
        }
        
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User saveOrUpdate(UserDTO userDTO, MultipartFile avatar, MultipartFile cover) {
        User user;

        if (userDTO.getId() != null) {
            user = userRepo.getUserById(userDTO.getId());
            if (user == null) {
                throw new RuntimeException("User not found with id: " + userDTO.getId());
            }

            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());

            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(passEncoder.encode(userDTO.getPassword()));
            }

        } else {
            user = modelMapper.map(userDTO, User.class);
            user.setPassword(passEncoder.encode(userDTO.getPassword()));
            user.setRole("ALUMNI");
        }

        if (avatar != null && !avatar.isEmpty()) {
            user.setAvatar(cloudinaryService.uploadFile(avatar, "avatar"));
        }

        if (cover != null && !cover.isEmpty()) {
            user.setCover(cloudinaryService.uploadFile(cover, "cover"));
        }

        userRepo.saveOrUpdate(user);
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

    @Override
    public PageResponse<ResponseUserDTO> getAllUsers(Map<String, Object> params) {
        PageResponse<User> userPage = userRepo.getAllUsers(params);

        List<ResponseUserDTO> dtos = userPage.getContent()
                .stream()
                .map(user -> modelMapper.map(user, ResponseUserDTO.class))
                .collect(Collectors.toList());

        return new PageResponse<>(
                dtos,
                userPage.getCurrentPage(),
                userPage.getPageSize(),
                userPage.getTotalItems(),
                userPage.getTotalPages()
        );
    }

    @Override
    public void updateCurrentUser(String username, String email, String phone,
            MultipartFile avatar, MultipartFile cover) {

        User user = userRepo.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("Không tìm thấy người dùng!");
        }

        if (email != null) {
            user.setEmail(email);
        }
        if (phone != null) {
            user.setPhone(phone);
        }
        if (avatar != null) {
            String avatarUrl = cloudinaryService.uploadFile(avatar, "avatar");
            user.setAvatar(avatarUrl);
        }
        if (cover != null) {
            System.out.println("coverUrl: " + cover);
            String coverUrl = cloudinaryService.uploadFile(cover, "cover");
            user.setCover(coverUrl);
        }

        this.userRepo.saveOrUpdate(user);
    }

}
