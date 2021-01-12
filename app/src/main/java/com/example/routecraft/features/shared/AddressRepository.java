package com.example.routecraft.features.shared;

import androidx.annotation.NonNull;

import com.example.routecraft.data.api.RouteCraftApiService;
import com.example.routecraft.data.pojos.Address;
import com.example.routecraft.data.pojos.api.AddressRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressRepository extends BaseRepository{

    private final String DEBUG_TAG = "DEBUG_TAG";

    private Listener listener;
    private RouteCraftApiService service;

    public interface Listener{
        void addressRequestOnResponse(Address address);
        void addressRequestOnFailure(String message);
    }

    public AddressRepository(Listener listener) {
        this.listener = listener;
        this.service = createRouteCraftApiService();
    }

    public void get(AddressRequest request){

        Call<Address> call = service.addressRequest(request);

        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(@NonNull Call<Address> call, @NonNull Response<Address> response) {
                listener.addressRequestOnResponse(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Address> call, @NonNull Throwable t) {
                listener.addressRequestOnFailure(t.getMessage());
            }
        });
    }
}
