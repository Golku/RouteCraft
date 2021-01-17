package com.example.routecraft.features.main;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.routecraft.data.pojos.IdHolder;
import com.example.routecraft.data.pojos.Route;
import com.example.routecraft.data.pojos.Session;
import com.example.routecraft.features.shared.ItemManager;
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

    private ItemManager itemManager;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        routeRepository = new RouteRepository(application, this);
        allRoutes = routeRepository.getAllRoutes();
        gson = new Gson();
        itemManager = new ItemManager();
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
            currentRoute = itemManager.createRoute(1, "My first route");
        }else{

            if(currentRoute != null){
                Route updatedRoute = itemManager.copyRoute(currentRoute, false);
                updateRoute(updatedRoute);
            }

            currentRoute = route;

            if(!currentRoute.getSelected()){
                Route updatedRoute = itemManager.copyRoute(currentRoute, true);
                currentRoute = updatedRoute;
                updateRoute(updatedRoute);
            }
        }

        view.getSession().setCurrentRoute(currentRoute.getRouteId());

        Log.d(DEBUG_TAG, "Route ID: " + currentRoute.getRouteId());
        Log.d(DEBUG_TAG, "Route Name: " + currentRoute.getName());
        Log.d(DEBUG_TAG, "Route Selected: " + currentRoute.getSelected());

        Gson gson = new Gson();
        IdHolder addressIdHolder = gson.fromJson(currentRoute.getAddressIdList(), IdHolder.class);
        IdHolder driveIdHolder = gson.fromJson(currentRoute.getDriveIdList(), IdHolder.class);

        addressIdLIst = addressIdHolder.getIdList();
        driveIdLIst = driveIdHolder.getIdList();

//        Log.d(DEBUG_TAG, "AddressIdList size: " + addressIdLIst.size());
//        Log.d(DEBUG_TAG, "DriveIdList size: " + driveIdLIst.size());

//        view.updateFragmentInfo();
    }

    private void debugRoute(Route route){
        Log.d(DEBUG_TAG, "Id: " + route.getRouteId());
        Log.d(DEBUG_TAG, "Name: " + route.getName());
        Log.d(DEBUG_TAG, "Selected: " + route.getSelected());
        Log.d(DEBUG_TAG, "AddressList: " + route.getAddressIdList());
        Log.d(DEBUG_TAG, "DriveList: " + route.getDriveIdList());
        Log.d(DEBUG_TAG, "Creation time: " + route.getCreationDate());
    }

    public void createNewRoute(String routeName){
        int newRouteId = view.getSession().getNewRouteId();
        Route route = itemManager.createRoute(newRouteId, routeName);
        insertRoute(route);
        loadRoute(route);
    }

    public void renameRoute(Route route, String routeName){
        Route updatedRoute = itemManager.copyRoute(route, routeName);
        if(currentRoute.getRouteId() == updatedRoute.getRouteId()){
            currentRoute = updatedRoute;
        }
        updateRoute(updatedRoute);
    }

    public void updateRouteAddressIdList(List<Integer> addressIdLIst){

        Log.d(DEBUG_TAG, "New address added to "+ currentRoute.getName());

        IdHolder idHolder = new IdHolder();
        idHolder.setIdList(addressIdLIst);

        String addressIdListJson = gson.toJson(idHolder);

//        currentRoute.setAddressIdList(addressIdListJson);

        updateRoute(currentRoute);
    }

    public void updateRouteDriveIdLIst(List<Integer> driveIdList){

        Log.d(DEBUG_TAG, "New drive added to "+ currentRoute.getName());

        IdHolder idHolder = new IdHolder();
        idHolder.setIdList(driveIdList);

        String driveIdListJson = gson.toJson(idHolder);

//        currentRoute.setDriveIdList(driveIdListJson);

        updateRoute(currentRoute);
    }

    public void getRoute(int routeId){
        Log.d(DEBUG_TAG, "Getting route with id: " + routeId);
        routeRepository.get(routeId);
    }

    @Override
    public void onRouteRetrieved(Route route) {
        loadRoute(route);
    }

    public void insertRoute(Route route){
        routeRepository.insert(route);
    }

    public void updateRoute(Route route){
        routeRepository.update(route);
    }

    public void deleteRoute(Route route){

        if(route.getRouteId() == currentRoute.getRouteId()){
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