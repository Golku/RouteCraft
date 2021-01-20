package com.example.routecraft.features.main;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.routecraft.data.pojos.Address;
import com.example.routecraft.data.pojos.IdHolder;
import com.example.routecraft.data.pojos.Route;
import com.example.routecraft.data.pojos.RouteWithAddresses;
import com.example.routecraft.data.pojos.Session;
import com.example.routecraft.features.shared.ItemManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivityViewModel extends AndroidViewModel implements RouteRepository.Listener {

    private final String DEBUG_TAG = "DEBUG_TAG";

    private Listener listener;

    private RouteRepository routeRepository;
    private LiveData<List<Route>> allRoutes;
    private LiveData<List<RouteWithAddresses>> allRoutesWithAddresses;

    private Route currentRoute;

    private Route routeToModify;

    private ItemManager itemManager;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        routeRepository = new RouteRepository(application, this);
        allRoutes = routeRepository.getAllRoutes();
        allRoutesWithAddresses = routeRepository.getRoutesWithAddressesList();
        itemManager = new ItemManager();
    }

    public interface Listener {
        Session getSession();
    }

    public void setViewListener(Listener listener) {
        this.listener = listener;
    }

    public void loadRoute(Route route) {

        if (route == null) {
            currentRoute = itemManager.createRoute(1, "My first route");
        } else {

            if (currentRoute != null) {
                Route updatedRoute = itemManager.copyRoute(currentRoute, false);
                updateRoute(updatedRoute);
            }

            currentRoute = route;

            if (!currentRoute.getSelected()) {
                Route updatedRoute = itemManager.copyRoute(currentRoute, true);
                currentRoute = updatedRoute;
                updateRoute(updatedRoute);
            }
        }

        listener.getSession().setCurrentRoute(currentRoute.getRouteId());
    }

    public List<Address> getAddressList(List<RouteWithAddresses> routeWithAddresses){

        int currentRouteId = listener.getSession().getCurrentRoute();
        List<Address> addressList = new ArrayList<>();

        Log.d(DEBUG_TAG, "Current route ID: " + currentRouteId);

        for(RouteWithAddresses route: routeWithAddresses){

            int routeId = route.getRoute().getRouteId();

            if(currentRouteId == routeId){

                Log.d(DEBUG_TAG, "Route found: " + route.getRoute().getName());

                if (route.getAddress().size()>0) {

                    for(Address address: route.getAddress()){

                        Log.d(DEBUG_TAG, "Address: " + address.getAddress());
                    }

                    addressList.addAll(route.getAddress());
                }else{
                    Log.d(DEBUG_TAG, "Address list is empty");
                }
                break;
            }
        }

        return addressList;
    }

    public void createNewRoute(String routeName) {
        int newRouteId = listener.getSession().getNewRouteId();
        Route route = itemManager.createRoute(newRouteId, routeName);
        insertRoute(route);
        loadRoute(route);
    }

    public void renameRoute(Route route, String routeName) {
        Route updatedRoute = itemManager.copyRoute(route, routeName);
        if (currentRoute.getRouteId() == updatedRoute.getRouteId()) {
            currentRoute = updatedRoute;
        }
        updateRoute(updatedRoute);
    }

    public void getRoute(int routeId) {
        //Log.d(DEBUG_TAG, "Getting route with id: " + routeId);
        routeRepository.get(routeId);
    }

    @Override
    public void onRouteRetrieved(Route route) {
        loadRoute(route);
    }

    public void insertRoute(Route route) {
        routeRepository.insert(route);
    }

    public void updateRoute(Route route) {
        routeRepository.update(route);
    }

    public void deleteRoute(Route route) {

        if (route.getRouteId() == currentRoute.getRouteId()) {
            Log.d(DEBUG_TAG, "Deleting currently selected");
            loadRoute(Objects.requireNonNull(allRoutes.getValue()).get(1));
        }
        routeRepository.delete(route);
    }

    public LiveData<List<Route>> getAllRoutes() {
        return allRoutes;
    }

    public LiveData<List<RouteWithAddresses>> getAllRoutesWithAddresses() {
        return allRoutesWithAddresses;
    }

    public Route getCurrentRoute() {
        return currentRoute;
    }

    public void setRouteToModify(Route routeToModify) {
        this.routeToModify = routeToModify;
    }

    public Route getRouteToModify() {
        return routeToModify;
    }
}