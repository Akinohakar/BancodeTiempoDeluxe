package com.example.bancodetiempodeluxe;

public class TransaccionesModel {
    public String date;
    public String hour;
    public String iduserhire;
    public String idusersupplier;
    public String job;
    public String nameuserhire;
    public String nameusersupplier;
    public String rating;
    public String status;

    public TransaccionesModel(){

    }
    public TransaccionesModel(String date, String hour, String iduserhire, String idusersupplier, String job, String nameuserhire, String nameusersupplier, String rating, String status){
        this.date   = date;
        this.hour    = hour;
        this.iduserhire = iduserhire;
        this.idusersupplier = idusersupplier;
        this.job  = job;
        this.nameuserhire = nameuserhire;
        this.nameusersupplier = nameusersupplier;
        this.rating = rating;
        this.status = status;
    }

    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }

    public String getHour(){
        return hour;
    }
    public void setHour(String hour){
        this.hour = hour;
    }

    public String getIduserhire(){
        return iduserhire;
    }
    public void setIduserhire(String iduserhire){
        this.iduserhire = iduserhire;
    }

    public String getIdusersupplier(){
        return idusersupplier;
    }
    public void setIdusersupplier(String idusersupplier){
        this.idusersupplier = idusersupplier;
    }

    public String getJob(){
        return job;
    }
    public void setJob(String job){
        this.job = job;
    }

    public String getNameuserhire(){
        return nameuserhire;
    }
    public void setNameuserhire(String nameuserhire){
        this.nameuserhire = nameuserhire;
    }

    public String getNameusersupplier(){
        return nameusersupplier;
    }
    public void setNameusersupplier(String nameusersupplier){
        this.nameusersupplier = nameusersupplier;
    }

    public String getRating(){
        return rating;
    }
    public void setRating(String rating){
        this.rating = rating;
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }

}
