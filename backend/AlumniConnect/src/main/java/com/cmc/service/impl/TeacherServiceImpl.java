/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.dtos.TeacherDTO;
import com.cmc.repository.TeacherRepository;
import com.cmc.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author FPTSHOP
 */
@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public void createTeacherAccount(TeacherDTO teacherDTO) {
        teacherRepository.createTeacherAccount(teacherDTO);
    }

    @Override
    public boolean isPasswordChangeDeadlineExceeded(String username) {
        return teacherRepository.isPasswordChangeDeadlineExceeded(username);
    }

    @Override
    public void resetPasswordChangeDeadline(String username) {
        teacherRepository.resetPasswordChangeDeadline(username);
    }
}

