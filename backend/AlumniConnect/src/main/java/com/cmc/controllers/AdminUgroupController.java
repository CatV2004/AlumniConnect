/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.dtos.GroupDTO;
import com.cmc.dtos.UgroupDetailDTO;
import com.cmc.dtos.UserSimpleDTO;
import com.cmc.pojo.Ugroup;
import com.cmc.pojo.User;
import com.cmc.service.UgroupService;
import com.cmc.service.UserService;
import com.cmc.validator.GroupValidator;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author FPTSHOP
 */
@Controller
@RequestMapping("/admin/groups")
public class AdminUgroupController {

    @Autowired
    private UgroupService ugroupService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;

    @Autowired
    private GroupValidator groupValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(groupValidator);
    }

    @PostMapping("")
    public ResponseEntity<?> createGroup(@Valid @RequestBody GroupDTO ugroupDTO, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("\n")); 
            Map<String, String> error = new  HashMap<>();
            error.put("message", errorMessage);

            return ResponseEntity.badRequest().body(error);
        }
        Ugroup ugroup = modelMapper.map(ugroupDTO, Ugroup.class);
        Ugroup savedUgroup = ugroupService.save(ugroup);
        GroupDTO response = modelMapper.map(savedUgroup, GroupDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("")
    public String groupsView(@RequestParam Map<String, String> params, Model model) {
        params.putIfAbsent("page", "1");
        params.putIfAbsent("size", "5");

        int page = Integer.parseInt(params.get("page"));
        int size = Integer.parseInt(params.get("size"));

        List<GroupDTO> groups = ugroupService.findGroups(params);

        long totalItems = ugroupService.countGroups(params);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("groups", groups);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("size", size);

        return "admin_group_management";
    }

    @GetMapping("/{id}")
    public ResponseEntity<UgroupDetailDTO> getGroupById(@PathVariable Long id) {
        Ugroup ugroup = ugroupService.findById(id);
        UgroupDetailDTO response = modelMapper.map(ugroup, UgroupDetailDTO.class);

        List<User> members = userService.findUsersInGroup(id);
        response.setMembers(members.stream()
                .map(user -> modelMapper.map(user, UserSimpleDTO.class))
                .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{groupId}/available-users")
    public ResponseEntity<List<User>> getUsersNotInGroup(@PathVariable Long groupId) {
        List<User> users = userService.findUsersNotInGroup(groupId);
        users.forEach(u -> {
            System.out.println("");
        });
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{groupId}/activate")
    @ResponseBody
    public ResponseEntity<?> activateGroup(@PathVariable Long groupId) {
        return updateGroupStatus(groupId, true);
    }

    @PostMapping("/{groupId}/deactivate")
    @ResponseBody
    public ResponseEntity<?> deactivateGroup(@PathVariable Long groupId) {
        return updateGroupStatus(groupId, false);
    }

    private ResponseEntity<?> updateGroupStatus(Long groupId, boolean isActive) {
        Ugroup group = ugroupService.findById(groupId);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Group not found");
        }

        group.setActive(isActive);
        ugroupService.save(group);

        Map<String, Object> response = new HashMap<>();
        response.put("message", isActive ? "Nhóm đã được kích hoạt" : "Nhóm đã bị vô hiệu hóa");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupDTO> updateGroup(
            @PathVariable Long id,
            @Valid @RequestBody GroupDTO ugroupDTO) {

        Ugroup existingGroup = ugroupService.findById(id);
        modelMapper.map(ugroupDTO, existingGroup);
        existingGroup.setUpdatedDate(LocalDateTime.now());

        Ugroup updatedGroup = ugroupService.save(existingGroup);
        GroupDTO response = modelMapper.map(updatedGroup, GroupDTO.class);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<Void> addMembersToGroup(
            @PathVariable Long groupId,
            @RequestBody List<Long> userIds) {

        ugroupService.addUsersToGroup(groupId, userIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Void> removeMemberFromGroup(
            @PathVariable Long groupId,
            @PathVariable Long userId) {

        ugroupService.removeUserFromGroup(groupId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        ugroupService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
