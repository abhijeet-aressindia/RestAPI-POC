package com.example.android.restapipoc.apiutils;

import com.example.android.restapipoc.model.CustomerResponse;
import com.example.android.restapipoc.model.OrderResponce;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;


public interface ApiInterface {
    String API_CUSTOMER = "api/customer";
    String API_CUSTOMER_ORDER = "api/Order/{CustomerID}";
    @GET(API_CUSTOMER)
    Call<List<CustomerResponse>> getEmployeeData(@Header("lat") double  lat , @Header ("long") double longValue );
 
    @GET(API_CUSTOMER_ORDER)
    Call<List<OrderResponce>> getOrderData(@Path("CustomerID") String id, @Header("lat") double  lat , @Header ("long") double longValue);
}