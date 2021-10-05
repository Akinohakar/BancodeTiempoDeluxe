package com.example.bancodetiempodeluxe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ContactUs extends AppCompatActivity {
    TextInputLayout contactMail, asunto, body;
    Button enviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        contactMail = findViewById(R.id.idContactEmail);
        asunto = findViewById(R.id.idContactSubject);
        body   = findViewById(R.id.idMailContents);
        enviar = findViewById(R.id.idContactanosEnviar);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = "contactobancodetiempo@gmail.com";
                String contrasena = "arenoman$1";
                String messageToSend = "Contactarse con: "+contactMail.getEditText().getText()+"\n"+body.getEditText().getText().toString();

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
                    message.setSubject("[CONTACTO]: "+asunto.getEditText().getText().toString());
                    message.setText("[CONTACTO]: "+messageToSend);
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