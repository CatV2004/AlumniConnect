/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.dtos;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public class PostResponseDTO {
    private Long id;
    private String content;
    private Boolean lockComment;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean active;

    private UserDTO userId;

    private List<String> postImages;

    private InvitationDTO invitationPost;

    private SurveyDTO surveyPost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getLockComment() {
        return lockComment;
    }

    public void setLockComment(Boolean lockComment) {
        this.lockComment = lockComment;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public UserDTO getUserId() {
        return userId;
    }

    public void setUserId(UserDTO userId) {
        this.userId = userId;
    }

    public List<String> getPostImages() {
        return postImages;
    }

    public void setPostImages(List<String> postImages) {
        this.postImages = postImages;
    }

    public InvitationDTO getInvitationPost() {
        return invitationPost;
    }

    public void setInvitationPost(InvitationDTO invitationPost) {
        this.invitationPost = invitationPost;
    }

    public SurveyDTO getSurveyPost() {
        return surveyPost;
    }

    public void setSurveyPost(SurveyDTO surveyPost) {
        this.surveyPost = surveyPost;
    }
    
    
    
    
}
