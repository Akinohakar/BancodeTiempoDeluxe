package com.example.bancodetiempodeluxe;

public class Contact {
    private String name;
    private String email;
    private String imageUrl;
    private String job;
    private String date;
    public Contact(String name,String email,String imageUrl,String job,String date){
        this.name=name;
        this.email=email;
        this.imageUrl=imageUrl;
        this.job=job;
        this.date=date;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", job='" + job + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
