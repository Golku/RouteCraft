package com.example.routecraft.features.addAddressWithAutocomplete;

import com.example.routecraft.data.api.OpenRouteApiService;
import com.example.routecraft.data.pojos.AutocompleteResponse;
import com.example.routecraft.features.shared.BaseRepository;

import retrofit2.Call;

public class AutocompletePredictionsRepository extends BaseRepository {

    private Listener listener;
    private OpenRouteApiService service;

    interface Listener{
        void predictionsResults(AutocompleteResponse response);
    }

    public AutocompletePredictionsRepository(Listener listener) {
        this.listener = listener;
    }

    public void get(String text, double lat, double lon){
        Call<AutocompleteResponse> call = service.autocompleteRequest(
                "",
                text,
                lat,
                lon,
                "openaddresses",
                4
        );
    }
}
