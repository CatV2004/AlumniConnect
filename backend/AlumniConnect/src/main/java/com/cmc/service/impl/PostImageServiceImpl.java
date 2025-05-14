/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.components.CloudinaryService;
import com.cmc.pojo.Post;
import com.cmc.pojo.PostImage;
import com.cmc.repository.PostImageRepository;
import com.cmc.service.PostImageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author FPTSHOP
 */
@Service
public class PostImageServiceImpl implements PostImageService {

    @Autowired
    private PostImageRepository postImageRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public void uploadAndSaveImages(Post post, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return;
        }

        postImageRepository.deleteImagesByPost(post);

        for (MultipartFile file : images) {
            if (!file.isEmpty()) {
                try {
                    String imageUrl = cloudinaryService.uploadFile(file);

                    PostImage postImage = new PostImage();
                    postImage.setImage(imageUrl);
                    postImage.setPostId(post);
                    postImageRepository.saveOrUpdate(postImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean updateImage(Long imageId, MultipartFile image) {
        try {
            String imageUrl = cloudinaryService.uploadFile(image);

            boolean isUpdated = this.postImageRepository.updateImage(imageId, imageUrl);

            return isUpdated;
        } catch (Exception e) {
            e.printStackTrace();
            return false; 
        }
    }

    @Override
    public boolean deleteImage(Long imageId) {
        return this.postImageRepository.deleteImage(imageId);
    }

}
