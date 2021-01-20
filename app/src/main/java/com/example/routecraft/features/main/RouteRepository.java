package com.example.routecraft.features.main;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.routecraft.data.database.RouteDao;
import com.example.routecraft.data.database.RouteDatabase;
import com.example.routecraft.data.pojos.Route;
import com.example.routecraft.data.pojos.RouteWithAddresses;

import java.util.List;

public class RouteRepository {

    private RouteDao routeDao;
    private LiveData<List<Route>> allRoutes;
    private LiveData<List<RouteWithAddresses>> routesWithAddressesList;
    private Listener listener;

    public interface Listener{
        void onRouteRetrieved(Route route);
        //void onAllRouteWithAddressesRetrieved(List<RouteWithAddresses> allRouteWithAddresses);
        //void onARouteWithAddressesRetrieved(RouteWithAddresses routeWithAddresses);
    }

    public RouteRepository(Application application, Listener listener) {
        RouteDatabase routeDatabase = RouteDatabase.getInstance(application);
        routeDao = routeDatabase.routeDao();
        allRoutes = routeDao.getAllRoutes();
        routesWithAddressesList = routeDao.getAllRouteWithAddresses();
        this.listener = listener;
    }

    public void get(int routeId){ new GetRouteAsyncTask(routeDao, listener).execute(routeId);}

    public void insert(Route route){
        new InsertRouteAsyncTask(routeDao).execute(route);
    }

    public void update(Route route){
        new UpdateRouteAsyncTask(routeDao).execute(route);
    }

    public void delete(Route route){
        new DeleteRouteAsyncTask(routeDao).execute(route);
    }

    public LiveData<List<Route>> getAllRoutes(){
        return allRoutes;
    }

    public LiveData<List<RouteWithAddresses>> getRoutesWithAddressesList(){
        return routesWithAddressesList;
    }

    private static class GetRouteAsyncTask extends AsyncTask<Integer, Void,Void> {
        private RouteDao routeDao;
        private Listener listener;

        private GetRouteAsyncTask(RouteDao routeDao, Listener listener){
            this.routeDao = routeDao;
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Integer... routeId) {
            listener.onRouteRetrieved(routeDao.get(routeId[0]));
            return null;
        }
    }

    private static class InsertRouteAsyncTask extends AsyncTask<Route, Void,Void>{
        private RouteDao routeDao;

        private InsertRouteAsyncTask(RouteDao routeDao){
            this.routeDao = routeDao;
        }

        @Override
        protected Void doInBackground(Route... routes) {
            routeDao.insert(routes[0]);
            return null;
        }
    }

    private static class UpdateRouteAsyncTask extends AsyncTask<Route, Void,Void>{
        private RouteDao routeDao;

        private UpdateRouteAsyncTask(RouteDao routeDao){
            this.routeDao = routeDao;
        }

        @Override
        protected Void doInBackground(Route... routes) {
            routeDao.update(routes[0]);
            return null;
        }
    }

    private static class DeleteRouteAsyncTask extends AsyncTask<Route, Void,Void>{
        private RouteDao routeDao;

        private DeleteRouteAsyncTask(RouteDao routeDao){
            this.routeDao = routeDao;
        }

        @Override
        protected Void doInBackground(Route... routes) {
            routeDao.delete(routes[0]);
            return null;
        }
    }
}
