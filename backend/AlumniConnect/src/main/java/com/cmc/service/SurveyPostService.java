/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.SurveyDTO;
import com.cmc.pojo.SurveyPost;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author FPTSHOP
 */
public interface SurveyPostService {
    boolean saveOrUpdate(SurveyDTO dto);
}
