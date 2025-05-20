/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.dtos.UgroupDTO;
import com.cmc.dtos.UgroupDetailDTO;
import com.cmc.dtos.UserSimpleDTO;
import com.cmc.pojo.Ugroup;
import com.cmc.pojo.User;
import com.cmc.repository.UgroupRepository;
import com.cmc.repository.UserRepository;
import com.cmc.service.UgroupService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author FPTSHOP
 */
@RestController
@RequestMapping("/api/groups")
public class ApiUgroupController {

    @Autowired
    private UgroupService ugroupService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<UgroupDTO> createGroup(@Valid @RequestBody UgroupDTO ugroupDTO) {
        Ugroup ugroup = modelMapper.map(ugroupDTO, Ugroup.class);
        ugroup.setCreatedDate(LocalDateTime.now());
        ugroup.setActive(true);

        Ugroup savedUgroup = ugroupService.save(ugroup);
        UgroupDTO response = modelMapper.map(savedUgroup, UgroupDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Lấy tất cả nhóm
    @GetMapping
    public ResponseEntity<List<UgroupDTO>> getAllGroups(
            @RequestParam(required = false) Boolean active) {

        List<Ugroup> groups;
        if (active != null) {
            groups = ugroupService.findAllActiveGroups();
        } else {
            groups = ugroupService.findAll();
        }

        List<UgroupDTO> response = groups.stream()
                .map(group -> modelMapper.map(group, UgroupDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UgroupDetailDTO> getGroupById(@PathVariable Long id) {
        Ugroup ugroup = ugroupService.findById(id);
        UgroupDetailDTO response = modelMapper.map(ugroup, UgroupDetailDTO.class);

        // Lấy danh sách thành viên trong nhóm
        List<User> members = userRepository.findUsersInGroup(id);
        response.setMembers(members.stream()
                .map(user -> modelMapper.map(user, UserSimpleDTO.class))
                .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UgroupDTO> updateGroup(
            @PathVariable Long id,
            @Valid @RequestBody UgroupDTO ugroupDTO) {

        Ugroup existingGroup = ugroupService.findById(id);
        modelMapper.map(ugroupDTO, existingGroup);
        existingGroup.setUpdatedDate(LocalDateTime.now());

        Ugroup updatedGroup = ugroupService.save(existingGroup);
        UgroupDTO response = modelMapper.map(updatedGroup, UgroupDTO.class);

        return ResponseEntity.ok(response);
    }

    // Thêm thành viên vào nhóm
    @PostMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Void> addMemberToGroup(
            @PathVariable Long groupId,
            @PathVariable Long userId) {

        ugroupService.addUserToGroup(groupId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Void> removeMemberFromGroup(
            @PathVariable Long groupId,
            @PathVariable Long userId) {

        ugroupService.removeUserFromGroup(groupId, userId);
        return ResponseEntity.noContent().build();
    }

    // Xóa nhóm (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        Ugroup ugroup = ugroupService.findById(id);
        ugroup.setActive(false);
        ugroup.setDeletedDate(LocalDateTime.now());
        ugroupService.save(ugroup);

        return ResponseEntity.noContent().build();
    }
}
