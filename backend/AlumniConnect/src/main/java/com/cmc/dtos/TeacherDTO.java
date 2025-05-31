/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.dtos;

import com.cmc.pojo.User;
import java.time.LocalDateTime;

/**
 *
 * @author FPTSHOP
 */
public class TeacherDTO {
    private Long id;
    private Boolean mustChangePassword;
    private LocalDateTime passwordResetTime;
    private TeacherRequestDTO userId;

    public TeacherDTO() {
    }

    public TeacherDTO(Boolean mustChangePassword, LocalDateTime passwordResetTime, TeacherRequestDTO user) {
        this.mustChangePassword = mustChangePassword;
        this.passwordResetTime = passwordResetTime;
        this.userId = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getMustChangePassword() {
        return mustChangePassword;
    }

    public void setMustChangePassword(Boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    public LocalDateTime getPasswordResetTime() {
        return passwordResetTime;
    }

    public void setPasswordResetTime(LocalDateTime passwordResetTime) {
        this.passwordResetTime = passwordResetTime;
    }

    public TeacherRequestDTO getUser() {
        return userId;
    }

    public void setUser(TeacherRequestDTO user) {
        this.userId = user;
    }
}