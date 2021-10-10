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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextInputLayout currentEmail, currentPassword, newPassword;
    Button changePswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        currentEmail    = findViewById(R.id.idCurrentEmailPswChange);
        currentPassword = findViewById(R.id.prevPasswordChange);
        newPassword     = findViewById(R.id.newPasswordChange);
        changePswd      = findViewById(R.id.changePasswordC);

        FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

        changePswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getEmail().equals(currentEmail.getEditText().getText().toString())){
                    AuthCredential credential = EmailAuthProvider.getCredential(currentEmail.getEditText().getText().toString(), currentPassword.getEditText().getText().toString());
                    //Reautenticacion
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Cambio de Email
                            if(!newPassword.getEditText().getText().toString().isEmpty()){
                                user.updatePassword(newPassword.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(),"Su Contraseña ha sido actualizado", Toast.LENGTH_LONG).show();
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(new Intent(getApplicationContext(),Login.class));
                                            finish();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(),"La contraseña nueva está vacío...", Toast.LENGTH_SHORT).show();
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