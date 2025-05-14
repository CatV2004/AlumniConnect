/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.pojo.Post;
import com.cmc.pojo.PostImage;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author FPTSHOP
 */
public interface PostImageRepository {

    void saveOrUpdate(PostImage postImage);

    void deleteImagesByPost(Post post);
    
    boolean deleteImage(Long imageId);
    
    boolean updateImage(Long imageId, String imgUrl);
}
