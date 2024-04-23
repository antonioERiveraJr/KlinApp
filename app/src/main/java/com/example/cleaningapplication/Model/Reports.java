package com.example.cleaningapplication.Model;

public class Reports {
    private String userId, user, date, reportId, reportMessage, reportSubject,sender;


    public Reports() {

    }

    public Reports(String userId, String user, String date, String reportId, String reportMessage, String reportSubject,String sender) {

        this.userId = userId;
        this.user = user;
        this.date = date;
        this.sender = sender;
        this.reportId = reportId;
        this.reportMessage = reportMessage;
        this.reportSubject = reportSubject;

    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }



    public String getReportMessage() {
        return reportMessage;
    }

    public void setReportMessage(String reportMessage) {
        this.reportMessage = reportMessage;
    }



    public String getReportSubject() {
        return reportSubject;
    }

    public void setReportSubject(String reportSubject) {
        this.reportSubject = reportSubject;
    }

}
