package com.example.courseproject;

public class Members {
    private int EventId;
    private int UId;

    public Members(int eventId, int UId) {
        EventId = eventId;
        this.UId = UId;
    }

    public int getEventId() {
        return EventId;
    }

    public void setEventId(int eventId) {
        EventId = eventId;
    }

    public int getUId() {
        return UId;
    }

    public void setUId(int UId) {
        this.UId = UId;
    }
}
