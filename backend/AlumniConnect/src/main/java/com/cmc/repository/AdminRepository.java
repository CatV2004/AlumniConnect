/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

/**
 *
 * @author FPTSHOP
 */
public interface AdminRepository {
    void confirmAlumniRegistration(String username);
    void resetTeacherPasswordDeadline(String username);
}
