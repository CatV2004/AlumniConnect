/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service;

import com.cmc.dtos.StatisticDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PHAT
 */
public interface StatsService {
    List<StatisticDTO> statisticalUser(Map<String, String> pagrams);
    List<StatisticDTO> statisticalPost(Map<String, String> pagrams);
}
