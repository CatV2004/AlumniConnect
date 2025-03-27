/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.dtos;

/**
 *
 * @author FPTSHOP
 */
public class AlumniDTO {
    private Long id;
    private String studentCode;
    private Boolean isVerified;
    private UserDTO user;

    public AlumniDTO() {
    }

    public AlumniDTO(Long id, String studentCode, Boolean isVerified, UserDTO user) {
        this.id = id;
        this.studentCode = studentCode;
        this.isVerified = isVerified;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
