package com.example.larkmessage.entity;

import com.example.larkmessage.unit.DateUnit;

import java.text.ParseException;

public class Message {

    String username;
    String path;
    String context;
    String time;
    String receiver;
    String ImageResource;
    Boolean status =false;
    public String getImageResource() {
        return ImageResource;
    }

    public void setImageResource(String imageResource) {
        ImageResource = imageResource;
    }

    public  Message()
    {
        this.time =null;
        try {
            this.time = DateUnit.getSystemTimeAndDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public Message( String username, String context, String receiver) {

        this.username = username;
        this.receiver =receiver;
        this.context = context;
        this.time =null;
        status =false;
        try {
            this.time = DateUnit.getSystemTimeAndDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String conetxt) {
        this.context = conetxt;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
