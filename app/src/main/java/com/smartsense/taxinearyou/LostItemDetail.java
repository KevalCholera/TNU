package com.smartsense.taxinearyou;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class LostItemDetail extends AppCompatActivity {

    TextView tvLostItemDetailTNR, tvLostItemDetailFrom, tvLostItemDetailTo, tvLostItemDetailProvider, tvLostItemDetailDateTime, tvLostItemDetailStatus, tvLostItemDetailLostItem, tvLostItemDetailStatusDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvLostItemDetailTNR = (TextView) findViewById(R.id.tvLostItemDetailTNR);
        tvLostItemDetailFrom = (TextView) findViewById(R.id.tvLostItemDetailFrom);
        tvLostItemDetailTo = (TextView) findViewById(R.id.tvLostItemDetailTo);
        tvLostItemDetailProvider = (TextView) findViewById(R.id.tvLostItemDetailProvider);
        tvLostItemDetailDateTime = (TextView) findViewById(R.id.tvLostItemDetailDateTime);
        tvLostItemDetailStatus = (TextView) findViewById(R.id.tvLostItemDetailStatus);
        tvLostItemDetailLostItem = (TextView) findViewById(R.id.tvLostItemDetailLostItem);
        tvLostItemDetailStatusDescription = (TextView) findViewById(R.id.tvLostItemDetailStatusDescription);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ratingforsearch, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
