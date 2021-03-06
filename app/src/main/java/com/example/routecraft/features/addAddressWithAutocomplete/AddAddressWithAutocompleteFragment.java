package com.example.routecraft.features.addAddressWithAutocomplete;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.routecraft.R;
import com.example.routecraft.data.pojos.AutocompletePrediction;
import com.example.routecraft.data.pojos.Session;
import com.example.routecraft.databinding.FragmentAddAddressWithAutocompleteBinding;
import com.example.routecraft.features.shared.LocationTracker;
import com.example.routecraft.features.shared.SharedViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AddAddressWithAutocompleteFragment extends Fragment implements
        AddAddressWithAutocompleteViewModel.Listener,
        AutocompleteAdapter.Listener{

    private final String DEBUG_TAG = "DEBUG_TAG";

    private AddAddressWithAutocompleteViewModel viewModel;
    private SharedViewModel sharedViewModel;

    private NavController navController;

    private FragmentAddAddressWithAutocompleteBinding binding;

    private AutocompleteAdapter adapter;

    private LocationTracker locationTracker;

    private Listener listener;

    public interface Listener{
        void addAddressWithAutoMessageDialog(String message);
    }

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
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
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        setHasOptionsMenu(true);

        locationTracker = new LocationTracker(requireActivity(), viewModel);

        adapter = new AutocompleteAdapter(this);
        binding.predictionRv.setAdapter(adapter);

        binding.addManuallyLink.setText(Html.fromHtml("<u>Try Adding it manually</u>"));
        binding.addManuallyLinkContainer.setOnClickListener(view -> {
            NavDirections action = AddAddressWithAutocompleteFragmentDirections.actionAddAddressWithAutocompleteFragmentToAddAddressFragment();
            navController.navigate(action);
        });

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
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 100);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_address_with_autocomplete_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_manual_address_input) {
            NavDirections action = AddAddressWithAutocompleteFragmentDirections.actionAddAddressWithAutocompleteFragmentToAddAddressFragment();
            navController.navigate(action);
            return true;
        }
        return false;
    }

    @Override
    public Session getSession() {
        return new Session(requireActivity());
    }

    @Override
    public void setPredictionsList(List<AutocompletePrediction> predictionsList) {
        adapter.submitList(predictionsList);
    }

    @Override
    public void predictionSelected(AutocompletePrediction prediction) {
        binding.routeNameEt.setText(String.format("%s ", prediction.getStreetName()));
        binding.routeNameEt.setSelection(prediction.getStreetName().length() + 1);
    }

    @Override
    public void predictionClick(@NonNull AutocompletePrediction prediction) {
        if(viewModel.addressAlreadyInRoute(
                Objects.requireNonNull(sharedViewModel.getAddressList().getValue()),
                prediction)
        ){
            Toast.makeText(requireActivity(), "Address already in route", Toast.LENGTH_SHORT).show();
        }else{
            viewModel.onPredictionClick(prediction);
            Objects.requireNonNull(binding.routeNameEt.getText()).clear();
        }
    }

    @Override
    public void setHelperTextVisibility(boolean visible) {
        if (visible) {
            binding.helperText.setVisibility(View.VISIBLE);
        } else {
            binding.helperText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setPredictionListVisibility(boolean visible) {
        if (visible) {
            binding.predictionRv.setVisibility(View.VISIBLE);
        } else {
            binding.predictionRv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setNoResultsContainerVisibility(boolean visible) {
        if (visible) {
            binding.noResultsContainer.setVisibility(View.VISIBLE);
        } else {
            binding.noResultsContainer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setNewAddressContainerVisibility(boolean visible) {
        if (visible) {
            binding.newAddressContainer.setVisibility(View.VISIBLE);
        } else {
            binding.newAddressContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void setFetchingAddressContainerVisibility(boolean visible) {
        if (visible) {
            binding.fetchingAddressContainer.setVisibility(View.VISIBLE);
        } else {
            binding.fetchingAddressContainer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void fetchingNewAddress(String street, String city) {
        binding.streetName.setText(street);
        binding.cityName.setText(city);
        binding.added.setVisibility(View.INVISIBLE);
        binding.addressType.setVisibility(View.INVISIBLE);
        binding.addingAddressPb.setVisibility(View.VISIBLE);
    }

    @Override
    public void newAddressAdded(String street, String city) {
        binding.streetName.setText(street);
        binding.cityName.setText(city);
        binding.addingAddressPb.setVisibility(View.INVISIBLE);
        binding.addressType.setImageResource(R.drawable.ic_marker_blue);
        binding.addressType.setVisibility(View.VISIBLE);
        binding.added.setVisibility(View.VISIBLE);
    }

    @Override
    public void failedToGetAddress() {
        binding.addingAddressPb.setVisibility(View.INVISIBLE);
        binding.addressType.setVisibility(View.VISIBLE);
        binding.addressType.setImageResource(R.drawable.ic_fail);
        listener.addAddressWithAutoMessageDialog("Failed to add address");
    }

    @Override
    public void failedToGetPrediction() {
        listener.addAddressWithAutoMessageDialog("Failed to get address prediction");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationTracker.getUserLocation();
            }
        }
        showKeyboard();
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.isActive();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            imm.showSoftInput(binding.routeNameEt, 0);
        }, 100);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Listener){
            listener = (Listener) context;
        }else{
            throw  new RuntimeException(context.toString() + " must implement Listener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
