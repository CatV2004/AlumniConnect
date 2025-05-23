/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.components.JwtService;
import com.cmc.dtos.LoginRequestDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.User;
import com.cmc.service.AdminService;
import com.cmc.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author FPTSHOP
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private JwtService jwtService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal, HttpSession session) {
//        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if (principal != null) {
            User user = userService.getUserByUsername(principal.getName());
            session.setAttribute("user", user);
            model.addAttribute("username", user);
        }
        return "admin_dashboard";
    }

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Bạn đã đăng xuất thành công!");
        }
        return "admin_login"; // Trả về trang đăng nhập
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("admin", new User());
        return "admin_register";
    }

    @PostMapping("/register")
    public String registerAdmin(@ModelAttribute("admin") UserDTO admin,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {

        System.out.println("Đã nhận request đăng ký: " + admin.getUsername());

        if (!admin.getPassword().equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "redirect:/admin/register";
        }

        try {
            adminService.registerAdmin(admin);
            redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Hãy đăng nhập.");
            return "redirect:/admin/login";
        } catch (Exception e) {
            e.printStackTrace(); 

            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        if (session != null) {
            session.invalidate();
        }

        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return "redirect:/admin/login?logout=true";
    }

}
