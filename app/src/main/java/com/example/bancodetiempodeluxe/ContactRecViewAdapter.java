package com.example.bancodetiempodeluxe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
//Esto es el adapter
//En este caso a difwerencia de list view este adaptador se debe crear desde 0

public class ContactRecViewAdapter extends RecyclerView.Adapter<ContactRecViewAdapter.ViewHolder>{//Se necesitan los metos obligatoriaos de abajo
    private ArrayList<Contact> contacts=new ArrayList<>();//Se crea una lista del tipo objeto Contact
    private Context contex;
    public ContactRecViewAdapter(Context contex){
        this.contex=contex;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//El paremtro viewgrup es pare generar un conjunto layout,ste es el padre de todos los layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ofert_list_item,parent,false);//para sacar elViewHolder,aque se relaciona con el Xml de contact lsit item
        ViewHolder holder=new ViewHolder(view);//se crea una istancia de la viewHoler class para cadda elemento en nuestra recicler view
        return holder;//Este return lo mandara a inBingViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {//Aqui se yulizann las propiedades de nuestro view holder classImportantePARA TENER ACCEDO A A LOS ATRIBUTOTMS NECESITAMSO ISNTANCIAR LOS ELEMENTOS EN RELATIVE LAYOUT
        holder.txtName.setText(contacts.get(position).getName());
        holder.txtJob.setText(contacts.get(position).getJob());
        holder.txtDispDate.setText(contacts.get(position).getDate());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contex, contacts.get(position).getJob()+" Selected", Toast.LENGTH_SHORT).show();
            }
        });
        Glide.with(contex)//para pasar la imagen
                .asBitmap()
                .load(contacts.get(position).getImageUrl())//sacar el atrributo del objeto
                .into(holder.image);//En que atributop lo quieres poner
    }

    @Override
    public int getItemCount() {
        return contacts.size();//regesa  el tama;o del arraylist
    }

    public void setContacts(ArrayList<Contact> contacts) {//seter para el arrayList,aqui se pasaran la info
        this.contacts = contacts;
        notifyDataSetChanged();//De estqa manera se refresca al Recicler View de que anda cabienado constantemete los datos,se le notifica al adaptador
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{//V a mantedner la view para cada elemetnto en nuestra recicler vie,mantine los view objects
        private TextView txtName,txtJob,txtDispDate;//Aqui se meten todos los elementos UI que quierar en tu recicler view,en si los mantiene las views
        private ImageView image;
        private CardView parent;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            txtName=itemView.findViewById(R.id.txtName);//Como no estamos en la main activity debemos utilizar el parametro item view
            parent=itemView.findViewById(R.id.parent);
            txtJob=itemView.findViewById(R.id.txtJob);
            txtDispDate=itemView.findViewById(R.id.txtDispDate);
            image=itemView.findViewById(R.id.image);
        }
    }
}

