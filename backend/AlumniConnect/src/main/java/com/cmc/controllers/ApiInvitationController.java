/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.components.ResourceNotFoundException;
import com.cmc.dtos.InvitationRequestDTO;
import com.cmc.dtos.InvitationResponseDTO;
import com.cmc.pojo.InvitationPost;
import com.cmc.pojo.Post;
import com.cmc.pojo.Ugroup;
import com.cmc.pojo.User;
import com.cmc.service.InvitationService;
import com.cmc.service.PostService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

/**
 *
 * @author FPTSHOP
 */
@RestController
@RequestMapping("/api/invitations")
public class ApiInvitationController {

    @Autowired
    private InvitationService invitationService;
    @Autowired
    private PostService postService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<InvitationResponseDTO> createInvitation(
            @Valid @RequestBody InvitationRequestDTO invitationRequest,
            Principal principal) {

        Post post = new Post();
        post.setContent(invitationRequest.getContent());
        post.setCreatedDate(LocalDateTime.now());
        post.setActive(true);

        Post savedPost = postService.addOrUpdatePost(post, null, null, true);

        invitationService.createInvitation(
                savedPost,
                invitationRequest.getEventName(),
                invitationRequest.getEventTime(),
                invitationRequest.getGroupIds(),
                invitationRequest.getUserIds(),
                invitationRequest.isSendToAll()
        );

        InvitationResponseDTO response = modelMapper.map(savedPost, InvitationResponseDTO.class);
        response.setPostId(savedPost.getId());
        response.setEventName(invitationRequest.getEventName());
        response.setEventTime(invitationRequest.getEventTime());
        response.setInvitedGroupIds(invitationRequest.getGroupIds());
        response.setInvitedUserIds(invitationRequest.getUserIds());
        response.setSentToAll(invitationRequest.isSendToAll());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvitationResponseDTO> getInvitationDetails(@PathVariable Long id) {
        InvitationPost invitation = invitationService.findById(id);
        InvitationResponseDTO response = modelMapper.map(invitation, InvitationResponseDTO.class);
        response.setPostId(invitation.getId());
        response.setEventName(invitation.getEventName());

        if (invitation.getUgroupSet() != null) {
            response.setInvitedGroupIds(invitation.getUgroupSet().stream()
                    .map(Ugroup::getId)
                    .collect(Collectors.toSet()));
        }

        if (invitation.getUserSet() != null) {
            response.setInvitedUserIds(invitation.getUserSet().stream()
                    .map(User::getId)
                    .collect(Collectors.toSet()));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<InvitationResponseDTO>> getInvitationsForUser(
            @PathVariable Long userId,
            @RequestParam(required = false) Boolean upcoming) {

        List<InvitationPost> invitations = invitationService.findByInvitedUserId(userId);

        if (upcoming != null && upcoming) {
            invitations = invitations.stream()
                    .filter(inv -> inv.getPost().getCreatedDate().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
        }

        List<InvitationResponseDTO> responses = invitations.stream()
                .map(inv -> {
                    InvitationResponseDTO dto = modelMapper.map(inv, InvitationResponseDTO.class);
                    dto.setPostId(inv.getId());
                    dto.setEventName(inv.getEventName());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<InvitationResponseDTO>> getInvitationsForGroup(
            @PathVariable Long groupId,
            @RequestParam(required = false) Boolean upcoming) {

        List<InvitationPost> invitations = invitationService.findByGroupId(groupId);

        if (upcoming != null && upcoming) {
            invitations = invitations.stream()
                    .filter(inv -> inv.getPost().getCreatedDate().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
        }

        List<InvitationResponseDTO> responses = invitations.stream()
                .map(inv -> {
                    InvitationResponseDTO dto = modelMapper.map(inv, InvitationResponseDTO.class);
                    dto.setPostId(inv.getId());
                    dto.setEventName(inv.getEventName());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
