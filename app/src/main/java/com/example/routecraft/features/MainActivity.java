package com.example.routecraft.features;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;

import com.example.routecraft.R;
import com.example.routecraft.databinding.ActivityMainBinding;
import com.example.routecraft.features.shared.SharedViewModel;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private MainActivityViewModel viewModel;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        Set<Integer> topLevelDestinations = new HashSet();
        topLevelDestinations.add(R.id.addressListFragment);
        topLevelDestinations.add(R.id.mapFragment);
        topLevelDestinations.add(R.id.driveListFragment);
        appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).setOpenableLayout(binding.drawerLayout).build();

        setSupportActionBar(binding.toolbar);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void init(){
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.setText("Hello");
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}