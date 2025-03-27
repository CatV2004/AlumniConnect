/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.TeacherDTO;

/**
 *
 * @author FPTSHOP
 */
public interface TeacherService {
    void createTeacherAccount(TeacherDTO teacherDTO);
    boolean isPasswordChangeDeadlineExceeded(String username);
    void resetPasswordChangeDeadline(String username);
}