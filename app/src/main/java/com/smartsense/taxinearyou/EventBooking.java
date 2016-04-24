package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.smartsense.taxinearyou.utill.CommonUtil;

public class EventBooking extends AppCompatActivity implements View.OnClickListener {

    CoordinatorLayout clEventBooking;
    EditText etEventBookingFirstName, etEventBookingLastName, etEventBookingEmailAddress, etEventBookingDateTime,
            etEventBookingEventDuration, etEventBookingContactNumber, etEventBookingVehicleType, etEventBookingPickUp,
            etEventBookingEventDropOff, etEventBookingPassengers, etEventBookingEventLuggage, etEventBookingReq;
    Button btEventBookingBookNow, btEventBookingCancel;
    RadioButton rbEventBookingOneWay, rbEventBookingReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_booking);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        clEventBooking = (CoordinatorLayout) findViewById(R.id.clEventBooking);
        rbEventBookingOneWay = (RadioButton) findViewById(R.id.rbEventBookingOneWay);
        rbEventBookingReturn = (RadioButton) findViewById(R.id.rbEventBookingReturn);
        btEventBookingBookNow = (Button) findViewById(R.id.btEventBookingBookNow);
        btEventBookingCancel = (Button) findViewById(R.id.btEventBookingCancel);
        etEventBookingFirstName = (EditText) findViewById(R.id.etEventBookingFirstName);
        etEventBookingLastName = (EditText) findViewById(R.id.etEventBookingLastName);
        etEventBookingEmailAddress = (EditText) findViewById(R.id.etEventBookingEmailAddress);
        etEventBookingDateTime = (EditText) findViewById(R.id.etEventBookingDateTime);
        etEventBookingEventDuration = (EditText) findViewById(R.id.etEventBookingEventDuration);
        etEventBookingContactNumber = (EditText) findViewById(R.id.etEventBookingContactNumber);
        etEventBookingVehicleType = (EditText) findViewById(R.id.etEventBookingVehicleType);
        etEventBookingPickUp = (EditText) findViewById(R.id.etEventBookingPickUp);
        etEventBookingEventDropOff = (EditText) findViewById(R.id.etEventBookingEventDropOff);
        etEventBookingPassengers = (EditText) findViewById(R.id.etEventBookingPassengers);
        etEventBookingEventLuggage = (EditText) findViewById(R.id.etEventBookingEventLuggage);
        etEventBookingReq = (EditText) findViewById(R.id.etEventBookingReq);

        btEventBookingBookNow.setOnClickListener(this);
        btEventBookingCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btEventBookingBookNow:
                if (TextUtils.isEmpty(etEventBookingFirstName.getText().toString()))
                    CommonUtil.showSnackBar(EventBooking.this, getResources().getString(R.string.enter_first_name), clEventBooking);
                else if (TextUtils.isEmpty(etEventBookingLastName.getText().toString()))
                    CommonUtil.showSnackBar(EventBooking.this, getResources().getString(R.string.enter_last_name), clEventBooking);
                else if (TextUtils.isEmpty(etEventBookingEmailAddress.getText().toString()))
                    CommonUtil.showSnackBar(EventBooking.this, getResources().getString(R.string.enter_email_id), clEventBooking);
                else if (CommonUtil.isValidEmail((etEventBookingEmailAddress.getText().toString())))
                    CommonUtil.showSnackBar(EventBooking.this, getResources().getString(R.string.enter_valid_email_id), clEventBooking);
                else
                    startActivity(new Intent(EventBooking.this, Search.class));
                break;
        }
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
