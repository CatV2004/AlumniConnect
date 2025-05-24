/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.dtos;

import java.io.Serializable;

/**
 *
 * @author FPTSHOP
 */
public class NotificationDTO implements Serializable {

    private Long id;
    private String message;
    private String link;
    private String senderName;
    private String senderAvatar;
    private String createdAt;
    private boolean read;
    private String type;

    public NotificationDTO() {
    }

    public NotificationDTO(Long id, String message, String link, String senderName, String senderAvatar, String createdAt, boolean read, String type) {
        this.id = id;
        this.message = message;
        this.link = link;
        this.senderName = senderName;
        this.senderAvatar = senderAvatar;
        this.createdAt = createdAt;
        this.read = read;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
