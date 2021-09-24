package com.example.bancodetiempodeluxe;

public class TransaccionesModel {
    public String fecha;
    public String hora;
    public String cliente;
    public String trabajo;
    public String status;

    public TransaccionesModel(String fecha, String hora, String cliente, String trabajo, String status){
        this.fecha   = fecha;
        this.hora    = hora;
        this.cliente = cliente;
        this.trabajo = trabajo;
        this.status  = status;
    }
}
