/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository;

import java.util.List;
import java.util.Map;

/**
 *
 * @author PHAT
 */
public interface StatsRepository {
    List<Object[]> countUsersByYear(Integer year);
    List<Object[]> countUsersByMonth(Integer year, Integer month);
    List<Object[]> countUsersByQuarter(Integer year, Integer quarter);

    List<Object[]> countPostsByYear(Integer year);
    List<Object[]> countPostsByMonth(Integer year, Integer month);
    List<Object[]> countPostsByQuarter(Integer year, Integer quarter);
    List<Object[]> statsUser(Map<String, String> pagram);
    List<Object[]> statsPost(Map<String, String> pagram);
}
