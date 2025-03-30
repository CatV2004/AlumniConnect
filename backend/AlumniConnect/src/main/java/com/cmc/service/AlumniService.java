/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.AlumniDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.Alumni;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public interface AlumniService {

    void registerAlumni(AlumniDTO alumni);

    boolean existsByStudentId(String studentCode);

    List<AlumniDTO> getPendingAlumniRegistrations();

    List<Alumni> getAlumnis();

    void approveAlumni(Long id);

}
