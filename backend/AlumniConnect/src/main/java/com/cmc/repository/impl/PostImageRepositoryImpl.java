/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.Post;
import com.cmc.pojo.PostImage;
import com.cmc.repository.PostImageRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author FPTSHOP
 */
@Repository
@Transactional
public class PostImageRepositoryImpl implements PostImageRepository {

    @Autowired
    private LocalSessionFactoryBean Factory;

    private Session getSession() {
        return Factory.getObject().getCurrentSession();
    }

    @Override
    public void saveOrUpdate(PostImage postImage) {
        if (postImage.getId() == null) {
            this.getSession().persist(postImage);
        } else {
            this.getSession().merge(postImage);
        }
    }

    @Override
    public void deleteImagesByPost(Post post) {
        Session session = getSession();

        String sql = "from PostImage where postId = :post";
        List<PostImage> images = session.createSelectionQuery(sql, PostImage.class)
                .setParameter("post", post)
                .getResultList();

        for (PostImage image : images) {
            post.getPostImageSet().remove(image);
            session.remove(image);
        }

    }

    @Override
    public boolean deleteImage(Long imageId) {
        PostImage postImage = getSession().get(PostImage.class, imageId);
        if (postImage != null) {
            getSession().remove(postImage);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateImage(Long imageId, String imgUrl) {
        PostImage postImage = getSession().get(PostImage.class, imageId);
        if (postImage != null) {
            postImage.setImage(imgUrl);
//            this.getSession().merge(postImage);
            return true;
        } else {
            return false;
        }
    }

}
