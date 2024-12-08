package edu.utsa.cs3773.bookworm.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

import edu.utsa.cs3773.bookworm.BuildConfig;

// Singleton class that provides abstraction for storing JWT tokens securely
public class SecureStorage {
    public enum TokenType {
        REFRESH, ACCESS
    }

    private static final Map<TokenType, String> tokenTypes = new EnumMap<>(TokenType.class);

    // Static initializer block for populating EnumMap once at runtime
    static {
        tokenTypes.put(TokenType.REFRESH, BuildConfig.REFRESH_TOKEN_SECRET);
        tokenTypes.put(TokenType.ACCESS, BuildConfig.ACCESS_TOKEN_SECRET);
    }


    private EncryptedSharedPreferences esf;

    private static SecureStorage secureStorage;
    // Disable class declaration
    private SecureStorage(Context context) {
        try {
            esf = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(
                    context,
                    "SECRET_SHARED_PREFS",
                    new MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // For server responses
    public static class Token {
        public String token;
    }

    public static class JsonWebToken {
        private final TokenType tokenType;
        private DecodedJWT token;

        public JsonWebToken(TokenType tokenType, String token) {
            this.tokenType = tokenType;
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenTypes.get(tokenType)))
                .acceptLeeway(60L)
                .build();

            try {
                // Throws an exception if verification fails
                this.token = verifier.verify(token);
            } catch (Exception e) {
                this.token = null;
                Log.e("FAILURE", "Token is invalid");
            }
        }

        public String getString() {
            return token.getToken();
        }

        public boolean isExpired() {
            return token.getExpiresAt().after(new Date());
        }

        public DecodedJWT getDecoded() {
            return token;
        }

        public String getTokenType() {
            return tokenType.name();
        }
    }

    // For instantiating SecureStorage for the first time
    public static SecureStorage getPreferences(Context context) {
        if (secureStorage == null)
            secureStorage = new SecureStorage(context);

        return secureStorage;
    }

    public EncryptedSharedPreferences getStorage() {
        return esf;
    }

    public void insertToStorage(String key, String value) {
        SharedPreferences.Editor editor = esf.edit();
        // Overwrites existing entry
        editor.putString(key, value);
        editor.apply();
    }
}
