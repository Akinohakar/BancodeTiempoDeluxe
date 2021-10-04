package com.example.bancodetiempodeluxe;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentSearch extends Fragment {
    private RecyclerView mUserList;
    private DatabaseReference mUsersDatabase;
    private EditText searchEngine;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragmentSearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmentSearch newInstance(String param1, String param2) {
        fragmentSearch fragment = new fragmentSearch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    //The important Stuff

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_search, container, false);//Inflate fragment layout
        super.onCreate(savedInstanceState);

        //For normar recicler view,in tis case we are using firebase ui recicler view
        //contactsRecView=view.findViewById(R.id.contactsRecView);//Se isntancia aqui
        //ArrayList<Contact> contacts=new ArrayList<>();
        //contacts.add(new Contact("Contacto 1","Toma@tomassitometonatiu.com","https://s1.1zoom.me/big3/471/Painting_Art_Back_view_Photographer_575380_3840x2400.jpg","Plomeria", "L M M J V S D"));
        //contacts.add(new Contact("Contacto 2","Toma@tomassitometonatiu.com","https://www.google.com.mx/","Carpinteria","L M M J V S D"));
        //contacts.add(new Contact("Contacto 3","Toma@tomassitometonatiu.com","https://www.google.com.mx/","Tineria","L M M J V S D"));
        //contacts.add(new Contact("Contacto 4","Toma@tomassitometonatiu.com","https://www.google.com.mx/","Programacion","L M M J V S D"));
        //contacts.add(new Contact("Contacto 5","Toma@tomassitometonatiu.com","https://www.google.com.mx/","Director de cine","L M M J V S D"));
        //contacts.add(new Contact("Contacto 6","Toma@tomassitometonatiu.com","https://www.google.com.mx/","Derecho","L M M J V S D"));
        //contacts.add(new Contact("Contacto 7","Toma@tomassitometonatiu.com","https://www.google.com.mx/","Asesorias","L M M J V S D"));
        ////Lista de contactos
        //ContactRecViewAdapter adapter=new ContactRecViewAdapter(getActivity());//se hace el adater o se intancia,el aprameto que se las pasa el constructor es el contecto de la main Activity
        //adapter.setContacts(contacts);//se le pone el arraylist
        //contactsRecView.setAdapter(adapter);
        ////contactsRecView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));//El linear leyourt es para indicar que los itesm va a estar en forma de Linear
        //contactsRecView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        //Firebase UI Recicler View


        mUserList= view.findViewById(R.id.contactsRecView);
        searchEngine=view.findViewById(R.id.editTextTypeJob);
        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users");//reference
        //mUserList.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mUserList.setLayoutManager(new GridLayoutManager(getActivity(),2));

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();


        Query query=mUsersDatabase.orderByChild("rating");



        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(query, Users.class).build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users users) {

                    holder.setName(users.getName());
                    holder.setJob(users.getJobtitle());
                    holder.SetThumbImage(users.getImage());
                    holder.setDate(users.getDatejob());
                    Log.d("TAG", "--------------");
                    Log.d("TAG", "users.getName() : " + users.getName());
                    Log.d("TAG", "users.getStatus() : " + users.getStatus());
                    Log.d("TAG", "users.getThumb_image() : " + users.getImage());


                final String selected_user_id = getRef(position).getKey();
//                Set event for component
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent= new Intent(getActivity(),offerDescription.class);
                        profileIntent.putExtra("user_id",selected_user_id);
                        startActivity(profileIntent);
                    }
                });
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ofert_list_item, parent, false);
                return new UsersViewHolder(view);
            }

        };
        mUserList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setName(String name){
            TextView userNameView=(TextView) mView.findViewById(R.id.txtName);
            userNameView.setText(name);

        }
        public void setStatus(String status){

        }
        public void SetThumbImage(String url_avatar){
            ImageView userAvatarView = mView.findViewById(R.id.list_item_image);
//                if user hadn't set avatar display default avatar
            if(!url_avatar.equals("default")){
                Picasso.get().load(url_avatar).placeholder(R.drawable.exampleuser).into(userAvatarView);
            }
        }
        public void setJob(String jobtitle){
            TextView userstatusView = mView.findViewById(R.id.txtJob);
            userstatusView.setText(jobtitle);
        }
        public  void setDate(String datejob){
            TextView datejobView=mView.findViewById(R.id.txtDispDate);
            datejobView.setText(datejob);
        }


    }

}