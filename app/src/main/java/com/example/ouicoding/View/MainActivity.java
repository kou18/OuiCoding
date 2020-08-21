package com.example.ouicoding.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ouicoding.Service.LoginResponse;
import com.example.ouicoding.Model.User;
import com.example.ouicoding.R;
import com.example.ouicoding.Service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private EditText Email;
    private EditText mdp;
    private CheckBox rester;
    private Button login;
    private Button register;
    private LoginResponse LoginResponsesData;
    private SharedPreferences mPreferences;

    public static final String PREF_NAME="PREF_NAME";
    public static final String PREF_FIRSTNAME="PREF_FIRSTNAME";
    public static final String PREF_KEY_LOGIN="PREF_KEY_LOGIN";
    public static final String PREF_EMAIL="PREF_EMAIL";

    private static User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Email= findViewById(R.id.email);
        mdp= findViewById(R.id.password);
        rester= findViewById(R.id.stay);
        login= findViewById(R.id.login);
        register= findViewById(R.id.register);
        mPreferences= getSharedPreferences("SaveData",MODE_PRIVATE);


        mUser = new User();

        rester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPreferences.edit().putBoolean(PREF_KEY_LOGIN, true).apply();

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= Email.getText().toString();
                String password= mdp.getText().toString();
                if(validateEmail() && validate(mdp)){
                    LogIn();
                }
            }
        });

        if (mPreferences.getBoolean(PREF_KEY_LOGIN,false)==true) {
            Intent intent = new Intent(MainActivity.this,DashboardRh.class);
            startActivity(intent);
        }

    }

    private boolean validateEmail(){
        String email=Email.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)){
            Email.setError("Email is not valid");
            Email.requestFocus();
            return false;
        }
        return true;
    }

    private static boolean isValidEmail(String email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validate(EditText editText){
        if(editText.getText().toString().trim().length()>0){
            return true;
        }
        editText.setError("Please fill this field");
        editText.requestFocus();
        return false;
    }


    private void LogIn(){
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        (RetrofitClient.getClient().userlogin(Email.getText().toString().trim(),
                mdp.getText().toString().trim()
        )).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponsesData=response.body();
                if (response.body().getResponse().equals("ok")){
                    Toast.makeText(getApplicationContext(), "Logged In successfully", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    mUser.setNom(response.body().getNom());
                    mUser.setPreom(response.body().getPrenom());
                    mUser.setRole(response.body().getRole());
                    mUser.setId(response.body().getId());
                    mUser.setEmail(response.body().getEmail());
                    mPreferences.edit().putString(PREF_EMAIL,mUser.getEmail()).apply();
                    mPreferences.edit().putString(PREF_FIRSTNAME,mUser.getPreom()).apply();
                    mPreferences.edit().putString(PREF_NAME,mUser.getNom()).apply();
                    if(mUser.getRole().equals("RH")){
                        Intent intent= new Intent(MainActivity.this, DashboardRh.class);
                        startActivity(intent);
                    }else{
                        Intent intent2= new Intent(MainActivity.this, DashboardEmploye.class);
                        startActivity(intent2);
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid email/Password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("response",t.getStackTrace().toString());
                progressDialog.dismiss();
            }
        });
    }



    public static User getUser() {
        return mUser;
    }
}