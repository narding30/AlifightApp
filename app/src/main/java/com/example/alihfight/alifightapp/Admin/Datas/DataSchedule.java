package com.example.alihfight.alifightapp.Admin.Datas;

public class DataSchedule {

    private String SessionName;
    private String SessionCapacity;
    private String SessionsPerWeek;
    private String Instructor;
    private String TimeStart;
    private String TimeEnd;
    private String Key;
    private String SessionDay;

    public DataSchedule(){

    }

    public DataSchedule(String SessionName, String SessionCapacity, String SessionsPerWeek, String Instructor, String TimeStart,
                        String TimeEnd, String Key, String SessionDay){
        this.SessionName = SessionName;
        this.SessionCapacity = SessionCapacity;
        this.SessionsPerWeek = SessionsPerWeek;
        this.Instructor = Instructor;
        this.TimeStart = TimeStart;
        this.TimeEnd = TimeEnd;
        this.Key = Key;
        this.SessionDay = SessionDay;
    }

    public String getSessionDay() {
        return SessionDay;
    }

    public void setSessionDay(String sessionDay) {
        SessionDay = sessionDay;
    }

    public String getSessionName() {
        return SessionName;
    }

    public void setSessionName(String sessionName) {
        SessionName = sessionName;
    }

    public String getSessionCapacity() {
        return SessionCapacity;
    }

    public void setSessionCapacity(String sessionCapacity) {
        SessionCapacity = sessionCapacity;
    }

    public String getSessionsPerWeek() {
        return SessionsPerWeek;
    }

    public void setSessionsPerWeek(String sessionsPerWeek) {
        SessionsPerWeek = sessionsPerWeek;
    }

    public String getInstructor() {
        return Instructor;
    }

    public void setInstructor(String instructor) {
        Instructor = instructor;
    }

    public String getTimeStart() {
        return TimeStart;
    }

    public void setTimeStart(String timeStart) {
        TimeStart = timeStart;
    }

    public String getTimeEnd() {
        return TimeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        TimeEnd = timeEnd;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
