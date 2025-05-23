/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.components;

import com.cmc.service.TeacherService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author PHAT
 */
@Component
public class TeacherComponent {
    
    @Autowired
    private TeacherService teacherService;
    
    @Scheduled(fixedRate = 5*60)
    public void autoLockUnchangedPasswords() {
        LocalDateTime deadline = LocalDateTime.now().minusHours(24);
        teacherService.lockExpiredAccounts(deadline);
    }
}
