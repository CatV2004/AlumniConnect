/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.pojo.InvitationPost;
import com.cmc.pojo.Post;
import com.cmc.pojo.Ugroup;
import com.cmc.pojo.User;
import com.cmc.repository.InvitationPostRepository;
import com.cmc.repository.UgroupRepository;
import com.cmc.repository.UserRepository;
import com.cmc.service.InvitationService;
import com.cmc.service.MailServices;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author FPTSHOP
 */
@Service
public class InvitationServiceImpl implements InvitationService {

    @Autowired
    private MailServices mailService;

    @Autowired
    private UgroupRepository ugroupRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private InvitationPostRepository invitationPostRepo;

    @Transactional
    @Override
    public void createInvitation(Post post, String eventName, LocalDateTime eventTime, Set<Long> groupIds, Set<Long> userIds, boolean sendToAll) {
        if (post == null || eventName == null || eventTime == null) {
            throw new IllegalArgumentException("Post, event name and event time cannot be null");
        }

        InvitationPost invitation = new InvitationPost();
        invitation.setId(post.getId());
        invitation.setEventName(eventName);
        invitation.setPost(post);

        if (groupIds != null && !groupIds.isEmpty()) {
            Set<Ugroup> groups = new HashSet<>();
            for (Long groupId : groupIds) {
                Ugroup group = ugroupRepo.findById(groupId);
                if (group == null) {
                    throw new EntityNotFoundException("Group not found with id: " + groupId);
                }
                groups.add(group);
            }
            invitation.setUgroupSet(groups);
        }

        if (userIds != null && !userIds.isEmpty()) {
            Set<User> users = new HashSet<>();
            for (Long userId : userIds) {
                User user = userRepo.getUserById(userId);
                if (user == null) {
                    throw new EntityNotFoundException("User not found with id: " + userId);
                }
                users.add(user);
            }
            invitation.setUserSet(users);
        }

        invitationPostRepo.save(invitation);

        sendInvitationEmails(invitation, eventTime, sendToAll);
    }

    private void sendInvitationEmails(InvitationPost invitation, LocalDateTime eventTime, boolean sendToAll) {
        Set<User> recipients = new HashSet<>();

        if (invitation.getUgroupSet() != null) {
            for (Ugroup group : invitation.getUgroupSet()) {
                List<User> groupUsers = userRepo.findUsersInGroup(group.getId());
                if (groupUsers != null) {
                    recipients.addAll(groupUsers);
                }
            }
        }

        if (invitation.getUserSet() != null) {
            recipients.addAll(invitation.getUserSet());
        }

        if (sendToAll) {
            List<User> allActiveUsers = userRepo.findAllActiveUsers();
            if (allActiveUsers != null) {
                recipients.addAll(allActiveUsers);
            }
        }

        recipients.stream()
                .filter(user -> user.getEmail() != null && !user.getEmail().isEmpty())
                .forEach(user -> {
                    mailService.sendInvitationEmail(
                            user.getEmail(),
                            invitation.getEventName(),
                            invitation.getPost().getContent(),
                            eventTime
                    );
                });
    }

    @Override
    public InvitationPost findById(Long id) {
        return this.invitationPostRepo.findById(id);
    }

    @Override
    public List<InvitationPost> findByInvitedUserId(Long userId) {
        return this.invitationPostRepo.findByInvitedUserId(userId);
    }

    @Override
    public List<InvitationPost> findByGroupId(Long groupId) {
        return this.invitationPostRepo.findByGroupId(groupId);
    }
}
