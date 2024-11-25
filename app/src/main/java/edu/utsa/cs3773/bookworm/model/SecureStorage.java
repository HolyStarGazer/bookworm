package edu.utsa.cs3773.bookworm.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Objects;

import edu.utsa.cs3773.bookworm.BuildConfig;
import edu.utsa.cs3773.bookworm.model.api.APIResponse;

// Static class that provides abstraction for storing JWT tokens securely
public class SecureStorage {
    private EncryptedSharedPreferences esf;

    private static SecureStorage secureStorage;
    // Disable class declaration
    private SecureStorage(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            esf = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(
                    context,
                    "SECRET_SHARED_PREFS",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class JWTToken extends APIResponse {
        private String token;
        private DecodedJWT decryptedToken;
        private final JWTVerifier jwtVerifier;

        public JWTToken() {
            Log.d("Secret Refresh", BuildConfig.REFRESH_TOKEN_SECRET);
            jwtVerifier = JWT.require(Algorithm.HMAC256(BuildConfig.REFRESH_TOKEN_SECRET))
                .acceptLeeway(60L)
                .build();
        }

//        public JWTToken(String token) {
//            signedToken = token;
//        }

        public String getSignedToken() {
            return token;
        }

        public DecodedJWT getDecodedToken() {
            return decryptedToken;
        }

        public void setToken(String token) {
            try {
                this.token = token;
                Log.d("Signed Token", "getSignedToken(): " + token);
                decryptedToken = jwtVerifier.verify(token);
                Log.d("Token Header", "Token Header: " + decryptedToken.getHeader());
                Log.d("Token Payload", "Token Payload: " + decryptedToken.getPayload());
                Log.d("Token Signature", "Token Signature: " + decryptedToken.getSignature());
                Log.d("Token ID", "Token ID: " + decryptedToken.getSubject());
            } catch (JWTVerificationException e) {
                Log.e("JWT Error", Objects.requireNonNull(e.getMessage()));
            }
        }

        @NonNull
        @Override
        public String toString() {
            return "Signed Token: " + getSignedToken() +
                    "\nHeader: " + decryptedToken.getHeader() +
                    "\nPayload: " + decryptedToken.getPayload() +
                    "\nSignature: " + decryptedToken.getSignature();
        }
    }

    public static SecureStorage getPreferences(Context context) {
        if (secureStorage == null)
            secureStorage = new SecureStorage(context);

        return secureStorage;
    }

    private void insertToStorage(SharedPreferences.Editor editor, String key, String value) {
        // Invalidate old entry
        if (esf.contains(key))
            editor.remove(key);

        editor.putString(key, value);
        editor.apply();
    }

    public void addTokenToStorage(DecodedJWT decodedToken) {
        SharedPreferences.Editor esfEditor = esf.edit();

        insertToStorage(esfEditor, "REFRESH_TOKEN_STRING", decodedToken.getToken());
        insertToStorage(esfEditor, "REFRESH_TOKEN_HEADER", decodedToken.getHeader());
        insertToStorage(esfEditor, "REFRESH_TOKEN_PAYLOAD", decodedToken.getPayload());
        insertToStorage(esfEditor, "REFRESH_TOKEN_SIGNATURE", decodedToken.getSignature());
    }
}
