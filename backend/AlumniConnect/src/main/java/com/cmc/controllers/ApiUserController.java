/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 *
 * @author FPTSHOP
 */
@RestController
@RequestMapping("/users")
public class ApiUserController {

//    @Autowired
//    private UserService userService;
//
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ResponseStatus(HttpStatus.CREATED)
//    @CrossOrigin
//    public void create(
//            @RequestPart("user") UserDTO userDTO,
//            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile,
//            @RequestPart(value = "cover", required = false) MultipartFile coverFile) {
//
//        userDTO.setAvatarFile(avatarFile);
//        userDTO.setCoverFile(coverFile);
//
//        userService.addUser(userDTO);
//    }
//
////    @PostMapping("/login/")
////    @CrossOrigin
////    public ResponseEntity<String> login(@RequestBody User user) {
////        if (this.userService.authUser(user.getUsername(), user.getPassword()) == true) {
////            String token = this.jwtService.generateTokenLogin(user.getUsername());
////            
////            return new ResponseEntity<>(token, HttpStatus.OK);
////        }
////
////        return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
////    }
//    @GetMapping(path = "/current-user/", produces = MediaType.APPLICATION_JSON_VALUE)
//    @CrossOrigin
//    public ResponseEntity<UserDTO> getCurrentUser(Principal p) {
//        UserDTO u = this.userService.getUserByUsername(p.getName());
//        return new ResponseEntity<>(u, HttpStatus.OK);
//    }

}
