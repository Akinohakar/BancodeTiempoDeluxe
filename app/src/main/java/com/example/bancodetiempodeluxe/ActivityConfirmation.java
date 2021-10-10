package com.example.bancodetiempodeluxe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ActivityConfirmation extends AppCompatActivity {
    private Intent intent;
    private ImageView mImageV;
    private TextView mTextConf, mTextMens;
    private DatabaseReference mJobDatabase;
    private Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        intent = getIntent();
        String JID = intent.getStringExtra("JID");
        String NID = intent.getStringExtra("NID");
        String status = intent.getStringExtra("Status");

        mJobDatabase = FirebaseDatabase.getInstance().getReference().child("Jobs in progress").child(JID);

        mImageV = findViewById(R.id.imageViewConf);
        mTextConf = findViewById(R.id.textViewConf);
        mTextMens = findViewById(R.id.textViewMensajeConf);

        if(status.equals("accepted")) {
            mImageV.setImageResource(R.drawable.ic_baseline_check_circle_24);
            mTextConf.setText("ÉXITO");
            mTextMens.setText("Ya se pueden comunicar unx con lx otrx");
            mJobDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    mJobDatabase.child("hirerstatus").setValue(0);
                    mJobDatabase.child("providerstatus").setValue(0);

                    DatabaseReference mHirerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(snapshot.child("iduserhire").getValue().toString());

                    DatabaseReference refNotif = mHirerDatabase.child("notifications").push();
                    mHirerDatabase.child("notifications").child(refNotif.getKey().toString()).child("type").setValue("confirmed");
                    mHirerDatabase.child("notifications").child(refNotif.getKey().toString()).child("status").setValue(0);
                    mHirerDatabase.child("notifications").child(refNotif.getKey().toString()).child("job").setValue(JID);

                    DatabaseReference refNotifn = mHirerDatabase.child("notifications").push();
                    mHirerDatabase.child("notifications").child(refNotifn.getKey().toString()).child("type").setValue("iscompleted");
                    mHirerDatabase.child("notifications").child(refNotifn.getKey().toString()).child("status").setValue(-1);
                    mHirerDatabase.child("notifications").child(refNotifn.getKey().toString()).child("job").setValue(JID);

                    DatabaseReference mProviderDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(snapshot.child("idusersupplier").getValue().toString());

                    DatabaseReference refNotifP = mProviderDatabase.child("notifications").push();
                    mProviderDatabase.child("notifications").child(refNotifP.getKey().toString()).child("type").setValue("iscompleted");
                    mProviderDatabase.child("notifications").child(refNotifP.getKey().toString()).child("status").setValue(-1);
                    mProviderDatabase.child("notifications").child(refNotifP.getKey().toString()).child("job").setValue(JID);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mJobDatabase.child("status").setValue("onProgress");

            btnRegresar = findViewById(R.id.buttonRegresar);
            btnRegresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent comeBack = new Intent(ActivityConfirmation.this, MainMenu.class);
                    startActivity(comeBack);
                    finish();
                }
            });

        }

        else if(status.equals("rejected")){
            mImageV.setImageResource(R.drawable.ic_dangerous);
            mTextConf.setText("CANCELADO");
            mTextMens.setText("La cancelación del trabajo solicitado fue exitosa.");
            mJobDatabase.child("status").setValue("cancelled");

            btnRegresar = findViewById(R.id.buttonRegresar);
            btnRegresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent comeBack = new Intent(ActivityConfirmation.this, MainMenu.class);
                    startActivity(comeBack);
                    finish();
                }
            });
        }

        else if(status.equals("confirmed")){
            mImageV.setImageResource(R.drawable.ic_baseline_check_circle_24);
            mTextConf.setText("ÉXITO");
            mTextMens.setText("Ya se pueden comunicar unx con lx otrx");
            mJobDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    DatabaseReference mProviderDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(snapshot.child("idusersupplier").getValue().toString());
                    DatabaseReference mHirerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(snapshot.child("iduserhire").getValue().toString());
                    mHirerDatabase.child("notifications").child(NID).child("status").setValue(1);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            btnRegresar = findViewById(R.id.buttonRegresar);
            btnRegresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent comeBack = new Intent(ActivityConfirmation.this, MainMenu.class);
                    startActivity(comeBack);
                    finish();
                }
            });

        }
    }
}