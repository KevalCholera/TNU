package com.smartsense.taxinearyou;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TripDetails extends AppCompatActivity implements View.OnClickListener {

TextView tvTripDetailBookingDate, tvTripDetailBookingTime, tvTripDetailLost, tvTripDetailTaxiProvider, tvTripDetailPickUpDate,
        tvTripDetailPickUpTime, tvTripDetailFare, tvTripDetailFrom, tvTripDetailVia1, tvTripDetailVia2, tvTripDetailTo,
        tvTripDetailTNR, tvTripDetailPassengers, tvTripDetailLugguages, tvTripDetailMiles, tvTripDetailPayment, tvTripDetailRideType,
        tvTripDetailRideStatus, tvTripDetailVehicle, tvTripDetailCancle, tvTripDetailInvoice, tvTripDetailFeedback;
LinearLayout lyTripDetailInvoiceFeedback;
AlertDialog alert;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trip_details);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    tvTripDetailBookingDate = (TextView) findViewById(R.id.tvTripDetailBookingDate);
    tvTripDetailBookingTime = (TextView) findViewById(R.id.tvTripDetailBookingTime);
    tvTripDetailLost = (TextView) findViewById(R.id.tvTripDetailLost);
    tvTripDetailTaxiProvider = (TextView) findViewById(R.id.tvTripDetailTaxiProvider);
    tvTripDetailPickUpDate = (TextView) findViewById(R.id.tvTripDetailPickUpDate);
    tvTripDetailPickUpTime = (TextView) findViewById(R.id.tvTripDetailPickUpTime);
    tvTripDetailFare = (TextView) findViewById(R.id.tvTripDetailFare);
    tvTripDetailFrom = (TextView) findViewById(R.id.tvTripDetailFrom);
    tvTripDetailVia1 = (TextView) findViewById(R.id.tvTripDetailVia1);
    tvTripDetailVia2 = (TextView) findViewById(R.id.tvTripDetailVia2);
    tvTripDetailTo = (TextView) findViewById(R.id.tvTripDetailTo);
    tvTripDetailTNR = (TextView) findViewById(R.id.tvTripDetailTNR);
    tvTripDetailPassengers = (TextView) findViewById(R.id.tvTripDetailPassengers);
    tvTripDetailLugguages = (TextView) findViewById(R.id.tvTripDetailLugguages);
    tvTripDetailMiles = (TextView) findViewById(R.id.tvTripDetailMiles);
    tvTripDetailPayment = (TextView) findViewById(R.id.tvTripDetailPayment);
    tvTripDetailRideType = (TextView) findViewById(R.id.tvTripDetailRideType);
    tvTripDetailRideStatus = (TextView) findViewById(R.id.tvTripDetailRideStatus);
    tvTripDetailVehicle = (TextView) findViewById(R.id.tvTripDetailVehicle);
    tvTripDetailCancle = (TextView) findViewById(R.id.tvTripDetailCancle);
    tvTripDetailInvoice = (TextView) findViewById(R.id.btTripDetailInvoice);
    tvTripDetailFeedback = (TextView) findViewById(R.id.tvTripDetailFeedback);
    lyTripDetailInvoiceFeedback = (LinearLayout) findViewById(R.id.lyTripDetailInvoiceFeedback);

    String s = getIntent().getStringExtra("key");
    tvTripDetailRideStatus.setText(s);


    if (tvTripDetailRideStatus.getText().toString().equalsIgnoreCase("waiting")) {
        tvTripDetailCancle.setVisibility(View.VISIBLE);
    } else if (tvTripDetailRideStatus.getText().toString().equalsIgnoreCase("completed")) {
        tvTripDetailLost.setVisibility(View.VISIBLE);
        lyTripDetailInvoiceFeedback.setVisibility(View.VISIBLE);
    }

    tvTripDetailCancle.setOnClickListener(this);
    tvTripDetailLost.setOnClickListener(this);
    tvTripDetailFeedback.setOnClickListener(this);
}

@Override
public void onClick(View v) {

    switch (v.getId()) {
        case R.id.tvTripDetailCancle:
            openOccasionsPopup();
            break;
        case R.id.tvTripDetailLost:
            startActivity(new Intent(TripDetails.this, YourDetails.class));
            break;
        case R.id.tvTripDetailFeedback:
            startActivity(new Intent(TripDetails.this, Feedback.class));
            break;
    }
}

public void openOccasionsPopup() {
    try {

        final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialog = inflater.inflate(R.layout.dialog_all, null);
        LinearLayout lyPopUpCancelRide = (LinearLayout) dialog.findViewById(R.id.lyPopUpCancelRide);
        lyPopUpCancelRide.setVisibility(View.VISIBLE);

        TextView tvPopupCancelRideOk, tvPopupCancelRideCancel;

        tvPopupCancelRideOk = (TextView) dialog.findViewById(R.id.tvPopupCancelRideOk);
        tvPopupCancelRideCancel = (TextView) dialog.findViewById(R.id.tvPopupCancelRideCancel);

        tvPopupCancelRideOk.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(TripDetails.this, Search.class));
            }
        });
        tvPopupCancelRideCancel.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alertDialogs.setView(dialog);
        alertDialogs.setCancelable(true);
        alert = alertDialogs.create();
        alert.show();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
