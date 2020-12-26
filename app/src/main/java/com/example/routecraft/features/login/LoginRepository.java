package com.example.routecraft.features.login;

import androidx.annotation.NonNull;

import com.example.routecraft.data.database.DatabaseService;
import com.example.routecraft.data.pojos.database.LoginResponse;
import com.example.routecraft.features.shared.BaseRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository extends BaseRepository {

    private DatabaseService databaseService;
    private RepositoryListener listener;

    public LoginRepository(RepositoryListener listener) {
        this.databaseService = createDatabaseService();
        this.listener = listener;
    }

    public interface RepositoryListener{
        void loginRequestSuccessful(LoginResponse response);
        void loginRequestFailed(String message);
    }

    public void loginRequest(String username, String password){
        Call<LoginResponse> call = databaseService.login(username, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                listener.loginRequestSuccessful(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                listener.loginRequestFailed(t.getMessage());
            }
        });
    }
}
