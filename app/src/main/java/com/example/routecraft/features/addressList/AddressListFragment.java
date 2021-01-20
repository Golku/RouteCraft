package com.example.routecraft.features.addressList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.routecraft.R;
import com.example.routecraft.data.pojos.Address;
import com.example.routecraft.data.pojos.RouteWithAddresses;
import com.example.routecraft.data.pojos.Session;
import com.example.routecraft.databinding.FragmentAddressListBinding;
import com.example.routecraft.features.addAddressWithAutocomplete.AddAddressWithAutocompleteFragmentDirections;
import com.example.routecraft.features.shared.SharedViewModel;

import java.util.List;

public class AddressListFragment extends Fragment implements AddressListViewModel.Listener,
        AddressAdapter.Listener {

    private final String DEBUG_TAG = "DEBUG_TAG";

    private FragmentAddressListBinding binding;
    private NavController navController;
    private SharedViewModel sharedViewModel;
    private AddressListViewModel viewModel;
    private AddressAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DEBUG_TAG, "AddressListFragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddressListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Log.d(DEBUG_TAG, "AddressListFragment onCreateView");
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

    private void init(){
        viewModel = new ViewModelProvider(requireActivity()).get(AddressListViewModel.class);
        viewModel.setViewListener(this);
//        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getAllRoutesWithAddresses().observe(getViewLifecycleOwner(), routeWithAddresses -> {
            if(routeWithAddresses != null){
                if(routeWithAddresses.size()>0){
                    viewModel.loadAddressList(routeWithAddresses);
                }
            }
        });

        setHasOptionsMenu(true);

        adapter = new AddressAdapter(this);
        binding.addressList.setAdapter(adapter);
        binding.addressList.setHasFixedSize(true);

        binding.addAddressBtn.setOnClickListener(view1 -> {
            NavDirections action = AddressListFragmentDirections.actionAddressListFragmentToAddAddressWithAutocompleteFragment();
            navController.navigate(action);
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_address_with_autocomplete_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_manual_address_input) {
            return true;
        }
        return false;
    }

    @Override
    public Session getSession() {
        return new Session(requireActivity());
    }

    @Override
    public void setAddressList(List<Address> addressList) {
        adapter.submitList(addressList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
