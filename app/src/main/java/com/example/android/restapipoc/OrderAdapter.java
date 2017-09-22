package com.example.android.restapipoc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.restapipoc.model.OrderResponce;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by User154 on 21-09-2017.
 */

public class OrderAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private final ArrayList<OrderResponce> mOrderList;
    private final RecyclerViewListener listener;

    public OrderAdapter(Context context, RecyclerViewListener listener, ArrayList<OrderResponce> mOrderList) {
        mContext = context;
        this.listener = listener;
        this.mOrderList = mOrderList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_customer, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        ((OrderHolder) holder).orederid.setText(String.format(Locale.getDefault(), "%d", mOrderList.get(position).getOrderID()));
        ((OrderHolder) holder).requiredDate.setText(mOrderList.get(position).getRequiredDate());
        ((OrderHolder) holder).adress.setText(mOrderList.get(position).getShipAddress());
        ((OrderHolder) holder).country.setText(mOrderList.get(position).getShipCountry());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, (int) view.getTag());
            }
        });

    }


    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    private class OrderHolder extends RecyclerView.ViewHolder {

        final TextView requiredDate;
        final TextView country;
        final TextView adress;
        final TextView orederid;

        OrderHolder(View itemView) {
            super(itemView);
            requiredDate = (TextView) itemView.findViewById(R.id.txt_company_name);
            orederid = (TextView) itemView.findViewById(R.id.txt_contact_name);
            adress = (TextView) itemView.findViewById(R.id.txt_country_postal_code);
            country = (TextView) itemView.findViewById(R.id.txt_phone);
        }
    }
}
