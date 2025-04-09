/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.components;

/**
 *
 * @author FPTSHOP
 */
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) {
        return uploadFile(file, ""); 
    }

    public String uploadFile(MultipartFile file, String subFolder) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            Map<String, Object> options = new HashMap<>();
            String folderPath = "AlumniSocialNetwork";
            if (subFolder != null && !subFolder.isBlank()) {
                folderPath += "/" + subFolder;
            }

            options.put("folder", folderPath);
            options.put("resource_type", "auto");

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
            return uploadResult.get("secure_url").toString(); 
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi upload file!", e);
        }
    }
}
