/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.dtos;

import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public class SurveyStatisticsResponseDTO {
    private List<SurveyOptionStatisticDTO> statistics;
    private Long participantCount;

    public SurveyStatisticsResponseDTO(List<SurveyOptionStatisticDTO> statistics, Long participantCount) {
        this.statistics = statistics;
        this.participantCount = participantCount;
    }

    public List<SurveyOptionStatisticDTO> getStatistics() {
        return statistics;
    }

    public Long getParticipantCount() {
        return participantCount;
    }
    
    
}

