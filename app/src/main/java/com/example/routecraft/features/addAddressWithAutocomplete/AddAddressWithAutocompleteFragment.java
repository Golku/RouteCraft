package com.example.routecraft.features.addAddressWithAutocomplete;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.routecraft.data.pojos.AutocompletePrediction;
import com.example.routecraft.data.pojos.Session;
import com.example.routecraft.databinding.FragmentAddAddressWithAutocompleteBinding;
import com.example.routecraft.features.dialogs.GenericMessageDialog;
import com.example.routecraft.features.main.MainActivity;
import com.example.routecraft.features.shared.LocationTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static androidx.core.app.ActivityCompat.requestPermissions;

public class AddAddressWithAutocompleteFragment extends Fragment implements
        AddAddressWithAutocompleteViewModel.Listener,
        AutocompleteAdapter.Listener{

    private final String DEBUG_TAG = "DEBUG_TAG";

    private AddAddressWithAutocompleteViewModel viewModel;
    private FragmentAddAddressWithAutocompleteBinding binding;

    private AutocompleteAdapter adapter;
    private LocationTracker locationTracker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DEBUG_TAG, "AddAddressWithAutocompleteFragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddAddressWithAutocompleteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Log.d(DEBUG_TAG, "AddAddressWithAutocompleteFragment onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        viewModel = new ViewModelProvider(requireActivity()).get(AddAddressWithAutocompleteViewModel.class);
        viewModel.setListener(this);
        viewModel.setUserId(new Session(requireActivity()).getUserId());
        adapter = new AutocompleteAdapter(this);
        binding.predictionRv.setAdapter(adapter);
        binding.predictionRv.setHasFixedSize(true);

        locationTracker = new LocationTracker(requireActivity(), viewModel);

        binding.routeNameEt.requestFocus();

        binding.routeNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = Objects.requireNonNull(binding.routeNameEt.getText()).toString().trim();
                viewModel.setQueryText(text);
                if (text.length() >= 3) {
                    viewModel.startPredictionQue();
                } else {
                    showProgressBar(false);
                    if (adapter.getItemCount() > 0) {
                        adapter.submitList(new ArrayList<>());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (locationTracker.hasLocationPermission()) {
            locationTracker.getUserLocation();
            showKeyboard();
        } else {
            requestPermissions(new String[] { ACCESS_FINE_LOCATION }, 100);
        }
    }

    @Override
    public void setPredictionsList(List<AutocompletePrediction> predictionsList) {
        adapter.submitList(predictionsList);
    }

    @Override
    public void predictionClick(@NonNull AutocompletePrediction prediction) {
        if (binding.routeNameEt.getText() != null) {
            binding.routeNameEt.getText().clear();
        }
        adapter.submitList(new ArrayList<>());
        viewModel.onPredictionClick(prediction);
    }

    @Override
    public void predictionSelected(AutocompletePrediction prediction) {
        binding.routeNameEt.setText(String.format("%s ", prediction.getStreetName()));
        binding.routeNameEt.setSelection(prediction.getStreetName().length() + 1);
        viewModel.onPredictionSelected();
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            binding.predictionPb.setVisibility(View.VISIBLE);
        } else {
            binding.predictionPb.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(DEBUG_TAG, "Permission granted");
                locationTracker.getUserLocation();
            }
        }
        showKeyboard();
    }

    private void showKeyboard(){
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) requireActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.routeNameEt, 0);
        }, 150);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
