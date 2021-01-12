package com.example.routecraft.features.addAddressWithAutocomplete;

import androidx.annotation.NonNull;

import com.example.routecraft.data.api.RouteCraftApiService;
import com.example.routecraft.data.pojos.AutocompletePrediction;
import com.example.routecraft.data.pojos.api.AutocompleteRequest;
import com.example.routecraft.features.shared.BaseRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutocompleteRepository extends BaseRepository {

    private final String DEBUG_TAG = "DEBUG_TAG";

    private Listener listener;
    private RouteCraftApiService service;

    interface Listener{
        void autocompleteRequestOnResponse(List<AutocompletePrediction> response);
        void autoCompleteRequestOnFailure(String message);
    }

    public AutocompleteRepository(Listener listener) {
        this.listener = listener;
        this.service = createRouteCraftApiService();
    }

    public void get(AutocompleteRequest request){

        Call<List<AutocompletePrediction>> call = service.autocompleteRequest(request);

        call.enqueue(new Callback<List<AutocompletePrediction>>() {
            @Override
            public void onResponse(@NonNull Call<List<AutocompletePrediction>> call, @NonNull Response<List<AutocompletePrediction>> response) {
                listener.autocompleteRequestOnResponse(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<AutocompletePrediction>> call, @NonNull Throwable t) {
                listener.autoCompleteRequestOnFailure(t.getMessage());
            }
        });
    }
}
