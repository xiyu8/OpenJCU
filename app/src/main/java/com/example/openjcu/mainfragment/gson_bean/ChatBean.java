package com.example.openjcu.mainfragment.gson_bean;

/**
 * Created by 晞余 on 2017/4/20.
 */

public class ChatBean {
    String chatItemId;
    String chatContent;
    String memberId;
    String chatItemDate;
    String memberPosition;
    String groupId;

    public String getChatItemId() {
        return chatItemId;
    }

    public void setChatItemId(String chatItemId) {
        this.chatItemId = chatItemId;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getChatItemDate() {
        return chatItemDate;
    }

    public void setChatItemDate(String chatItemDate) {
        this.chatItemDate = chatItemDate;
    }

    public String getMemberPosition() {
        return memberPosition;
    }

    public void setMemberPosition(String memberPosition) {
        this.memberPosition = memberPosition;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
