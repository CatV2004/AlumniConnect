/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import java.time.LocalDateTime;

/**
 *
 * @author FPTSHOP
 */
public interface MailServices {

    void notifyAlumniOnApproval(String alumniEmail, String alumniName);

    void notifyTeacherPasswordReset(String teacherEmail, String teacherName, LocalDateTime passwordChangeDeadline);

    void notifyTeacherAccountCreation(String teacherEmail, String teacherName, String username, String defaultPassword);

    void sendInvitationEmail(String recipientName, String recientName, String eventName, String eventDetails, LocalDateTime eventTime);
}
