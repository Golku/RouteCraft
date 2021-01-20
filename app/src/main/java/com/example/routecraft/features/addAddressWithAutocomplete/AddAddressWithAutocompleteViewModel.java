package com.example.routecraft.features.addAddressWithAutocomplete;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.routecraft.data.pojos.Address;
import com.example.routecraft.data.pojos.AddressResponse;
import com.example.routecraft.data.pojos.AutocompletePrediction;
import com.example.routecraft.data.pojos.RouteAddressCrossRef;
import com.example.routecraft.data.pojos.Session;
import com.example.routecraft.data.pojos.api.AddressRequest;
import com.example.routecraft.data.pojos.api.AutocompleteRequest;
import com.example.routecraft.data.pojos.LatLng;
import com.example.routecraft.features.shared.AddressRepository;
import com.example.routecraft.features.shared.ItemManager;
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

    private AddressRequest addressRequest;

    private ItemManager itemManager;

    interface Listener {
        void fetchingNewAddress(String street, String city);

        void newAddressAdded(String street, String city);

        void failedToGetAddress();

        void failedToGetPrediction();

        void setPredictionsList(List<AutocompletePrediction> predictionsList);

        void setHelperTextVisibility(boolean visible);

        void setPredictionListVisibility(boolean visible);

        void setNoResultsContainerVisibility(boolean visible);

        void setNewAddressContainerVisibility(boolean visible);

        void setFetchingAddressContainerVisibility(boolean visible);

        Session getSession();
    }

    public AddAddressWithAutocompleteViewModel(@NonNull Application application) {
        super(application);

        autocompleteRepository = new AutocompleteRepository(this);
        addressRepository = new AddressRepository(application, this);
        handler = new Handler(Looper.getMainLooper());
        idGenerator = new Random();
        itemManager = new ItemManager();

        sessionId = idGenerator.nextInt(1000000);

        autocompletePredictionQue = this::getPrediction;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean addressAlreadyInRoute(List<Address> addressList, AutocompletePrediction prediction){

        boolean inRoute = false;

        for(Address address: addressList){
            String street = address.getStreet();
            String city = address.getCity();
            Log.d(DEBUG_TAG, prediction.getStreetName()+" vs " + street);
            Log.d(DEBUG_TAG, prediction.getCityName()+" vs " + city);
            if(street.equals(prediction.getStreetName()) && prediction.getCityName().contains(city)){
                inRoute = true;
                break;
            }
        }

        return inRoute;
    }

    public void onPredictionClick(AutocompletePrediction prediction) {
        addressRequest = new AddressRequest(
                userId,
                sessionId,
                prediction.getPlaceId(),
                ""
        );
        Log.d(DEBUG_TAG, "Prediction click:" + prediction.getStreetName() + ", " + prediction.getCityName());
        listener.fetchingNewAddress(prediction.getStreetName(), prediction.getCityName());
        listener.setPredictionListVisibility(false);
        listener.setNewAddressContainerVisibility(true);
        showingNewAddressContainer = true;

        handler.postDelayed(() -> {
            addressRepository.getFromDb(prediction.getStreetName(), prediction.getCityName());
        }, 500);
    }

    public void startPredictionQue() {
        listener.setHelperTextVisibility(false);
        listener.setPredictionListVisibility(false);
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
//        Log.d(DEBUG_TAG, "Text query: " + queryText + " and location: " + userLocation.getLatitude() + ", " + userLocation.getLongitude());
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
            if (predictionList.size() > 0) {
                listener.setPredictionListVisibility(true);
            } else {
                listener.setNoResultsContainerVisibility(true);
            }
        }, 250);
    }

    @Override
    public void autoCompleteRequestOnFailure(String message) {
        Log.d(DEBUG_TAG, "Failed to get autocomplete predictions: " + message);
        listener.setFetchingAddressContainerVisibility(false);
        listener.failedToGetPrediction();

    }

    @Override
    public void AllAddress(List<Address> addressList) {

        if (addressList != null) {
            if (addressList.size() > 0) {

                for (Address address : addressList) {

                    Log.d(DEBUG_TAG, "Id:" + address.getAddressId());
                    Log.d(DEBUG_TAG, "Street:" + address.getStreet());
                    Log.d(DEBUG_TAG, "postcode:" + address.getPostCode());
                    Log.d(DEBUG_TAG, "city:" + address.getCity());
                    Log.d(DEBUG_TAG, "country:" + address.getCountry());
                    Log.d(DEBUG_TAG, "lat:" + address.getLat());
                    Log.d(DEBUG_TAG, "lng:" + address.getLng());
                }

            } else {
                Log.d(DEBUG_TAG, "List is empty");
            }
        } else {
            Log.d(DEBUG_TAG, "List is null");
        }
    }

    private void addNewAddress(Address address) {
        listener.newAddressAdded(address.getStreet(), address.getPostCode() + " " + address.getCity());
    }

    @Override
    public void addressRetrievedFromDb(Address address) {
        if (address != null) {
            Log.d(DEBUG_TAG, "Address already in DB: " + address.getAddress());
            handler.post(() -> addNewAddress(address));
            addressRepository.insertRouteAddressCrossRef(
                    new RouteAddressCrossRef(
                            listener.getSession().getCurrentRoute(),
                            address.getAddressId()
                    )
            );
        } else {
            Log.d(DEBUG_TAG, "Address is null, fetching from API");
            addressRepository.getFromApi(addressRequest);
        }
    }

    @Override
    public void addressRequestOnResponse(AddressResponse response) {
        if (response != null && response.isValid()) {
            Log.d(DEBUG_TAG, "Address response is valid");
            Address address = itemManager.copyAddress(response.getAddress(),
                    listener.getSession().getNewAddressId());
            Log.d(DEBUG_TAG, "Address id: " + address.getAddressId());
            addressRepository.insert(address);
            addressRepository.insertRouteAddressCrossRef(
                    new RouteAddressCrossRef(
                            listener.getSession().getCurrentRoute(),
                            address.getAddressId()
                    )
            );
            addNewAddress(address);
            sessionId = idGenerator.nextInt(1000000);
        } else {
            Log.d(DEBUG_TAG, "Address response is not valid");
            listener.failedToGetAddress();
        }
    }

    @Override
    public void addressRequestOnFailure(String message) {
        listener.failedToGetAddress();
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
        if (queryText.length() < 3) {
            listener.setFetchingAddressContainerVisibility(false);
            listener.setNoResultsContainerVisibility(false);
            if (!showingNewAddressContainer) {
                listener.setHelperTextVisibility(true);
            }
        }
    }
}
