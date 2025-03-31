/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;


/**
 *
 * @author FPTSHOP
 */
@Controller
@ControllerAdvice
public class HomeController {


    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("name", "Thanh");
        return "home";
    }

   

}
