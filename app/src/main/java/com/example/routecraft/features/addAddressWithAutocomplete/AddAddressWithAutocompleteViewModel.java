package com.example.routecraft.features.addAddressWithAutocomplete;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.routecraft.data.pojos.AutocompletePrediction;
import com.example.routecraft.data.pojos.AutocompleteResponse;

import java.util.List;

public class AddAddressWithAutocompleteViewModel extends AndroidViewModel implements AutocompletePredictionsRepository.Listener{

    LiveData<List<AutocompletePrediction>> predictions;

    public AddAddressWithAutocompleteViewModel(@NonNull Application application) {
        super(application);
    }

    public void getPredictions(String text){

    }

    @Override
    public void predictionsResults(AutocompleteResponse response) {

    }
}
