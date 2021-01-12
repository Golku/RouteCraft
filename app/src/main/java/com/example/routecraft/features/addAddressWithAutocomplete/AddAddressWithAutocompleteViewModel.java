package com.example.routecraft.features.addAddressWithAutocomplete;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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

    private int userId;
    private int sessionId;
    private Random idGenerator;
    private LatLng userLocation;

    interface Listener {
        void showProgressBar(boolean show);
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

    public void onPredictionClick(AutocompletePrediction prediction) {
        AddressRequest request = new AddressRequest(
                userId,
                sessionId,
                prediction.getPlaceId(),
                ""
        );
        addressRepository.get(request);
        sessionId = idGenerator.nextInt(1000000);
        Log.d(DEBUG_TAG, "Prediction click:" + prediction.getStreetName() + ", " + prediction.getCityName());
    }

    public void onPredictionSelected() {
        handler.removeCallbacks(autocompletePredictionQue);
        getPrediction();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void startPredictionQue() {
        listener.showProgressBar(true);
        handler.removeCallbacks(autocompletePredictionQue);
        handler.postDelayed(autocompletePredictionQue, 1000);
    }

    @Override
    public void onUserLocation(LatLng userLocation) {
        Log.d(DEBUG_TAG, "User location updated to: " + userLocation.getLatitude() + ", " + userLocation.getLongitude());
        this.userLocation = userLocation;
    }

    @Override
    public void autocompleteRequestOnResponse(List<AutocompletePrediction> response) {

        if (response != null) {

            List<AutocompletePrediction> predictionList = new ArrayList<>();

            if (response.size() > 0) {
                predictionList.addAll(response);
            }

            listener.setPredictionsList(predictionList);
        }

        listener.showProgressBar(false);
    }

    @Override
    public void autoCompleteRequestOnFailure(String message) {
        Log.d(DEBUG_TAG, "Failed to get autocomplete predictions: " + message);
        listener.showProgressBar(false);
    }

    @Override
    public void addressRequestOnResponse(Address address) {
        Log.d(DEBUG_TAG, "Address response");
        Log.d(DEBUG_TAG, "Address: " + address.getAddress());
    }

    @Override
    public void addressRequestOnFailure(String message) {

    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }
}
