/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.InvitationRequestDTO;
import com.cmc.dtos.InvitationResponseDTO;
import com.cmc.pojo.InvitationPost;
import com.cmc.pojo.Post;
import com.cmc.repository.InvitationPostRepository;
import com.cmc.repository.UgroupRepository;
import com.cmc.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author FPTSHOP
 */
public interface InvitationService {

    void createInvitation(InvitationRequestDTO invitationRequestDTO);

    InvitationResponseDTO findById(Long id);

    List<InvitationPost> findByInvitedUserId(Long userId);
    
    List<InvitationPost> findByGroupId(Long groupId);
    
    List<InvitationPost> findInvitationPosts(Map<String, String> params);

    long countInvitationPosts(Map<String, String> params);

}
