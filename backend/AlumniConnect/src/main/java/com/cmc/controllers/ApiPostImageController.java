/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.components.PostComponents;
import com.cmc.pojo.Post;
import com.cmc.service.PostImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author FPTSHOP
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiPostImageController {

    @Autowired
    private PostImageService postImageService;
    @Autowired
    private PostComponents postComponents;

    @PutMapping("/post-images/{imageId}")
    public ResponseEntity<String> updateImage(
            @PathVariable("imageId") Long imageId,
            @RequestParam("image") MultipartFile image
    ) {
        try {
            boolean isUpdated = postImageService.updateImage(imageId, image);

            if (isUpdated) {
                return ResponseEntity.ok("Image updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found or update failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating image");
        }
    }

    @DeleteMapping("/post-images/{imageId}")
    public ResponseEntity<String> deleteImage(
            @PathVariable("imageId") Long imageId
    ) {
        try {
            boolean isDeleted = postImageService.deleteImage(imageId);

            if (isDeleted) {
                return ResponseEntity.ok("Image deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting image");
        }
    }
}
