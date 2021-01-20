package com.example.routecraft.features.addressList;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.routecraft.data.pojos.Address;
import com.example.routecraft.data.pojos.Route;
import com.example.routecraft.data.pojos.RouteWithAddresses;
import com.example.routecraft.data.pojos.Session;
import com.example.routecraft.features.main.MainActivityViewModel;
import com.example.routecraft.features.main.RouteRepository;
import com.example.routecraft.features.shared.AddressRepository;

import java.util.ArrayList;
import java.util.List;

public class AddressListViewModel extends AndroidViewModel {

    private final String DEBUG_TAG = "DEBUG_TAG";

    private AddressRepository addressRepository;
    private LiveData<List<RouteWithAddresses>> allRoutesWithAddresses;

    private Listener listener;

    interface Listener{
        Session getSession();
        void setAddressList(List<Address> addressList);
    }

    public AddressListViewModel(@NonNull Application application) {
        super(application);
        addressRepository = new AddressRepository(application);
        allRoutesWithAddresses = addressRepository.getRoutesWithAddresses();
    }

    public void setViewListener(Listener listener) {
        this.listener = listener;
    }

    public void loadAddressList(List<RouteWithAddresses> routeWithAddressesList){

        int currentRouteId = listener.getSession().getCurrentRoute();
        List<Address> addressList = new ArrayList<>();

        Log.d(DEBUG_TAG, "Current route ID: " + currentRouteId);

        for(RouteWithAddresses routeWithAddresses: routeWithAddressesList){
            int routeId = routeWithAddresses.getRoute().getRouteId();
            if(currentRouteId == routeId){
                Log.d(DEBUG_TAG, "Route found: " + routeWithAddresses.getRoute().getName());
                if (routeWithAddresses.getAddress().size()>0) {
                    for(Address address: routeWithAddresses.getAddress()){
                        Log.d(DEBUG_TAG, "Address: " + address.getAddress());
                    }
                    addressList.addAll(routeWithAddresses.getAddress());
                }else{
                    Log.d(DEBUG_TAG, "Address list is empty");
                }
                break;
            }else{
                Log.d(DEBUG_TAG, "No Match");
            }
        }

        listener.setAddressList(addressList);
    }

    public LiveData<List<RouteWithAddresses>> getAllRoutesWithAddresses() {
        return allRoutesWithAddresses;
    }
}
