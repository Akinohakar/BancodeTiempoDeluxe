package com.example.bancodetiempodeluxe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterDatosTransacciones extends RecyclerView.Adapter<AdapterDatosTransacciones.ViewHolderDatos> {

    ArrayList<String> listTransacciones;

    public AdapterDatosTransacciones(ArrayList<String> listTransacciones){
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
        TextView dato;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            dato = (TextView) itemView.findViewById(R.id.idDatosTransacciones);

        }

        public void asignarDatos(String s) {
            dato.setText(s);
        }
    }
}
