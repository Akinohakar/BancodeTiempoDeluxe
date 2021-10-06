package com.example.bancodetiempodeluxe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class DescriptionNotification extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_description_notification);
        intent = getIntent();
        String key = intent.getStringExtra("type");
        switch (key){
            case "Request":
                getSupportFragmentManager().beginTransaction().replace(R.id.notificationFrame,new FragmentNotificationGotRequest()).commit();
                break;
            default:
                break;
        }

    }

}