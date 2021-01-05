package com.example.routecraft.features.addAddressWithAutocomplete;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.routecraft.R;
import com.example.routecraft.databinding.FragmentAddAddressWithAutocompleteBinding;
import com.example.routecraft.databinding.FragmentAddressListBinding;

public class AddAddressWithAutocompleteFragment extends Fragment {

    private final String debugTag = "debugTag";

    private AddAddressWithAutocompleteViewModel viewModel;
    private FragmentAddAddressWithAutocompleteBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(debugTag, "AddAddressWithAutocompleteFragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddAddressWithAutocompleteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Log.d(debugTag, "AddAddressWithAutocompleteFragment onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        viewModel = new ViewModelProvider(getActivity()).get(AddAddressWithAutocompleteViewModel.class);

        binding.routeNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = binding.routeNameEt.getText().toString().trim();
                if(text.length() >= 3){
                    viewModel.getPredictions(text);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
