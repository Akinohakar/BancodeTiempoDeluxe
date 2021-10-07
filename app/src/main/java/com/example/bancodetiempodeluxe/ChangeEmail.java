package com.example.bancodetiempodeluxe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmail extends AppCompatActivity {

    TextInputLayout actualMail, newMail, password;
    Button changeEmail;

    //TODO
    //Consultar https://stackoverflow.com/questions/49357150/how-to-update-email-from-firebase-in-android

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        actualMail  = findViewById(R.id.prevEmailChange);
        newMail     = findViewById(R.id.newEmailChange);
        password    = findViewById(R.id.passwdChangeMail);
        changeEmail = findViewById(R.id.idCambiarCorreoButton);

        FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getEmail().equals(actualMail.getEditText().getText().toString())){
                    AuthCredential credential = EmailAuthProvider.getCredential(actualMail.getEditText().getText().toString(), password.getEditText().getText().toString());
                    //Reautenticacion
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Cambio de Email
                            if(!newMail.getEditText().getText().toString().isEmpty()){
                                user.updateEmail(newMail.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(),"Su Email ha sido actualizado", Toast.LENGTH_LONG).show();
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(new Intent(getApplicationContext(),Login.class));
                                            finish();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(),"El email nuevo está vacío...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    Toast.makeText(getApplicationContext(),"Email actual incorrecto!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}