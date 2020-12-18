package com.example.routecraft.features.driveList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.routecraft.databinding.FragmentDriveListBinding;
import com.example.routecraft.features.shared.SharedViewModel;

public class DriveListFragment extends Fragment {

    private final String debugTag = "debugTag";

    private FragmentDriveListBinding binding;
    private SharedViewModel sharedViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(debugTag, "DriveListFragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDriveListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Log.d(debugTag, "DriveListFragment onCreateView");
        return view;
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
