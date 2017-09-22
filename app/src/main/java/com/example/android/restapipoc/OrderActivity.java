package com.example.android.restapipoc;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.android.restapipoc.database.DBHelper;
import com.example.android.restapipoc.databinding.ActivityOrderBinding;
import com.example.android.restapipoc.model.OrderResponce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity implements RecyclerViewListener {

    private static final double DEFAUL_LONG = 20.00;
    private static final double DEFAUL_LAT = 19.00;
    private static final String TAG = "OrderActivity";
    private ActivityOrderBinding mBinding;
    private ArrayList<OrderResponce> mOrderList = new ArrayList<>();
    private OrderAdapter mOrderAdapter;
    private double mLongi;
    private String mCustId;
    private double mLati;
    private DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.orders_title);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDBHelper = new DBHelper(this);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mOrderAdapter = new OrderAdapter(this, this, mOrderList);
        mBinding.recyclerView.setAdapter(mOrderAdapter);
        mBinding.swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchOrderData();
            }
        });

        mCustId = getIntent().getStringExtra("customerId");
        mLati = getIntent().getDoubleExtra("latitude", DEFAUL_LAT);
        mLongi = getIntent().getDoubleExtra("longitude", DEFAUL_LONG);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Map<String, Object> map = new HashMap<>();
        map.put("customerID", mCustId);
        List<OrderResponce> temp = mDBHelper.query(OrderResponce.class, map);
        if (temp.size() > 0) {
            mOrderList.clear();
            mOrderList.addAll(temp);
            mOrderAdapter.notifyDataSetChanged();
        } else {
            fetchOrderData();
        }
    }

    private void fetchOrderData() {
        if (mBinding.swipeToRefresh.isRefreshing())
            mBinding.swipeToRefresh.setRefreshing(false);
        final ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setCancelable(false);
        mProgress.setMessage(getString(R.string.wait));
        mProgress.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<OrderResponce>> call = apiService.getOrderData(mCustId, mLati, mLongi);
        call.enqueue(new Callback<List<OrderResponce>>() {
            @Override
            public void onResponse(@NonNull Call<List<OrderResponce>> call, @NonNull Response<List<OrderResponce>> response) {
                mDBHelper.fillObjects(OrderResponce.class, response.body());
                mProgress.dismiss();
                if (mOrderList.size() > 0) mOrderList.clear();
                mOrderList.addAll(response.body());
                mOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<OrderResponce>> call, @NonNull Throwable t) {
                Log.e(TAG, t.toString());
                mProgress.dismiss();
                showSnackbar(t.toString(), R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fetchOrderData();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                this.finish();
                break;

        }
        return false;
    }


    @Override
    public void onItemClick(View v, int position) {

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
}
