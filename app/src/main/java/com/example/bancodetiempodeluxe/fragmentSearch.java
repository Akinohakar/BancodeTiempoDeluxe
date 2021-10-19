package com.example.bancodetiempodeluxe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentSearch extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference mUsersDatabase;
    private AdapterSearchCustom adapter;
    private ArrayList<CustomUser> usuarios;
    private FirebaseAuth mAuth;
    private String current_id;

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

        recyclerView = view.findViewById(R.id.contactsRecView);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        searchEngine = view.findViewById(R.id.editTextTypeJob);

        searchEngine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(null);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(null);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(editable.toString());
                adapter.notifyDataSetChanged();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {//If user is not sign in
            getData();
        }



        return view;
    }

    public void getData(){
        mAuth = FirebaseAuth.getInstance();
        current_id = mAuth.getCurrentUser().getUid();

        usuarios = new ArrayList<>();
        adapter = new AdapterSearchCustom(getActivity(),usuarios);

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CustomUser user = new CustomUser();
                    if (!dataSnapshot.getKey().equals(current_id)) {
                        user.setKey(dataSnapshot.getKey());

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            if (dataSnapshot1.getKey().equals("name")) {
                                user.setName(dataSnapshot1.getValue(String.class));
                                usuarios.add(user);
                                adapter.notifyDataSetChanged();
                                //Log.d("Added: ", user.toString());
                            }
                            if (dataSnapshot1.getKey().equals("image")) {
                                user.setImage(dataSnapshot1.getValue(String.class));
                            }
                            if (dataSnapshot1.getKey().equals("jobtitle")) {
                                user.setJobtitle(dataSnapshot1.getValue(String.class));
                            }
                            if (dataSnapshot1.getKey().equals("datejob")) {
                                user.setDatejob(dataSnapshot1.getValue(String.class));
                            }
                        }

                    }
                }

                adapter.getFilter().filter(null);

                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }


//private RecyclerView mUserList;
//FirebaseRecyclerAdapter adapter;
//FirebaseRecyclerAdapter mAdapter;
//FirebaseRecyclerOptions<Users> options;


    //Firebase UI Recicler View
        /*mUserList= view.findViewById(R.id.contactsRecView);
        searchEngine=view.findViewById(R.id.editTextTypeJob);
        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users");//reference
        mUserList.setLayoutManager(new GridLayoutManager(getActivity(),2));
        lupaBuscar = view.findViewById(R.id.searchButtonLupa);*/
    //mUserList.setLayoutManager(new GridLayoutManager(getActivity(),2));

/*Query query=mUsersDatabase.orderByKey();
        options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(query, Users.class).build();

         adapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {
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
        adapter.startListening();*/


}

    /*public static class UsersViewHolder extends RecyclerView.ViewHolder{
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


    }*/