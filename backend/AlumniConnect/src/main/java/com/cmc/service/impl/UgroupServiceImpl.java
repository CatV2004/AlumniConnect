/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.pojo.Ugroup;
import com.cmc.repository.UgroupRepository;
import com.cmc.service.UgroupService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author FPTSHOP
 */
public class UgroupServiceImpl implements UgroupService {

    @Autowired
    private UgroupRepository ugroupRepo;

    @Override
    public Ugroup save(Ugroup ugroup) {
        return this.ugroupRepo.save(ugroup);
    }

    @Override
    public Ugroup findById(Long id) {
        return this.ugroupRepo.findById(id);
    }

    @Override
    public List<Ugroup> findAllActiveGroups() {
        return this.ugroupRepo.findAllActiveGroups();
    }

    @Override
    public List<Ugroup> findAll() {
        return this.ugroupRepo.findAll();
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

}
