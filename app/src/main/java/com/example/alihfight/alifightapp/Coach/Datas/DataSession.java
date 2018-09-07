package com.example.alihfight.alifightapp.Coach.Datas;

public class DataSession {

    private String Instructor;
    private String SessionCapacity;
    private String SessionDay;
    private String SessionName;
    private String SessionsPerWeek;
    private String TimeStart;
    private String TimeEnd;
    private String Attendees;
    private String Key;

    public DataSession(){

    }

    public DataSession(String Instructor, String SessionCapacity, String SessionDay, String SessionName,
                       String SessionsPerWeek, String TimeStart, String TimeEnd, String Attendees, String Key){
        this.Instructor = Instructor;
        this.SessionCapacity = SessionCapacity;
        this.SessionDay = SessionDay;
        this.SessionName = SessionName;
        this.SessionsPerWeek = SessionsPerWeek;
        this.TimeStart = TimeStart;
        this.TimeEnd = TimeEnd;
        this.Attendees = Attendees;
        this.Key = Key;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getAttendees() {
        return Attendees;
    }

    public void setAttendees(String attendees) {
        Attendees = attendees;
    }

    public String getInstructor() {
        return Instructor;
    }

    public void setInstructor(String instructor) {
        Instructor = instructor;
    }

    public String getSessionCapacity() {
        return SessionCapacity;
    }

    public void setSessionCapacity(String sessionCapacity) {
        SessionCapacity = sessionCapacity;
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

    public String getSessionsPerWeek() {
        return SessionsPerWeek;
    }

    public void setSessionsPerWeek(String sessionsPerWeek) {
        SessionsPerWeek = sessionsPerWeek;
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
}






















