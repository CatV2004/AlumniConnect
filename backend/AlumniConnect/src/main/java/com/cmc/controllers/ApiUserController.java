/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.components.JwtService;
import com.cmc.dtos.AlumniDTO;
import com.cmc.dtos.AlumniRegisterDTO;
import com.cmc.dtos.AlumniResponseDTO;
import com.cmc.dtos.ChangePasswordDTO;
import com.cmc.dtos.ErrorResponseDTO;
import com.cmc.dtos.LoginRequestDTO;
import com.cmc.dtos.PageResponse;
import com.cmc.dtos.PostResponseDTO;
import com.cmc.dtos.ResponseUserDTO;
import com.cmc.dtos.UserDTO;
import com.cmc.pojo.Alumni;
import com.cmc.pojo.Post;
import com.cmc.pojo.User;
import com.cmc.service.AlumniService;
import com.cmc.service.PostService;
import com.cmc.service.UserService;
import com.cmc.validator.AlumniRegisterValidator;
import com.cmc.validator.ChangePasswordValidator;
import com.cmc.validator.LoginValidate;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author FPTSHOP
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
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
    @Autowired
    private PostService postService;
    @Autowired
    private PasswordEncoder passEncoder;

    @Autowired
    private ChangePasswordValidator changePasswordValidator;

    @Autowired
    private AlumniRegisterValidator alumniRegisterValidator;
    
    @Autowired
    private LoginValidate loginValidate;

    @InitBinder("alumniRegisterDTO")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(alumniRegisterValidator);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error
                -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
    
    @InitBinder("loginDTO")
    public void initBinderLogin(WebDataBinder binder) {
        binder.addValidators(loginValidate);    
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequestDTO loginDTO, BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            result.getFieldErrors().forEach(err
                    -> response.put(err.getField(), err.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(response);
        }

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
    
    @PutMapping("/users/password")
    public ResponseEntity<Map<String, Object>> changePassword(
            @Valid @RequestBody ChangePasswordDTO dto,
            BindingResult result,
            Principal principal) {

        changePasswordValidator.validate(dto, result);

        if (result.hasErrors()) {
            Map<String, Object> errors = new HashMap<>();
            result.getFieldErrors().forEach(err
                    -> errors.put(err.getField(), err.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        Map<String, Object> response = new HashMap<>();

        User user = userService.getUserByUsername(principal.getName());

        if (user == null) {
            response.put("message", "Người dùng không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (!passEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            response.put("message", "Mật khẩu cũ không đúng");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        userService.changePassword(user, dto);

        response.put("message", "Đổi mật khẩu thành công");
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/users",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addUser(@Valid @ModelAttribute AlumniRegisterDTO alumniRegisterDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                Map<String, Object> errors = new HashMap<>();
                result.getFieldErrors().forEach(err
                        -> errors.put(err.getField(), err.getDefaultMessage())
                );
                return ResponseEntity.badRequest().body(errors);
        }
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
    public ResponseEntity<?> details(Principal user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized: Missing or expired token");
        }

        User u = this.userService.getUserByUsername(user.getName());
        ResponseUserDTO resU = modelMaper.map(u, ResponseUserDTO.class);
        return ResponseEntity.ok(resU);
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

    @GetMapping("/users/{id}/posts")
    public ResponseEntity<PageResponse<PostResponseDTO>> getPostsByUser(
            @PathVariable("id") Long userId,
            @RequestParam Map<String, Object> params) {

        Map<String, Object> filterParams = new HashMap<>();

        filterParams.put("userId", userId);

        int page = params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1;
        filterParams.put("page", page);

        int size = params.containsKey("size") ? Integer.parseInt(params.get("size").toString()) : 10;
        filterParams.put("size", size);

        PageResponse<PostResponseDTO> response = postService.getPostsByUser(filterParams);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<PageResponse<ResponseUserDTO>> getUsers(
            @RequestParam Map<String, String> params) {

        Map<String, Object> filterParams = new HashMap<>();

        try {
            // Xử lý tham số
            if (params.containsKey("currentUsername")) {
                filterParams.put("excludeUsername", params.get("currentUsername"));
            }

            if (params.containsKey("page")) {
                filterParams.put("page", Integer.parseInt(params.get("page")));
            }

            if (params.containsKey("size")) {
                int size = Integer.parseInt(params.get("size"));
                filterParams.put("size", Math.min(size, 100)); // Giới hạn max 100 items/page
            }

            if (params.containsKey("keyword")) {
                filterParams.put("keyword", params.get("keyword"));
            }

            PageResponse<ResponseUserDTO> response = userService.getAllUsers(filterParams);
            return ResponseEntity.ok(response);

        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page/size parameter");
        }
    }

    @PutMapping(value = "/user/update",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateCurrentUser(
            @RequestParam(required = false) MultipartFile avatar,
            @RequestParam(required = false) MultipartFile cover,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String password,
            Principal principal) {

        try {
            String username = principal.getName();
            User existingUser = userService.getUserByUsername(username);

            UserDTO userDTO = new UserDTO();
            userDTO.setId(existingUser.getId());

            userDTO.setEmail(email != null ? email : existingUser.getEmail());
            userDTO.setPhone(phone != null ? phone : existingUser.getPhone());
            userDTO.setLastName(lastName != null ? lastName : existingUser.getLastName());
            userDTO.setFirstName(firstName != null ? firstName : existingUser.getFirstName());
            userDTO.setPassword(password);

            User updatedUser = userService.saveOrUpdate(userDTO, avatar, cover);

            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật thông tin: " + e.getMessage());
        }
    }

}
