package com.example.alihfight.alifightapp.Admin.Datas;

public class DataHome {

    private String DateTime;
    private String Content;
    private String key;
    private String time;
    private String month;
    private String feedtype;
    private String day;

    public DataHome(){

    }

    public DataHome(String DateTime, String Content, String key,String time, String month, String feedtype, String day){
        this.DateTime = DateTime;
        this.Content = Content;
        this.key = key;
        this.time = time;
        this.month = month;
        this.feedtype = feedtype;
        this.day = day;
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
