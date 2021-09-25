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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    private TextInputLayout mDisplayName,mEmail,mpas,mpass;
    private Button mCreateBtn;

    private FirebaseAuth mAuth;//Intancia autenticacion Firebase

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
        //Pasword check
        String newPass1=mpas.getEditText().toString();
        String newPass2=mpass.getEditText().toString();
        boolean isSamePass=validateIndenticalPas(newPass1,newPass2);


        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String name=mDisplayName.getEditText().getText().toString();
                System.out.println(name);
                String email=mEmail.getEditText().getText().toString();
                System.out.println(email);
                String pass=mpas.getEditText().getText().toString();
                System.out.println(pass);

                register_user(name,email,pass);}

        });
    }

    private boolean validateIndenticalPas(String newPass1, String newPass2) {
        if(newPass1==newPass2){
            System.out.println("Si");
            return true;
        }else{
            System.out.println(newPass1+"  " +newPass2);
            return false;
        }

    }

    private void register_user(String name, String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {//listen to the registation is completed
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //En caso el usuario este registrado o fue completada la tarea
                            Intent mainIntent=new Intent(SignUp.this,MainActivity.class);
                            startActivity(mainIntent);
                            finish();//para que el usuario no se regrese a registrarse con finish acabas la actividad

                        }else{
                            Toast.makeText(SignUp.this, "Tienes un error ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}