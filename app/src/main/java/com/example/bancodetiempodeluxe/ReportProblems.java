package com.example.bancodetiempodeluxe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

public class ReportProblems extends AppCompatActivity {

    TextInputLayout subject, body;
    Button enviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problems);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        subject = findViewById(R.id.idProblemTitle);
        body    = findViewById(R.id.idProblemDescription);
        enviar  = findViewById(R.id.idSendReport);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = "contactobancodetiempo@gmail.com";
                String contrasena = "arenoman$1";
                String messageToSend = body.getEditText().getText().toString();

                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable","true");
                props.put("mail.smtp.host","smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                Session session = Session.getInstance(props,
                        new javax.mail.Authenticator(){
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(correo,contrasena);
                            }
                        });
                try{
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(correo));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correo.toString()));
                    message.setSubject("[PROBLEMA REPORTADO]: "+subject.getEditText().getText().toString());
                    message.setText("[PROBLEMA REPORTADO]: "+messageToSend);
                    Transport.send(message);
                    //Toast.makeText(getApplicationContext(), "Email sent successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),CorreoEnviado.class));

                }catch(MessagingException e){
                    //Toast.makeText(getApplicationContext(), "Error al enviar el correo", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),ProblemasCorreo.class));
                }
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
