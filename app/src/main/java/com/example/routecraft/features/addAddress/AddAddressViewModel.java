package com.example.routecraft.features.addAddress;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.routecraft.data.pojos.Address;
import com.example.routecraft.data.pojos.api.AddressRequest;
import com.example.routecraft.features.shared.AddressRepository;

public class AddAddressViewModel extends AndroidViewModel implements AddressRepository.Listener {

    private final String DEBUG_TAG = "DEBUG_TAG";

    private AddressRepository addressRepository;
    private int userId;

    public AddAddressViewModel(@NonNull Application application) {
        super(application);
        addressRepository = new AddressRepository(this);
    }

    public void getAddress(String address){
        AddressRequest request = new AddressRequest(
                userId,
                0,
                "",
                address
        );
        addressRepository.get(request);
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    @Override
    public void addressRequestOnResponse(Address address) {
        Log.d(DEBUG_TAG, "Street: " + address.getStreet());
        Log.d(DEBUG_TAG, "postcode: " + address.getPostCode());
        Log.d(DEBUG_TAG, "city: " + address.getCity());
        Log.d(DEBUG_TAG, "country: " + address.getCountry());
        Log.d(DEBUG_TAG, "lat: " + address.getLat());
        Log.d(DEBUG_TAG, "lng: " + address.getLng());
    }

    @Override
    public void addressRequestOnFailure(String message) {

    }
}
