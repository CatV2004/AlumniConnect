/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.components.CloudinaryService;
import com.cmc.components.JwtService;
import com.cmc.dtos.LoginRequestDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.User;
import com.cmc.service.AdminService;
import com.cmc.service.UserService;
import com.cmc.validator.UserValidator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UserValidator userValidator;
    
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

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        if (binder.getTarget() != null && userValidator.supports(binder.getTarget().getClass())) {
            binder.addValidators(userValidator);
        }
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String registerAdmin(
            @Valid @ModelAttribute("admin") UserDTO admin,
            BindingResult bindingResult,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam("avatarFile") MultipartFile avatarFile,
            RedirectAttributes redirectAttributes,
            Model model) {

        System.out.println("Đã nhận request đăng ký: " + admin.getUsername());

        if (bindingResult.hasErrors()) {
            model.addAttribute("admin", admin);
            model.addAttribute("confirmPasswordError", null);
            System.out.println(model.addAttribute("admin", admin));
            return "admin_register";
        }
        System.out.println(bindingResult.hasErrors());
        if (!admin.getPassword().equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "redirect:/admin/register";
        }

        try {
            if (!avatarFile.isEmpty()) {
                String fileName = this.cloudinaryService.uploadFile(avatarFile);
                admin.setAvatar(fileName);
            }
            admin.setActive(Boolean.TRUE);
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
