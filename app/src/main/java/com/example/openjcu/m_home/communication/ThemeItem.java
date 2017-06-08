package com.example.openjcu.m_home.communication;

/**
 * Created by 晞余 on 2017/3/22.
 */

public class ThemeItem {
    int usericon;
    String name;
    String themeTitle;
    String themeContent;

    public ThemeItem(int ui,String n,String tt,String tc) {
        usericon=ui;
        name=n;
        themeTitle=tt;
        themeContent=tc;
    }

    public int getUsericon() {
        return usericon;
    }

    public void setUsericon(int usericon) {
        this.usericon = usericon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThemeTitle() {
        return themeTitle;
    }

    public void setThemeTitle(String themeTitle) {
        this.themeTitle = themeTitle;
    }

    public String getThemeContent() {
        return themeContent;
    }

    public void setThemeContent(String themeContent) {
        this.themeContent = themeContent;
    }
}
