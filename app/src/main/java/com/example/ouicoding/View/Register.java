package com.example.ouicoding.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ouicoding.Service.ImageResponse;
import com.example.ouicoding.R;
import com.example.ouicoding.Service.RetrofitClient;
import com.example.ouicoding.Service.SignUpResponse;

import java.io.File;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    private EditText nom,prenom,entreprise,email,mdp,confirmmdp,id;
    private Button upload,register;
    private ImageView photo;
    private SignUpResponse signUpResponsesData;
    private String imagepath="PATH";
    private File selectedImageFile;


    private static final int REQUEST_CODE_STORAGE_PERMISSION=1;
    private static final int REQUEST_CODE_SELECT_IMAGE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        id=findViewById(R.id.iden);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        entreprise = findViewById(R.id.entreprise);
        email = findViewById(R.id.remail);
        mdp = findViewById(R.id.rpassword);
        confirmmdp = findViewById(R.id.confirmpassword);
        upload=findViewById(R.id.photo);
        register=findViewById(R.id.signup);
        photo=findViewById(R.id.photoview);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(
                        getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE
                )!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                            Register.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    selectImage();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String confpass=confirmmdp.getText().toString().trim();
                String pass=mdp.getText().toString().trim();
                if(validate(nom) && validate (prenom) && validateEmail() && validate(mdp)){
                    if(confpass.equals(pass)){
                        signUp();
                        uploadFile();
                        Intent intent = new Intent (Register.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        mdp.setText("");
                        confirmmdp.setText("");
                        mdp.requestFocus();
                        mdp.setError("Your password doesn't match");
                    }

                }
            }
        });
    }

    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getPackageManager())!= null){
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode== REQUEST_CODE_STORAGE_PERMISSION && grantResults.length>0){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                selectImage();
            }else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== REQUEST_CODE_SELECT_IMAGE && resultCode== RESULT_OK){
            if(data!=null){
                Uri selectedImageUri= data.getData();
                if(selectedImageUri!= null){
                    try{
                        InputStream inputStream=getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                        photo.setImageBitmap(bitmap);

                        selectedImageFile = new File(getPathFromUri(selectedImageUri));

                    }catch (Exception exception){
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getPathFromUri(Uri contentUri){
        String filePath;
        Cursor cursor= getContentResolver()
                .query(contentUri,null,null,null);
        if(cursor== null){
            filePath= contentUri.getPath();
        }else {
            cursor.moveToFirst();
            int index= cursor.getColumnIndex("_data");
            filePath=cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    private boolean validateEmail(){
        String emailtext=email.getText().toString().trim();
        if (emailtext.isEmpty() || !isValidEmail(emailtext)){
            email.setError("Email is not valid");
            email.requestFocus();
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

    private void signUp(){
        final ProgressDialog progressDialog = new ProgressDialog(Register.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        (RetrofitClient.getClient().registration(id.getText().toString().trim(),
                nom.getText().toString().trim(),
                prenom.getText().toString().trim(),
                entreprise.getText().toString().trim(),
                email.getText().toString().trim(),
                mdp.getText().toString().trim()
        )).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                signUpResponsesData=response.body();
                Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.d("response",t.getStackTrace().toString());
                progressDialog.dismiss();
            }
        });
    }




    private void uploadFile(){

        RequestBody nomPart=RequestBody.create(MultipartBody.FORM, nom.getText().toString());
        RequestBody prenomPart=RequestBody.create(MultipartBody.FORM, prenom.getText().toString());
        RequestBody emailPart=RequestBody.create(MultipartBody.FORM, email.getText().toString());


        RequestBody filePart= RequestBody.create(
                MediaType.parse("multipart/form-data"),
                selectedImageFile
        );

        MultipartBody.Part file= MultipartBody.Part.createFormData("image",selectedImageFile.getName(),filePart);
        Call<ImageResponse> call =RetrofitClient.getClient().upload(nomPart,prenomPart,emailPart,file);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
               // Toast.makeText(Register.this, response.body().getResponse(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Toast.makeText(Register.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }





}