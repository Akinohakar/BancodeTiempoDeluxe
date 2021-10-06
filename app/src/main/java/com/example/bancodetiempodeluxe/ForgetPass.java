package com.example.bancodetiempodeluxe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPass extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button requestPass;
    private TextInputLayout emailfield;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_pass);


        //Auth instace
        mAuth=FirebaseAuth.getInstance();

        //Find views
        emailfield=findViewById(R.id.forgetPassemail);//Email Field
        requestPass=findViewById(R.id.forgetPassconfirmbtn);//Button



        requestPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email=emailfield.getEditText().getText().toString();
                if(!TextUtils.isEmpty(email)){

                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Snackbar snackbar=  Snackbar.make(view,"Correo enviado,favor de checar correo para cambio de contraseña",Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }else{
                                String error=task.getException().getMessage();
                                Snackbar snackbar=  Snackbar.make(view,error,Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }

                        }
                    });

                }else{
                    Toast.makeText(ForgetPass.this, "Porfavor llena todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}