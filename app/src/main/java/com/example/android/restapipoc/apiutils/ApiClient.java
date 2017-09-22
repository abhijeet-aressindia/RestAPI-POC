package com.example.android.restapipoc.apiutils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    public static final String BASE_URL = "http://88.99.217.226:8000/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(20L, TimeUnit.SECONDS)
                    .readTimeout(20L, TimeUnit.SECONDS)
                    .writeTimeout(20L, TimeUnit.SECONDS)
                    .addInterceptor(new BasicAuthInterceptor("admin", "admin"))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)

                    .build();

        }
        return retrofit;
    }

    /**
     * isNetworkAvailable(Context) provides a network state status.
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean var = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            if (cm.getActiveNetworkInfo() != null) {
                var = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return var;
    }
}