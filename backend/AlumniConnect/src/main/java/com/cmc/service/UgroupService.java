/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.pojo.Ugroup;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public interface UgroupService {

    Ugroup save(Ugroup ugroup);

    Ugroup findById(Long id);

    List<Ugroup> findAllActiveGroups();

    List<Ugroup> findAll();

    void addUserToGroup(Long groupId, Long userId);

    void removeUserFromGroup(Long groupId, Long userId);
}
