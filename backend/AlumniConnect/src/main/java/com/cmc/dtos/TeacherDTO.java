/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.dtos;

import com.cmc.pojo.User;
import java.util.Date;

/**
 *
 * @author FPTSHOP
 */
public class TeacherDTO {
    private Long id;
    private Boolean mustChangePassword;
    private Date passwordResetTime;
    private UserDTO user;

    public TeacherDTO() {
    }

    public TeacherDTO(Long id, Boolean mustChangePassword, Date passwordResetTime, UserDTO user) {
        this.id = id;
        this.mustChangePassword = mustChangePassword;
        this.passwordResetTime = passwordResetTime;
        this.user = user;
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

    public Date getPasswordResetTime() {
        return passwordResetTime;
    }

    public void setPasswordResetTime(Date passwordResetTime) {
        this.passwordResetTime = passwordResetTime;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}