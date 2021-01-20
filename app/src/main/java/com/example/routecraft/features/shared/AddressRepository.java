package com.example.routecraft.features.shared;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.routecraft.data.api.RouteCraftApiService;
import com.example.routecraft.data.database.AddressDao;
import com.example.routecraft.data.database.RouteDatabase;
import com.example.routecraft.data.pojos.Address;
import com.example.routecraft.data.pojos.AddressResponse;
import com.example.routecraft.data.pojos.RouteAddressCrossRef;
import com.example.routecraft.data.pojos.RouteWithAddresses;
import com.example.routecraft.data.pojos.api.AddressRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressRepository extends BaseRepository{

    private final String DEBUG_TAG = "DEBUG_TAG";

    private Listener listener;
    private RouteCraftApiService service;
    private AddressDao addressDao;
    private LiveData<List<RouteWithAddresses>> routesWithAddresses;
    private ItemManager itemManager;

    public interface Listener{
        void addressRetrievedFromDb(Address address);
        void addressRequestOnResponse(AddressResponse response);
        void addressRequestOnFailure(String message);
        void AllAddress(List<Address> addressList);
    }

    public AddressRepository(Application application){
        RouteDatabase routeDatabase = RouteDatabase.getInstance(application);
        addressDao = routeDatabase.addressDao();
        routesWithAddresses = addressDao.getAllRouteWithAddresses();
        itemManager = new ItemManager();
    }

    public AddressRepository(Application application, Listener listener) {
        RouteDatabase routeDatabase = RouteDatabase.getInstance(application);
        addressDao = routeDatabase.addressDao();
        this.listener = listener;
        this.service = createRouteCraftApiService();
        itemManager = new ItemManager();
    }

    public void getAllAddress(){
        new GetAllAddressAsyncTask(addressDao, listener).execute();
    }

    public LiveData<List<RouteWithAddresses>> getRoutesWithAddresses(){
        return routesWithAddresses;
    }

    public void getFromApi(AddressRequest request){

        Call<AddressResponse> call = service.addressRequest(request);

        call.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddressResponse> call, @NonNull Response<AddressResponse> response) {
                listener.addressRequestOnResponse(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<AddressResponse> call, @NonNull Throwable t) {
                listener.addressRequestOnFailure(t.getMessage());
            }
        });
    }

    public void getFromDb(String street, String city){
        Address address = itemManager.createAddress(street.toLowerCase(), city.toLowerCase());
        new GetAddressFromDbAsyncTask(addressDao, listener).execute(address);
    }

    public void insert(Address address){
        new InsertAddressAsyncTask(addressDao).execute(address);
    }

    public void insertRouteAddressCrossRef(RouteAddressCrossRef routeAddressCrossRef){
        new InsertRouteAddressCrossRefAsyncTask(addressDao).execute(routeAddressCrossRef);
    }

    private static class GetAllAddressAsyncTask extends AsyncTask<Void, Void,Void> {
        private AddressDao addressDao;
        private Listener listener;

        private GetAllAddressAsyncTask(AddressDao addressDao, Listener listener){
            this.addressDao = addressDao;
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            listener.AllAddress(addressDao.getAllAddress());
            return null;
        }
    }

    private static class GetAddressFromDbAsyncTask extends AsyncTask<Address, Void,Void> {
        private AddressDao addressDao;
        private Listener listener;

        private GetAddressFromDbAsyncTask(AddressDao addressDao, Listener listener){
            this.addressDao = addressDao;
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Address... addresses) {
            listener.addressRetrievedFromDb(addressDao.get(addresses[0].getStreet(),
                    addresses[0].getCity()));
            return null;
        }
    }

    private static class InsertAddressAsyncTask extends AsyncTask<Address, Void,Void> {
        private AddressDao addressDao;

        private InsertAddressAsyncTask(AddressDao addressDao){
            this.addressDao = addressDao;
        }

        @Override
        protected Void doInBackground(Address... addresses) {
            addressDao.insert(addresses[0]);
            return null;
        }
    }

    private static class InsertRouteAddressCrossRefAsyncTask extends AsyncTask<RouteAddressCrossRef, Void,Void> {
        private AddressDao addressDao;

        private InsertRouteAddressCrossRefAsyncTask(AddressDao addressDao){
            this.addressDao = addressDao;
        }

        @Override
        protected Void doInBackground(RouteAddressCrossRef... routeAddressCrossRefs) {
            addressDao.insertRouteAddressCrossRef(routeAddressCrossRefs[0]);
            return null;
        }
    }
}
