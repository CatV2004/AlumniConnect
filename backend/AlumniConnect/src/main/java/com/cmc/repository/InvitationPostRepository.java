/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.pojo.InvitationPost;
import java.util.List;
import java.util.Map;

/**
 *
 * @author FPTSHOP
 */
public interface InvitationPostRepository {

    InvitationPost findById(Long id);

    void save(InvitationPost invitationPost);
    
    List<InvitationPost> findByEventName(String eventName);
    
    List<InvitationPost> findByGroupId(Long groupId);
    
    List<InvitationPost> findByInvitedUserId(Long userId);
    
    void delete(Long id);
    
    List<InvitationPost> findInvitationPosts(Map<String, String> params);

    long countInvitationPosts(Map<String, String> params);
    
}
