package edu.utsa.cs3773.bookworm.model;

import retrofit2.Retrofit;
import retrofit2.http.GET;

public class APIHandler {
    private Retrofit retrofit;
    private APIHandler(String apiUrl) {
        retrofit = new Retrofit.Builder()
            .baseUrl(apiUrl)
            .build();
    }

    public Retrofit getInstance() {
        if (retrofit == null)
            new APIHandler("http://localhost:3000/");

        return retrofit;
    }

    @GET("/")
    public void sampleFetch() {

    }
}
