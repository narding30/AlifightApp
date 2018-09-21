package com.example.alihfight.alifightapp.User.Datas;

public class DataSpecialSession {

    private String UserID;
    private String SessionDay;
    private String Instructor;
    private String Time;
    private String Key;
    private String Status;
    private String SessionName;
    private String FullName;


    public DataSpecialSession(){

    }

    public DataSpecialSession(String UserID, String SessionDay, String Instructor, String Time, String Key, String Status,String SessionName, String FullName){
        this.UserID = UserID;
        this.SessionDay = SessionDay;
        this.Instructor = Instructor;
        this.Time = Time;
        this.Key = Key;
        this.Status = Status;
        this.SessionName = SessionName;
        this.FullName = FullName;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getSessionName() {
        return SessionName;
    }

    public void setSessionName(String sessionName) {
        SessionName = sessionName;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getSessionDay() {
        return SessionDay;
    }

    public void setSessionDay(String sessionDay) {
        SessionDay = sessionDay;
    }

    public String getInstructor() {
        return Instructor;
    }

    public void setInstructor(String instructor) {
        Instructor = instructor;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
