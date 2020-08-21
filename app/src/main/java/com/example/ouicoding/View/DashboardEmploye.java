package com.example.ouicoding.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ouicoding.R;
import com.google.android.material.navigation.NavigationView;

import static com.example.ouicoding.View.MainActivity.PREF_FIRSTNAME;
import static com.example.ouicoding.View.MainActivity.PREF_KEY_LOGIN;
import static com.example.ouicoding.View.MainActivity.PREF_NAME;

public class DashboardEmploye extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    TextView titre,welcome;
    Button logout;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_employe);
        preferences=getSharedPreferences("SaveData",MODE_PRIVATE);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        logout=findViewById(R.id.logout);
        drawerLayout=findViewById(R.id.drawer);
        navigationView= (NavigationView) findViewById(R.id.navigationView);
        View HeaderView=navigationView.getHeaderView(0);
        welcome = HeaderView.findViewById(R.id.welcome);
        titre = findViewById(R.id.titre);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        //Load default fragment
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment,new MainFragmentEmploye());
        fragmentTransaction.commit();
        titre.setText("Principal");
        welcome.setText("Bonjour "+preferences.getString(PREF_NAME, "")+" "+preferences.getString(PREF_FIRSTNAME,"User"));


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.edit().remove(PREF_KEY_LOGIN).apply();
                Intent intentLogOut = new Intent(DashboardEmploye.this,MainActivity.class);
                startActivity(intentLogOut);
            }
        });



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(menuItem.getItemId()== R.id.home2){
            titre.setText("Principal");
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new MainFragmentEmploye());
            fragmentTransaction.commit();
        }
        if(menuItem.getItemId()== R.id.moncontrat){
            titre.setText("Mon Contrat");
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new FragmentSecondEmploye());
            fragmentTransaction.commit();
        }

        return true;
    }
}