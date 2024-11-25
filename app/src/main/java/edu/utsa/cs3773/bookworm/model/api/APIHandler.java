package edu.utsa.cs3773.bookworm.model.api;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import edu.utsa.cs3773.bookworm.BuildConfig;
import edu.utsa.cs3773.bookworm.LoginActivity;
import edu.utsa.cs3773.bookworm.MainActivity;
import edu.utsa.cs3773.bookworm.model.SecureStorage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

// Responsible for handling requests and serializing eligible classes for proper data retrieval
// Should only have one active connection to the database from this app, so all metadata is considered STATIC
public class APIHandler {
    // Automatically converts JSON to fit class structure
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.API_DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    // Binds the service to the Retrofit client
    public static final AuthService AUTH_SERVICE = retrofit.create(AuthService.class);

    // Route handler responsible for user authentication
    public interface AuthService {
        @POST("/auth/register")
        Call<JsonObject> registerUser(@Body JsonObject registerBody);
        @POST("/auth/login")
        Call<SecureStorage.JWTToken> loginUser(@Body JsonObject loginBody);
    }

    // Readonly class to parse body from retrofit Response class
    public static class Message {
        // The HTTP Status Code retrieved from the backend
        public final int code;
        // Stores whether the response is successful
        public final boolean isSuccessful;
        // The message received from the backend
        public final String message;

        public Message(int code, boolean isSuccessful, String message) {
            this.code = code;
            this.isSuccessful = isSuccessful;
            this.message = message;
        }

        // Network exception occurred (No response code generated)
        public Message(String message) {
            code = 500;
            isSuccessful = false;
            this.message = message;
        }

        public void displayToast(Context context) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }


    // Parsing a string value with the key supplied as a parameter
    // Since we are using JSON as our data format for this project, this method will be used heavily
    private static String parseStringFromJson(String jsonStr, String field) {
        // Convert String to JSON
        JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
        // Extract field value from ERROR field
        return jsonObject.get(field).getAsString();
    }

    // Registers the user once all passes are checked
    public static void registerUser(Activity activity, Context context, String username, String password) {
        JsonObject registerBody = new JsonObject();

        registerBody.addProperty("username", username);
        registerBody.addProperty("password", password);

        AUTH_SERVICE.registerUser(registerBody).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> r) {
                // If the server responds with 2xx
                if (r.isSuccessful()) {
                    assert r.body() != null;
                    Toast.makeText(context, "Account created. Redirecting you to login page", Toast.LENGTH_SHORT).show();
                    ((LoginActivity) activity).showLoginFragment();
                    return;
                }
                // This point: server responds with an unsuccessful response (Not 2xx)
                try {
                    // Create a message to display as a Toast for the user to view
                    Message error = new Message(r.code(), false, parseStringFromJson(r.errorBody().string(), "error"));
                    Log.e("CODE " + error.code, error.message);
                    error.displayToast(context);
                } catch (IOException e) {
                    Log.e("IO Failure", "Failed to handle unsuccessful response");
                }
            }

            // A network error has occurred
            // Possible cases: client does not have internet, server is not active
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Message failure = new Message(t.getMessage());
                Log.e("Request Failure", failure.message);
            }
        });
    }

    public static void loginUser(Activity activity, Context context, String username, String password) {
        // The JSON object to send to the server for proper handling
        JsonObject loginBody = new JsonObject();

        loginBody.addProperty("username", username);
        loginBody.addProperty("password", password);

        SecureStorage.JWTToken token = new SecureStorage.JWTToken();

        // Meant to receive a token from the server, but it is currently not doing anything
        // TODO: Implement token functionality possibly in the next sprint
        AUTH_SERVICE.loginUser(loginBody).enqueue(new Callback<>() {
            // Server responds with any HTTP status code
            @Override
            public void onResponse(@NonNull Call<SecureStorage.JWTToken> call, @NonNull Response<SecureStorage.JWTToken> r) {
                // Server responds with 2xx (successful response)
                if (r.isSuccessful()) {
                    assert r.body() != null;
                    token.setToken(r.body().getSignedToken());

                    // Login successful
                    // User is redirected to the MainActivity
                    Toast.makeText(context, "Welcome, " + username + "!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    Bundle args = new Bundle();
                    args.putLong("user", 0); // must pass user ID as argument when navigating to MainActivity
                    activity.startActivity(intent, args);
                    activity.finish(); // Close this activity
                    return;
                }

                // This point: server responds with an unsuccessful response (not 2xx)
                try {
                    // Generate message to show Toast for user to view any failures
                    Message error = new Message(r.code(), false, parseStringFromJson(r.errorBody().string(), "error"));
                    Log.e("CODE " + error.code, error.message);
                    error.displayToast(context);
                } catch (IOException e) {
                    Log.e("IO Failure", "Failed to handle unsuccessful response");
                }
            }
            // A network error has occurred
            // Possible cases: client does not have internet, server is not active
            @Override
            public void onFailure(@NonNull Call<SecureStorage.JWTToken> call, @NonNull Throwable t) {
                Message failure = new Message(t.getMessage());
                Log.e("Request Failure", failure.message);
            }
        });
    }
}
