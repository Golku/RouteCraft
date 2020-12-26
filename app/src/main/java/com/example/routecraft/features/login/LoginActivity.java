package com.example.routecraft.features.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.routecraft.data.pojos.Session;
import com.example.routecraft.databinding.ActivityLoginBinding;
import com.example.routecraft.features.main.MainActivity;
import com.example.routecraft.features.dialogs.GenericLoadingDialog;
import com.example.routecraft.features.dialogs.GenericMessageDialog;

public class LoginActivity extends AppCompatActivity implements LoginViewModel.ViewListener {

    private final String debugTag = "debugTag";

    private Session session;
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
    
    private String username;
    private DialogFragment genericLoadingDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init(){
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.setViewListener(this);
        session = new Session(this);

        if(session.getRememberUsername()){
            binding.usernameEt.setText(session.getUsername());
            binding.rememberMeCb.setChecked(true);
            binding.passwordEt.requestFocus();
        }

        setOnClickListeners();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if(session.getStayLoggedIn()){
                grantAccess();
            }else{
                binding.loginCl.setVisibility(View.VISIBLE);

                if(session.getRememberUsername()){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(binding.passwordEt, 0);
                }
            }
        }, 1000);
    }

    private void setOnClickListeners(){
        binding.loginBtn.setOnClickListener(view -> {

            if(getCurrentFocus() != null){
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            showGenericLoadingDialog();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                username = binding.usernameEt.getText().toString();
                viewModel.loginRequest(username.toLowerCase(), binding.passwordEt.getText().toString());
            }, 1000);
        });
    }

    @Override
    public void loginSuccessful(int userId) {

        dismissGenericLoadingDialog();

        session.setUserId(userId);
        session.setUsername(username);

        session.setRememberUsername(binding.rememberMeCb.isChecked());

        if(binding.stayLoggedInCb.isChecked()) {
            session.setStayLoggedIn(true);
        }

        grantAccess();
    }

    private void grantAccess(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFailed(String message) {
        dismissGenericLoadingDialog();
        new Handler(Looper.getMainLooper()).postDelayed(() -> showGenericMessageDialog(message), 150);
    }

    private void showGenericMessageDialog(String message){
        Bundle bundle = new Bundle();
        bundle.putString("DIALOG_MESSAGE_KEY", message);
        DialogFragment genericMessageDialog = new GenericMessageDialog();
        genericMessageDialog.setArguments(bundle);
        genericMessageDialog.show(getSupportFragmentManager(), "generic message");
    }

    private void showGenericLoadingDialog(){
        Bundle bundle = new Bundle();
        bundle.putString("DIALOG_MESSAGE_KEY", "Logging in");
        genericLoadingDialog = new GenericLoadingDialog();
        genericLoadingDialog.setArguments(bundle);
        genericLoadingDialog.setCancelable(false);
        genericLoadingDialog.show(getSupportFragmentManager(), "generic loading");
    }

    private void dismissGenericLoadingDialog(){
        genericLoadingDialog.dismiss();
    }
}