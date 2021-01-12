package com.example.routecraft.data.pojos.api;

public class AddressRequest {

    private int userId;
    private int sessionId;
    private String placeId;
    private String address;

    public AddressRequest(int userId, int sessionId, String placeId, String address) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.placeId = placeId;
        this.address = address;
    }
}
