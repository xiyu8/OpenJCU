package com.example.myapplication;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017-04-13.
 */

public class Curriculum extends DataSupport{
    private String lessons;
    private int day;
    private int section;
    public String getLessons(){
        return  lessons;
    }
    public void setLessons(String lessons){
        this.lessons=lessons;
    }
    public int getDay(){return day;}
    public void setDay(int day){
        this.day=day;
    }
    public int getSection(){return section;}
    public void setSection(int section){
        this.section=section;
    }
}
