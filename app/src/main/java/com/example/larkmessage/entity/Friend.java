package com.example.larkmessage.entity;

import com.example.larkmessage.R;
import com.example.larkmessage.unit.DateUnit;

import java.io.Serializable;
import java.text.ParseException;

public class Friend implements Serializable {
    String email;
    Boolean accessMoment;
    Boolean letHimAccessMoment;
    String relationShipDate;
    Boolean type;
    String userName;
    Integer icon;
    String messageId;
    String nickName=null;
    Boolean status;
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Boolean getAccessMoment() {
        return accessMoment;
    }

    public void setAccessMoment(Boolean accessMoment) {
        this.accessMoment = accessMoment;
    }

    public Boolean getLetHimAccessMoment() {
        return letHimAccessMoment;
    }

    public void setLetHimAccessMoment(Boolean letHimAccessMoment) {
        this.letHimAccessMoment = letHimAccessMoment;
    }

    public String getRelationShipDate() {
        return relationShipDate;
    }

    public void setRelationShipDate(String relationShipDate) {
        this.relationShipDate = relationShipDate;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDefault() throws ParseException {
        this.relationShipDate = DateUnit.getSystemTimeAndDate();
        this.accessMoment=true;
        this.letHimAccessMoment=true;
        this.type=true;
        this.icon = R.drawable.nn2;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
