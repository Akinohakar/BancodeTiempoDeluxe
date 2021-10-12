package com.example.bancodetiempodeluxe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterDatosTransacciones extends RecyclerView.Adapter<AdapterDatosTransacciones.ViewHolderDatos> {

    ArrayList<TransaccionesModel> transaccionesRealizadas, transaccionesContratadas;

    public AdapterDatosTransacciones(ArrayList<TransaccionesModel> transaccionesContratadas, ArrayList<TransaccionesModel> transaccionesRealizadas){
        this.transaccionesContratadas = transaccionesContratadas;
        this.transaccionesRealizadas = transaccionesRealizadas;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transacciones_item_list, null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        if(position < transaccionesContratadas.size()){
            holder.asignarDatos(transaccionesContratadas.get(position));
        }else{
            holder.asignarDatos(transaccionesRealizadas.get(position - transaccionesContratadas.size()));
        }

    }

    @Override
    public int getItemCount() {
        return transaccionesContratadas.size() + transaccionesRealizadas.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView fecha;
        TextView hora;
        TextView user;
        TextView work;
        TextView status;
        TextView contrato;
        ImageView imgStatus;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            fecha    = (TextView) itemView.findViewById(R.id.idTransFecha);
            hora     = (TextView) itemView.findViewById(R.id.idTransHora);
            user     = (TextView) itemView.findViewById(R.id.idTransUser);
            work     = (TextView) itemView.findViewById(R.id.idTransWork);
            status   = (TextView) itemView.findViewById(R.id.idTransStatus);
            contrato = (TextView) itemView.findViewById(R.id.idTransContract);
            imgStatus= (ImageView) itemView.findViewById(R.id.idTransIMGStatus);
        }

        public void asignarDatos(TransaccionesModel tModel) {
            contrato.setText(tModel.getRating());
            fecha.setText(tModel.getDate());
            hora.setText(tModel.getHour());
            user.setText(tModel.getNameusersupplier());
            work.setText(tModel.getJob());
            if(tModel.status.equals("cancelled")){
                status.setText("Cancelado");
                imgStatus.setImageResource(R.drawable.ic_cancel);
            }else if(tModel.status.equals("completed")){
                status.setText("Completado");
                imgStatus.setImageResource(R.drawable.ic_check);
            }else{
                imgStatus.setImageResource(R.drawable.ic_proceso);
                if(tModel.getStatus().equals("sent")){
                    status.setText("Solicitud En Espera");
                }
                if(tModel.getStatus().equals("onProgress")){
                    status.setText("En Progreso");
                }
            }
        }
    }
}

