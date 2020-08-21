package com.example.ouicoding.Service;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {
    @FormUrlEncoded
    @POST("register.php")
    Call<SignUpResponse> registration(
            @Field("id") String identificateur,
            @Field("nom") String nom,
            @Field("prenom") String prenom,
            @Field("entreprise") String entreprise,
            @Field("email") String email,
            @Field("mdp") String mdp);

    @FormUrlEncoded
    @POST("registerEmploye.php")
    Call<SignUpResponse> registration2(
            @Field("nom") String nom,
            @Field("prenom") String prenom,
            @Field("entreprise") String entreprise,
            @Field("id") String id,
            @Field("type") String type,
            @Field("debut") String debut,
            @Field("fin") String fin,
            @Field("email") String email,
            @Field("mdp") String mdp
            );

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> userlogin(
            @Field("email") String email,
            @Field("mdp") String mdp);


    @GET("getContrat.php")
    Call<ContratResponse> getContrat(
            @Query("id") int id
    );

    @Multipart
    @POST("uploadimage.php")
    Call<ImageResponse> upload(
            @Part("nom") RequestBody nom,
            @Part("prenom") RequestBody prenom,
            @Part("email") RequestBody email,
            @Part MultipartBody.Part image
            );

    @GET("getEmployes.php")
    Call<List<EmployeResponse>> getEmploye();

    @GET("notif.php")
    Call<List<NotifResponse>> getNotif();

    @FormUrlEncoded
    @POST("UpdateConge.php")
    Call<CongeResponse> updateConge(
            @Field("email") String email,
            @Field("nbjours") String nbjours
    );

    @FormUrlEncoded
    @POST("UpdateMdp.php")
    Call<CongeResponse> updateMdp(
            @Field("mdp") String mdp,
            @Field("email") String email
    );
    @FormUrlEncoded
    @POST("downloadimage.php")
    Call<DownloadResponse> download(
            @Field("email") String email
    );
}
