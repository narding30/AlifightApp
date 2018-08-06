package com.example.alihfight.alifightapp.Admin.Datas;

public class DataUser {

    private String usertype;
    private String Email;
    private String Address;
    private String Age;
    private String FirstName;
    private String Gender;
    private String LastName;
    private String MiddleName;
    private String Occupation;

    public DataUser(){

    }


    public DataUser(String usertype, String Email, String Address, String Age, String FirstName, String Gender, String LastName,
                    String MiddleName, String Occupation){
        this.usertype = usertype;
        this.Email = Email;
        this.Address = Address;
        this.Age = Age;
        this.FirstName = FirstName;
        this.Gender = Gender;
        this.LastName = LastName;
        this.MiddleName = MiddleName;
        this.Occupation = Occupation;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }
}
