package edu.utsa.cs3773.bookworm.model;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Objects;

import edu.utsa.cs3773.bookworm.BuildConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class APIHandler {
    // Automatically converts JSON to fit class structure
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.API_DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static final AuthService authService = retrofit.create(AuthService.class);

    public interface AuthService {
        @POST("/auth/register")
        Call<SecureStorage.ReceivedToken> getJWTTokenFromRegister(@Body JsonObject registerBody);
        @POST("/auth/login")
        Call<SecureStorage.ReceivedToken> getJWTTokenFromLogin(@Body JsonObject loginBody);
    }

    // Readonly class to parse body from retrofit Response class
    public static class Message {
        public final int code;
        public final boolean isSuccessful;
        public final String message;

        public Message(int code, boolean isSuccessful, String message) {
            this.code = code;
            this.isSuccessful = isSuccessful;
            this.message = message;
        }
    }

    // To propagate error messages to the main thread for proper error handling
    private static Message handleError(String label, Response<?> response) throws IOException {
        // Convert String to JSON
        JsonObject json = JsonParser.parseString(response.errorBody().string()).getAsJsonObject();
        // Extract error message from ERROR field
        String errorMessage = json.get("ERROR").getAsString();
        // Log the message as an error in logcat
        Log.e(label, errorMessage);

        return new Message(response.code(), false, errorMessage);
    }

    public static Message registerUser(String username, String password) throws AuthException {
        final Message[] responseMessage = {null};

        JsonObject registerBody = new JsonObject();

        registerBody.addProperty("username", username);
        registerBody.addProperty("password", password);

        authService.getJWTTokenFromRegister(registerBody).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<SecureStorage.ReceivedToken> call, @NonNull Response<SecureStorage.ReceivedToken> r) {
                if (r.isSuccessful()) {
                    String successMessage = "token: " + token.getSignedToken();
                    Log.d("Retrieved Token", successMessage);
                    responseMessage[0] = new Message(r.code(), true, successMessage);

                    return;
                }
                // This point: server responds with an unsuccessful response
                try {
                    responseMessage[0] = handleError("CODE " + r.code(), r);
                } catch (IOException e) {
                    Log.e("IO Failure", "Failed to handle unsuccessful response");
                }
            }

            @Override
            public void onFailure(@NonNull Call<SecureStorage.ReceivedToken> call, @NonNull Throwable t) {
                Log.e("Request Failed", Objects.requireNonNull(t.getMessage()));
            }
        });

        return responseMessage[0];
    }

    public static Message loginUser(String username, String password) throws AuthException {
        JsonObject loginBody = new JsonObject();

        loginBody.addProperty("username", username);
        loginBody.addProperty("password", password);

        SecureStorage.JWTToken token = new SecureStorage.JWTToken();

        authService.getJWTTokenFromLogin(loginBody).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<SecureStorage.ReceivedToken> call, @NonNull Response<SecureStorage.ReceivedToken> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    token.setToken(response.body().token);
                } else {
                    try {
                        assert response.errorBody() != null;
                        handleError("CODE " + response.code(), new AuthException(response.errorBody().string()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SecureStorage.ReceivedToken> call, @NonNull Throwable t) {
                Log.e("Request Failed", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}
