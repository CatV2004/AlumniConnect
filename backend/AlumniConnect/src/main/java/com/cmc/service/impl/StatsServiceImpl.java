/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.service.impl;

import com.cmc.dtos.StatisticDTO;
import com.cmc.repository.StatsRepository;
import com.cmc.service.StatsService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author PHAT
 */
@Service
public class StatsServiceImpl implements StatsService{
    
    @Autowired
    private StatsRepository statsRepo;

//    @Override
//    public List<StatisticDTO> getUserCountByMonth(Map<String, String> pagrams) {
//        List<Object[]> results;
//        results = this.statsRepo.countUsersByMonth(Integer.valueOf(pagrams.get("year")), Integer.valueOf(pagrams.get("month")));
//
//        return results.stream()
//            .map(row -> new StatisticDTO(
//                (Integer) row[0],
//                (Integer) row[1],
//                (Long) row[2]
//            ))
//            .collect(Collectors.toList());
//        }
//    

    @Override
    public List<StatisticDTO> statisticalUser(Map<String, String> pagrams) {
        List<Object[]> results;
        results = this.statsRepo.statsUser(pagrams);
        
        return results.stream().map(row -> {
            StatisticDTO stats = new StatisticDTO();
            stats.setYear((Integer) row[0]);
            stats.setMonth((Integer)row[1]);
            stats.setUsers((Integer) row[2]);
            return stats;
        }).collect(Collectors.toList());
    }
    
    @Override
    public List<StatisticDTO> statisticalPost(Map<String, String> pagrams) {
        List<Object[]> results;
        results = this.statsRepo.statsUser(pagrams);
        
        return results.stream().map(row -> {
            StatisticDTO stats = new StatisticDTO();
            stats.setYear((Integer) row[0]);
            stats.setMonth((Integer)row[1]);
            stats.setPosts((Integer) row[2]);
            return stats;
        }).collect(Collectors.toList());
    }
}