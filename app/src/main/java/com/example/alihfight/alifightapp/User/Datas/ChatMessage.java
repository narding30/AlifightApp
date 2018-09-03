package com.example.alihfight.alifightapp.User.Datas;

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private String messageTime;
    private String from;

    public ChatMessage(String messageText, String messageUser, String messagetime, String from) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = messagetime;
    }

    public ChatMessage(){

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}