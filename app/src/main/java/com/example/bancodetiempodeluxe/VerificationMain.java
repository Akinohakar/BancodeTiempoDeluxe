package com.example.bancodetiempodeluxe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.getmati.mati_sdk.MatiButton;
import com.getmati.mati_sdk.MatiSdk;
import com.getmati.mati_sdk.Metadata;

public class VerificationMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //TODO
        //Credenciales a obtener de Mati
        this.<MatiButton>findViewById(R.id.matiKYCButton).setParams(
                this,
                "CLIENT_ID",
                "FLOW_ID",
                new Metadata.Builder().build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MatiSdk.REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Toast.makeText( this,"Verificacion Completada!!!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainMenu.class));

            } else {
                Toast.makeText( this,"Error en la verificación, por favor checa tus documentos", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}