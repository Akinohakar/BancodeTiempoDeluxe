package com.example.bancodetiempodeluxe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class editProfile extends AppCompatActivity {
    private TextInputLayout editName,editAge,editPronouns,editPhone,editAdr,editJob,editDescJob;
    private Button submitChanges;
    private DatabaseReference mStatusDatabse;
    private FirebaseUser mCurrentUser;
    private FloatingActionButton editProfilePic;

    //Progress
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_profile);
        //Finding Views
        editName=findViewById(R.id.editName);
        submitChanges=findViewById(R.id.submitChangesButton);
        editAge=findViewById(R.id.editDate);
        editPronouns=findViewById(R.id.editPronombres);
        editPhone=findViewById(R.id.editNoTel);
        editAdr=findViewById(R.id.editDirect);
        editJob=findViewById(R.id.editJob);
        editDescJob=findViewById(R.id.editJobDesc);
        String name_value=getIntent().getStringExtra("name_value");
        editName.getEditText().setText(name_value);

        //Firebase
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String current_uid=mCurrentUser.getUid();
          mStatusDatabse= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        submitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress=new ProgressDialog(editProfile.this);

                mProgress.setTitle("Guardando cambios");
                mProgress.setMessage("Porfavor espero en lo que se guarda el mensaje");
                mProgress.show();
                //String geters
                String name=editName.getEditText().getText().toString();
                String age=editAge.getEditText().getText().toString();
                String pronouns=editPronouns.getEditText().getText().toString();
                String phone=editPhone.getEditText().getText().toString();
                String adrs=editAdr.getEditText().getText().toString();
                String job=editJob.getEditText().getText().toString();
                String descjob=editDescJob.getEditText().getText().toString();
                mStatusDatabse.child("address").setValue(adrs);
                mStatusDatabse.child("jobtitle").setValue(job);
                mStatusDatabse.child("phone").setValue(phone);
                mStatusDatabse.child("pronoun").setValue(pronouns);
                mStatusDatabse.child("jobdesc").setValue(descjob);
                mStatusDatabse.child("age").setValue(age);


                mStatusDatabse.child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mProgress.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(), "Ups no quiero cambairme : )", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }
}