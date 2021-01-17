package com.example.routecraft.features.addAddress;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class AddAddressFragment extends Fragment {

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
        viewModel.setUserId(new Session(requireActivity()).getUserId());
        setOnClickListeners();
    }

    void setOnClickListeners(){
        binding.addBtn.setOnClickListener(view -> {
            String address = (binding.streetNameEt.getText()+", "+
                                    binding.postcodeNumEt.getText()+" "+
                                    binding.postcodeLettersEt.getText()+" "+
                                    binding.cityEt.getText()+" "+
                                    "The Netherlands");
            viewModel.getAddress(address);
        });
    }
}
