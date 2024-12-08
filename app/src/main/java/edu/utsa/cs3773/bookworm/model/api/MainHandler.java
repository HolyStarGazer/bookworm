package edu.utsa.cs3773.bookworm.model.api;

import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import edu.utsa.cs3773.bookworm.model.Book;
import edu.utsa.cs3773.bookworm.model.SecureStorage;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;

public class MainHandler extends APIHandler {
    private static final MainService MAIN_SERVICE = retrofit.create(MainService.class);

    private interface MainService {
        @GET("/api/books")
        Call<List<Book.Preview>> getSavedBooks(@Header("authorization") String accessToken);
    }

    // Do not instantiate class
    private MainHandler() {}

    public static CompletableFuture<List<Book.Preview>> getSavedBooks() {
        SharedPreferences esf = SecureStorage.getPreferences(null).getStorage();

        return callWithToken(esf.getString(SecureStorage.TokenType.REFRESH.name(), "NULL"), () -> {
            Response<List<Book.Preview>> response;
            try {
                response = MAIN_SERVICE.getSavedBooks(esf.getString(SecureStorage.TokenType.ACCESS.name(), "NULL")).execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (!response.isSuccessful()) {
                try {
                    String m = parseStringFromJson(response.errorBody().string(), "error");
                    Log.e("BAD RESPONSE", m);
                } catch (IOException e) {
                    // IOException will be thrown as a runtime error instead for exceptionally()
                    throw new RuntimeException(e.getMessage());
                }

                // Return an empty list
                return new ArrayList<Book.Preview>(0);
            }

            return response.body();
        }).exceptionally(e -> {
            Log.e("FETCH FAILURE", e.getMessage());

            // Return an empty list on failure
            return new ArrayList<>(0);
        });
    }
}
