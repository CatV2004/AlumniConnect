/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.PostImage;
import com.cmc.repository.PostImageRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
