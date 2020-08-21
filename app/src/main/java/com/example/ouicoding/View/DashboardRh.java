package com.example.ouicoding.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ouicoding.Service.DownloadResponse;
import com.example.ouicoding.Service.NotifResponse;
import com.example.ouicoding.R;
import com.example.ouicoding.Service.RetrofitClient;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ouicoding.Service.App.CHANNEL_ID;
import static com.example.ouicoding.View.MainActivity.PREF_EMAIL;
import static com.example.ouicoding.View.MainActivity.PREF_FIRSTNAME;
import static com.example.ouicoding.View.MainActivity.PREF_KEY_LOGIN;
import static com.example.ouicoding.View.MainActivity.PREF_NAME;

public class DashboardRh extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    TextView titre, welcome;
    Button logout;
    SharedPreferences preferences;
    private ImageView photo;
    private List<NotifResponse> notifList = null;
    private NotificationManagerCompat notificationManager;
    private String enterKey = System.getProperty("line.separator");
    private String goers = "";
    public static final String URL="http://192.168.1.6/OuiCoding/";
    private String path,finalpath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_rh);
        notificationManager = NotificationManagerCompat.from(this);
        navigationView= (NavigationView) findViewById(R.id.navigationView);
        View HeaderView=navigationView.getHeaderView(0);
        welcome = HeaderView.findViewById(R.id.welcome);
        preferences=getSharedPreferences("SaveData",MODE_PRIVATE);
        welcome.setText("Bonjour "+preferences.getString(PREF_NAME, "")+" "+preferences.getString(PREF_FIRSTNAME,"User"));
        toolbar = findViewById(R.id.toolbar);
        photo=toolbar.findViewById(R.id.photorh);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        logout = findViewById(R.id.logout);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        titre = findViewById(R.id.titre);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        //Load default fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, new MainFragment());
        fragmentTransaction.commit();
        titre.setText("Principal");
        getPath();




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.edit().remove(PREF_KEY_LOGIN).apply();
                Intent intentLogOut = new Intent(DashboardRh.this, MainActivity.class);
                startActivity(intentLogOut);
            }
        });

        setup();


    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (menuItem.getItemId() == R.id.home) {
            titre.setText("Principal");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new MainFragment());
            fragmentTransaction.commit();
        }
        if (menuItem.getItemId() == R.id.liste_employés) {
            titre.setText("Liste des employés");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new FragmentSecond());
            fragmentTransaction.commit();
        }
        if (menuItem.getItemId() == R.id.notifications) {
            titre.setText("Congés");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new FragmentThird());
            fragmentTransaction.commit();
        }
        return true;
    }

    public void setup() {
        (RetrofitClient.getClient().getNotif()).enqueue(new Callback<List<NotifResponse>>() {
            @Override
            public void onResponse(Call<List<NotifResponse>> call, Response<List<NotifResponse>> response) {
                notifList = response.body();
                for (int i = 0; i < notifList.size(); i++) {
                    goers += (notifList.get(i).getNom() + " " + notifList.get(i).getPrenom() + enterKey);
                }

                Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_baseline_assignment_late_24)
                        .setContentTitle("Alerte")
                        .setContentText("Les contrats des employés suivants auront fin dans moins de 7 jours :\n"+goers)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Les contrats des employés suivants auront fin dans moins de 7 jours :\n" + goers))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_REMINDER)
                        .build();
                if (notifList.size() > 0) {
                    notificationManager.notify(1, notification);
                }
            }

            @Override
            public void onFailure(Call<List<NotifResponse>> call, Throwable t) {

            }
        });

    }
    private void getPath(){
        Call<DownloadResponse> call = RetrofitClient.getClient().download(preferences.getString(PREF_EMAIL,""));
        call.enqueue(new Callback<DownloadResponse>() {
            @Override
            public void onResponse(Call<DownloadResponse> call, Response<DownloadResponse> response) {
                path=response.body().getPath();
                finalpath=URL+path;
                Picasso.with(getApplicationContext()).load(finalpath).placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(photo, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }
                        });
                System.out.println(path);
            }

            @Override
            public void onFailure(Call<DownloadResponse> call, Throwable t) {

            }
        });
    }

}



