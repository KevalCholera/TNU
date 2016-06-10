package com.smartsense.taxinearyou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.WakeLocker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class LostItemDetail extends AppCompatActivity {

    TextView tvLostItemDetailTNR, tvLostItemDetailFrom, tvLostItemDetailTo, tvLostItemDetailProvider, tvLostItemDetailDateTime, tvLostItemDetailStatus, tvLostItemDetailLostItem, tvLostItemDetailStatusDescription;
    private int requestRefresh = 0;
    JSONObject lostItemDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerReceiver(tripMessageReceiver, new IntentFilter(String.valueOf(Constants.Events.ADD_LOST_ITEM)));
        registerReceiver(tripMessageReceiver, new IntentFilter(String.valueOf(Constants.Events.LOST_ITEM_STATUS)));
        tvLostItemDetailTNR = (TextView) findViewById(R.id.tvLostItemDetailTNR);
        tvLostItemDetailFrom = (TextView) findViewById(R.id.tvLostItemDetailFrom);
        tvLostItemDetailTo = (TextView) findViewById(R.id.tvLostItemDetailTo);
        tvLostItemDetailProvider = (TextView) findViewById(R.id.tvLostItemDetailProvider);
        tvLostItemDetailDateTime = (TextView) findViewById(R.id.tvLostItemDetailDateTime);
        tvLostItemDetailStatus = (TextView) findViewById(R.id.tvLostItemDetailStatus);
        tvLostItemDetailLostItem = (TextView) findViewById(R.id.tvLostItemDetailLostItem);
        tvLostItemDetailStatusDescription = (TextView) findViewById(R.id.tvLostItemDetailStatusDescription);

        try {
            lostItemDetails = new JSONObject(getIntent().getStringExtra("lostItemDetails"));
            tvLostItemDetailTNR.setText(lostItemDetails.optString("pnr"));
            tvLostItemDetailFrom.setText(lostItemDetails.optString("fromArea"));
            tvLostItemDetailTo.setText(lostItemDetails.optString("toArea"));
            tvLostItemDetailProvider.setText(lostItemDetails.optString("partnerName"));
            tvLostItemDetailStatusDescription.setText(lostItemDetails.optString("statusMsg"));
            tvLostItemDetailDateTime.setText(Constants.DATE_FORMAT_DATE_TIME.format(Constants.DATE_FORMAT_FULL_DATE_TIME.parse(lostItemDetails.optString("rideDate"))));
            tvLostItemDetailStatus.setText(lostItemDetails.optString("status"));
            tvLostItemDetailLostItem.setText(lostItemDetails.optString("itemDescription"));//lostItemDetails.optString("color") + " color\n" +
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        if (requestRefresh == 1)
            setResult(RESULT_OK);
        else
            setResult(RESULT_CANCELED);
        finish();
    }

    private final BroadcastReceiver tripMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WakeLocker.acquire(context);
            Log.i("Push ", intent.getStringExtra(Constants.EXTRAS));
            try {
                requestRefresh = 1;
                JSONObject pushData = new JSONObject(intent.getStringExtra(Constants.EXTRAS));
                if (pushData.has("lostItem")) {
                    if(pushData.optJSONObject("lostItem").optInt("id")==lostItemDetails.optInt("id"))
                    tvLostItemDetailStatus.setText(pushData.optJSONObject("lostItem").optString("status"));
                    tvLostItemDetailStatusDescription.setText(pushData.optJSONObject("lostItem").optString("statusMsg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            WakeLocker.release();
        }
    };

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(tripMessageReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
