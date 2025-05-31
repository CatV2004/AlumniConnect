/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.pojo.Post;
import com.cmc.pojo.SurveyPost;
import java.util.List;
import java.util.Map;

/**
 *
 * @author FPTSHOP
 */
public interface SurveyPostRepository {

    void saveOrUpdate(SurveyPost s);

    SurveyPost findById(Long id);

    List<SurveyPost> findAll();

    boolean exitSurveyContent(String content);

    void updateExpiredStatus();

    List<Post> findExpiredSurveyPosts(Map<String, Object> params);
}
