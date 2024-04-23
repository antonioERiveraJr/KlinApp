package com.example.cleaningapplication.Model;

public class LogActivity {
    String Action;
    String User;
    String Topic;
    String Date;
/*    public LogActivity(String action, String user, String topic, String date) {
        this.Action = action;
        this.User = user;
        this.Topic = topic;
        this.Date = date;
    }*/


    public String getAction() {
        return Action;
    }

    public void setAction(String Action) {
        Action = Action;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String User) {
        User = User;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String Topic) {
        Topic = Topic;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        Date = Date;
    }
}
