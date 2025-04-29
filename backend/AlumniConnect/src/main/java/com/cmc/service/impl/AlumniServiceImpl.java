/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.dtos.AlumniDTO;
import com.cmc.dtos.AlumniRegisterDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.Alumni;
import com.cmc.pojo.User;
import com.cmc.repository.AlumniRepository;
import com.cmc.repository.UserRepository;
import com.cmc.service.AlumniService;
import com.cmc.service.MailServices;
import com.cmc.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author FPTSHOP
 */
@Service
@Transactional
public class AlumniServiceImpl implements AlumniService {

    @Autowired
    private AlumniRepository alumniRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MailServices mailServices;
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean existsByStudentId(String studentCode) {
        return alumniRepository.checkStudentCode(studentCode);
    }

    @Override
    public List<AlumniDTO> getPendingAlumniRegistrations() {
        return alumniRepository.getPendingAlumniRegistrations();
    }

    @Override
    public Alumni registerAlumni(AlumniRegisterDTO alumniDTO) {
        UserDTO userDTO = new UserDTO(
                alumniDTO.getUsername(),
                alumniDTO.getPassword(),
                alumniDTO.getFirstName(),
                alumniDTO.getLastName(),
                alumniDTO.getEmail(),
                alumniDTO.getPhone(),
                "ALUMNI",
                Boolean.FALSE
        );

        if (alumniRepository.checkStudentCode(alumniDTO.getStudentCode())) {
            throw new IllegalArgumentException("Mã sinh viên đã tồn tại!");
        }
        if (userRepository.existsByUsername(alumniDTO.getUsername())) {
            throw new RuntimeException("Tài khoản đã tồn tại!");
        }

        User savedUser = userService.saveOrUpdate(userDTO, alumniDTO.getAvatar(), alumniDTO.getCover());

        Alumni alumni = new Alumni();
        alumni.setStudentCode(alumniDTO.getStudentCode());
        alumni.setUserId(savedUser); 

        alumniRepository.saveOrUpdateAlumni(alumni);

        return alumni;
    }

    @Override
    public List<Alumni> getAlumnis(Map<String, String> params) {
        return this.alumniRepository.getAlumnis(params);
    }

    @Override
    public long countAlumnis() {
        return alumniRepository.countAlumnis();
    }

    @Override
    public void approveAlumni(Long id) {
        try {
            boolean isApproved = alumniRepository.approveAlumni(id);

            Alumni a = alumniRepository.getAlumniById(id);

            if (isApproved) {
                String alumniEmail = a.getUserId().getEmail();
                String alumniName = a.getUserId().toString();
                mailServices.notifyAlumniOnApproval(alumniEmail, alumniName);
            }
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Approval failed: " + e.getMessage());
        }
    }

}
