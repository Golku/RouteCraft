package com.example.routecraft.features.addressList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.routecraft.NavGraphDirections;
import com.example.routecraft.databinding.FragmentAddressListBinding;
import com.example.routecraft.features.shared.SharedViewModel;

public class AddressListFragment extends Fragment {

    private final String debugTag = "debugTag";

    private FragmentAddressListBinding binding;
    private NavController navController;
    private SharedViewModel sharedViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(debugTag, "AddressListFragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddressListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Log.d(debugTag, "AddressListFragment onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        binding.addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = AddressListFragmentDirections.actionAddressListFragmentToAddAddressFragment();
                navController.navigate(action);
            }
        });
        binding.showAddressDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = NavGraphDirections.actionGlobalAddressDetailsFragment();
                navController.navigate(action);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        binding.modTv.setText(sharedViewModel.getText());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
