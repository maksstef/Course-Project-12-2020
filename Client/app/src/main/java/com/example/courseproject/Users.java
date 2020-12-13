package com.example.courseproject;

public class Users {
    private int UId;
    private String Name;
    private String Login;
    private String Password;
    private String Phone;
    private String Email;

    public Users() {
    }

    public Users(int UId, String name, String login, String password, String phone, String email) {
        this.UId = UId;
        Name = name;
        Login = login;
        Password = password;
        Phone = phone;
        Email = email;
    }

    public int getUId() {
        return UId;
    }

    public void setUId(int UId) {
        this.UId = UId;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
