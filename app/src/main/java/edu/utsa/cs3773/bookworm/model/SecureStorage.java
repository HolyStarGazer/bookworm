package edu.utsa.cs3773.bookworm;

import android.content.Context;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

// Singleton class that provides abstraction for storing JWT tokens securely
public class SecureStorage {
    private EncryptedSharedPreferences esf;
    private static String jwtToken;
    // Disable class declaration
    private SecureStorage() {}

    public EncryptedSharedPreferences getPreferences(Context context) {
        if (esf == null) {
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

        return esf;
    }
}
