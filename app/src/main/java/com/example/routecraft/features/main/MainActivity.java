package com.example.routecraft.features.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.routecraft.R;
import com.example.routecraft.data.pojos.Route;
import com.example.routecraft.data.pojos.Session;
import com.example.routecraft.databinding.ActivityMainBinding;
import com.example.routecraft.features.dialogs.CreateNewRouteDialog;
import com.example.routecraft.features.dialogs.DeleteRouteDialog;
import com.example.routecraft.features.dialogs.RenameRouteDialog;
import com.example.routecraft.features.login.LoginActivity;
import com.example.routecraft.features.shared.SharedViewModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements MainActivityViewModel.Listener,
        RouteAdapter.Listener,
        CreateNewRouteDialog.Listener,
        RenameRouteDialog.Listener,
        DeleteRouteDialog.Listener {

    private final String DEBUG_TAG = "DEBUG_TAG";

    private Session session;

    private MainActivityViewModel viewModel;
    private SharedViewModel sharedViewModel;
    private NavController navController;
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;

    private RouteAdapter routeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.setViewListener(this);
        //sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        session = new Session(this);

        binding.usernameTv.setText(session.getUsername());

        routeAdapter = new RouteAdapter(this);
        binding.routeListRv.setAdapter(routeAdapter);
        binding.routeListRv.setHasFixedSize(true);

        viewModel.getAllRoutes().observe(this, new Observer<List<Route>>() {
            @Override
            public void onChanged(List<Route> routes) {
                Log.d(DEBUG_TAG, "Submitting new list");

//                routeAdapter = new RouteAdapter(MainActivity.this);
//                binding.routeListRv.setAdapter(routeAdapter);

                routeAdapter.submitList(routes);
            }
        });

        setOnClickListeners();

        viewModel.getRoute(session.getCurrentRoute());
    }

    @Override
    protected void onStart() {
        super.onStart();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.addressListFragment);
        topLevelDestinations.add(R.id.mapFragment);
        topLevelDestinations.add(R.id.driveListFragment);
        appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).setOpenableLayout(binding.drawerLayout).build();

        setSupportActionBar(binding.toolbar);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    private void setOnClickListeners() {
        binding.createNewRouteBtn.setOnClickListener(view -> openNewRouteDialog());

        binding.logoutBtn.setOnClickListener(view -> {

            if (!session.getRememberUsername()) {
                session.setUserId(0);
                session.setUsername("");
            }

            if (session.getStayLoggedIn()) {
                session.setStayLoggedIn(false);
            }

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void routeClicked(Route route) {
        Log.d(DEBUG_TAG, "Route clicked, route ID: " + route.getId());
        //binding.drawerLayout.closeDrawer(GravityCompat.START);
        if(viewModel.getCurrentRoute().getId() == route.getId()){
            Log.d(DEBUG_TAG, "This route is already loaded");
            return;
        }
        viewModel.loadRoute(route);
    }

    private void openNewRouteDialog() {
        DialogFragment createNewRouteDialog = new CreateNewRouteDialog();
        createNewRouteDialog.setCancelable(false);
        createNewRouteDialog.show(getSupportFragmentManager(), "create route");
    }

    @Override
    public void onNewRouteName(String routeName) {
//        binding.drawerLayout.closeDrawer(GravityCompat.START);
        viewModel.createNewRoute(routeName);
    }

    @Override
    public void openRenameRouteDialog(@NonNull Route route) {
        //Log.d(DEBUG_TAG, "Rename dialog");
        viewModel.setRouteToModify(route);
        Bundle bundle = new Bundle();
        bundle.putString("ROUTE_NAME", route.getName());
        DialogFragment renameRouteDialog = new RenameRouteDialog();
        renameRouteDialog.setArguments(bundle);
        renameRouteDialog.setCancelable(false);
        renameRouteDialog.show(getSupportFragmentManager(), "rename route");
    }

    @Override
    public void onRenameRoute(String routeName) {
        viewModel.renameRoute(viewModel.getRouteToModify(), routeName);
    }

    @Override
    public void openDeleteRouteDialog(@NonNull Route route) {

        if(viewModel.getAllRoutes().getValue() != null && viewModel.getAllRoutes().getValue().size() == 1){
            Toast.makeText(this, "Can't delete only route", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.setRouteToModify(route);
        Bundle bundle = new Bundle();
        bundle.putString("ROUTE_NAME", route.getName());
        DialogFragment deleteRouteDialog = new DeleteRouteDialog();
        deleteRouteDialog.setArguments(bundle);
        deleteRouteDialog.show(getSupportFragmentManager(), "delete route");
    }

    @Override
    public void onDeleteRoute() {
        viewModel.deleteRoute(viewModel.getRouteToModify());
    }

    @Override
    public Session getSession() {
        return session;
    }
}