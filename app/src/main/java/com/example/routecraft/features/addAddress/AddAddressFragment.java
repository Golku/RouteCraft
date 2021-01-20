package com.example.routecraft.features.addAddress;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.routecraft.R;
import com.example.routecraft.data.pojos.Session;
import com.example.routecraft.databinding.FragmentAddAddressBinding;
import com.example.routecraft.databinding.FragmentAddAddressWithAutocompleteBinding;
import com.example.routecraft.features.addAddressWithAutocomplete.AddAddressWithAutocompleteViewModel;

import java.util.Objects;

public class AddAddressFragment extends Fragment implements AddAddressViewModel.Listener {

    private final String DEBUG_TAG = "DEBUG_TAG";

    private FragmentAddAddressBinding binding;

    private AddAddressViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DEBUG_TAG, "AddAddressFragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddAddressBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Log.d(DEBUG_TAG, "AddAddressFragment onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init(){
        viewModel = new ViewModelProvider(requireActivity()).get(AddAddressViewModel.class);
        viewModel.setListener(this);
        viewModel.setUserId(new Session(requireActivity()).getUserId());
        setOnClickListeners();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) requireActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.streetNameEt, 0);
        }, 150);
    }

    void setOnClickListeners(){
        binding.addBtn.setOnClickListener(view -> {
            viewModel.getAddress(
                    Objects.requireNonNull(binding.streetNameEt.getText()).toString(),
                    binding.postcodeNumEt.getText()+" "+ binding.postcodeLettersEt.getText(),
                    Objects.requireNonNull(binding.cityEt.getText()).toString()
            );
            Objects.requireNonNull(binding.streetNameEt.getText()).clear();
            Objects.requireNonNull(binding.postcodeNumEt.getText()).clear();
            Objects.requireNonNull(binding.postcodeLettersEt.getText()).clear();
            Objects.requireNonNull(binding.cityEt.getText()).clear();
            binding.streetNameEt.requestFocus();
        });
        binding.cancelBtn.setOnClickListener(view -> {
            requireActivity().onBackPressed();
        });
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
        binding.addressType.setVisibility(View.VISIBLE);
        binding.added.setVisibility(View.VISIBLE);
    }

    @Override
    public void setNewAddressContainerVisibility(boolean visible) {
        Log.d(DEBUG_TAG, "setNewAddressContainerVisibility: " + visible);
        if (visible) {
            binding.newAddressContainer.setVisibility(View.VISIBLE);
        } else {
            binding.newAddressContainer.setVisibility(View.GONE);
        }
    }
}
