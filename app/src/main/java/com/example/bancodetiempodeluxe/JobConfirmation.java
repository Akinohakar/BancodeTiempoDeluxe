package com.example.bancodetiempodeluxe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class JobConfirmation extends AppCompatActivity {
    private Intent intent;
    private TextInputEditText hirerName, providerName, mJob;
    private TextView jobDesc, waitingTextV;
    private Button completeJob, cancelJob;
    private DatabaseReference mJobDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_confirmation);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        intent = getIntent();
        String JID = intent.getStringExtra("JID");
        String NID = intent.getStringExtra("NID");

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        hirerName = findViewById(R.id.hirerName);
        providerName = findViewById(R.id.providerName);
        mJob = findViewById(R.id.jobConf);
        jobDesc = findViewById(R.id.descJobConf);
        waitingTextV = findViewById(R.id.waitingTextV);
        waitingTextV.setVisibility(View.GONE);
        completeJob = findViewById(R.id.completeJobConf);
        cancelJob = findViewById(R.id.cancelJobConf);

        mJobDatabase = FirebaseDatabase.getInstance().getReference().child("Jobs in progress").child(JID);
        mJobDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String display_hName = snapshot.child("nameuserhire").getValue().toString();
                String display_sName = snapshot.child("nameusersupplier").getValue().toString();
                String display_job = snapshot.child("job").getValue().toString();

                hirerName.setText(display_hName);
                providerName.setText(display_sName);
                mJob.setText(display_job);

                DatabaseReference mSupplierDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(snapshot.child("idusersupplier").getValue().toString());
                mSupplierDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshotS) {
                        String display_jobDesc = snapshotS.child("jobdesc").getValue().toString();

                        jobDesc.setText(display_jobDesc);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if(mCurrentUser.getUid().toString().equals(snapshot.child("iduserhire").getValue().toString())){
                    //Usuario actual es el solicitante

                    if(snapshot.child("hirerstatus").getValue(Integer.class) == 1){
                        completeJob.setVisibility(View.GONE);
                        cancelJob.setVisibility(View.GONE);
                        waitingTextV.setVisibility(View.VISIBLE);
                    }
                }
                if(mCurrentUser.getUid().toString().equals(snapshot.child("idusersupplier").getValue().toString())){
                    //Usuario actual es el proveedor

                    if(snapshot.child("providerstatus").getValue(Integer.class) == 1){
                        completeJob.setVisibility(View.GONE);
                        cancelJob.setVisibility(View.GONE);
                        waitingTextV.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        completeJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mCurrentDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid().toString());
                mCurrentDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("Actual Job").child(JID).child("Rol").getValue().toString().equals("hirer")){
                            mJobDatabase.child("hirerstatus").setValue(1);
                            mJobDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshotJ) {
                                    if(snapshotJ.child("providerstatus").getValue(Integer.class) == 1){
                                        HashMap<String, String> jobMap = new HashMap<>();
                                        jobMap.put("iduserhire",snapshotJ.child("iduserhire").getValue().toString());
                                        jobMap.put("idusersupplier",snapshotJ.child("idusersupplier").getValue().toString());
                                        jobMap.put("nameuserhire",snapshotJ.child("nameuserhire").getValue().toString());
                                        jobMap.put("nameusersupplier",snapshotJ.child("nameusersupplier").getValue().toString());
                                        jobMap.put("status","completed");
                                        jobMap.put("rating",snapshotJ.child("rating").getValue().toString());
                                        jobMap.put("date",snapshotJ.child("date").getValue().toString());
                                        jobMap.put("hour",snapshotJ.child("hour").getValue().toString());
                                        jobMap.put("job",snapshotJ.child("job").getValue().toString());

                                        DatabaseReference trabajoCont = mCurrentDatabase.child("Trabajos Contratados").push();
                                        trabajoCont.setValue(jobMap);

                                        DatabaseReference trabajoReal = FirebaseDatabase.getInstance().getReference().child("Users").child(snapshotJ.child("idusersupplier").getValue().toString()).child("Trabajos Realizados").push();
                                        trabajoReal.setValue(jobMap);

                                        mCurrentDatabase.child("Actual Job").removeValue();
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(snapshotJ.child("idusersupplier").getValue().toString()).child("Actual Job").removeValue();

                                        mCurrentDatabase.child("notifications").child(NID).removeValue();

                                        mJobDatabase.removeValue();

                                        Toast.makeText(JobConfirmation.this, "Trabajo completado exitosamente", Toast.LENGTH_LONG).show();

                                        Intent comeBack = new Intent(JobConfirmation.this, MainMenu.class);
                                        startActivity(comeBack);
                                        finish();
                                    }
                                    else{
                                        mCurrentDatabase.child("notifications").child(NID).removeValue();

                                        Toast.makeText(JobConfirmation.this, "Esperando la confirmación de la otra parte", Toast.LENGTH_LONG).show();

                                        Intent comeBack = new Intent(JobConfirmation.this, MainMenu.class);
                                        startActivity(comeBack);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        if(snapshot.child("Actual Job").child(JID).child("Rol").getValue().toString().equals("supplier")){
                            mJobDatabase.child("providerstatus").setValue(1);
                            mJobDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshotJ) {
                                    if(snapshotJ.child("hirerstatus").getValue(Integer.class) == 1){
                                        HashMap<String, String> jobMap = new HashMap<>();
                                        jobMap.put("iduserhire",snapshotJ.child("iduserhire").getValue().toString());
                                        jobMap.put("idusersupplier",snapshotJ.child("idusersupplier").getValue().toString());
                                        jobMap.put("nameuserhire",snapshotJ.child("nameuserhire").getValue().toString());
                                        jobMap.put("nameusersupplier",snapshotJ.child("nameusersupplier").getValue().toString());
                                        jobMap.put("status","completed");
                                        jobMap.put("rating",snapshotJ.child("rating").getValue().toString());
                                        jobMap.put("date",snapshotJ.child("date").getValue().toString());
                                        jobMap.put("hour",snapshotJ.child("hour").getValue().toString());
                                        jobMap.put("job",snapshotJ.child("job").getValue().toString());

                                        DatabaseReference trabajoReal = mCurrentDatabase.child("Trabajos Realizados").push();
                                        trabajoReal.setValue(jobMap);

                                        DatabaseReference trabajoCont = FirebaseDatabase.getInstance().getReference().child("Users").child(snapshotJ.child("iduserhire").getValue().toString()).child("Trabajos Contratados").push();
                                        trabajoCont.setValue(jobMap);

                                        mCurrentDatabase.child("Actual Job").removeValue();
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(snapshotJ.child("iduserhire").getValue().toString()).child("Actual Job").removeValue();

                                        mCurrentDatabase.child("notifications").child(NID).removeValue();

                                        mJobDatabase.removeValue();

                                        Toast.makeText(JobConfirmation.this, "Trabajo completado exitosamente", Toast.LENGTH_LONG);

                                        Intent comeBack = new Intent(JobConfirmation.this, MainMenu.class);
                                        startActivity(comeBack);
                                        finish();
                                    }
                                    else{
                                        mCurrentDatabase.child("notifications").child(NID).removeValue();

                                        Toast.makeText(JobConfirmation.this, "Esperando la confirmación de la otra parte", Toast.LENGTH_LONG);

                                        Intent comeBack = new Intent(JobConfirmation.this, MainMenu.class);
                                        startActivity(comeBack);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
}