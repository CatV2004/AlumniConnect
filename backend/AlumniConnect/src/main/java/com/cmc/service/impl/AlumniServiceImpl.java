/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.dtos.AlumniDTO;
import com.cmc.repository.AlumniRepository;
import com.cmc.service.AlumniService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author FPTSHOP
 */
@Service
public class AlumniServiceImpl implements AlumniService {

    @Autowired
    private AlumniRepository alumniRepository;

    @Override
    public void registerAlumni(AlumniDTO alumniDTO) {
        alumniRepository.registerAlumni(alumniDTO);
    }

    @Override
    public boolean existsByStudentId(String studentCode) {
        return alumniRepository.existsByStudentId(studentCode);
    }

    @Override
    public List<AlumniDTO> getPendingAlumniRegistrations() {
        return alumniRepository.getPendingAlumniRegistrations();
    }

    @Override
    public void approveAlumni(String username) {
        alumniRepository.approveAlumni(username);
    }
}
