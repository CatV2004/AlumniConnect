/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.dtos.TeacherDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.Teacher;
import com.cmc.pojo.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 *
 * @author FPTSHOP
 */
public interface TeacherRepository {

    boolean createTeacherAccount(TeacherDTO teacherDTO);

    boolean isPasswordChangeDeadlineExceeded(String username);

    boolean resetPasswordChangeDeadline(Long id);

    List<Teacher> getTeachers(Map<String, String> params);

    Long countTeachers();
    
    void saveOrUpdate(Teacher teacher);
    
    Teacher getTeacherById(Long id);
    
    List<Teacher> findAllByMustChangePassword(LocalDateTime deadline);
}
