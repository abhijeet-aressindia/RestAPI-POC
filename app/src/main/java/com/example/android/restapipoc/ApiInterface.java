package com.example.android.restapipoc;

import com.example.android.restapipoc.model.CustomerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    String API_CUSTOMER = "api/customer";
    String API_CUSTOMER_ORDER = "api/Order/{CustomerID}";
    @GET(API_CUSTOMER)
    Call<List<CustomerResponse>> getEmployeeData(@Header("lat") double  lat , @Header ("long") double longValue );
 
    @GET(API_CUSTOMER_ORDER)
    Call<CustomerResponse> getMovieDetails(@Path("CustomerID") int id, @Query("api_key") String apiKey);
}