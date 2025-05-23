/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.dtos;

import java.time.LocalDateTime;
import java.util.Set;

/**
 *
 * @author FPTSHOP
 */
public class InvitationResponseDTO {

    private Long postId;
    private PostResponseDTO post;
    private String eventName;
    private LocalDateTime eventTime;
    private String content;
    private LocalDateTime createdDate;
    private Set<Long> invitedGroupIds;
    private Set<Long> invitedUserIds;
    private Set<GroupDTO> ugroupSet; 
    private Set<UserSimpleDTO> userSet;
    private Boolean lockComment;
    private boolean sentToAll;
    
    

    public PostResponseDTO getPost() {
        return post;
    }

    public void setPost(PostResponseDTO post) {
        this.post = post;
    }

    public Set<GroupDTO> getUgroupSet() {
        return ugroupSet;
    }

    public void setUgroupSet(Set<GroupDTO> ugroupSet) {
        this.ugroupSet = ugroupSet;
    }

    public Set<UserSimpleDTO> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<UserSimpleDTO> userSet) {
        this.userSet = userSet;
    }

    public Boolean getLockComment() {
        return lockComment;
    }

    public void setLockComment(Boolean lockComment) {
        this.lockComment = lockComment;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Set<Long> getInvitedGroupIds() {
        return invitedGroupIds;
    }

    public void setInvitedGroupIds(Set<Long> invitedGroupIds) {
        this.invitedGroupIds = invitedGroupIds;
    }

    public Set<Long> getInvitedUserIds() {
        return invitedUserIds;
    }

    public void setInvitedUserIds(Set<Long> invitedUserIds) {
        this.invitedUserIds = invitedUserIds;
    }

    public boolean isSentToAll() {
        return sentToAll;
    }

    public void setSentToAll(boolean sentToAll) {
        this.sentToAll = sentToAll;
    }
}
