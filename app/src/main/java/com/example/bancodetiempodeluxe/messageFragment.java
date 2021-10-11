package com.example.bancodetiempodeluxe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link messageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class messageFragment extends Fragment {
    private RecyclerView mMessageList;
    private DatabaseReference mMessageDatabase;
    private FirebaseAuth mAuth;
    private String current_user;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public messageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment messageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static messageFragment newInstance(String param1, String param2) {
        messageFragment fragment = new messageFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_message, container, false);//Inflate fragment layout

        super.onCreate(savedInstanceState);
        mMessageList=view.findViewById(R.id.message_list);
        mMessageDatabase=FirebaseDatabase.getInstance().getReference().child("Users");
        mMessageList.setLayoutManager(new LinearLayoutManager(getActivity()));



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query=mMessageDatabase;

        //RECUCLERVIEW FIREBASE UI
        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(query, Users.class).build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users users) {

                holder.setName(users.getName());
                holder.setJob(users.getJobtitle());
                holder.SetThumbImage(users.getImage());

                Log.d("TAG", "--------------");
                Log.d("TAG", "users.getName() : " + users.getName());
                Log.d("TAG", "users.getStatus() : " + users.getStatus());
                Log.d("TAG", "users.getThumb_image() : " + users.getImage());


                final String selected_user_id = getRef(position).getKey();
                //PARA SELECCIONAR LAS OPCIONES.
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[]{"Abrir Perfil","Enviar Mensaje"};
                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        builder.setTitle("Selecciona la opcion");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i==0){
                                    Intent profile=new Intent(getActivity(),offerDescription.class);
                                    profile.putExtra("user_id",selected_user_id);
                                    startActivity(profile);

                                }else if(i==1){
                                    Intent chat=new Intent(getActivity(),ChatActivity.class);
                                    chat.putExtra("user_id",selected_user_id);
                                    startActivity(chat);


                                }
                            }
                        });
                        builder.show();




                    }
                });
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_single_layout, parent, false);
                return new UsersViewHolder(view);
            }

        };
        mMessageList.setAdapter(adapter);
        adapter.startListening();
        super.onStart();


    }
    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setName(String name){
            TextView userNameView=(TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }
        public void setStatus(String status){

        }
        public void SetThumbImage(String url_avatar){
            ImageView userAvatarView = mView.findViewById(R.id.user_single_image);
//                if user hadn't set avatar display default avatar
            if(!url_avatar.equals("default")){
                Picasso.get().load(url_avatar).placeholder(R.drawable.exampleuser).into(userAvatarView);
            }
        }
        public void setJob(String jobtitle){
            TextView userstatusView = mView.findViewById(R.id.user_single_status);
            userstatusView.setText(jobtitle);
        }
        public  void setDate(String datejob){
            TextView datejobView=mView.findViewById(R.id.txtDispDate);
            datejobView.setText(datejob);
        }


    }


}