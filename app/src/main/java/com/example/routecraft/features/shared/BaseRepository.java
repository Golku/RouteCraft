package com.example.routecraft.features.shared;

import com.example.routecraft.data.api.OpenRouteApiService;
import com.example.routecraft.data.api.RouteCraftApiService;
import com.example.routecraft.data.database.DatabaseService;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseRepository {

    private OkHttpClient createOkHttpClient(){
        return new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    protected RouteCraftApiService createRouteCraftApiService(){
        Retrofit retrofit = new Retrofit.Builder()
                .client(createOkHttpClient())
                .baseUrl("http://212.187.39.139:8080/RouteApi_war/webapi/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        return retrofit.create(RouteCraftApiService.class);
    }

    protected OpenRouteApiService createOpenRouteApiService(){
        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl("https://api.openrouteservice.org/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        return retrofit.create(OpenRouteApiService.class);
    }

    protected DatabaseService createDatabaseService(){
        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl("http://212.187.39.139/mapv2/v1/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        return retrofit.create(DatabaseService.class);
    }
}
