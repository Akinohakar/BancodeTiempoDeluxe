package com.example.bancodetiempodeluxe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class offerDescription extends AppCompatActivity {
    private ImageView mProfileImage, mBack;
    private TextView mProfileName,mProfileJobTitle,mProfileJobDesc,mProfileState,mProfileDateJob;
    private Button mProfileDoRequest;
    private DatabaseReference mUsersDatabase,mServiceRequestDatabase,mThisUser;
    private ProgressDialog mProgressDialog;
    private FirebaseUser mCurrent_user;
    private int mcurrent_state;
    private Date dateintancer= Calendar.getInstance().getTime();
    private SimpleDateFormat simpleDate=new SimpleDateFormat("dd-MMM-YYYY", Locale.getDefault());
    private String date=simpleDate.format(dateintancer);
    private SimpleDateFormat simplehour=new SimpleDateFormat("HH:mm",Locale.getDefault());
    private String hour=simplehour.format(dateintancer);
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_description);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String user_id=getIntent().getStringExtra("user_id");
        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mServiceRequestDatabase=FirebaseDatabase.getInstance().getReference().child("Jobs in progress");


        mCurrent_user= FirebaseAuth.getInstance().getCurrentUser();
        mThisUser=FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrent_user.getUid().toString());
        mProfileImage=findViewById(R.id.offer_userimg);
        mProfileName=findViewById(R.id.offer_username);
        mProfileJobTitle=findViewById(R.id.offer_job_title);
        mProfileJobDesc=findViewById(R.id.offer_job_descip);
        mProfileDoRequest=findViewById(R.id.offer_button_agree);
        mProfileState=findViewById(R.id.offer_verificado_state);
        mProfileDateJob=findViewById(R.id.offer_datejob);
        mBack = findViewById(R.id.volverBuscar);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainMenu.class));
            }
        });

        mcurrent_state=0;

        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setTitle("Cargando datos del usuario");
        mProgressDialog.setMessage("Porfavor espere en lo que se carga la informacion");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        //States:
        //0 no se ha enviado una solicitud de servicio
        //1 ya se ha enviado una solicitud de servicio
        //2 se recivio una solicitud de servicio
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String display_name=snapshot.child("name").getValue().toString();
                String status=snapshot.child("status").getValue().toString();
                String image =snapshot.child("image").getValue().toString();
                String job=snapshot.child("jobtitle").getValue().toString();
                String jobdesc=snapshot.child("jobdesc").getValue().toString();
                String jobdate=snapshot.child("datejob").getValue().toString();
                mProfileName.setText(display_name);
                mProfileState.setText(status);
                mProfileJobTitle.setText(job);
                mProfileJobDesc.setText(jobdesc);
                mProfileDateJob.setText(jobdate);
                Picasso.get().load(image).placeholder(R.drawable.exampleuser).into(mProfileImage);
                mProgressDialog.dismiss();
                //--------------------------Service Request Feature-----------------
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       mProfileDoRequest.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               DatabaseReference pushedRef=mServiceRequestDatabase.push();
               mThisUser.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                        name=snapshot.child("name").getValue().toString();


                       HashMap<String,String> jobMap=new HashMap<>();
                       jobMap.put("iduserhire",mCurrent_user.getUid());
                       jobMap.put("idusersupplier",user_id);
                       jobMap.put("nameuserhire",name);
                       jobMap.put("nameusersupplier",mProfileName.getText().toString());
                       jobMap.put("status","sent");
                       jobMap.put("rating","0.0");
                       jobMap.put("date",date);
                       jobMap.put("hour",hour);
                       jobMap.put("job",mProfileJobTitle.getText().toString());


                       pushedRef.setValue(jobMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               Toast.makeText(offerDescription.this, "Servicio Solicitado", Toast.LENGTH_SHORT).show();
                               String value_key=pushedRef.getKey();
                               System.out.println(value_key);

                           }
                       });

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
               mThisUser.child("Actual Job").child(pushedRef.getKey().toString()).child("Rol").setValue("hirer");
               mUsersDatabase.child("Actual Job").child(pushedRef.getKey().toString()).child("Rol").setValue("supplier");

               DatabaseReference refNotif = mUsersDatabase.child("notifications").push();
               mUsersDatabase.child("notifications").child(refNotif.getKey().toString()).child("type").setValue("request");
               mUsersDatabase.child("notifications").child(refNotif.getKey().toString()).child("status").setValue(0);
               mUsersDatabase.child("notifications").child(refNotif.getKey().toString()).child("job").setValue(pushedRef.getKey().toString());

               Intent intent = new Intent(offerDescription.this, MainMenu.class);
               startActivity(intent);
               finish();
           }
       });
    }
}