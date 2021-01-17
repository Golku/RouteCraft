package com.example.routecraft.features.addAddressWithAutocomplete;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.routecraft.data.pojos.Address;
import com.example.routecraft.data.pojos.AutocompletePrediction;
import com.example.routecraft.data.pojos.api.AddressRequest;
import com.example.routecraft.data.pojos.api.AutocompleteRequest;
import com.example.routecraft.data.pojos.LatLng;
import com.example.routecraft.features.shared.AddressRepository;
import com.example.routecraft.features.shared.LocationTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AddAddressWithAutocompleteViewModel extends AndroidViewModel implements
        AutocompleteRepository.Listener,
        AddressRepository.Listener,
        LocationTracker.Listener {

    private final String DEBUG_TAG = "DEBUG_TAG";

    private Listener listener;

    private AutocompleteRepository autocompleteRepository;
    private AddressRepository addressRepository;

    private Handler handler;
    private Runnable autocompletePredictionQue;

    private String queryText;
    private boolean showingNewAddressContainer;

    private int userId;
    private int sessionId;
    private Random idGenerator;
    private LatLng userLocation;

    interface Listener {
        void setHelperTextVisibility(boolean visible);
        void setPredictionListVisibility(int visible);
        void setNoResultsContainerVisibility(boolean visible);
        void setNewAddressContainerVisibility(boolean visible);
        void setFetchingAddressContainerVisibility(boolean visible);
        void fetchingNewAddress(String street, String city);
        void newAddressAdded(String street, String city);
        void setPredictionsList(List<AutocompletePrediction> predictionsList);
    }

    public AddAddressWithAutocompleteViewModel(@NonNull Application application) {
        super(application);

        autocompleteRepository = new AutocompleteRepository(this);
        addressRepository = new AddressRepository(this);
        handler = new Handler(Looper.getMainLooper());
        idGenerator = new Random();

        sessionId = idGenerator.nextInt(1000000);

        autocompletePredictionQue = this::getPrediction;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void onPredictionClick(AutocompletePrediction prediction) {
        AddressRequest request = new AddressRequest(
                userId,
                sessionId,
                prediction.getPlaceId(),
                ""
        );
        Log.d(DEBUG_TAG, "Prediction click:" + prediction.getStreetName() + ", " + prediction.getCityName());
        listener.fetchingNewAddress(prediction.getStreetName(), prediction.getCityName());
        listener.setPredictionListVisibility(View.INVISIBLE);
        listener.setNewAddressContainerVisibility(true);
        showingNewAddressContainer = true;
        handler.postDelayed(() -> {
            addressRepository.get(request);
            sessionId = idGenerator.nextInt(1000000);
        },500);
    }

    public void startPredictionQue() {
        listener.setHelperTextVisibility(false);
        listener.setPredictionListVisibility(View.INVISIBLE);
        listener.setNewAddressContainerVisibility(false);
        listener.setNoResultsContainerVisibility(false);
        showingNewAddressContainer = false;
        listener.setFetchingAddressContainerVisibility(true);
        handler.removeCallbacks(autocompletePredictionQue);
        handler.postDelayed(autocompletePredictionQue, 1000);
    }

    private void getPrediction() {
        if (queryText.length() < 3) {
            return;
        }
        if (userLocation == null) {
            userLocation = new LatLng(52.0082339, 4.3129992);
        }
        Log.d(DEBUG_TAG, "Text query: " + queryText + " and location: " + userLocation.getLatitude() + ", " + userLocation.getLongitude());
        AutocompleteRequest request = new AutocompleteRequest(
                userId,
                sessionId,
                queryText,
                new com.example.routecraft.data.pojos.LatLng(
                        userLocation.getLatitude(),
                        userLocation.getLongitude()
                )
        );
        autocompleteRepository.get(request);
    }

    @Override
    public void onUserLocation(LatLng userLocation) {
        Log.d(DEBUG_TAG, "User location updated to: " + userLocation.getLatitude() + ", " + userLocation.getLongitude());
        this.userLocation = userLocation;
    }

    @Override
    public void autocompleteRequestOnResponse(List<AutocompletePrediction> response) {

        List<AutocompletePrediction> predictionList = new ArrayList<>();

        if (response != null) {


            if (response.size() > 0) {
                predictionList.addAll(response);
            }

            listener.setPredictionsList(predictionList);
        }

        handler.postDelayed(() -> {
            listener.setFetchingAddressContainerVisibility(false);
            if(predictionList.size()>0){
                listener.setPredictionListVisibility(View.VISIBLE);
            }else{
                listener.setNoResultsContainerVisibility(true);
            }
        }, 250);
    }

    @Override
    public void autoCompleteRequestOnFailure(String message) {
        Log.d(DEBUG_TAG, "Failed to get autocomplete predictions: " + message);
        listener.setFetchingAddressContainerVisibility(false);
    }

    @Override
    public void addressRequestOnResponse(Address address) {
        Log.d(DEBUG_TAG, "Address response");
        Log.d(DEBUG_TAG, "Address: " + address.getAddress());

        listener.newAddressAdded(address.getStreet(), address.getPostCode()+" "+ address.getCity());
    }

    @Override
    public void addressRequestOnFailure(String message) {

    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
        if(queryText.length()<3){
            listener.setFetchingAddressContainerVisibility(false);
            listener.setNoResultsContainerVisibility(false);
            if(!showingNewAddressContainer){
                listener.setHelperTextVisibility(true);
            }
        }
    }
}
