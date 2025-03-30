/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.pojo.Alumni;
import com.cmc.pojo.User;
import com.cmc.service.AlumniService;
import com.cmc.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author FPTSHOP
 */
@Controller
@RequestMapping("/admin")
public class AlumniManageController {

    @Autowired
    private AlumniService alumniService;
    @Autowired
    private UserService userService;

    @GetMapping("/alumnis")
    public String alumisView(Model model) {
        List<Alumni> alumnis = alumniService.getAlumnis();
        model.addAttribute("alumnis", alumnis);
        return "admin_alumni_management";
    }

    @CrossOrigin
    @PostMapping("/alumnis/approve/{id}")
    public ResponseEntity<String> approveAlumni(@PathVariable Long id) {
        System.out.println("approveAlumni() nhận ID: " + id);
        try {
            alumniService.approveAlumni(id);
            return ResponseEntity.ok("Alumni approved successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error approving alumni");
        }
    }

}
