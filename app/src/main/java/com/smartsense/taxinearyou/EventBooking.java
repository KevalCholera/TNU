package com.smartsense.taxinearyou;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cn.qqtheme.framework.picker.OptionPicker;


public class EventBooking extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {

    CoordinatorLayout clEventBooking;
    EditText etEventBookingFirstName, etEventBookingLastName, etEventBookingEmailAddress, etEventBookingDateTime,
            etEventBookingEventDuration, etEventBookingContactNumber, etEventBookingVehicleType, etEventBookingPickUp,
            etEventBookingEventDropOff, etEventBookingPassengers, etEventBookingEventLuggage, etEventBookingReq;
    Button btEventBookingBookNow;
    RadioButton rbEventBookingOneWay, rbEventBookingReturn;
    private AlertDialog alert;
    String luggage = "luggage";
    String passenger = "passenger";
    String vehicleType = "vehicleType";
    String duration = "duration";
    boolean check;
    String jsonDate, jsonTimeHour, jsonTimeMin;

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
        rbEventBookingReturn.setOnClickListener(this);
        rbEventBookingOneWay.setOnClickListener(this);
        etEventBookingEventDuration.setOnClickListener(null);
        check = true;
        setValue(true);
        getServerDateTime();
    }

    public void setValue(boolean check) {
        try {
            JSONArray jsonLuggage = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_EVENT_lUGGAGE_LIST, ""));
            JSONArray jsonDuration = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_EVENT_DURATION, ""));
            JSONArray jsonPassenger = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_EVENT_PASSENGER_LIST, ""));
            JSONArray jsonVehicleType = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_EVENT_VEHICLE_TYPE, ""));

            if (check) {
                etEventBookingEventLuggage.setText(jsonLuggage.optJSONObject(0).optString("name"));
                etEventBookingEventLuggage.setTag(jsonLuggage.optJSONObject(0).optString("id"));

                etEventBookingEventDuration.setText(jsonDuration.optJSONObject(0).optString("name"));
                etEventBookingEventDuration.setTag(jsonLuggage.optJSONObject(0).optString("id"));

                etEventBookingPassengers.setText(jsonPassenger.optJSONObject(0).optString("name"));
                etEventBookingPassengers.setTag(jsonLuggage.optJSONObject(0).optString("id"));

                etEventBookingVehicleType.setText(jsonVehicleType.optJSONObject(0).optString("name"));
                etEventBookingVehicleType.setTag(jsonLuggage.optJSONObject(0).optString("id"));
            } else {
                etEventBookingEventDuration.setText(jsonDuration.optJSONObject(0).optString("name"));
                etEventBookingEventDuration.setTag(jsonLuggage.optJSONObject(0).optString("id"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getServerDateTime() {
        final String tag = "Get Server Date Time";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.GET_SERVER_DATE_TIME), tag, this, this);
    }

    Calendar mCalendar = Calendar.getInstance();
    Boolean once = false;

    public void datePicker(String dateTime) {
        DatePickerDialog DatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker DatePicker, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (once) {
                    Time(Constants.DATE_FORMAT_ONLY_DATE.format(mCalendar.getTime()));
                    once = false;
                }
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

        try {
            long newDate = Constants.DATE_FORMAT_ONLY_DATE.parse(dateTime.substring(0, dateTime.indexOf(' '))).getTime();
            DatePicker.getDatePicker().setMinDate(newDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(newDate));
            calendar.add(Calendar.YEAR, 1);
            DatePicker.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        DatePicker.show();
        DatePicker.setCancelable(false);
    }

    public void Time(final String date) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                updateTime(selectedHour, selectedMinute, date);
            }
        }, hour, minute, false);

        mTimePicker.setTitle("Select Time");
        mTimePicker.updateTime(Integer.valueOf(jsonTimeHour), Integer.valueOf(jsonTimeMin));

        mTimePicker.show();
        mTimePicker.setCancelable(false);
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

        String hrs = null, minute = null, timeSlice = null;
        String daTime = etEventBookingDateTime.getText().toString();

        try {
            long longDate = Constants.DATE_FORMAT_ONLY_DATE.parse(date).getTime();
            long longJsonDate = Constants.DATE_FORMAT_ONLY_DATE.parse(jsonDate).getTime();

            if (longDate > longJsonDate) {
                hrs = hour;
                minute = minutes;
                timeSlice = timeSet;
            } else {
                try {
                    hrs = Constants.DATE_FORMAT_SMALL_TIME_HOUR.format(Constants.DATE_FORMAT_SET.parse(daTime));
                    minute = Constants.DATE_FORMAT_TIME_MIN.format(Constants.DATE_FORMAT_SET.parse(daTime));
                    timeSlice = Constants.DATE_FORMAT_TIME_AM_PM.format(Constants.DATE_FORMAT_SET.parse(daTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (longDate == longJsonDate) {
                Date dateSet, dateGet;
                try {
                    dateSet = Constants.DATE_FORMAT_SET.parse(daTime);
                    dateGet = Constants.DATE_FORMAT_SET.parse((daTime.substring(0, daTime.indexOf(' '))) + " " + hour + ':' + minutes + " " + timeSet);
                    long millisecondSet = dateSet.getTime();
                    long millisecondGet = dateGet.getTime();
                    if (millisecondGet < millisecondSet) {
                        CommonUtil.byToastMessage(this, getResources().getString(R.string.invalid_time));
                    } else {
                        hrs = hour;
                        minute = minutes;
                        timeSlice = timeSet;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String timePickerTime = hrs + ':' + minute + " " + timeSlice;

            jsonTimeHour = Constants.DATE_FORMAT_BIG_TIME_HOUR.format(Constants.DATE_FORMAT_ONLY_TIME.parse(timePickerTime));
            jsonTimeMin = Constants.DATE_FORMAT_TIME_MIN.format(Constants.DATE_FORMAT_ONLY_TIME.parse(timePickerTime));

            etEventBookingDateTime.setText(date + " " + timePickerTime);
            etEventBookingDateTime.setTag(date + " " + timePickerTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    Calendar calendar = Calendar.getInstance();
    long oneYear = 1L * 365 * 1000 * 60 * 60 * 24L;

    public void selectPicker(long minTime) {
        com.jzxiang.pickerview.TimePickerDialog mDialogAll = new com.jzxiang.pickerview.TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(com.jzxiang.pickerview.TimePickerDialog timePickerView, long millseconds) {

                        calendar.setTimeInMillis(millseconds);
                        updateTime(Integer.valueOf(Constants.DATE_FORMAT_BIG_TIME_HOUR.format(calendar.getTime())), Integer.valueOf(Constants.DATE_FORMAT_TIME_MIN.format(calendar.getTime())), Constants.DATE_FORMAT_ONLY_DATE.format(calendar.getTime()));
                    }
                })
                .setCancelStringId("CANCEL")
                .setSureStringId("OK")
                .setTitleStringId("Select Date and Time")
                .setYearText("")
                .setMonthText("")
                .setDayText("")
                .setHourText("")
                .setMinuteText("")
                .setCyclic(true)
                .setCurrentMillseconds(minTime)
                .setThemeColor(ActivityCompat.getColor(EventBooking.this, R.color.colorAccent))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(ActivityCompat.getColor(EventBooking.this, R.color.colorAccent))
                .setWheelItemTextSize(12)
                .setMinMillseconds(minTime)
                .setMaxMillseconds(minTime + oneYear)
                .build();
        mDialogAll.show(getSupportFragmentManager(), "all");

    }

    @Override
    public void onClick(View v) {
        CommonUtil.closeKeyboard(EventBooking.this);
        switch (v.getId()) {
            case R.id.btEventBookingBookNow:

                if (TextUtils.isEmpty(etEventBookingFirstName.getText().toString()) && TextUtils.isEmpty(etEventBookingLastName.getText().toString()) && TextUtils.isEmpty(etEventBookingEmailAddress.getText().toString()) && TextUtils.isEmpty(etEventBookingContactNumber.getText().toString()) && TextUtils.isEmpty(etEventBookingPickUp.getText().toString()) && TextUtils.isEmpty(etEventBookingEventDropOff.getText().toString()) && TextUtils.isEmpty(etEventBookingReq.getText().toString()))
                    CommonUtil.showSnackBar(getResources().getString(R.string.enter_fields_below), clEventBooking);
                else if (TextUtils.isEmpty(etEventBookingFirstName.getText().toString()))
                    CommonUtil.showSnackBar(getResources().getString(R.string.enter_first_name), clEventBooking);
                else if (etEventBookingFirstName.getText().toString().length() < 2 || etEventBookingFirstName.getText().toString().length() > 20)
                    CommonUtil.showSnackBar(getString(R.string.enter_first_name_validation), clEventBooking);
                else if (TextUtils.isEmpty(etEventBookingLastName.getText().toString()))
                    CommonUtil.showSnackBar(getResources().getString(R.string.enter_last_name), clEventBooking);
                else if (etEventBookingLastName.getText().toString().length() < 2 || etEventBookingLastName.getText().toString().length() > 20)
                    CommonUtil.showSnackBar(getString(R.string.enter_last_name_validation), clEventBooking);
                else if (TextUtils.isEmpty(etEventBookingEmailAddress.getText().toString()) || !CommonUtil.isValidEmail(etEventBookingEmailAddress.getText().toString()))
                    CommonUtil.showSnackBar(getResources().getString(R.string.enter_email_id), clEventBooking);
                else if (etEventBookingContactNumber.length() != 10)
                    CommonUtil.showSnackBar(getResources().getString(R.string.enter_con), clEventBooking);
                else if (TextUtils.isEmpty(etEventBookingPickUp.getText().toString()))
                    CommonUtil.showSnackBar(getResources().getString(R.string.event_bookin_pick), clEventBooking);
                else if (TextUtils.isEmpty(etEventBookingEventDropOff.getText().toString()))
                    CommonUtil.showSnackBar(getResources().getString(R.string.event_bookin_drop), clEventBooking);
                else
                    eventBooking();
                break;

            case R.id.etEventBookingDateTime:
                once = true;
                getServerDateTime();
                break;

            case R.id.rbEventBookingOneWay:
                etEventBookingEventDuration.setTextColor(ContextCompat.getColor(this, R.color.hintColor));
                etEventBookingEventDuration.setOnClickListener(null);
                setValue(false);
                break;

            case R.id.rbEventBookingReturn:
                etEventBookingEventDuration.setTextColor(ContextCompat.getColor(this, R.color.black));
                etEventBookingEventDuration.setOnClickListener(this);
                break;

            case R.id.etEventBookingEventDuration:
                selectPassenger(duration);
                break;

            case R.id.etEventBookingPassengers:
                selectPassenger(passenger);
                break;

            case R.id.etEventBookingEventLuggage:
                selectPassenger(luggage);
                break;

            case R.id.etEventBookingVehicleType:
                selectPassenger(vehicleType);
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
                tvCityDialogHead.setText("Select People/Peoples");
            }
            JSONArray jsonArray = new JSONArray(str);
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);
            AdapterSelectOption adapterSelectOption = new AdapterSelectOption(this, jsonArray, check);
            list_view.setAdapter(adapterSelectOption);
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(true);
            alert = alertDialogs.create();
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    HashMap<String, String> spinnerMap = new HashMap<>();

    public void selectPassenger(final String check) {
        try {
            JSONArray newJsonArr;
            if (check.equalsIgnoreCase(luggage)) {
                newJsonArr = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_EVENT_lUGGAGE_LIST, ""));
            } else if (check.equalsIgnoreCase(vehicleType)) {
                newJsonArr = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_EVENT_VEHICLE_TYPE, ""));
            } else if (check.equalsIgnoreCase(duration)) {
                newJsonArr = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_EVENT_DURATION, ""));
            } else {
                newJsonArr = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_EVENT_PASSENGER_LIST, ""));
            }
            String[] stringArr = new String[newJsonArr.length()];
            for (int i = 0; i < newJsonArr.length(); i++) {
                stringArr[i] = newJsonArr.optJSONObject(i).optString("name");
                spinnerMap.put(newJsonArr.optJSONObject(i).optString("name"), newJsonArr.optJSONObject(i).optString("id"));
            }
            OptionPicker picker = new OptionPicker(this, stringArr);
//            picker.setOffset(2);
//            picker.setSelectedIndex(1);
            picker.setTextSize(15);

            if (check.equalsIgnoreCase(luggage)) {
                picker.setTitleText("Select Luggages");
            } else if (check.equalsIgnoreCase(vehicleType)) {
                picker.setTitleText("Select Vehicle Type");
            } else if (check.equalsIgnoreCase(duration)) {
                picker.setTitleText("Select Duration");
            } else {
                picker.setTitleText("Select People/Peoples");
            }
            picker.setTitleTextColor(ActivityCompat.getColor(this, R.color.white));
            picker.setTopBackgroundColor(ActivityCompat.getColor(this, R.color.colorAccent));
            picker.setTopLineColor(ActivityCompat.getColor(this, R.color.colorAccent));
            picker.setCancelTextColor(ActivityCompat.getColor(this, R.color.white));
            picker.setSubmitTextColor(ActivityCompat.getColor(this, R.color.white));
            picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                @Override
                public void onOptionPicked(String option) {
                    final String id1 = spinnerMap.get(option);
                    if (check.equalsIgnoreCase(luggage)) {
                        etEventBookingEventLuggage.setText(option);
                        etEventBookingEventLuggage.setTag(id1);
                    } else if (check.equalsIgnoreCase(vehicleType)) {
                        etEventBookingVehicleType.setText(option);
                        etEventBookingVehicleType.setTag(id1);
                    } else if (check.equalsIgnoreCase(duration)) {
                        etEventBookingEventDuration.setText(option);
                        etEventBookingEventDuration.setTag(id1);
                    } else {
                        etEventBookingPassengers.setText(option);
                        etEventBookingPassengers.setTag(id1);
                    }
                }
            });
            picker.show();
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
                    if (!message.equalsIgnoreCase(etEventBookingEventDropOff.getText().toString())) {
                        etEventBookingPickUp.setText(message);
                        SharedPreferenceUtil.putValue(Constants.FROM_ADDRESS, data.getStringExtra("address"));
                    } else {
                        CommonUtil.showSnackBar(getString(R.string.same_filled_error), clEventBooking);
                    }
                    break;
                case 2:
                    if (!message.equalsIgnoreCase(etEventBookingPickUp.getText().toString())) {
                        etEventBookingEventDropOff.setText(message);
                        SharedPreferenceUtil.putValue(Constants.TO_ADDRESS, data.getStringExtra("address"));
                    } else {
                        CommonUtil.showSnackBar(getString(R.string.same_filled_error), clEventBooking);
                    }
                    break;
            }
            SharedPreferenceUtil.save();
        }
    }

    public class AdapterSelectOption extends BaseAdapter {
        private final String check;
        private JSONArray data;
        private LayoutInflater inflater = null;
        Activity a;

        public AdapterSelectOption(Activity a, JSONArray data, String check) {
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
            final View vLine = (View) vi.findViewById(R.id.vElementQuestionName);
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
//            if(position==data.length())
//                vLine.setPadding(16,0,16,5);
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

        try {
            if (rbEventBookingReturn.isChecked())
                jsonData.put("eventduration", etEventBookingEventDuration.getText().toString());

            builder.append(jsonData.put("noofpeople", ((String) etEventBookingPassengers.getTag()).trim())
                    .put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("otherrequirement", etEventBookingReq.getText().toString().trim())
                    .put("coaches", etEventBookingVehicleType.getText().toString().trim())
                    .put("luggage", ((String) etEventBookingEventLuggage.getTag()).trim())
                    .put("firstname", etEventBookingFirstName.getText().toString().trim())
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("email", etEventBookingEmailAddress.getText().toString().trim())
                    .put("eventdate", Constants.DATE_FORMAT_SEND.format(Constants.DATE_FORMAT_SET.parse(((String) etEventBookingDateTime.getTag()).trim())))
                    .put("phoneno", etEventBookingContactNumber.getText().toString().trim())
                    .put("pickuparea", etEventBookingPickUp.getText().toString().trim())
                    .put("droparea", etEventBookingEventDropOff.getText().toString().trim())
                    .put("type", rbEventBookingOneWay.isChecked() ? "1" : "2")
                    .put("lastname", etEventBookingLastName.getText().toString().trim()));

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.EVENT_BOOKING), tag, this, this);
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
            if (jsonObject.optInt("status") == Constants.STATUS_SUCCESS) {
                if (jsonObject.optString("__eventid").equalsIgnoreCase((Constants.Events.EVENT_BOOKING) + "")) {
                    CommonUtil.alertBox(this, getResources().getString(R.string.event_complete));

                } else if (jsonObject.optString("__eventid").equalsIgnoreCase((Constants.Events.GET_SERVER_DATE_TIME) + "")) {

                    try {
                        String dateTime = Constants.DATE_FORMAT_SET.format(Constants.DATE_FORMAT_GET.parse(jsonObject.optJSONObject("json").optString("serverTime")));

                        jsonDate = Constants.DATE_FORMAT_ONLY_DATE.format(Constants.DATE_FORMAT_SET.parse(dateTime));
                        if (check) {
                            jsonTimeHour = Constants.DATE_FORMAT_BIG_TIME_HOUR.format(Constants.DATE_FORMAT_SET.parse(dateTime));
                            jsonTimeMin = Constants.DATE_FORMAT_TIME_MIN.format(Constants.DATE_FORMAT_SET.parse(dateTime));
                            etEventBookingDateTime.setText(dateTime);
                            etEventBookingDateTime.setTag(dateTime);
                            check = false;
                        }
                        if (once)
                            selectPicker(Constants.DATE_FORMAT_SET.parse(dateTime).getTime());
//                            datePicker(dateTime);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } else
                CommonUtil.conditionAuthentication(this, jsonObject);
        } else CommonUtil.jsonNullError(this);
    }

    @Override
    public void onBackPressed() {
        CommonUtil.closeKeyboard(this);
        super.onBackPressed();
    }

}
