package com.smartsense.taxinearyou;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class LostItemDetail extends AppCompatActivity {

    TextView tvLostItemDetailTNR, tvLostItemDetailFrom, tvLostItemDetailTo, tvLostItemDetailProvider, tvLostItemDetailDateTime, tvLostItemDetailStatus, tvLostItemDetailLostItem, tvLostItemDetailStatusDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item_detail);

        tvLostItemDetailTNR = (TextView) findViewById(R.id.tvLostItemDetailTNR);
        tvLostItemDetailFrom = (TextView) findViewById(R.id.tvLostItemDetailFrom);
        tvLostItemDetailTo = (TextView) findViewById(R.id.tvLostItemDetailTo);
        tvLostItemDetailProvider = (TextView) findViewById(R.id.tvLostItemDetailProvider);
        tvLostItemDetailDateTime = (TextView) findViewById(R.id.tvLostItemDetailDateTime);
        tvLostItemDetailStatus = (TextView) findViewById(R.id.tvLostItemDetailStatus);
        tvLostItemDetailLostItem = (TextView) findViewById(R.id.tvLostItemDetailLostItem);
        tvLostItemDetailStatusDescription = (TextView) findViewById(R.id.tvLostItemDetailStatusDescription);
    }
}
