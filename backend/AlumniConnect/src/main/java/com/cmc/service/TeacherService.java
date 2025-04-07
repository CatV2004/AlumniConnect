/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.TeacherDTO;
import com.cmc.pojo.Teacher;
import java.util.List;
import java.util.Map;

/**
 *
 * @author FPTSHOP
 */
public interface TeacherService {

    boolean createTeacherAccount(TeacherDTO teacherDTO);

    boolean isPasswordChangeDeadlineExceeded(String username);

    void resetPasswordChangeDeadline(Long id);

    List<Teacher> getTeachers(Map<String, String> params);

    Long countTeachers();
}
