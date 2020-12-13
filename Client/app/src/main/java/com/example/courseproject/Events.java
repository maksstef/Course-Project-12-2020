package com.example.courseproject;

public class Events {
    private int Eid;
    private int Cid;
    private String Title;
    private String Description;
    private String Date;

    public Events(int e_id, int c_id, String title, String description, String date){
        this.Eid = e_id;
        this.Cid = c_id;
        this.Title = title;
        this.Description = description;
        this.Date = date;
    }

    public int getEid() {
        return Eid;
    }

    public void setEid(int eid) {
        Eid = eid;
    }

    public int getCid() {
        return Cid;
    }

    public void setCid(int cid) {
        Cid = cid;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
