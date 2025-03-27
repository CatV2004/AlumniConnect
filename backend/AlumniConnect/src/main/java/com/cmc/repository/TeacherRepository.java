/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.dtos.TeacherDTO;
import com.cmc.dtos.UserDTO;

/**
 *
 * @author FPTSHOP
 */
public interface TeacherRepository {
    void createTeacherAccount(TeacherDTO teacherDTO);
    boolean isPasswordChangeDeadlineExceeded(String username);
    void resetPasswordChangeDeadline(String username);
}
