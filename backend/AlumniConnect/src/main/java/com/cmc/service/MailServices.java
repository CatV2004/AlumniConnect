/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

/**
 *
 * @author FPTSHOP
 */
public interface MailServices {

    void notifyAlumniOnApproval(String alumniEmail, String alumniName);

    void notifyTeacherAccountCreation(String teacherEmail, String teacherName, String username, String defaultPassword);
}
