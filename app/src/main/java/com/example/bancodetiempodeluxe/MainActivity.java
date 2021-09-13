package com.example.bancodetiempodeluxe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN=5000;
    //Varaibles
    Animation topAnim,bottomAnim;
    ImageView logo1,logo2;
    TextView logoTxt;
    ConstraintLayout consti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //Anumations
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
       consti=findViewById(R.id.constImage);
        logoTxt=findViewById(R.id.textView);

        consti.setAnimation(topAnim);
        logoTxt.setAnimation(bottomAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this,Login.class);
                //For Animation we set a pair Animation,View
                Pair[] pairs=new Pair[2];
                pairs[0]=new Pair<View,String>(consti,"logoImage");
                pairs[1]=new Pair<View,String>(logoTxt,"logoTxt");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);//to make aniimation
                startActivity(intent,options.toBundle());//To bundle to get the extras in this case is the animation
            }
        },SPLASH_SCREEN);


    }
}