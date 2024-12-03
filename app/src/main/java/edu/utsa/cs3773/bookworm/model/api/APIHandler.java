package edu.utsa.cs3773.bookworm.model.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import edu.utsa.cs3773.bookworm.BuildConfig;
import edu.utsa.cs3773.bookworm.model.SecureStorage;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;
import retrofit2.http.POST;

// Responsible for handling requests and serializing eligible classes for proper data retrieval
// Should only have one active connection to the database from this app, so all metadata is considered STATIC
public class APIHandler {
    // Automatically converts JSON to fit class structure
    protected static final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(BuildConfig.API_DOMAIN)
        .addConverterFactory(GsonConverterFactory.create())
        .build();



    private static final TokenService TOKEN_SERVICE = retrofit.create(TokenService.class);

    // For handling only successful responses
    protected static class Success {
        public String success;
    }

    private interface TokenService {
        @POST("/auth/refresh")
        Call<SecureStorage.Token> getAccessToken(@Header("authorization") String refreshToken);
    }

    // Readonly class to parse body from retrofit Response class
    public static class Message {
        // The HTTP Status Code retrieved from the backend
        public final int code;
        // Stores whether the response is successful
        public final boolean isSuccessful;
        // The message received from the backend
        public final String message;

        private Message(int code, boolean isSuccessful, String message) {
            this.code = code;
            this.isSuccessful = isSuccessful;
            this.message = message;
        }

        // Network exception occurred (No response code generated)
        private Message(String message) {
            code = 500;
            isSuccessful = false;
            this.message = message;
        }

        public static Message createMessage(int code, boolean isSuccessful, String message) {
            return new Message(code, isSuccessful, message);
        }

        public static Message createMessage(String message) {
            return new Message(message);
        }

        public void displayToast(Context context) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    // Parsing a string value with the key supplied as a parameter
    // Since we are using JSON as our data format for this project, this method will be used heavily
    public static String parseStringFromJson(String jsonStr, String field) {
        // Convert String to JSON
        JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
        // Extract field value from ERROR field
        return jsonObject.get(field).getAsString();
    }

    // For API calls that requires token before retrieval
    protected static <T> CompletableFuture<T> callWithToken(String refreshToken, Supplier<T> future) {
        return refreshAccessToken(refreshToken).thenCompose(v -> CompletableFuture.supplyAsync(future));
    }
    // To use when requesting for user-specific data
    // Made To be used as a helper function
    private static CompletableFuture<Void> refreshAccessToken(String token) {
        return CompletableFuture.runAsync(() -> {
            SecureStorage secureStorage = SecureStorage.getPreferences(null);
            SharedPreferences esf = SecureStorage.getPreferences(null).getStorage();

            SecureStorage.JWTToken storageAccessToken = new SecureStorage.JWTToken(
                SecureStorage.TokenType.ACCESS,
                esf.getString(SecureStorage.TokenType.ACCESS.name(), "NULL")
            );

            // Access token will be null if invalid
            // No need to refresh access token if already valid
            if (storageAccessToken.getDecoded() == null) {
                try {
                    // Access tokens can only be generated from valid refresh tokens
                    if (!esf.contains(SecureStorage.TokenType.REFRESH.name())) {
                        Log.e("No Refresh Token", "Refresh token was not found.");
                        throw new AuthException();
                    }

                    Response<SecureStorage.Token> response = TOKEN_SERVICE.getAccessToken(token).execute();

                    if (!response.isSuccessful())
                        throw new AuthException();

                    Log.d("ACCESS TOKEN", response.body().token);

                    SecureStorage.JWTToken responseToken = new SecureStorage.JWTToken(SecureStorage.TokenType.ACCESS, response.body().token);
                    secureStorage.insertToStorage(responseToken.getTokenType(), responseToken.getDecoded().getToken());
                } catch (IOException e) {
                    Log.e("Network Failure", "Failed to connect to the database. Exiting...");
                } catch (AuthException e) {
                    Log.e("Invalid Refresh Token", "Supplied refresh token is invalid");
                }
            }
        });
    }
}
