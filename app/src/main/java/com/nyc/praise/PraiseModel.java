package com.nyc.praise;

import java.util.Map;

/**
 * Created by Wayne Kellman on 5/1/18.
 */

public class PraiseModel {
    private String uId;
    private String location;
    private String message;
    private Map<String, Object> likes;
    private Map<String, Object> comments;
    private Long date;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Object> likes) {
        this.likes = likes;
    }

    public Map<String, Object> getComments() {
        return comments;
    }

    public void setComments(Map<String, Object> comments) {
        this.comments = comments;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
