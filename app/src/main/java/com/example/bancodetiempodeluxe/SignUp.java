package com.example.bancodetiempodeluxe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

//IMPORTANTE CHECAR ADVERTIENCIAS DE NO HAY INTERNET
public class SignUp extends AppCompatActivity {
    private TextInputLayout mDisplayName,mEmail,mpas,mpass,mTel,mJobTile,mPronoun;
    private Button mCreateBtn;

    private FirebaseAuth mAuth;//Intancia autenticacion Firebase
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        //FIREABSE
        mAuth=FirebaseAuth.getInstance();//La instanciad de Firebase

        //ANDROID REGISTATION FIELDS
        mDisplayName=findViewById(R.id.newUserName);
        mEmail=findViewById(R.id.newEmail);
        mpas=findViewById(R.id.newPas);
        mpass=findViewById(R.id.newPass);
        mCreateBtn=findViewById(R.id.newConfirm);
        mTel=findViewById(R.id.newPhone);
        mJobTile=findViewById(R.id.newJob);
        mPronoun=findViewById(R.id.newPronoun);

        //Pasword check



        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String name=mDisplayName.getEditText().getText().toString();
                System.out.println(name);
                String email=mEmail.getEditText().getText().toString();
                System.out.println(email);
                String pass=mpas.getEditText().getText().toString();
                System.out.println(pass);
                String tel=mTel.getEditText().getText().toString();
                System.out.println(tel);
                String job=mJobTile.getEditText().getText().toString();
                String pronoun=mPronoun.getEditText().getText().toString();
                if(!TextUtils.isEmpty(name)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(pass)||!TextUtils.isEmpty(tel)||!TextUtils.isEmpty(job)||!TextUtils.isEmpty(pronoun)){
                    //register_user(name,email,pass,tel,job,pronoun);
                    Intent goJobActivity= new Intent(SignUp.this,RegisterJobActivity.class);
                    goJobActivity.putExtra("newusername",name);
                    goJobActivity.putExtra("newuseremail",email);
                    goJobActivity.putExtra("newuserpass",pass);
                    goJobActivity.putExtra("newusertel",tel);
                    goJobActivity.putExtra("newuserage",job);
                    goJobActivity.putExtra("newuserpronoun",pronoun);
                    startActivity(goJobActivity);
                    finish();
                }

                }

        });
    }



    //private void register_user(String name, String email, String pass,String tel,String job,String pronoun) {
    //    mAuth.createUserWithEmailAndPassword(email,pass)
    //            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {//listen to the registation is completed
    //                @Override
    //                public void onComplete(@NonNull Task<AuthResult> task) {
    //                    if(task.isSuccessful()){
//
    //                        //To get the user id
    //                        FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
    //                        String uid=current_user.getUid();
//
    //                        //Create database reference
    //                        mDatabase=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);//Until reference is the root
    //                        //Hasmap to store complex data
    //                        HashMap<String,String> userMap=new HashMap<>();
    //                        userMap.put("name",name);
    //                        userMap.put("age"," ");
    //                        userMap.put("status","0");//Es que no esta autenticado
    //                        userMap.put("phone",tel);
    //                        userMap.put("pronoun",pronoun);
    //                        userMap.put("image","default");
    //                        userMap.put("thumb_image","default");
    //                        userMap.put("jobtitle",job);
    //                        userMap.put("address"," ");
    //                        userMap.put("rating","0.0");
    //                        userMap.put("balance","0");
    //                        userMap.put("role","user");
    //                        userMap.put("jobdesc"," ");
//
//
    //                        //puting the hash map in the reference
    //                        mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {//Para saber si se cumplio
    //                            @Override
    //                            public void onComplete(@NonNull Task<Void> task) {
    //                                if(task.isSuccessful()){
//
    //                                    //En caso el usuario este registrado o fue completada la tarea
    //                                    Intent mainIntent=new Intent(SignUp.this,MainActivity.class);
    //                                    startActivity(mainIntent);
    //                                    finish();//para que el usuario no se regrese a registrarse con finish acabas la actividad
    //                                }else{
//
    //                                }
    //                            }
    //                        });
//
//
//
//
//
    //                    }else{
    //                        Toast.makeText(SignUp.this, "Tienes un error ", Toast.LENGTH_SHORT).show();
    //                    }
//
    //                }
    //            });
    //}
}