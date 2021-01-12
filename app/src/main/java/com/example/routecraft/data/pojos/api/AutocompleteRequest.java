package com.example.routecraft.data.pojos.api;

import com.example.routecraft.data.pojos.LatLng;

public class AutocompleteRequest {

    private int userId;
    private int sessionId;
    private String queryText;
    private LatLng userLocation;

    public AutocompleteRequest(int userId, int sessionId, String queryText, LatLng userLocation) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.queryText = queryText;
        this.userLocation = userLocation;
    }
}
