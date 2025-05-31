/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.dtos.TeacherDTO;
import com.cmc.dtos.TeacherRequestDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.Teacher;
import com.cmc.pojo.User;
import com.cmc.service.AlumniService;
import com.cmc.service.TeacherService;
import com.cmc.service.UserService;
import com.cmc.utils.ResponseMessage;
import com.cmc.validator.TeacherRequestValidator;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author FPTSHOP
 */
@Controller
@RequestMapping("/admin")
public class TeacherManageController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private TeacherRequestValidator teacherValidate;

    @GetMapping("/teachers")
    public String teachersView(@RequestParam Map<String, String> params, Model model) {
        params.putIfAbsent("page", "1");
        params.putIfAbsent("size", "5");

        int page = Integer.parseInt(params.get("page"));
        int size = Integer.parseInt(params.get("size"));

        String username = params.get("username");

        List<Teacher> teachers = teacherService.getTeachers(params);

        long totalTeacher = teacherService.countTeachers();
        int totalPages = (int) Math.ceil((double) totalTeacher / size);

        model.addAttribute("teachers", teachers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("username", username);

        return "admin_teacher_management";
    }

    @PostMapping("/teachers/create")
    @CrossOrigin
    @ResponseBody
    public ResponseEntity<?> createTeacher(@Valid @RequestBody TeacherRequestDTO user, BindingResult result) {
        this.teacherValidate.validate(user, result);
        if (result.hasErrors()) {
            String errors = result.getFieldErrors().stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.joining("\n"));

            Map<String, String> response = new HashMap<>();
            response.put("message", errors);
            return ResponseEntity.badRequest().body(response);
        }

        TeacherDTO teacher = new TeacherDTO();
        try {
            if (user != null) {
                teacher.setUser(user);
            }

            boolean isCreated = teacherService.createTeacherAccount(teacher);

            if (isCreated) {
                return ResponseEntity.ok(new ResponseMessage("Tạo tài khoản giảng viên thành công!"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseMessage("Tạo tài khoản giảng viên thất bại."));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Đã có lỗi xảy ra khi tạo tài khoản giảng viên."));
        }
    }

    @PostMapping("/teachers/resetPassword/{id}")
    public String updateResetPasswordTime(@PathVariable("id") Long teacherId) {
        teacherService.resetPasswordChangeDeadline(teacherId);
        return "redirect:/admin/teachers";
    }

}
