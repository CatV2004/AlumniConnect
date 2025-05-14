/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.pojo.Post;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author FPTSHOP
 */
public interface PostImageService {

    void uploadAndSaveImages(Post post, List<MultipartFile> images);

    boolean deleteImage(Long imageId);

    boolean updateImage(Long imageId, MultipartFile file);

}
