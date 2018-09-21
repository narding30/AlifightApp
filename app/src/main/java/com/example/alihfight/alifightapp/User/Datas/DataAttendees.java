package com.example.alihfight.alifightapp.User.Datas;

public class DataAttendees {

    private String Fullname;
    private String PackageCount;
    private String UserID;
    private String Key;
    private String SessionsCount;


    public DataAttendees(){

    }

    public DataAttendees(String Fullname, String PackageCount, String UserID, String Key, String SessionsCount){
        this.Fullname = Fullname;
        this.PackageCount = PackageCount;
        this.UserID = UserID;
        this.Key = Key;
        this.SessionsCount = SessionsCount;
    }

    public String getSessionsCount() {
        return SessionsCount;
    }

    public void setSessionsCount(String sessionsCount) {
        SessionsCount = sessionsCount;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getPackageCount() {
        return PackageCount;
    }

    public void setPackageCount(String packageCount) {
        PackageCount = packageCount;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
