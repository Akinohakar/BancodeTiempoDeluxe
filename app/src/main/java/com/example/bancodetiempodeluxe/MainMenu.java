package com.example.bancodetiempodeluxe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
   private DrawerLayout drawer;
    //iNATANCE OF FIREBASE
    private FirebaseAuth mAuth;//Creating an instance firebase auth
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private TextView  profileHeaderName,profilePronoun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);

        mAuth = FirebaseAuth.getInstance();//Inicializion the instance

        Toolbar toolbar=findViewById(R.id.toolabar);
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.drawerLayout);
        NavigationView navigationView=findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_closed);

        drawer.addDrawerListener(toggle);

        toggle.syncState();

        if(savedInstanceState==null){//to safe the state when the state is rotated
        getSupportFragmentManager().beginTransaction().replace(R.id.framentContainer,new fragmentSearch()).commit();//cUNDO SE INICIA LA ACTIVIDAD SE QUIRE ABIERTO EL PRIMERO
        navigationView.setCheckedItem(R.id.navBuscar);
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {//If user is not sign in
            headerFirebase();
        }
    }
   public void headerFirebase(){
       //get instances navigation view
       NavigationView navigationView=findViewById(R.id.navView);
       View headerView = navigationView.getHeaderView(0);
       TextView navUsername = (TextView) headerView.findViewById(R.id.headerTitleName);
       TextView navPronoun = (TextView) headerView.findViewById(R.id.headerPronoun);
       TextView navRating = (TextView) headerView.findViewById(R.id.headerRating);
       ImageView navProfileImg=(ImageView) headerView.findViewById(R.id.headerImageProfile);

//
       mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
       String current_uid=mCurrentUser.getUid();
       mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);//pOINTING TO THE USERS OBJECT and to the id object
       mUserDatabase.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String name=snapshot.child("name").getValue().toString();
               String pronoun=snapshot.child("pronoun").getValue().toString();
               String rating=snapshot.child("rating").getValue().toString();
               String image=snapshot.child("image").getValue().toString();
               navUsername.setText(name);
               navPronoun.setText(pronoun);
               navRating.setText(rating);
               Picasso.get().load(image).into(navProfileImg);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
   }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();//get the current user and store it in the variable
        //If current user is not sign in the variable current user is going to be null
        if(currentUser==null){//If user is not sign in
            sendToStart();
        }

    }

    private void sendToStart() {
        Intent startIntent=new Intent(MainMenu.this,Login.class);
        startActivity(startIntent);//the user is going back to the login page
        finish();//so the user dont come back to this activity,the main activity
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {//
        switch(item.getItemId()){
            case R.id.navBuscar:
                getSupportFragmentManager().beginTransaction().replace(R.id.framentContainer,new fragmentSearch()).commit();
                break;
            case R.id.navTransac:
                getSupportFragmentManager().beginTransaction().replace(R.id.framentContainer,new transactionsFragment()).commit();
                break;
            case R.id.navMensajes:
                getSupportFragmentManager().beginTransaction().replace(R.id.framentContainer,new messageFragment()).commit();
                break;
            case R.id.navNotificaciones:
                getSupportFragmentManager().beginTransaction().replace(R.id.framentContainer,new NotificationsFragment()).commit();
                break;
            case R.id.navPerfil:
                getSupportFragmentManager().beginTransaction().replace(R.id.framentContainer,new profileFragment()).commit();
                break;
            case R.id.navconfiPerfil:
                getSupportFragmentManager().beginTransaction().replace(R.id.framentContainer,new configPerfilFragment()).commit();
                break;
            case R.id.navWho:
                getSupportFragmentManager().beginTransaction().replace(R.id.framentContainer,new whoFragment()).commit();
                break;
            case R.id.logOut:
                //Caso apra logout
                goOut();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goOut() {
        Toast.makeText(MainMenu.this, "Sesion cerrada", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        sendToStart();//to go to login
    }

    public void goProfile(View view){//method to go profile from header image
        NavigationView navigationView=findViewById(R.id.navView);
        getSupportFragmentManager().beginTransaction().replace(R.id.framentContainer,new profileFragment()).commit();
        navigationView.setCheckedItem(R.id.navPerfil);
        drawer.closeDrawer(GravityCompat.START);

    }
    

    @Override
    public void onBackPressed() {//Post Accion of clicked item in navigation drawer
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }
}