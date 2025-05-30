/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.GroupDTO;
import com.cmc.pojo.Ugroup;
import java.util.List;
import java.util.Map;

/**
 *
 * @author FPTSHOP
 */
public interface UgroupService {

    Ugroup save(Ugroup ugroup);

    Ugroup findById(Long id);

    List<GroupDTO> findGroups(Map<String, String> params);

    List<GroupDTO> findGroups();

    long countGroups(Map<String, String> params);

    void addUserToGroup(Long groupId, Long userId);

    void addUsersToGroup(Long groupId, List<Long> userIds);

    void removeUserFromGroup(Long groupId, Long userId);

    void deleteById(Long id);
    
    boolean isByGroupName(String name);
}
