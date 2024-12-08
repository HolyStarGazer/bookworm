package edu.utsa.cs3773.bookworm.model.api;

import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import edu.utsa.cs3773.bookworm.model.SecureStorage;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class TokenHandler extends APIHandler {
    private static final TokenService TOKEN_SERVICE = retrofit.create(TokenService.class);

    private interface TokenService {
        @POST("/auth/refresh")
        Call<SecureStorage.Token> getAccessToken(@Header("authorization") String refreshToken);
    }

    // Do not instantiate this class
    private TokenHandler() {}

    // To use when requesting for user-specific data
    // Made To be used as a helper function
    public static CompletableFuture<Void> refreshAccessToken(String refreshToken) {
        return CompletableFuture.runAsync(() -> {
            SecureStorage secureStorage = SecureStorage.getPreferences(null);
            SharedPreferences esf = SecureStorage.getPreferences(null).getStorage();

            SecureStorage.JsonWebToken storageAccessToken = new SecureStorage.JsonWebToken(
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

                    Response<SecureStorage.Token> response = TOKEN_SERVICE.getAccessToken(refreshToken).execute();

                    if (!response.isSuccessful())
                        throw new AuthException();

                    Log.d("ACCESS TOKEN", response.body().token);

                    SecureStorage.JsonWebToken responseToken = new SecureStorage.JsonWebToken(SecureStorage.TokenType.ACCESS, response.body().token);
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
