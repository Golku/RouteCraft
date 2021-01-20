package com.example.routecraft.data.pojos;

public class AddressResponse {

    private boolean valid;
    private Address address;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
