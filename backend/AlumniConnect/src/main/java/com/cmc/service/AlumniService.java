/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.AlumniDTO;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public interface AlumniService {
    void registerAlumni(AlumniDTO alumniDTO);
    boolean existsByStudentId(String studentCode);
    List<AlumniDTO> getPendingAlumniRegistrations();
    void approveAlumni(String username);
}

