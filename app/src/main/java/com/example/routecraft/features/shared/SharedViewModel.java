package com.example.routecraft.features.shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.routecraft.data.pojos.Address;

import java.util.List;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<List<Address>> addressList = new MutableLiveData<>();

    public LiveData<List<Address>> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList.setValue(addressList);
    }
}
