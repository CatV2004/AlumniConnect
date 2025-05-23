/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.dtos.GroupDTO;
import com.cmc.dtos.InvitationRequestDTO;
import com.cmc.dtos.InvitationResponseDTO;
import com.cmc.dtos.PostResponseDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.dtos.UserSimpleDTO;
import com.cmc.pojo.InvitationPost;
import com.cmc.pojo.Post;
import com.cmc.pojo.Ugroup;
import com.cmc.pojo.User;
import com.cmc.repository.InvitationPostRepository;
import com.cmc.repository.PostRepository;
import com.cmc.repository.UgroupRepository;
import com.cmc.repository.UserRepository;
import com.cmc.service.InvitationService;
import com.cmc.service.MailServices;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
@Transactional
public class InvitationServiceImpl implements InvitationService {

    @Autowired
    private MailServices mailService;

    @Autowired
    private UgroupRepository ugroupRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private InvitationPostRepository invitationPostRepo;

    @Override
    public void createInvitation(InvitationRequestDTO invitationRequestDTO) {
        try {
            if (invitationRequestDTO.getContent() == null
                    || invitationRequestDTO.getEventName() == null
                    || invitationRequestDTO.getEventTime() == null) {
                throw new IllegalArgumentException("Post, event name and event time cannot be null");
            }

            System.out.println("✅ Content: " + invitationRequestDTO.getContent());
            System.out.println("✅ Username: " + (invitationRequestDTO.getUser() != null ? invitationRequestDTO.getUser().getUsername() : "null"));
            System.out.println("✅ Event Name: " + invitationRequestDTO.getEventName());
            System.out.println("✅ Event Time: " + invitationRequestDTO.getEventTime());
            System.out.println("✅ Target Users: " + invitationRequestDTO.getUserIds());
            System.out.println("✅ Target Groups: " + invitationRequestDTO.getGroupIds());

            Post post = new Post();
            post.setContent(invitationRequestDTO.getContent());
            post.setLockComment(invitationRequestDTO.getLockComment());
            post.setUserId(invitationRequestDTO.getUser());

            Post savedPost = postRepo.addPost(post);

            InvitationPost invitation = new InvitationPost();
            invitation.setEventName(invitationRequestDTO.getEventName());
            invitation.setEventTime(invitationRequestDTO.getEventTime());
            invitation.setPost(savedPost);
            savedPost.setInvitationPost(invitation);

            if (invitationRequestDTO.getGroupIds() != null && !invitationRequestDTO.getGroupIds().isEmpty()) {
                Set<Ugroup> groups = new HashSet<>();
                for (Long groupId : invitationRequestDTO.getGroupIds()) {
                    System.out.println("👉 Fetching group by ID: " + groupId);
                    Ugroup group = ugroupRepo.findById(groupId);
                    if (group == null) {
                        throw new EntityNotFoundException("Group not found with id: " + groupId);
                    }
                    groups.add(group);
                }
                invitation.setUgroupSet(groups);
            }

            if (invitationRequestDTO.getUserIds() != null && !invitationRequestDTO.getUserIds().isEmpty()) {
                Set<User> users = new HashSet<>();
                for (Long userId : invitationRequestDTO.getUserIds()) {
                    User user = userRepo.getUserById(userId);
                    if (user == null) {
                        throw new EntityNotFoundException("User not found with id: " + userId);
                    }
                    users.add(user);
                }
                invitation.setUserSet(users);
            }
            invitationPostRepo.save(invitation);

            sendInvitationEmails(invitation, invitationRequestDTO.getEventTime());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void sendInvitationEmails(InvitationPost invitation, LocalDateTime eventTime) {
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

        recipients.stream()
                .filter(user -> user.getEmail() != null && !user.getEmail().isEmpty())
                .forEach(user -> {
                    mailService.sendInvitationEmail(
                            user.getEmail(),
                            user.getFirstName() + " " + user.getLastName(),
                            invitation.getEventName(),
                            invitation.getPost().getContent(),
                            eventTime
                    );
                });
    }

    @Override
    public InvitationResponseDTO findById(Long id) {
        InvitationPost invitationPost = this.invitationPostRepo.findById(id);
        InvitationResponseDTO responseDTO = new InvitationResponseDTO();

        responseDTO.setPostId(invitationPost.getId());
        responseDTO.setEventName(invitationPost.getEventName());
        responseDTO.setEventTime(invitationPost.getEventTime());

        if (invitationPost.getPost() != null) {
            PostResponseDTO postDTO = new PostResponseDTO();
            postDTO.setId(invitationPost.getPost().getId());
            postDTO.setContent(invitationPost.getPost().getContent());
            postDTO.setLockComment(invitationPost.getPost().getLockComment());
            postDTO.setCreatedDate(invitationPost.getPost().getCreatedDate());
            postDTO.setUpdatedDate(invitationPost.getPost().getUpdatedDate());
            postDTO.setDeletedDate(invitationPost.getPost().getDeletedDate());
            postDTO.setActive(invitationPost.getPost().getActive());

            if (invitationPost.getPost().getUserId() != null) {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(invitationPost.getPost().getUserId().getId());
                userDTO.setUsername(invitationPost.getPost().getUserId().getUsername());
                userDTO.setEmail(invitationPost.getPost().getUserId().getEmail());
                userDTO.setFirstName(invitationPost.getPost().getUserId().getFirstName());
                userDTO.setLastName(invitationPost.getPost().getUserId().getLastName());
                userDTO.setAvatar(invitationPost.getPost().getUserId().getAvatar());
                postDTO.setUserId(userDTO);
            }

            responseDTO.setPost(postDTO);
            responseDTO.setContent(invitationPost.getPost().getContent());
            responseDTO.setLockComment(invitationPost.getPost().getLockComment());
        }

        if (invitationPost.getUgroupSet() != null) {
            Set<Long> groupIds = invitationPost.getUgroupSet().stream()
                    .map(Ugroup::getId)
                    .collect(Collectors.toSet());
            responseDTO.setInvitedGroupIds(groupIds);

            Set<GroupDTO> groupDTOs = invitationPost.getUgroupSet().stream()
                    .map(group -> {
                        GroupDTO dto = new GroupDTO();
                        dto.setId(group.getId());
                        dto.setGroupName(group.getGroupName());
                        dto.setCreatedDate(group.getCreatedDate() != null ? group.getCreatedDate().toString() : null);
                        dto.setUpdatedDate(group.getUpdatedDate() != null ? group.getUpdatedDate().toString() : null);
                        dto.setActive(group.getActive());
                        if (group.getUserSet() != null) {
                            dto.setMemberCount(group.getUserSet().size());
                        }
                        return dto;
                    })
                    .collect(Collectors.toSet());
            responseDTO.setUgroupSet(groupDTOs);
        }

        if (invitationPost.getUserSet() != null) {
            Set<Long> userIds = invitationPost.getUserSet().stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());
            responseDTO.setInvitedUserIds(userIds);

            Set<UserSimpleDTO> userDTOs = invitationPost.getUserSet().stream()
                    .map(user -> {
                        UserSimpleDTO dto = new UserSimpleDTO();
                        dto.setId(user.getId());
                        dto.setUsername(user.getUsername());
                        dto.setEmail(user.getEmail());
                        dto.setFirstName(user.getFirstName());
                        dto.setLastName(user.getLastName());
                        dto.setAvatar(user.getAvatar());
                        return dto;
                    })
                    .collect(Collectors.toSet());
            responseDTO.setUserSet(userDTOs);
        }

        return responseDTO;
    }

    @Override
    public List<InvitationPost> findByInvitedUserId(Long userId) {
        return this.invitationPostRepo.findByInvitedUserId(userId);
    }

    @Override
    public List<InvitationPost> findByGroupId(Long groupId) {
        return this.invitationPostRepo.findByGroupId(groupId);
    }

    @Override
    public List<InvitationPost> findInvitationPosts(Map<String, String> params) {
        return this.invitationPostRepo.findInvitationPosts(params);
    }

    @Override
    public long countInvitationPosts(Map<String, String> params) {
        return this.invitationPostRepo.countInvitationPosts(params);
    }
}
