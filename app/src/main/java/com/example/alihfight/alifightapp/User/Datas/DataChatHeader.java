package com.example.alihfight.alifightapp.User.Datas;

public class DataChatHeader {

    private String SessionName;
    private String MessageStatus;
    private String Time;
    private String Key;

    public DataChatHeader(){

    }

    public DataChatHeader(String SessionName, String MessageStatus, String Time, String Key){
        this.SessionName = SessionName;
        this.MessageStatus = MessageStatus;
        this.Time = Time;
        this.Key = Key;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getSessionName() {
        return SessionName;
    }

    public void setSessionName(String sessionName) {
        SessionName = sessionName;
    }

    public String getMessageStatus() {
        return MessageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        MessageStatus = messageStatus;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
