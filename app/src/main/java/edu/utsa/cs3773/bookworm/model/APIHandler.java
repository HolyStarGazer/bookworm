package edu.utsa.cs3773.bookworm.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class APIHandler {
    private final AuthService authService;
    private static APIHandler api;


    public interface AuthService {
        @POST("/auth/register")
        Call<SecureStorage.JWTToken> getJWTTokenFromRegister(@Body JsonObject registerBody);
        @POST("/auth/login")
        Call<SecureStorage.JWTToken> getJWTTokenFromLogin(@Body JsonObject loginBody);
    }

    private APIHandler() {
        // Automatically converts JSON to fit class structure
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        authService = retrofit.create(AuthService.class);
    }

    public static APIHandler getInstance() {
        if (api == null)
            api = new APIHandler();

        return api;
    }

    public SecureStorage.JWTToken registerUser(String email, String username, String password) {
        JsonObject registerBody = new JsonObject();

        registerBody.addProperty("email", email);
        registerBody.addProperty("username", username);
        registerBody.addProperty("password", password);

        SecureStorage.JWTToken token = new SecureStorage.JWTToken("");
        authService.getJWTTokenFromRegister(registerBody).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<SecureStorage.JWTToken> call, @NonNull Response<SecureStorage.JWTToken> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    token.setToken(response.body().getToken());
                    Log.d("Retrieved Token", "token: " + token.getToken());
                } else {
                    Log.e("CODE " + response.code(), "Token was not able to be retrieved");
                }
            }

            @Override
            public void onFailure(@NonNull Call<SecureStorage.JWTToken> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

        return token;
    }
}
