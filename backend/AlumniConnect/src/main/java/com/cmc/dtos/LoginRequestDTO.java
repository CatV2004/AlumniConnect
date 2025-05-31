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
public class LoginRequestDTO {
    @NotBlank(message = "Username là bắt buộc")
    private String username;
    @NotBlank(message = "Password là bắt buộc")
    @Size(min = 6,max=50, message = "Password phải ít nhất 6 ký tự")
    private String password;
    @NotBlank(message = "Vai tro là bắt buộc")
    private String role;

    // Getters & Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}

