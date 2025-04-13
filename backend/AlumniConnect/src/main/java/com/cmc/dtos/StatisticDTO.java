/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.dtos;

/**
 *
 * @author PHAT
 */
public class StatisticDTO {
    private int year;
    private int month;
    private long posts;
    private long users;

    public StatisticDTO() {
    }

    public StatisticDTO(int year, int month, long posts, long users) {
        this.year = year;
        this.month = month;
        this.posts = posts;
        this.users = users;
    }

    
 
    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * @return the posts
     */
    public long getPosts() {
        return posts;
    }

    /**
     * @param posts the posts to set
     */
    public void setPosts(long posts) {
        this.posts = posts;
    }

    /**
     * @return the users
     */
    public long getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(long users) {
        this.users = users;
    }

   
}
