package com.example.routecraft.features.shared;

import androidx.lifecycle.ViewModel;

import com.example.routecraft.data.pojos.Address;

import java.util.List;

public class SharedViewModel extends ViewModel {

    private List<Address> addressList;

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}
