package com.example.routecraft.data.pojos.api;

import com.example.routecraft.data.pojos.AutocompletePrediction;

import java.util.List;

public class AutocompleteResponse {

    private List<AutocompletePrediction> autocompletePredictions;

    public List<AutocompletePrediction> getAutocompletePredictions() {
        return autocompletePredictions;
    }

    public void setAutocompletePredictions(List<AutocompletePrediction> autocompletePredictions) {
        this.autocompletePredictions = autocompletePredictions;
    }
}
