/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 *
 * @author FPTSHOP
 */
public class ChangePasswordDTO {
    @NotBlank(message = "Bắt Buộc nhập password cũ")
    private String oldPassword;
    @NotBlank(message = "Password mới là bắt buộc")
    @Size(min = 5,max=50, message = "Password phải ít nhất 6 ký tự")
    @Pattern(regexp = ".*[!@#$%^&*()].*", message = "Password phải có ký tự đặc biệt")
    private String newPassword;

    public ChangePasswordDTO() {
    }
    
    public ChangePasswordDTO(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    
}
