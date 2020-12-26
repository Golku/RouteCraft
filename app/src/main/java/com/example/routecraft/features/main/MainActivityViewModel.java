package com.example.routecraft.features.main;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.routecraft.data.pojos.IdHolder;
import com.example.routecraft.data.pojos.Route;
import com.google.gson.Gson;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel implements RouteRepository.Listener {

    private final String DEBUG_TAG = "DEBUG_TAG";

    private Listener view;

    private RouteRepository routeRepository;
    private LiveData<List<Route>> allRoutes;

    private Route currentRoute;
    private List<Integer> addressIdLIst;
    private List<Integer> driveIdLIst;

    private Route routeToModify;

    private Gson gson;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        routeRepository = new RouteRepository(application, this);
        allRoutes = routeRepository.getAllRoutes();
        gson = new Gson();
    }

    public interface Listener {
//        void updateFragmentInfo();
    }

    public void setViewListener(Listener view){
        this.view = view;
    }

    public void findRoute(String routeName){

        Log.d(DEBUG_TAG, "Looking for: "+ routeName);

        if(allRoutes.getValue() != null){
            for(Route route : allRoutes.getValue()){
                Log.d(DEBUG_TAG, "Checking: "+ route.getRouteName());
                Log.d(DEBUG_TAG, "ID: "+ route.getId());

                if(route.getRouteName().equals(routeName)){
                    loadRoute(route);
                    break;
                }
            }
        }else{
            getRoute(routeName);
        }
    }

    public void loadRoute(Route route){

        if(route == null){
            Log.d(DEBUG_TAG, "Route is null");
            currentRoute = new Route("My first route");
            currentRoute.setId(1);
        }else{
            currentRoute = route;
        }

        Log.d(DEBUG_TAG, "Route name: " + currentRoute.getRouteName());
        Log.d(DEBUG_TAG, "AddressIdList: " + currentRoute.getAddressIdList());
        Log.d(DEBUG_TAG, "DriveIdList: " + currentRoute.getDriveIdList());

        Gson gson = new Gson();
        IdHolder addressIdHolder = gson.fromJson(currentRoute.getAddressIdList(), IdHolder.class);
        IdHolder driveIdHolder = gson.fromJson(currentRoute.getDriveIdList(), IdHolder.class);

        addressIdLIst = addressIdHolder.getIdList();
        driveIdLIst = driveIdHolder.getIdList();

        Log.d(DEBUG_TAG, "AddressIdList size: " + addressIdLIst.size());
        Log.d(DEBUG_TAG, "DriveIdList size: " + driveIdLIst.size());

//        view.updateFragmentInfo();
    }

    public void createNewRoute(String routeName){
        Route route = new Route(routeName);
        route.setSelected(true);
        insertRoute(route);
        loadRoute(route);
    }

    public void renameRoute(Route route, String routeName){
        route.setRouteName(routeName);
        updateRoute(route);
    }

    public void updateRouteAddressIdList(List<Integer> addressIdLIst){

        Log.d("debugTag", "New address added to "+ currentRoute.getRouteName());

        IdHolder idHolder = new IdHolder();
        idHolder.setIdList(addressIdLIst);

        String addressIdListJson = gson.toJson(idHolder);

        currentRoute.setAddressIdList(addressIdListJson);

        updateRoute(currentRoute);
    }

    public void updateRouteDriveIdLIst(List<Integer> driveIdList){

        Log.d("debugTag", "New drive added to "+ currentRoute.getRouteName());

        IdHolder idHolder = new IdHolder();
        idHolder.setIdList(driveIdList);

        String driveIdListJson = gson.toJson(idHolder);

        currentRoute.setDriveIdList(driveIdListJson);

        updateRoute(currentRoute);
    }

    @Override
    public void onRouteRetrieved(Route route) {
        loadRoute(route);
    }

    public void getRoute(String routeName){
        routeRepository.get(routeName);
    }

    public void insertRoute(Route route){
        routeRepository.insert(route);
    }

    public void updateRoute(Route route){
        routeRepository.update(route);
    }

    public void deleteRoute(Route route){
        routeRepository.delete(route);
    }

    public LiveData<List<Route>> getAllRoutes() {
        return allRoutes;
    }

    public Route getCurrentRoute() {
        return currentRoute;
    }

    public List<Integer> getAddressIdLIst() {
        return addressIdLIst;
    }

    public List<Integer> getDriveIdLIst() {
        return driveIdLIst;
    }

    public Route getRouteToModify() {
        return routeToModify;
    }

    public void setRouteToModify(Route routeToModify) {
        this.routeToModify = routeToModify;
    }
}