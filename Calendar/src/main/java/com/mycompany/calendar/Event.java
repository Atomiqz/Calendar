/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.calendar;

/**
 *
 * @author josho
 */
public class Event {

    // variable declarations
    private String date;
    private String time;
    private String desc;

    // constructor
    public Event(String date, String time, String desc) {
        this.date = date;
        this.time = time;
        this.desc = desc;
    }

    // getters and setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    // method to return event time and description
    public String getEvent() {
        return getTime() + ", " + getDesc() + "; ";
    }

}
