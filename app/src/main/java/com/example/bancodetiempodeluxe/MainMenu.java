package com.example.bancodetiempodeluxe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
   private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);

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
        navigationView.setCheckedItem(R.id.navBuscar);}

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
                getSupportFragmentManager().beginTransaction().replace(R.id.framentContainer,new notificationsFragment()).commit();
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
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
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