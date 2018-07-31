package com.example.alihfight.alifightapp.Admin.Datas;

public class DataHome {

    public String DateTime;
    public String Content;
    public String key;
    public String time;
    public String month;
    public String feedtype;

    public DataHome(){

    }

    public DataHome(String DateTime, String Content, String key,String time, String month, String feedtype){
        this.DateTime = DateTime;
        this.Content = Content;
        this.key = key;
        this.time = time;
        this.month = month;
        this.feedtype = feedtype;
    }

    public String getFeedtype() {
        return feedtype;
    }

    public void setFeedtype(String feedtype) {
        this.feedtype = feedtype;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
