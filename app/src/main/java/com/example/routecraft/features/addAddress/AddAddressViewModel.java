package com.example.routecraft.features.addAddress;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.routecraft.data.pojos.Address;
import com.example.routecraft.data.pojos.AddressResponse;
import com.example.routecraft.data.pojos.api.AddressRequest;
import com.example.routecraft.features.shared.AddressRepository;

import java.util.List;

public class AddAddressViewModel extends AndroidViewModel implements AddressRepository.Listener {

    private final String DEBUG_TAG = "DEBUG_TAG";

    private Listener listener;

    private AddressRepository addressRepository;
    private int userId;

    interface Listener{
        void fetchingNewAddress(String street, String city);
        void newAddressAdded(String street, String city);
        void setNewAddressContainerVisibility(boolean visible);
    }

    public AddAddressViewModel(@NonNull Application application) {
        super(application);
        addressRepository = new AddressRepository(application,this);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void getAddress(String street, String postCode, String city){
        String address = street+", "+postCode+" "+city+" , The Netherlands";
        AddressRequest request = new AddressRequest(
                userId,
                0,
                "",
                address
        );
        listener.fetchingNewAddress(street, city);
        listener.setNewAddressContainerVisibility(true);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            addressRepository.getFromApi(request);
        }, 500);
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    @Override
    public void addressRetrievedFromDb(Address address) {

    }

    @Override
    public void addressRequestOnResponse(AddressResponse response) {
        Log.d(DEBUG_TAG, "Street: " + response.getAddress().getStreet());
        Log.d(DEBUG_TAG, "postcode: " + response.getAddress().getPostCode());
        Log.d(DEBUG_TAG, "city: " + response.getAddress().getCity());
        Log.d(DEBUG_TAG, "country: " + response.getAddress().getCountry());
        Log.d(DEBUG_TAG, "lat: " + response.getAddress().getLat());
        Log.d(DEBUG_TAG, "lng: " + response.getAddress().getLng());
        listener.newAddressAdded(response.getAddress().getStreet(), response.getAddress().getPostCode()+" "+ response.getAddress().getCity());
    }
    @Override
    public void addressRequestOnFailure(String message) {

    }

    @Override
    public void AllAddress(List<Address> addressList) {

    }
}
