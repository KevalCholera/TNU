package com.smartsense.taxinearyou;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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

        try {
            JSONObject lostItemDetails = new JSONObject(getIntent().getStringExtra("lostItemDetails"));
            tvLostItemDetailTNR.setText(lostItemDetails.optString("pnr"));
            tvLostItemDetailFrom.setText(lostItemDetails.optString("fromArea"));
            tvLostItemDetailTo.setText(lostItemDetails.optString("toArea"));
            tvLostItemDetailProvider.setText(lostItemDetails.optString("partnerName"));
            tvLostItemDetailStatusDescription.setText(lostItemDetails.optString("statusMsg"));
            tvLostItemDetailDateTime.setText(new SimpleDateFormat("dd.MM.yyyy hh:mm aa").format(new SimpleDateFormat("dd-MMMM-yyyy HH:mm").parse(lostItemDetails.optString("rideDate"))));
            tvLostItemDetailStatus.setText(lostItemDetails.optString("status"));
            tvLostItemDetailLostItem.setText(lostItemDetails.optString("color") + " color\n" + lostItemDetails.optString("itemDescription"));
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.ratingforsearch, menu);
        return true;
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
