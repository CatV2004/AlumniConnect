/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.pojo.InvitationPost;
import com.cmc.pojo.Post;
import com.cmc.repository.InvitationPostRepository;
import com.cmc.repository.UgroupRepository;
import com.cmc.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 *
 * @author FPTSHOP
 */
public interface InvitationService {

    void createInvitation(Post post, String eventName, LocalDateTime eventTime, Set<Long> groupIds, Set<Long> userIds, boolean sendToAll);

    InvitationPost findById(Long id);

    List<InvitationPost> findByInvitedUserId(Long userId);
    
    List<InvitationPost> findByGroupId(Long groupId);
}
