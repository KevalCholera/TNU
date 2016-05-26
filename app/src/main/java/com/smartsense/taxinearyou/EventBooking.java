package com.smartsense.taxinearyou;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.smartsense.taxinearyou.utill.CommonUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

//    final Calendar mCalendar = Calendar.getInstance();
//
//    public void datePicker() {
//        DatePickerDialog DatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(android.widget.DatePicker DatePicker, int year, int month, int dayOfMonth) {
//                mCalendar.set(Calendar.YEAR, year);
//                mCalendar.set(Calendar.MONTH, month);
//                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//                etAddEventDate.setTag(year + "-" + (month + 1) + "-" + dayOfMonth);
//                etAddEventDate.setText(com.myclasscampus.classroom.utill.CommonUtil.settingFormat.format(mCalendar.getTime()));
//
//            }
//        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
//
//        DatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//
//        DatePicker.show();
//    }
//
//    public void Time() {
//        final Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//
//        TimePickerDialog mTimePicker = new TimePickerDialog(this, TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                updateTime(selectedHour, selectedMinute);
//            }
//        }, hour, minute, true);
//
//        mTimePicker.setTitle("Select Time");
//
//        mTimePicker.show();
//    }
//
//    private void updateTime(int hours, int mins) {
//
//        int new_hour = hours;
//        int new_min = mins;
//
//        String timeSet;
//        if (new_hour > 12) {
//            new_hour -= 12;
//            timeSet = "PM";
//        } else if (new_hour == 0) {
//            new_hour += 12;
//            timeSet = "AM";
//        } else if (new_hour == 12)
//            timeSet = "PM";
//        else
//            timeSet = "AM";
//
//        String minutes;
//        if (new_min < 10)
//            minutes = "0" + new_min;
//        else
//            minutes = String.valueOf(new_min);
//
//        String hour;
//        if (new_hour < 10)
//            hour = "0" + new_hour;
//        else
//            hour = String.valueOf(new_hour);
//
//        // Append in a StringBuilder
//        String timepickerTime = hour + ':' + minutes + " " + timeSet;
//
//        if (forConstantTime == startTime) {
//            try {
//                if ((etAddEventDate.getText().toString()).equalsIgnoreCase(com.myclasscampus.classroom.utill.CommonUtil.settingFormat.format(Long.parseLong(System.currentTimeMillis() + "")) + "")) {
//                    if (Integer.valueOf(new SimpleDateFormat("HHmm").format(Long.parseLong(System.currentTimeMillis() + ""))) > Integer.valueOf(new SimpleDateFormat("HHmm").format(new SimpleDateFormat("HHm").parse((hours + "") + (mins + "")))))
//                        Toast.makeText(this, "Select Time Should be Greater than current Time", Toast.LENGTH_SHORT).show();
//                    else if (new SimpleDateFormat("HHmm").format(Long.parseLong(System.currentTimeMillis() + "")).equalsIgnoreCase(new SimpleDateFormat("HHmm").format(new SimpleDateFormat("HHm").parse((hours + "") + (mins + "")))))
//                        Toast.makeText(this, "Select Time Should be Greater than current Time", Toast.LENGTH_SHORT).show();
//                    else {
//                        etAddEventStartTime.setTag(hours + ":" + mins + ":" + "00");
//                        etAddEventStartTime.setText(timepickerTime);
//                    }
//                } else {
//                    etAddEventStartTime.setTag(hours + ":" + mins + ":" + "00");
//                    etAddEventStartTime.setText(timepickerTime);
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        } else if (forConstantTime == endTime) {
//
//            try {
//                if (new SimpleDateFormat("HHmm").format(new SimpleDateFormat("HHm").parse((hours + "") + (mins + ""))).equalsIgnoreCase(new SimpleDateFormat("HHmm").format(new SimpleDateFormat("hh:mm aa").parse(etAddEventStartTime.getText().toString()))) || Integer.valueOf(new SimpleDateFormat("HHmm").format(new SimpleDateFormat("HHm").parse((hours + "") + (mins + "")))) < Integer.valueOf((new SimpleDateFormat("HHmm").format(new SimpleDateFormat("hh:mm aa").parse(etAddEventStartTime.getText().toString())))))
//                    Toast.makeText(this, "Select Time Should be Greater than Start Time", Toast.LENGTH_SHORT).show();
//                else {
//                    etAddEventEndTime.setTag(hours + ":" + mins + ":" + "00");
//                    etAddEventEndTime.setText(timepickerTime);
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//    }

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
//            getMenuInflater().inflate(R.menu.ratingforsearch, menu);
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
