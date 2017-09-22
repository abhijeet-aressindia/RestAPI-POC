package com.example.android.restapipoc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.restapipoc.model.CustomerResponse;

import java.util.ArrayList;

/**
 * Created by User154 on 21-09-2017.
 */

public class CustomerAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private final ArrayList<CustomerResponse> mCustomerList;
    private final RecyclerViewListener listener;

    public CustomerAdapter(Context context, RecyclerViewListener listener, ArrayList<CustomerResponse> mCustomerList) {
        mContext = context;
        this.listener = listener;
        this.mCustomerList = mCustomerList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_customer, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        ((CustomerHolder) holder).cumpanyName.setText(mCustomerList.get(position).getCompanyName());
        ((CustomerHolder) holder).contactName.setText(mCustomerList.get(position).getContactName());
        ((CustomerHolder) holder).phone.setText(mCustomerList.get(position).getPhone());
        ((CustomerHolder) holder).countryAndPostalCode.setText(mCustomerList.get(position).getCountry() + " - " + mCustomerList.get(position).getPostalCode());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, (int) view.getTag());
            }
        });

    }


    @Override
    public int getItemCount() {
        return mCustomerList.size();
    }

    private class CustomerHolder extends RecyclerView.ViewHolder {

        final TextView cumpanyName;
        final TextView phone;
        final TextView countryAndPostalCode;
        final TextView contactName;

        CustomerHolder(View itemView) {
            super(itemView);
            cumpanyName = (TextView) itemView.findViewById(R.id.txt_company_name);
            contactName = (TextView) itemView.findViewById(R.id.txt_contact_name);
            countryAndPostalCode = (TextView) itemView.findViewById(R.id.txt_country_postal_code);
            phone = (TextView) itemView.findViewById(R.id.txt_phone);
        }
    }
}
