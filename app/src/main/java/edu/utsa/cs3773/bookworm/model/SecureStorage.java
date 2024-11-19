package edu.utsa.cs3773.bookworm.model;

import android.content.Context;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

// Static class that provides abstraction for storing JWT tokens securely
public class SecureStorage {
    private EncryptedSharedPreferences esf;
    private JWTToken jwtToken;

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

    public static class JWTToken {
        private String token;

        public JWTToken(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public static SecureStorage getPreferences(Context context) {
        if (secureStorage == null)
            secureStorage = new SecureStorage(context);

        return secureStorage;
    }

    public void setJwtToken(JWTToken addedJwtToken) {
        jwtToken = addedJwtToken;
    }

    public JWTToken getJwtToken() {
        return jwtToken;
    }

    public void addTokenToStorage() {
        esf.edit().putString("JWT_REFRESH_TOKEN", jwtToken.getToken()).apply();
    }
}
