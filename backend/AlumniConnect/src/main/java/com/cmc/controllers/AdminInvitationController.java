/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.dtos.GroupDTO;
import com.cmc.dtos.InvitationRequestDTO;
import com.cmc.dtos.InvitationResponseDTO;
import com.cmc.dtos.ResponseDTO;
import com.cmc.pojo.InvitationPost;
import com.cmc.pojo.Ugroup;
import com.cmc.pojo.User;
import com.cmc.repository.UserRepository;
import com.cmc.service.InvitationService;
import com.cmc.service.UgroupService;
import com.cmc.service.UserService;
import com.cmc.validator.InvitationValidator;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *
 * @author FPTSHOP
 */
@Controller
@RequestMapping("/admin/invitations")
@CrossOrigin
public class AdminInvitationController {

    @Autowired
    private InvitationService invitationService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UgroupService groupSerice;
    @Autowired
    private UserService userService;
    @Autowired
    private InvitationValidator invitationValidator;

    @GetMapping("")
    public String invitationsView(@RequestParam Map<String, String> params, Model model) {
        params.putIfAbsent("page", "1");
        params.putIfAbsent("size", "5");

        int page = Integer.parseInt(params.get("page"));
        int size = Integer.parseInt(params.get("size"));

        List<InvitationPost> invitations = invitationService.findInvitationPosts(params);
        long totalItems = invitationService.countInvitationPosts(params);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        List<User> allUsers = userService.getUsers();

        List<GroupDTO> allGroups = groupSerice.findGroups();
        
        allGroups.forEach(g -> System.out.println("groupname: " + g.getGroupName()));

        model.addAttribute("invitations", invitations);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("size", size);
        model.addAttribute("allUsers", allUsers); 
        model.addAttribute("allGroups", allGroups);
                

        return "admin_invitation_management";
    }

    @PostMapping
    public ResponseEntity<?> createInvitation(
            @Valid @RequestBody InvitationRequestDTO invitationRequest,
            BindingResult result,
            Principal principal) {
        
        this.invitationValidator.validate(invitationRequest, result);
        if (result.hasErrors()){
            String errors = result.getFieldErrors().stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.joining("; \n"));

            Map<String, String> response = new HashMap<>();
            response.put("message", errors);
            return ResponseEntity.badRequest().body(response);
        }

        String username = principal.getName();
        User user = userRepo.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDTO.failure(401, "Không xác định được người dùng đăng nhập."));
        }
        try {
            invitationRequest.setUser(user);

            invitationService.createInvitation(invitationRequest);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ResponseDTO.success("Tạo lời mời thành công"));

        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.failure(400, e.getMessage()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.failure(400, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.failure(500, "Tạo lời mời thất bại. Vui lòng thử lại sau."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvitationResponseDTO> getInvitationDetails(@PathVariable Long id) {
        InvitationResponseDTO response = invitationService.findById(id);
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
}
