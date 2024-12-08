package edu.utsa.cs3773.bookworm.model.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import edu.utsa.cs3773.bookworm.LoginActivity;
import edu.utsa.cs3773.bookworm.MainActivity;
import edu.utsa.cs3773.bookworm.model.AdminUser;
import edu.utsa.cs3773.bookworm.model.BasicUser;
import edu.utsa.cs3773.bookworm.model.SecureStorage;
import edu.utsa.cs3773.bookworm.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

// Handles ALL authentication processes
public class AuthHandler extends APIHandler {
    // Binds the service to the Retrofit client
    private static final AuthService AUTH_SERVICE = retrofit.create(AuthService.class);

    // Route handler responsible for user authentication
    private interface AuthService {
        @GET("/auth/user/{username}")
        Call<User.Response> getUser(@Path("username") String username);
        @POST("/auth/register")
        Call<JsonObject> registerUser(@Body JsonObject registerBody);
        @POST("/auth/login")
        Call<SecureStorage.Token> loginUser(@Body JsonObject loginBody);
    }

    // Do not instantiate class
    private AuthHandler() {}

    // Registers the user once all passes are checked
    public static void registerUser(Activity activity, Context context, String username, String password) {
        JsonObject registerBody = new JsonObject();
        registerBody.addProperty("username", username);
        registerBody.addProperty("password", password);

        AUTH_SERVICE.registerUser(registerBody).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull retrofit2.Response<JsonObject> r) {
                // If the server responds with 2xx
                if (r.isSuccessful()) {
                    assert r.body() != null;
                    APIHandler.Message
                        .createMessage(r.code(), true, "Account created. Redirecting you to login page")
                        .displayToast(context);
                    ((LoginActivity) activity).showLoginFragment();
                    return;
                }
                // This point: server responds with an unsuccessful response (Not 2xx)
                try {
                    // Create a message to display as a Toast for the user to view
                    APIHandler.Message
                        .createMessage(r.code(), true, parseStringFromJson(r.errorBody().string(), "error"))
                        .displayToast(context);
                } catch (IOException e) {
                    Log.e("IO Failure", "Failed to handle unsuccessful response");
                }
            }

            // A network error has occurred
            // Possible cases: client does not have internet, server is not active
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                APIHandler.Message
                    .createMessage(t.getMessage())
                    .displayToast(context);

                Log.e("Network Failure", t.getMessage());
            }
        });
    }

    public static void loginUser(Activity activity, Context context, String username, String password) {
        // The JSON object to send to the server for proper handling
        JsonObject loginBody = new JsonObject();

        loginBody.addProperty("username", username);
        loginBody.addProperty("password", password);

        SecureStorage secureStorage = SecureStorage.getPreferences(null);

        // Meant to receive a token from the server, but it is currently not doing anything
        AUTH_SERVICE.loginUser(loginBody).enqueue(new Callback<>() {
            // Server responds with any HTTP status code
            @Override
            public void onResponse(@NonNull Call<SecureStorage.Token> call, @NonNull retrofit2.Response<SecureStorage.Token> response) {
                // Server responds with 2xx (successful response)
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    // Refresh token will be guaranteed to be valid
                    String responseToken = response.body().token;
                    secureStorage.insertToStorage(SecureStorage.TokenType.REFRESH.name(), responseToken);
                    // Login successful
                    // User is redirected to the MainActivity
                    Toast.makeText(context, "Welcome, " + username + "!", Toast.LENGTH_SHORT).show();
                    // Async: Will not block the main thread when starting a new activity
                    getUser(context, username, password).thenAccept(user -> {
                        MainActivity.setCurrentUser(user);
                        Log.d("USERNAME", user.getUsername());
                    });
                    Intent intent = new Intent(context, MainActivity.class);
                    Bundle args = new Bundle();
                    activity.startActivity(intent, args);
                    activity.finish(); // Close this activity

                    return;
                }

                // This point: server responds with an unsuccessful response (not 2xx)
                try {
                    // Generate message to show Toast for user to view any failures
                    APIHandler.Message
                        .createMessage(response.code(), false, parseStringFromJson(response.errorBody().string(), "error"))
                        .displayToast(context);
                } catch (IOException e) {
                    Log.e("IO Failure", "Failed to handle unsuccessful response");
                }
            }

            // A network error has occurred
            // Possible cases: client does not have internet, server is not active
            @Override
            public void onFailure(@NonNull Call<SecureStorage.Token> call, @NonNull Throwable t) {
                APIHandler.Message
                    .createMessage(t.getMessage())
                    .displayToast(context);
            }
        });
    }

    // Client-specific activity
    // Does not require communication with the backend
    public static void logoutUser() {
        SharedPreferences.Editor sfEditor = SecureStorage.getPreferences(null).getStorage().edit();

        // Remove both refresh and access tokens to revoke access from the database
        sfEditor
            .remove(SecureStorage.TokenType.REFRESH.name())
            .remove(SecureStorage.TokenType.ACCESS.name())
            .apply();
    }

    public static CompletableFuture<User> getUser(Context context, String username, String password) {
        return CompletableFuture.supplyAsync(() -> {
            User user;

            try {
                retrofit2.Response<User.Response> response = AUTH_SERVICE.getUser(username).execute();
                if (!response.isSuccessful()) {
                    APIHandler.Message
                        .createMessage(response.code(), false, parseStringFromJson(response.errorBody().string(), "error"))
                        .displayToast(context);

                    return null;
                }

                User.Response foundUser = response.body();

                user = foundUser.isAdmin
                    ? new AdminUser(foundUser.id, username, password, foundUser.email)
                    : new BasicUser(foundUser.id, username, password, foundUser.email);
            } catch (IOException e) {
                Log.e("FETCH FAILURE", e.getMessage());
                return null;
            }

            return user;
        });
    }
}
