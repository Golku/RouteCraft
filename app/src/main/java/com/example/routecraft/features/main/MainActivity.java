package com.example.routecraft.features.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.routecraft.R;
import com.example.routecraft.data.pojos.Route;
import com.example.routecraft.data.pojos.Session;
import com.example.routecraft.databinding.ActivityMainBinding;
import com.example.routecraft.features.addAddressWithAutocomplete.AddAddressWithAutocompleteFragment;
import com.example.routecraft.features.dialogs.CreateNewRouteDialog;
import com.example.routecraft.features.dialogs.DeleteRouteDialog;
import com.example.routecraft.features.dialogs.GenericMessageDialog;
import com.example.routecraft.features.dialogs.RenameRouteDialog;
import com.example.routecraft.features.login.LoginActivity;
import com.example.routecraft.features.shared.SharedViewModel;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements MainActivityViewModel.Listener,
        RouteAdapter.Listener,
        CreateNewRouteDialog.Listener,
        RenameRouteDialog.Listener,
        DeleteRouteDialog.Listener,
        GenericMessageDialog.Listener,
        AddAddressWithAutocompleteFragment.Listener {

    private final String DEBUG_TAG = "DEBUG_TAG";

    private Session session;

    private ActivityMainBinding binding;

    private MainActivityViewModel viewModel;
    private SharedViewModel sharedViewModel;

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    private AlertDialog createNewRouteDialog;
    private AlertDialog renameRouteDialog;
    private AlertDialog deleteRouteDialog;
    private AlertDialog genericMessageDialog;

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
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        session = new Session(this);

        binding.usernameTv.setText(session.getUsername());

        RouteAdapter routeAdapter = new RouteAdapter(this);
        binding.routeListRv.setAdapter(routeAdapter);
        binding.routeListRv.setHasFixedSize(true);

        viewModel.getAllRoutes().observe(this, routeAdapter::submitList);

        viewModel.getAllRoutesWithAddresses().observe(this, routeWithAddresses ->
                sharedViewModel.setAddressList(viewModel.getAddressList(routeWithAddresses))
        );

        setOnClickListeners();
        viewModel.getRoute(session.getCurrentRoute());
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

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void routeClicked(Route route) {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        if (viewModel.getCurrentRoute().getRouteId() == route.getRouteId()) {
            return;
        }
        routeListScrollToTop();
        viewModel.loadRoute(route);
    }

    private void openNewRouteDialog() {
        createNewRouteDialog = new CreateNewRouteDialog(this,
                getLayoutInflater())
                .create();
        createNewRouteDialog.show();
    }

    @Override
    public void onCreateRoute(String routeName, boolean cancel) {
        createNewRouteDialog.dismiss();
        if (cancel) {
            return;
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        routeListScrollToTop();
        viewModel.createNewRoute(routeName);
    }

    @Override
    public void renameRouteBtnClicked(@NonNull Route route) {
        viewModel.setRouteToModify(route);
        renameRouteDialog = new RenameRouteDialog(this,
                getLayoutInflater(),
                route.getName())
                .create();
        renameRouteDialog.show();
    }

    @Override
    public void onRenameRoute(String routeName, boolean cancel) {
        renameRouteDialog.dismiss();
        if (cancel) {
            return;
        }
        viewModel.renameRoute(viewModel.getRouteToModify(), routeName);
    }

    @Override
    public void deleteRouteBtnClicked(@NonNull Route route) {

        if (viewModel.getAllRoutes().getValue() != null && viewModel.getAllRoutes().getValue().size() == 1) {
            Toast.makeText(this, "Can't delete only route", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.setRouteToModify(route);

        deleteRouteDialog = new DeleteRouteDialog(this,
                getLayoutInflater(),
                route.getName())
                .create();
        deleteRouteDialog.show();
    }

    @Override
    public void onDeleteRoute(boolean cancel) {
        deleteRouteDialog.dismiss();
        if (cancel) {
            return;
        }
        viewModel.deleteRoute(viewModel.getRouteToModify());
    }

    private void routeListScrollToTop() {
        new Handler(Looper.getMainLooper()).postDelayed(() ->
                binding.routeListRv.scrollToPosition(0), 500);
    }

    @Override
    public void addAddressWithAutoMessageDialog(String message) {
        showGenericMessageDialog(message);
    }

    private void showGenericMessageDialog(String message) {
        genericMessageDialog = new GenericMessageDialog(this,
                getLayoutInflater(),
                message)
                .create();
        genericMessageDialog.show();
    }

    @Override
    public void genericMessageDialogOkBtnClick() {
        genericMessageDialog.dismiss();
    }
}