package com.example.bancodetiempodeluxe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private Button callSigUp,goMenu;
    private TextInputLayout emaillogin,pasLogin;
    private ProgressDialog mLoginProgress;
    //Firebase
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        callSigUp=findViewById(R.id.goSignUp);
        goMenu=findViewById(R.id.btnGo);
        emaillogin=findViewById(R.id.username);
        pasLogin=findViewById(R.id.password);
        //Firebase
        mAuth=FirebaseAuth.getInstance();

        mLoginProgress =new ProgressDialog(this);//inicialica progress dialog
        callSigUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });


        goMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emaillogin.getEditText().getText().toString();
                String pass=pasLogin.getEditText().getText().toString();
                if(!TextUtils.isEmpty(email)||!TextUtils.isEmpty(pass)){
                    //progress dialog
                    mLoginProgress.setTitle("Iniciando Sesion");
                    mLoginProgress.setMessage("Esperate prro en lo que las credenciales nos llegan del bigote");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();
                    //send to login
                    loginUser(email,pass);
                }
            }
        });
    }

    private void loginUser(String email, String pass) {
       mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){

                   mLoginProgress.dismiss();
                   Intent mainIntent=new Intent(Login.this,MainMenu.class);
                   startActivity(mainIntent);
                   finish();
               }else{
                   mLoginProgress.hide();
                   Toast.makeText(Login.this, "Gfe acuerdese de su cuenta o chequele xd", Toast.LENGTH_LONG).show();
               }

           }
       });

    }
}