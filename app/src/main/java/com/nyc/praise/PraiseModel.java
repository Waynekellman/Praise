package com.nyc.praise;

import java.util.Map;

/**
 * Created by Wayne Kellman on 5/1/18.
 */

public class PraiseModel {
    long uId;
    String location;
    String message;
    Map<Long, String> likes;
    Map<Long, String> comments;
    long date;

    public long getuId() {
        return uId;
    }

    public void setuId(long uId) {
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

    public Map<Long, String> getLikes() {
        return likes;
    }

    public void setLikes(Map<Long, String> likes) {
        this.likes = likes;
    }

    public Map<Long, String> getComments() {
        return comments;
    }

    public void setComments(Map<Long, String> comments) {
        this.comments = comments;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
