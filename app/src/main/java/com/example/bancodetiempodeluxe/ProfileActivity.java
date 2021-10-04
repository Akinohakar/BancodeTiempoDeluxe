package com.example.bancodetiempodeluxe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mProfileImage;
    private TextView mProfileName,mProfileStatus,mProfileFriendsCount;
    private Button mProfileSendReqBtn;
    private DatabaseReference mUsersDatabase;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        String user_id=getIntent().getStringExtra("user_id");
        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mProfileImage=findViewById(R.id.baseprofileimage);
        mProfileName=findViewById(R.id.baseprofile_display_name);
        mProfileStatus=findViewById(R.id.baseprofileuserstatus);
        mProfileFriendsCount=findViewById(R.id.baseprofilefriends);
        mProfileSendReqBtn=findViewById(R.id.baseprofilebuttom);
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setTitle("Cargando datos del usuario");
        mProgressDialog.setMessage("Porfavor espere en lo que se carga la informacion");
        mProgressDialog.setCanceledOnTouchOutside(false);

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String display_name=snapshot.child("name").getValue().toString();
                String status=snapshot.child("status").getValue().toString();
                String image =snapshot.child("image").getValue().toString();

                mProfileName.setText(display_name);
                mProfileStatus.setText(status);
                Picasso.get().load(image).placeholder(R.drawable.exampleuser).into(mProfileImage);

                mProgressDialog.dismiss();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}