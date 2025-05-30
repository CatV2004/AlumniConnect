/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.dtos.GroupDTO;
import com.cmc.pojo.Ugroup;
import com.cmc.pojo.User;
import com.cmc.repository.UgroupRepository;
import com.cmc.repository.UserRepository;
import com.cmc.service.UgroupService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author FPTSHOP
 */
@Service
@Transactional
public class UgroupServiceImpl implements UgroupService {

    @Autowired
    private UgroupRepository ugroupRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Ugroup save(Ugroup ugroup) {
        return this.ugroupRepo.save(ugroup);
    }

    @Override
    public Ugroup findById(Long id) {
        return this.ugroupRepo.findById(id);
    }

    @Override
    public void addUserToGroup(Long groupId, Long userId) {
        if (!this.ugroupRepo.existsById(groupId)) {
            throw new EntityNotFoundException("Group not found with id: " + groupId);
        }
        if (!this.ugroupRepo.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }

        this.ugroupRepo.addUserToGroup(groupId, userId);
    }

    @Override
    public void addUsersToGroup(Long groupId, List<Long> userIds) {
        Ugroup group = ugroupRepo.findById(groupId);

        Set<User> usersToAdd = new HashSet<>();

        for (Long userId : userIds) {
            try {
                User user = userRepo.getUserById(userId);
                usersToAdd.add(user);
            } catch (EntityNotFoundException e) {
                throw new EntityNotFoundException("User not found with id: " + userId);
            }
        }

        group.getUserSet().addAll(usersToAdd);
        ugroupRepo.save(group);
    }

    @Override
    public void removeUserFromGroup(Long groupId, Long userId) {
        if (!this.ugroupRepo.existsById(groupId)) {
            throw new EntityNotFoundException("Group not found with id: " + groupId);
        }
        if (!this.ugroupRepo.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }

        this.ugroupRepo.removeUserFromGroup(groupId, userId);
    }

    public boolean isUserInGroup(Long userId, Long groupId) {
        return this.ugroupRepo.isUserInGroup(userId, groupId);
    }

    @Override
    public List<GroupDTO> findGroups(Map<String, String> params) {
        List<Ugroup> groups = this.ugroupRepo.findGroups(params);

        return groups.stream()
                .map(group -> {
                    GroupDTO dto = modelMapper.map(group, GroupDTO.class);
                    long memberCount = ugroupRepo.countUsersInGroup(group.getId());
                    dto.setMemberCount(memberCount);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public long countGroups(Map<String, String> params) {
        return this.ugroupRepo.countGroups(params);
    }

    @Override
    public void deleteById(Long id) {
        ugroupRepo.deleteById(id);
    }

    @Override
    public List<GroupDTO> findGroups() {
        List<Ugroup> groups = this.ugroupRepo.findGroups();

        return groups.stream()
                .map(group -> {
                    GroupDTO dto = modelMapper.map(group, GroupDTO.class);
                    long memberCount = ugroupRepo.countUsersInGroup(group.getId());
                    dto.setMemberCount(memberCount);
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean isByGroupName(String name){
        return this.ugroupRepo.isByGroupName(name);
    }
}
