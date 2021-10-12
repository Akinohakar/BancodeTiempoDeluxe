package com.example.bancodetiempodeluxe;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterSearchCustom extends RecyclerView.Adapter<AdapterSearchCustom.ViewHolderDatos> implements Filterable {

    Context context;
    ArrayList<CustomUser> users;
    ArrayList<CustomUser> usersFull;

    public AdapterSearchCustom(Context context,ArrayList <CustomUser>users){
        this.context = context;
        this.usersFull = users;
        this.users = new ArrayList<>(usersFull);

    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ofert_list_item,parent,false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos(context, users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private final Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<CustomUser> filteredUsers = new ArrayList<>();

            if(charSequence == null || charSequence.length() <= 0){
                //Log.d("Filtro Vacío: ", "Entrando a filtro vacío");
                filteredUsers.addAll(usersFull);
            }
            else{
                String filterPattern = charSequence.toString().toLowerCase(Locale.ROOT).trim();

                for(CustomUser customUser : usersFull){

                    if(customUser.getJobtitle().toLowerCase(Locale.ROOT).contains(filterPattern)){
                        filteredUsers.add(customUser);
                    }

                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredUsers;
            results.count  = filteredUsers.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            users.clear();
            //Log.d("Filter results: ", String.valueOf(filterResults.count));
            users.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class ViewHolderDatos extends RecyclerView.ViewHolder{
        CircleImageView profilePic;
        TextView contactName;
        TextView days;
        TextView job;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            profilePic  = itemView.findViewById(R.id.list_item_image);
            contactName = itemView.findViewById(R.id.txtName);
            days        = itemView.findViewById(R.id.txtDispDate);
            job         = itemView.findViewById(R.id.txtJob);



        }

        public void asignarDatos(Context context, CustomUser user){
            String url_avatar = user.getImage();
            if(!url_avatar.equals("default")){
                Picasso.get().load(url_avatar).placeholder(R.drawable.exampleuser).into(profilePic);
            }
            contactName.setText(user.getName());
            days.setText(user.getDatejob());
            job.setText(user.getJobtitle());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profileIntent= new Intent(context,offerDescription.class);
                    profileIntent.putExtra("user_id", user.getKey());
                    context.startActivity(profileIntent);
                }
            });

        }

    }
}
