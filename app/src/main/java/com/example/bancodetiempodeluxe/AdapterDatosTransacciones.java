package com.example.bancodetiempodeluxe;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterDatosTransacciones extends RecyclerView.Adapter<AdapterDatosTransacciones.ViewHolderDatos> {

    ArrayList<TransaccionesModel> listTransacciones;

    public AdapterDatosTransacciones(ArrayList<TransaccionesModel> listTransacciones){
        this.listTransacciones = listTransacciones;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transacciones_item_list, null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos(listTransacciones.get(position));
    }

    @Override
    public int getItemCount() {
        return listTransacciones.size();
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
            contrato.setText(tModel.contrato);
            fecha.setText(tModel.fecha);
            hora.setText(tModel.hora);
            user.setText(tModel.cliente);
            work.setText(tModel.trabajo);
            status.setText(tModel.status);
            if(tModel.status == "Cancelado"){
                imgStatus.setImageResource(R.drawable.ic_cancel);
            }else{
                imgStatus.setImageResource(R.drawable.ic_checked);
            }
        }
    }
}
