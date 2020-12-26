package com.example.routecraft.data.database;

import com.example.routecraft.data.pojos.database.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DatabaseService {

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );

}
