/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.dtos.UserDTO;
import com.cmc.pojo.User;
import com.cmc.service.UserService;
import com.cmc.validator.WebAppValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author FPTSHOP
 */
@Controller
public class LoginController {

//    @Autowired
//    private UserService userService;
//    @Autowired
//    private WebAppValidator userValidator;
//
//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        binder.setValidator((Validator) userValidator);
//    }
//
//    @GetMapping(value = "/register")
//    public String registerView(Model model) {
//        model.addAttribute("user", new User());
//        return "register";
//    }
//
//    @PostMapping(value = "/register")
//    public String registerProcess(
//            @ModelAttribute(name = "user") @Valid UserDTO user,
//            BindingResult result) {
//        if (result.hasErrors()) {
//            return "register";
//        }
//        userService.addUser(user);
//        return "redirect:/login";
//    }
}
