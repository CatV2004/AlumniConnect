/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.dtos.AlumniDTO;
import com.cmc.dtos.UserDTO;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public interface AlumniRepository {
    void registerAlumni(AlumniDTO alumniDTO);
    boolean existsByStudentId(String studentId);
    List<AlumniDTO> getPendingAlumniRegistrations();
    void approveAlumni(String username);
}
