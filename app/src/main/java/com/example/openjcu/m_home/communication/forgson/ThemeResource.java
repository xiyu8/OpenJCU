package com.example.openjcu.m_home.communication.forgson;

/**
 * Created by 晞余 on 2017/3/23.
 */

public class ThemeResource {
    String themeId;
    String title;
    String content;
    String userId;
    String time;
    String category;
    String userName;
    String pwd;
    String createTime;

    public String getReadCommentQuantity() {
        return readCommentQuantity;
    }

    public void setReadCommentQuantity(String readCommentQuantity) {
        this.readCommentQuantity = readCommentQuantity;
    }

    public String getThemePics() {
        return themePics;
    }

    public void setThemePics(String themePics) {
        this.themePics = themePics;
    }

    String readCommentQuantity;
    String themePics;

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
