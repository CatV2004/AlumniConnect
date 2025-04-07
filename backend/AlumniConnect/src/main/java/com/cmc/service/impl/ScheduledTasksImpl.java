/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.pojo.Teacher;
import com.cmc.pojo.User;
import com.cmc.repository.TeacherRepository;
import com.cmc.repository.UserRepository;
import com.cmc.service.ScheduledTasks;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author FPTSHOP
 */
@Service
public class ScheduledTasksImpl implements ScheduledTasks {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Ho_Chi_Minh")
    public void updateTeacherAccounts() {
        List<Teacher> teachers = teacherRepository.getTeachers(null);

        for (Teacher teacher : teachers) {
            if (teacher.getPasswordResetTime() != null) {
                LocalDateTime now = LocalDateTime.now();

                if (teacher.getPasswordResetTime().isEqual(now)) {
                    System.out.println("Reset time reached for teacher: " + teacher.getUserId().getUsername());  
                    User user = teacher.getUserId();
                    if (user != null) {
                        user.setActive(Boolean.FALSE);
                        teacher.setMustChangePassword(Boolean.FALSE);

                        userRepository.saveOrUpdate(user);
                        teacherRepository.saveOrUpdate(teacher);

                    }
                }
            }
        }
    }
}
