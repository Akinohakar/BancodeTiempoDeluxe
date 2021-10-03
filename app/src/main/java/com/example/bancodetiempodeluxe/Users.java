package com.example.bancodetiempodeluxe;

public class Users {
    public String name;
    public String image;
    public String status;
    public String jobtitle;



    public Users(String name, String image, String status, String jobtitle) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.jobtitle = jobtitle;
    }
    public Users(){

    }



    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
