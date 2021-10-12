package com.example.bancodetiempodeluxe;

public class CustomUser {
    public String address;
    public String age;
    public String balance;
    public String datejob;
    public String image;
    public String jobdesc;
    public String jobtitle;
    public String name;
    public String phone;
    public String pronoun;
    public String rating;
    public String role;
    public String status;
    public String thumb_image;

    public String key;

    public CustomUser(String address, String age, String balance, String datejob, String image, String jobdesc, String jobtitle, String name, String phone, String pronoun, String rating, String role, String status, String thumb_image) {
        this.address = address;
        this.age = age;
        this.balance = balance;
        this.datejob = datejob;
        this.image = image;
        this.jobdesc = jobdesc;
        this.jobtitle = jobtitle;
        this.name = name;
        this.phone = phone;
        this.pronoun = pronoun;
        this.rating = rating;
        this.role = role;
        this.status = status;
        this.thumb_image = thumb_image;
    }

    public CustomUser(){

    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDatejob() {
        return datejob;
    }

    public void setDatejob(String datejob) {
        this.datejob = datejob;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJobdesc() {
        return jobdesc;
    }

    public void setJobdesc(String jobdesc) {
        this.jobdesc = jobdesc;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPronoun() {
        return pronoun;
    }

    public void setPronoun(String pronoun) {
        this.pronoun = pronoun;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public void setHired_Jobs(Object obj){

    }

    public void setWorked_Jobs(Object obj){

    }

    public void setNotifications(Object obj){

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "CustomUser{" +
                "datejob='" + datejob + '\'' +
                ", image='" + image + '\'' +
                ", jobtitle='" + jobtitle + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
