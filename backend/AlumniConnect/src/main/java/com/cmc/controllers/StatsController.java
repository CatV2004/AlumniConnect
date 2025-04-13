/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.controllers;

import com.cmc.dtos.StatisticDTO;
import com.cmc.service.StatsService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author PHAT
 */
@Controller
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/admin/statistics")
    public String getUserCountByMonth(@RequestParam Map<String, String> pagrams, Model model) {
        List<Integer> yearPost = this.statsService.statisticalPost(pagrams).stream().map(StatisticDTO::getYear).toList();
        List<Integer> monthPost = this.statsService.statisticalPost(pagrams).stream().map(StatisticDTO::getMonth).toList();
        List<Long> dataPost = this.statsService.statisticalPost(pagrams).stream().map(StatisticDTO::getPosts).toList();
        
        List<Integer> yearUser = this.statsService.statisticalUser(pagrams).stream().map(StatisticDTO::getYear).toList();
        List<Integer> monthUser = this.statsService.statisticalUser(pagrams).stream().map(StatisticDTO::getMonth).toList();
        List<Long> dataUser = this.statsService.statisticalUser(pagrams).stream().map(StatisticDTO::getUsers).toList();
        List<Map<String, Object>> statsUser = new ArrayList<>();
        for (int i = 0; i < dataUser.size(); i++) {
            Map<String, Object> row = new HashMap<>();
            row.put("stt", i + 1);
            row.put("time", String.format("%02d/%d", monthUser.get(i), yearUser.get(i)));
            row.put("users", dataUser.get(i));
            statsUser.add(row);
        }
        List<Map<String, Object>> statsPost = new ArrayList<>();
        for (int i = 0; i < dataPost.size(); i++) {
            Map<String, Object> row = new HashMap<>();
            row.put("stt", i + 1);
            row.put("time", String.format("%02d/%d", monthPost.get(i), yearPost.get(i)));
            row.put("posts", dataPost.get(i));
            statsPost.add(row);
        }
        model.addAttribute("statsUser", statsUser);
        model.addAttribute("statsPost", statsPost);

        model.addAttribute("yearPost", yearPost);
        model.addAttribute("monthPost", monthPost);
        model.addAttribute("dataPost", dataPost);
        model.addAttribute("yearUser", yearUser);
        model.addAttribute("monthUser", monthUser);
        model.addAttribute("dataUser", dataUser);

        return "admin_stats";
    }
}
