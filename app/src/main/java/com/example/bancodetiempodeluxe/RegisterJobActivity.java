package com.example.bancodetiempodeluxe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Queue;

public class RegisterJobActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;//Intancia autenticacion Firebase
    private DatabaseReference mDatabase;
    private TextView header;
    private TextInputLayout mDisplayJob,mJobdesc,mHomeAddress;
    private Button goLogIN,goNewAccount;
    private CheckBox dayL,dayM,dayMI,dayJV,dayV,dayS,dayD;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_job);
        mAuth=FirebaseAuth.getInstance();
        //Get intances
        MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(RegisterJobActivity.this);
        builder.setTitle("Aceptar Terminos y Condiciones");
        builder.setMessage("Aqui los terminso y condiciones");
        builder.setIcon(R.drawable.ic_infogrey);

        header=findViewById(R.id.register_job_header);
        mDisplayJob=findViewById(R.id.register_job_jobtitle);
        mJobdesc=findViewById(R.id.register_job_jobdesc);
        mHomeAddress=findViewById(R.id.register_job_homeaddress);
        goLogIN=findViewById(R.id.register_job_alreadygoLogin);
        goNewAccount=findViewById(R.id.register_job_goNewAccount);
        dayL=findViewById(R.id.register_job_monday);
        dayM=findViewById(R.id.register_job_thursday);
        dayMI=findViewById(R.id.register_job_wednesday);
        dayJV=findViewById(R.id.register_job_thursday);
        dayV=findViewById(R.id.register_job_friday);
        dayS=findViewById(R.id.register_job_saturday);
        dayD=findViewById(R.id.register_job_sunday);

        //Get data from previus activity
        String name=getIntent().getStringExtra("newusername");
        String email=getIntent().getStringExtra("newuseremail");
        String pass=getIntent().getStringExtra("newuserpass");
        String tel=getIntent().getStringExtra("newusertel");
        String age=getIntent().getStringExtra("newuserage");
        String pronoun=getIntent().getStringExtra("newuserpronoun");
        //Set real header
        header.setText("Hola,"+name+"!");
        //Create account listener
        goNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Contatenate into date
                StringBuilder strDate= new StringBuilder();
                if (dayL.isChecked()){
                    strDate.append("L ");
                }
                if (dayM.isChecked()){
                    strDate.append("M ");
                }
                if (dayMI.isChecked()){
                    strDate.append("X ");

                }
                if (dayJV.isChecked()){
                    strDate.append("J ");
                }
                if (dayV.isChecked()){
                    strDate.append("V ");

                }
                if (dayS.isChecked()){
                    strDate.append("S ");
                }
                if (dayD.isChecked()){
                    strDate.append("D ");

                }
                Toast.makeText(RegisterJobActivity.this, "Dias selecioandos: "+strDate.toString(), Toast.LENGTH_SHORT).show();

                String job=mDisplayJob.getEditText().getText().toString();
                String jobdesc=mJobdesc.getEditText().getText().toString();
                String homeaddress=mHomeAddress.getEditText().getText().toString();

                if (!TextUtils.isEmpty(job)||!TextUtils.isEmpty(jobdesc)|| !TextUtils.isEmpty(homeaddress)||!TextUtils.isEmpty(strDate.toString())){
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            register_user(name,email,pass,tel,age,pronoun,job,jobdesc,homeaddress,strDate.toString());
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();


                }else{
                    Toast.makeText(RegisterJobActivity.this, "Existe algun campo en blanco", Toast.LENGTH_LONG).show();
                }
            }
        });




    }
    private void register_user(String name, String email, String pass, String tel, String age, String pronoun, String job, String jobdesc, String homeaddress, String date) {
        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {//listen to the registation is completed
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            //To get the user id
                            FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
                            String uid=current_user.getUid();

                            //Create database reference
                            mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);//Until reference is the root
                            //Hasmap to store complex data
                            HashMap<String,String> userMap=new HashMap<>();
                            userMap.put("name",name);
                            userMap.put("age",age);
                            userMap.put("status","No verificado");//Es que no esta autenticado
                            userMap.put("phone",tel);
                            userMap.put("pronoun","pronoun");//En caso de que se ponda el botton de pronombres
                            userMap.put("image","default");
                            userMap.put("thumb_image","default");
                            userMap.put("jobtitle",job);
                            userMap.put("address",homeaddress);
                            userMap.put("rating","0.0");
                            userMap.put("balance","0");
                            userMap.put("role","user");
                            userMap.put("jobdesc",jobdesc);
                            userMap.put("datejob",date);


                            //puting the hash map in the reference
                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {//Para saber si se cumplio
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        //En caso el usuario este registrado o fue completada la tarea
                                        Intent mainIntent=new Intent(RegisterJobActivity.this,MainActivity.class);
                                        startActivity(mainIntent);
                                        finish();//para que el usuario no se regrese a registrarse con finish acabas la actividad
                                    }else{

                                    }
                                }
                            });





                        }else{
                            Toast.makeText(RegisterJobActivity.this, "Tienes un error ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}