/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.components.JwtService;
import com.cmc.dtos.AlumniDTO;
import com.cmc.dtos.AlumniRegisterDTO;
import com.cmc.dtos.AlumniResponseDTO;
import com.cmc.dtos.ErrorResponseDTO;
import com.cmc.dtos.LoginRequestDTO;
import com.cmc.dtos.ResponseUserDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.Alumni;
import com.cmc.pojo.User;
import com.cmc.service.AlumniService;
import com.cmc.service.UserService;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author FPTSHOP
 */
@RestController
@RequestMapping("/api")
public class ApiUserController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMaper;
    @Autowired
    private AlumniService alumniService;

    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDTO loginDTO) {
        Map<String, Object> response = new HashMap<>();

        if (this.userService.authUser(loginDTO.getUsername(), loginDTO.getPassword(), loginDTO.getRole())) {
            String token = jwtService.generateTokenLogin(loginDTO.getUsername(), loginDTO.getRole());

            response.put("message", "Đăng nhập thành công!");
            response.put("token", token);
            response.put("role", jwtService.getRoleFromToken(token));
            return ResponseEntity.ok(response);
        }

        response.put("message", "Tên đăng nhập hoặc mật khẩu không đúng!");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping(path = "/users",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @CrossOrigin
    public ResponseEntity<?> addUser(@ModelAttribute AlumniRegisterDTO alumniRegisterDTO) {
        try {
            Alumni alumni = this.alumniService.registerAlumni(alumniRegisterDTO);
            if (alumni != null) {
//                return new ResponseEntity<>(alumniRegisterDTO, HttpStatus.CREATED);
                User user = alumni.getUserId();

                AlumniResponseDTO responseDTO = new AlumniResponseDTO();
                responseDTO.setUsername(user.getUsername());
                responseDTO.setFirstName(user.getFirstName());
                responseDTO.setLastName(user.getLastName());
                responseDTO.setEmail(user.getEmail());
                responseDTO.setPhone(user.getPhone());
                responseDTO.setAvatarUrl(user.getAvatar());
                responseDTO.setCoverUrl(user.getCover());
                responseDTO.setStudentCode(alumni.getStudentCode());
                return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

            } else {
                ErrorResponseDTO error = new ErrorResponseDTO("Không thể tạo người dùng", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ErrorResponseDTO error = new ErrorResponseDTO("Lỗi máy chủ: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/current-user", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<ResponseUserDTO> details(Principal user) {
        User u = this.userService.getUserByUsername(user.getName());
        
        ResponseUserDTO resU = modelMaper.map(u, ResponseUserDTO.class);

        return new ResponseEntity<>(resU, HttpStatus.OK);
    }
    
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        ResponseUserDTO resU = modelMaper.map(user, ResponseUserDTO.class);
        if (user != null) {
            return ResponseEntity.ok(resU);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Người dùng không tồn tại với ID: " + id);
        }
    }

}
