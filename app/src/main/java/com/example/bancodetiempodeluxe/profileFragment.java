package com.example.bancodetiempodeluxe;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileFragment extends Fragment {
    private FloatingActionButton fab;
    //Database
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    //View Elements
    private TextView userStatus,userName,userRating,userActualJob,userAge,userPronoun,userJobDesc;
    private ImageView userImage;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
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
        ViewGroup view= (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);//Inflate fragment layout
        //Listing the view elements
        userStatus=view.findViewById(R.id.profile_user_status);
        userName=view.findViewById(R.id.profile_user_name);
        userImage=view.findViewById(R.id.profile_image);
        userActualJob=view.findViewById(R.id.profileWorkTitle);
        userRating=view.findViewById(R.id.profileActualRating);
        userAge=view.findViewById(R.id.profileAge);
        userPronoun=view.findViewById(R.id.profilePronoun);
        userJobDesc=view.findViewById(R.id.profileJobDesc);
        //To retrive the data
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String current_uid=mCurrentUser.getUid();
        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);//pOINTING TO THE USERS OBJECT and to the id object
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//Add data or retrive data
                String name=snapshot.child("name").getValue().toString();
                String image=snapshot.child("image").getValue().toString();
                String status=snapshot.child("status").getValue().toString();
                String thumb_image=snapshot.child("thumb_image").getValue().toString();
                String number=snapshot.child("phone").getValue().toString();
                String work=snapshot.child("jobtitle").getValue().toString();
                String rating=snapshot.child("rating").getValue().toString();
                String pronoun=snapshot.child("pronoun").getValue().toString();
                String jobdesc=snapshot.child("jobdesc").getValue().toString();
                String edad=snapshot.child("age").getValue().toString();
                userName.setText(name);
                userRating.setText(rating);
                userActualJob.setText(work);
                userPronoun.setText("Pronombres: "+pronoun);
                userJobDesc.setText(jobdesc);
                userAge.setText("Edad: "+edad);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {//For errors

            }
        });


        fab=view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_value=userName.toString();
                Intent intent=new Intent(getActivity(),editProfile.class);
                intent.putExtra("name",name_value);
                startActivity(intent);
            }
        });
        return view;
    }
}