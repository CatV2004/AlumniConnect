/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.dtos;

import com.cmc.pojo.SurveyStatus;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public class InvitationDTO {
    private String eventName;
    private LocalDateTime eventTime;
    private List<Long> invitedUserIds;

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }
    
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public List<Long> getInvitedUserIds() {
        return invitedUserIds;
    }

    public void setInvitedUserIds(List<Long> invitedUserIds) {
        this.invitedUserIds = invitedUserIds;
    }

    
}
