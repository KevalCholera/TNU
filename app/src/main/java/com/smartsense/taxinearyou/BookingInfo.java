package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BookingInfo extends AppCompatActivity {

    TextView tvBookInfoDate, tvBookInfoTime, tvBookInfoFrom, tvBookInfoVia1, tvBookInfoVia2, tvBookInfoTo, tvBookInfoVehicleType,
            tvBookInfoProvider, tvBookInfoPassengers, tvBookInfoLugguages, tvBookInfoETA, tvBookInfoDistance, tvBookInfoRideType,
            tvBookInfoCost;

    Button btBookingInfoConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvBookInfoDate = (TextView) findViewById(R.id.tvBookInfoDate);
        tvBookInfoTime = (TextView) findViewById(R.id.tvBookInfoTime);
        tvBookInfoFrom = (TextView) findViewById(R.id.tvBookInfoFrom);
        tvBookInfoVia1 = (TextView) findViewById(R.id.tvBookInfoVia1);
        tvBookInfoVia2 = (TextView) findViewById(R.id.tvBookInfoVia2);
        tvBookInfoTo = (TextView) findViewById(R.id.tvBookInfoTo);
        tvBookInfoVehicleType = (TextView) findViewById(R.id.tvBookInfoVehicleType);
        tvBookInfoProvider = (TextView) findViewById(R.id.tvBookInfoProvider);
        tvBookInfoPassengers = (TextView) findViewById(R.id.tvBookInfoPassengers);
        tvBookInfoLugguages = (TextView) findViewById(R.id.tvBookInfoLugguages);
        tvBookInfoETA = (TextView) findViewById(R.id.tvBookInfoETA);
        tvBookInfoDistance = (TextView) findViewById(R.id.tvBookInfoDistance);
        tvBookInfoRideType = (TextView) findViewById(R.id.tvBookInfoRideType);
        tvBookInfoCost = (TextView) findViewById(R.id.tvBookInfoCost);
        btBookingInfoConfirm = (Button) findViewById(R.id.btBookingInfoConfirm);

        tvBookInfoCost.setText((char) 0x00A3 + "406.00");


        btBookingInfoConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingInfo.this, PersonalInfo.class));
            }
        });

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
