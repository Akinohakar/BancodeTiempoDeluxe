package com.example.bancodetiempodeluxe;

public class Notification {
    private String type;
    private int status;

    public Notification(String type, int status) {
        this.type = type;
        this.status = status;
    }

    public Notification(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
