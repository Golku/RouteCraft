package com.example.routecraft.features.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.routecraft.data.pojos.database.LoginResponse;

public class LoginViewModel extends AndroidViewModel implements LoginRepository.RepositoryListener{

    private ViewListener view;
    private LoginRepository repository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new LoginRepository(this);
    }

    public interface ViewListener{
        void loginSuccessful(int userId);
        void loginFailed(String message);
    }

    public void setViewListener(ViewListener view){
        this.view = view;
    }

    public void loginRequest(String username, String password) {
        repository.loginRequest(username, password);
    }

    @Override
    public void loginRequestSuccessful(LoginResponse response) {

        switch (response.getMessage()){
            case "match":
                view.loginSuccessful(response.getUserId());
                break;
            case "no_match":
                view.loginFailed("Username or password incorrect");
                break;
            default:
                view.loginFailed("Bad login request: "+response.getMessage());
        }

    }

    @Override
    public void loginRequestFailed(String message) {
        view.loginFailed("Failed to login: " + message);
    }
}
