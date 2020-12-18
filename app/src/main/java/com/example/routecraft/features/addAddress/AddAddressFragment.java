package com.example.routecraft.features.addAddress;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.routecraft.R;

public class AddAddressFragment extends Fragment {

    private final String debugTag = "debugTag";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(debugTag, "AddAddressFragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);
        Log.d(debugTag, "AddAddressFragment onCreateView");
        return view;
    }
}
