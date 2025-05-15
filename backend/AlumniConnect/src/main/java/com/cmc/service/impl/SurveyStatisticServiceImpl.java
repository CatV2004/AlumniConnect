/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.dtos.SurveyOptionStatisticDTO;
import com.cmc.dtos.SurveyStatisticsResponseDTO;
import com.cmc.repository.SurveyOptionStatistic;
import com.cmc.service.SurveyStatisticService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author FPTSHOP
 */
@Service
public class SurveyStatisticServiceImpl implements SurveyStatisticService{
    @Autowired
    private SurveyOptionStatistic surveyOptionStatistic;

    @Override
    public SurveyStatisticsResponseDTO getSurveyStatisticsWithParticipants(Long surveyPostId) {
        return surveyOptionStatistic.getSurveyStatisticsWithParticipants(surveyPostId);
    }
    
}
