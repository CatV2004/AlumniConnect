/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.pojo.Ugroup;
import java.util.List;
import java.util.Map;

/**
 *
 * @author FPTSHOP
 */
public interface UgroupRepository {

    Ugroup save(Ugroup ugroup);

    Ugroup findById(Long id);

    Ugroup findByGroupName(String groupName);

    List<Ugroup> findGroups(Map<String, String> params);
    
    List<Ugroup> findGroups();

    long countGroups(Map<String, String> params);

    void addUserToGroup(Long groupId, Long userId);

    void removeUserFromGroup(Long groupId, Long userId);

    boolean existsById(Long id);

    boolean isUserInGroup(Long userId, Long groupId);

    long countUsersInGroup(Long groupId);

    void deleteById(Long id);
}
