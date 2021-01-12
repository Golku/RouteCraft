package com.example.routecraft.data.api;

import com.example.routecraft.data.pojos.Address;
import com.example.routecraft.data.pojos.AutocompletePrediction;
import com.example.routecraft.data.pojos.api.AutocompleteRequest;
import com.example.routecraft.data.pojos.api.AddressRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RouteCraftApiService {

    @POST("autocomplete")
    Call<List<AutocompletePrediction>> autocompleteRequest(@Body AutocompleteRequest request);

    @POST("address")
    Call<Address> addressRequest(@Body AddressRequest request);
}
