package com.example.routecraft.data.api;

import com.example.routecraft.data.pojos.AutocompleteResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenRouteApiService {

    @GET("geocode/autocomplete")
    Call<AutocompleteResponse> autocompleteRequest(@Query("api_key") String key,
                                                   @Query("text") String address,
                                                   @Query("focus.point.lat") double focusPointLat,
                                                   @Query("focus.point.lon") double focusPointLon,
                                                   @Query("sources") String sources,
                                                   @Query("size") int size
    );

}
