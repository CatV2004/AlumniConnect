/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import com.cmc.pojo.SurveyOption;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public interface SurveyOptionRepository {

    void saveOrUpdate(SurveyOption o);

    SurveyOption findById(Long id);

    List<SurveyOption> findByQuestionId(Long questionId);
}
