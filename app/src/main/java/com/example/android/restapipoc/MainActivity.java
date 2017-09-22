package com.example.android.restapipoc;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.restapipoc.database.DBHelper;
import com.example.android.restapipoc.databinding.ActivityMainBinding;
import com.example.android.restapipoc.model.CustomerResponse;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerViewListener {
    private static final int PERMISSION_REQUEST = 1001;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private String TAG = "MainActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    ActivityMainBinding mBinding;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private LocationSettingsRequest mLocationSettingsRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private Location mCurrentLocation;
    private boolean mPermissionDenied;

    ArrayList<CustomerResponse> mCustomerList = new ArrayList<>();
    private CustomerAdapter mCustomerAdapter;
    private DBHelper mDBHelper;
    private double longitude;
    private double latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mDBHelper = new DBHelper(this);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.customers_title);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCustomerAdapter = new CustomerAdapter(this, this, mCustomerList);
        mBinding.recyclerView.setAdapter(mCustomerAdapter);
        mBinding.swipeToRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        startLocationUpdates();
                    }
                }
        );


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(INTERVAL);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                if (mCurrentLocation != null) {
                    if (ApiClient.isNetworkAvailable(MainActivity.this))
                        makeAPICall(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    else
                        showSnackbar(getString(R.string.no_network), R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                startActivity(intent);
                            }
                        });
                }
            }
        };
    }

    private void makeAPICall(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        if (mBinding.swipeToRefresh.isRefreshing())
            mBinding.swipeToRefresh.setRefreshing(false);
        stopLocationUpdates();
        final ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setCancelable(false);
        mProgress.setMessage(getString(R.string.wait));
        mProgress.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<CustomerResponse>> call = apiService.getEmployeeData(latitude, longitude);
        call.enqueue(new Callback<List<CustomerResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<CustomerResponse>> call, @NonNull Response<List<CustomerResponse>> response) {

                mDBHelper.fillObjects(CustomerResponse.class, response.body());
                mProgress.dismiss();
                if (mCustomerList.size() > 0) mCustomerList.clear();
                mCustomerList.addAll(response.body());
                mCustomerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<CustomerResponse>> call, @NonNull Throwable t) {
                Log.e(TAG, t.toString());
                mProgress.dismiss();
                showSnackbar(t.toString(), R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startLocationUpdates();
                    }
                });
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            List<CustomerResponse> list = mDBHelper.getAll(CustomerResponse.class);
            if (!list.isEmpty()) {
                mCustomerList.clear();
                mCustomerList.addAll(list);
                mCustomerAdapter.notifyDataSetChanged();
            } else
                startLocationUpdates();

        } else {
            requestPermissions();
        }

    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(getString(R.string.permission_rationale),
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    PERMISSION_REQUEST);
                        }
                    });
        } else if (!mPermissionDenied) {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST);
        }
    }


    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                mPermissionDenied = true;
                showSnackbar(getString(R.string.permission_denied_explanation),
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mPermissionDenied = false;
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }


    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Toast.makeText(MainActivity.this, "Location settings cannot be " +
                                        "fixed here. Fix in Settings.", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);

    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final String mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                mainTextStringId,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra("customerId", mCustomerList.get(position).getCustomerID());
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
    }
}
