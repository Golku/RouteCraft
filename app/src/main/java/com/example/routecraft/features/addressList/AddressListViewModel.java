package com.example.routecraft.features.addressList;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.routecraft.data.pojos.Address;
import com.example.routecraft.data.pojos.Session;

import java.util.ArrayList;
import java.util.List;

public class AddressListViewModel extends AndroidViewModel {

    private final String DEBUG_TAG = "DEBUG_TAG";

    private Listener listener;

    interface Listener{
        Session getSession();
    }

    public AddressListViewModel(@NonNull Application application) {
        super(application);
    }

    public void setViewListener(Listener listener) {
        this.listener = listener;
    }

    public List<Address> sortAddressList(List<Address> currentList){

        int sortOrder = listener.getSession().geAddressListOrder();

        List<Address> sortedList = new ArrayList<>();
        List<Address> business = new ArrayList<>();
        List<Address> individual = new ArrayList<>();

        if(sortOrder == 1){
            sortedList = currentList;
        }else if(sortOrder == 2){
            for(Address address: currentList){
                if(address.getBusiness()){
                    business.add(address);
                }else{
                    individual.add(address);
                }
            }
            sortedList.addAll(business);
            sortedList.addAll(individual);
        }

        return sortedList;
    }
}
