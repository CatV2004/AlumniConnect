/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.dtos.AlumniDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.Alumni;
import com.cmc.pojo.User;
import com.cmc.repository.AlumniRepository;
import com.cmc.repository.UserRepository;
import com.cmc.service.AlumniService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MailServicesImpl mailServices;

    @Override
    public boolean existsByStudentId(String studentCode) {
        return alumniRepository.checkStudentCode(studentCode);
    }

    @Override
    public List<AlumniDTO> getPendingAlumniRegistrations() {
        return alumniRepository.getPendingAlumniRegistrations();
    }

    @Override
    public void registerAlumni(AlumniDTO alumniDTO) {
        Alumni alumni = modelMapper.map(alumniDTO, Alumni.class);
        if (alumniRepository.checkStudentCode(alumni.getStudentCode())) {
            throw new IllegalArgumentException("Mã sinh viên đã tồn tại!");
        }
        UserDTO u = alumniDTO.getUser();
        if (userRepository.existsByUsername(u.getUsername())) {
            throw new RuntimeException("Tài khoản đã tồn tại!");
        }
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        u.setRole("ALUMNI");
        u.setActive(Boolean.FALSE);
        User user = modelMapper.map(u, User.class);
        userRepository.saveOrUpdate(user);

        alumniRepository.registerAlumni(new Alumni(alumniDTO.getStudentCode(), user));
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
