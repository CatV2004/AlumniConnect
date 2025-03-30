/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.dtos.AlumniDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.Alumni;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public interface AlumniRepository {
    void registerAlumni(Alumni alumni);
    List<AlumniDTO> getPendingAlumniRegistrations();
    void approveAlumni(Long id);
    Alumni findByStudentCode(String studentCode);
    boolean checkStudentCode(String studentId);
    List<Alumni> getAlumnis();
}
