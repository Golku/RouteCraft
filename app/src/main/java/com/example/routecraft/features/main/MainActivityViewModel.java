package com.example.routecraft.features.main;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.routecraft.data.pojos.IdHolder;
import com.example.routecraft.data.pojos.Route;
import com.example.routecraft.data.pojos.Session;
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
        Session getSession();
    }

    public void setViewListener(Listener view){
        this.view = view;
    }

    public void loadRoute(Route route){

        if(route == null){
            Log.d(DEBUG_TAG, "Route is null");
            currentRoute = new Route(1, "My first route");
        }else{

            if(currentRoute != null){
                currentRoute.setSelected(false);
                Log.d(DEBUG_TAG, "Deselected: " + currentRoute.getRouteName());
                updateRoute(currentRoute);
            }

            currentRoute = route;

            if(!currentRoute.isSelected()){
                currentRoute.setSelected(true);
                Log.d(DEBUG_TAG, "Selecting: " + currentRoute.getRouteName());
                updateRoute(route);
            }
        }

        view.getSession().setCurrentRoute(currentRoute.getId());

        Log.d(DEBUG_TAG, "Route ID: " + currentRoute.getId());
        Log.d(DEBUG_TAG, "Route Name: " + currentRoute.getRouteName());
        Log.d(DEBUG_TAG, "Route Selected: " + currentRoute.isSelected());

//        if(route == null){
//            Log.d(DEBUG_TAG, "Route is null");
//            currentRoute = new Route("My first route");
//            currentRoute.setSelected(true);
//            currentRoute.setId(1);
//        }else{
//
//            if (currentRoute != null) {
//                Log.d(DEBUG_TAG, "Setting route: " + currentRoute.getRouteName() + " to not selected");
//                currentRoute.setSelected(false);
//                updateRoute(currentRoute);
//            }else{
//                Log.d(DEBUG_TAG, "Current route is null");
//            }
//
//            currentRoute = route;
//
//            if(!currentRoute.isSelected()){
//                currentRoute.setSelected(true);
//                updateRoute(route);
//            }
//        }
//
//        Log.d(DEBUG_TAG, "Route name: " + currentRoute.getRouteName());
//        Log.d(DEBUG_TAG, "AddressIdList: " + currentRoute.getAddressIdList());
//        Log.d(DEBUG_TAG, "DriveIdList: " + currentRoute.getDriveIdList());

//        Gson gson = new Gson();
//        IdHolder addressIdHolder = gson.fromJson(currentRoute.getAddressIdList(), IdHolder.class);
//        IdHolder driveIdHolder = gson.fromJson(currentRoute.getDriveIdList(), IdHolder.class);
//
//        addressIdLIst = addressIdHolder.getIdList();
//        driveIdLIst = driveIdHolder.getIdList();

//        Log.d(DEBUG_TAG, "AddressIdList size: " + addressIdLIst.size());
//        Log.d(DEBUG_TAG, "DriveIdList size: " + driveIdLIst.size());

//        view.updateFragmentInfo();
    }

    public void createNewRoute(String routeName){
        int newRouteId = view.getSession().getNewRouteId();
        Log.d(DEBUG_TAG, "Creating new route with id: " + newRouteId);
        Log.d(DEBUG_TAG, "Creating new route with name: " + routeName);

        Route route = new Route(newRouteId, routeName);
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

    public void getRoute(int routeId){
        Log.d(DEBUG_TAG, "Getting route with id: " + routeId);
        routeRepository.get(routeId);
    }

    public void insertRoute(Route route){
        routeRepository.insert(route);
    }

    public void updateRoute(Route route){
        routeRepository.update(route);
    }

    public void deleteRoute(Route route){

        if(route.getId() == currentRoute.getId()){
            Log.d(DEBUG_TAG, "Deleting currently selected");
            loadRoute(allRoutes.getValue().get(1));
        }
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