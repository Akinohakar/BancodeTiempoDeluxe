package com.example.bancodetiempodeluxe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DescriptionNotificationRequest extends AppCompatActivity {
    private CircleImageView profileImgV;
    private TextInputEditText employeersName, employeerPronoun, job;
    private TextView descJob;
    private DatabaseReference mNotifsDatabase, mJobDatabase, mHirerDatabase;
    private FirebaseUser mCurrentUser;
    private Button acceptJob, rejectJob;
    private Intent intent;
    //private String pronoun, theJob, theJobDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_description_notification_request);
        intent = getIntent();
        String JID = intent.getStringExtra("JID");
        String NID = intent.getStringExtra("NID");

        profileImgV = findViewById(R.id.profile_image);
        employeersName = findViewById(R.id.employeerName);
        employeerPronoun = findViewById(R.id.employeerPronoun);
        job = findViewById(R.id.job);
        descJob = findViewById(R.id.descJob);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mNotifsDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid().toString()).child("notifications").child(NID);
        mJobDatabase = FirebaseDatabase.getInstance().getReference().child("Jobs in progress").child(JID);

        //System.out.println(mHirerDatabase.toString());

        mNotifsDatabase.child("status").setValue(1);

        mJobDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference mHirerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(snapshot.child("iduserhire").getValue().toString());

                mHirerDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshotH) {
                        String display_pronoun = snapshotH.child("pronoun").getValue().toString();
                        String display_image = snapshotH.child("image").getValue().toString();

                        Picasso.get().load(display_image).placeholder(R.drawable.exampleuser).into(profileImgV);
                        employeerPronoun.setText(display_pronoun);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference mCurrentDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid().toString());

                mCurrentDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshotC) {
                        String display_job = snapshotC.child("jobtitle").getValue().toString();
                        String display_jobDesc = snapshotC.child("jobdesc").getValue().toString();

                        job.setText(display_job);
                        descJob.setText(display_jobDesc);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                String display_name = snapshot.child("nameuserhire").getValue().toString();

                employeersName.setText(display_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        acceptJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acceptIntent = new Intent(DescriptionNotificationRequest.this, ActivityConfirmation.class);
                acceptIntent.putExtra("JID", JID);
                acceptIntent.putExtra("Status", "accepted");
                startActivity(acceptIntent);
            }
        });

        rejectJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rejectIntent = new Intent(DescriptionNotificationRequest.this, ActivityConfirmation.class);
                rejectIntent.putExtra("JID", JID);
                rejectIntent.putExtra("Status", "rejected");
                startActivity(rejectIntent);
            }
        });
    }

}