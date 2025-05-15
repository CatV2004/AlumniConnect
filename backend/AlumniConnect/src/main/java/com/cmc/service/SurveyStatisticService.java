/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.SurveyStatisticsResponseDTO;

/**
 *
 * @author FPTSHOP
 */
public interface SurveyStatisticService {
    SurveyStatisticsResponseDTO getSurveyStatisticsWithParticipants(Long surveyPostId);
}
