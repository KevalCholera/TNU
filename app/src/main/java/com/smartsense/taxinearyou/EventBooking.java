package com.smartsense.taxinearyou;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.DataRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.SimpleFormatter;

public class EventBooking extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {

    CoordinatorLayout clEventBooking;
    EditText etEventBookingFirstName, etEventBookingLastName, etEventBookingEmailAddress, etEventBookingDateTime,
            etEventBookingEventDuration, etEventBookingContactNumber, etEventBookingVehicleType, etEventBookingPickUp,
            etEventBookingEventDropOff, etEventBookingPassengers, etEventBookingEventLuggage, etEventBookingReq;
    Button btEventBookingBookNow, btEventBookingCancel;
    RadioButton rbEventBookingOneWay, rbEventBookingReturn;
    private AlertDialog alert;
    String luggage = "luggage";
    String passenger = "passenger";
    String vehicleType = "vehicleType";
    String duration = "duration";

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
        etEventBookingDateTime.setOnClickListener(this);
        etEventBookingEventLuggage.setOnClickListener(this);
        etEventBookingPassengers.setOnClickListener(this);
        etEventBookingPickUp.setOnClickListener(this);
        etEventBookingVehicleType.setOnClickListener(this);
        etEventBookingEventDropOff.setOnClickListener(this);
        btEventBookingCancel.setOnClickListener(this);
        rbEventBookingReturn.setOnClickListener(this);
        rbEventBookingOneWay.setOnClickListener(this);
        etEventBookingEventDuration.setOnClickListener(null);
        setValue();
    }

    final Calendar mCalendar = Calendar.getInstance();
    Boolean once = true;

    public void datePicker() {
        DatePickerDialog DatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker DatePicker, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (once) {
                    Time(new SimpleDateFormat("dd-MMM-yyyy").format(mCalendar.getTime()));
                    once = false;
                }
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

        DatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        DatePicker.show();
    }

    public void setValue() {
        try {
            JSONArray jsonLuggage = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_EVENT_lUGGAGE_LIST, ""));
            JSONArray jsonDuration = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_EVENT_DURATION, ""));
            JSONArray jsonPassenger = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_EVENT_PASSENGER_LIST, ""));
            JSONArray jsonVehicleType = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_EVENT_VEHICLE_TYPE, ""));

            etEventBookingEventLuggage.setText(jsonLuggage.optJSONObject(0).optString("name"));
            etEventBookingEventLuggage.setTag(jsonLuggage.optJSONObject(0).optString("id"));

            etEventBookingEventDuration.setText(jsonDuration.optJSONObject(0).optString("name"));
            etEventBookingEventDuration.setTag(jsonLuggage.optJSONObject(0).optString("id"));

            etEventBookingPassengers.setText(jsonPassenger.optJSONObject(0).optString("name"));
            etEventBookingPassengers.setTag(jsonLuggage.optJSONObject(0).optString("id"));

            etEventBookingVehicleType.setText(jsonVehicleType.optJSONObject(0).optString("name"));
            etEventBookingVehicleType.setTag(jsonLuggage.optJSONObject(0).optString("id"));

            etEventBookingDateTime.setText(new SimpleDateFormat("dd-MM-yyyy -- hh:mm aa").format(System.currentTimeMillis()));
            etEventBookingDateTime.setTag(new SimpleDateFormat("dd-MM-yyyy hh:mm aa").format(System.currentTimeMillis()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Time(final String date) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker = new TimePickerDialog(this, TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                updateTime(selectedHour, selectedMinute, date);
            }
        }, hour, minute, true);

        mTimePicker.setTitle("Select Time");

        mTimePicker.show();
    }

    private void updateTime(int hours, int mins, String date) {

        int new_hour = hours;

        String timeSet;
        if (new_hour > 12) {
            new_hour -= 12;
            timeSet = "PM";
        } else if (new_hour == 0) {
            new_hour += 12;
            timeSet = "AM";
        } else if (new_hour == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes;
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        String hour;
        if (new_hour < 10)
            hour = "0" + new_hour;
        else
            hour = String.valueOf(new_hour);

        String timepickerTime = hour + ':' + minutes + " " + timeSet;

        try {
            if (date.equalsIgnoreCase(new SimpleDateFormat("dd-MMM-yyyy").format(Long.parseLong(System.currentTimeMillis() + "")) + "")) {
                if (Integer.valueOf(new SimpleDateFormat("HHmm").format(Long.parseLong(System.currentTimeMillis() + ""))) > Integer.valueOf(new SimpleDateFormat("HHmm").format(new SimpleDateFormat("HHm").parse((hours + "") + (mins + "")))))
                    Toast.makeText(this, "Select Time Should be Greater than current Time", Toast.LENGTH_SHORT).show();
                else if (new SimpleDateFormat("HHmm").format(Long.parseLong(System.currentTimeMillis() + "")).equalsIgnoreCase(new SimpleDateFormat("HHmm").format(new SimpleDateFormat("HHm").parse((hours + "") + (mins + "")))))
                    Toast.makeText(this, "Select Time Should be Greater than current Time", Toast.LENGTH_SHORT).show();
                else {
                    etEventBookingDateTime.setTag(date + " " + timepickerTime);
                    etEventBookingDateTime.setText(date + " -- " + timepickerTime);
                }
            } else {
                etEventBookingDateTime.setTag(date + " " + timepickerTime);
                etEventBookingDateTime.setText(date + " -- " + timepickerTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
                else if (CommonUtil.isValidEmail((etEventBookingContactNumber.getText().toString())))
                    CommonUtil.showSnackBar(EventBooking.this, getResources().getString(R.string.enter_contact_no), clEventBooking);
                else if (etEventBookingContactNumber.length() < 7 || etEventBookingContactNumber.length() > 13)
                    CommonUtil.showSnackBar(EventBooking.this, getResources().getString(R.string.enter_valid_contact_no), clEventBooking);
                else if (CommonUtil.isValidEmail((etEventBookingPickUp.getText().toString())))
                    CommonUtil.showSnackBar(EventBooking.this, getResources().getString(R.string.enter_from), clEventBooking);
                else if (CommonUtil.isValidEmail((etEventBookingEventDropOff.getText().toString())))
                    CommonUtil.showSnackBar(EventBooking.this, getResources().getString(R.string.enter_to), clEventBooking);
                else if (CommonUtil.isValidEmail((etEventBookingReq.getText().toString())))
                    CommonUtil.showSnackBar(EventBooking.this, getResources().getString(R.string.enter_req), clEventBooking);
                else
                    eventBooking();

                break;
            case R.id.etEventBookingDateTime:
                once = true;
                datePicker();
                break;
            case R.id.rbEventBookingOneWay:
                etEventBookingEventDuration.setOnClickListener(null);
                break;
            case R.id.rbEventBookingReturn:
                etEventBookingEventDuration.setOnClickListener(this);
                break;
            case R.id.etEventBookingEventDuration:
                openQuestionSelectPopup(duration);
                break;
            case R.id.etEventBookingPassengers:
                openQuestionSelectPopup(passenger);
                break;
            case R.id.etEventBookingEventLuggage:
                openQuestionSelectPopup(luggage);
                break;
            case R.id.etEventBookingVehicleType:
                openQuestionSelectPopup(vehicleType);
                break;
            case R.id.etEventBookingPickUp:
                startActivityForResult(new Intent(this, GooglePlaces.class).putExtra("typeAddress", 1), 1);
                break;
            case R.id.etEventBookingEventDropOff:
                startActivityForResult(new Intent(this, GooglePlaces.class).putExtra("typeAddress", 2), 2);
                break;
        }
    }

    public void openQuestionSelectPopup(String check) {
        try {

            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_select_question, null);

            TextView tvCityDialogHead = (TextView) dialog.findViewById(R.id.tvCityDialogHead);
            String str;
            if (check.equalsIgnoreCase(luggage)) {
                str = SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_EVENT_lUGGAGE_LIST, "");
                tvCityDialogHead.setText("Select Luggages");
            } else if (check.equalsIgnoreCase(vehicleType)) {
                str = SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_EVENT_VEHICLE_TYPE, "");
                tvCityDialogHead.setText("Select Vehicle Type");
            } else if (check.equalsIgnoreCase(duration)) {
                str = SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_EVENT_DURATION, "");
                tvCityDialogHead.setText("Select Duration");
            } else {
                str = SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_EVENT_PASSENGER_LIST, "");
                tvCityDialogHead.setText("Select Passenger/Passengers");
            }
            JSONArray jsonArray = new JSONArray(str);
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);
            AdapterClass adapterClass = new AdapterClass(this, jsonArray, check);
            list_view.setAdapter(adapterClass);
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(true);
            alert = alertDialogs.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String message;
        if (resultCode == Activity.RESULT_OK) {
            message = data.getStringExtra("AreaName");
            switch (data.getIntExtra("typeAddress", 0)) {
                case 1:
                    etEventBookingPickUp.setText(message);
                    SharedPreferenceUtil.putValue(Constants.FROM_ADDRESS, data.getStringExtra("address"));
                    break;
                case 2:
                    etEventBookingEventDropOff.setText(message);
                    SharedPreferenceUtil.putValue(Constants.TO_ADDRESS, data.getStringExtra("address"));
                    break;
            }
            SharedPreferenceUtil.save();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.cancelProgressDialog();
        CommonUtil.errorToastShowing(this);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        CommonUtil.cancelProgressDialog();
        if (jsonObject != null) {
            CommonUtil.successToastShowing(this, jsonObject);
            if (jsonObject.optInt("status") == Constants.STATUS_SUCCESS)
                finish();
        }
    }

    public class AdapterClass extends BaseAdapter {
        private final String check;
        private JSONArray data;
        private LayoutInflater inflater = null;
        Activity a;

        public AdapterClass(Activity a, JSONArray data, String check) {
            this.data = data;
            this.a = a;
            this.check = check;
            inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return data.length();
        }


        public Object getItem(int position) {
            return data.optJSONObject(position);
        }


        public long getItemId(int position) {
            return position;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.element_select_question, null);
            final TextView cbElementClassName = (TextView) vi.findViewById(R.id.tvElementQuestionName);
            JSONObject object = data.optJSONObject(position);
            cbElementClassName.setTag(object.optString("id"));
            cbElementClassName.setText(object.optString("name"));
            cbElementClassName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (check.equalsIgnoreCase(luggage)) {
                        etEventBookingEventLuggage.setText(cbElementClassName.getText().toString());
                        etEventBookingEventLuggage.setTag(cbElementClassName.getTag());
                    } else if (check.equalsIgnoreCase(vehicleType)) {
                        etEventBookingVehicleType.setText(cbElementClassName.getText().toString());
                        etEventBookingVehicleType.setTag(cbElementClassName.getTag());
                    } else if (check.equalsIgnoreCase(duration)) {
                        etEventBookingEventDuration.setText(cbElementClassName.getText().toString());
                        etEventBookingEventDuration.setTag(cbElementClassName.getTag());
                    } else {
                        etEventBookingPassengers.setText(cbElementClassName.getText().toString());
                        etEventBookingPassengers.setTag(cbElementClassName.getTag());
                    }
                    alert.dismiss();
                }
            });
            return vi;
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
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void eventBooking() {
        CommonUtil.cancelProgressDialog();
        final String tag = "Event Booking";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();
        String url = "";

        try {
            if (rbEventBookingReturn.isChecked())
                jsonData.put("eventduration", etEventBookingEventDuration.getText().toString());

            builder.append(jsonData.put("noofpeople", etEventBookingPassengers.getTag())
                    .put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("otherrequirement", etEventBookingReq.getText().toString())
                    .put("coaches", etEventBookingVehicleType.getText().toString())
                    .put("luggage", etEventBookingEventLuggage.getTag())
                    .put("firstname", etEventBookingFirstName.getText().toString())
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("email", etEventBookingEmailAddress.getText().toString())
                    .put("eventdate", etEventBookingDateTime.getTag())
                    .put("phoneno", etEventBookingContactNumber.getText().toString())
                    .put("pickuparea", etEventBookingPickUp.getText().toString())
                    .put("droparea", etEventBookingEventDropOff.getText().toString())
                    .put("type", rbEventBookingOneWay.isChecked() ? "0" : "1")
                    .put("lastname", etEventBookingLastName.getText().toString()));

            try {
                url = Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.EVENT_BOOKING + "&json="
                        + URLEncoder.encode(builder.toString(), "UTF8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        CommonUtil.showProgressDialog(this, "getting data...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }
}
