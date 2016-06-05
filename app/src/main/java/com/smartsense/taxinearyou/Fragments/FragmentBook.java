package com.smartsense.taxinearyou.Fragments;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.GooglePlaces;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.SearchCars;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class FragmentBook extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {

    TextView tvBookDateTime, tvBookLuggage, tvBookPassenger, tvBookFrom, tvBookTo, tvBookvia1, tvBookvia2, tvBookSearchCars;
    RadioButton rbBookNow, rbBookToday, rbBookTomorrow;
    ImageView imgBookFromToReverse, ivBookVia, ivBookDeleteVia1, ivBookDeleteVia2;
    LinearLayout llBookVia1, llBookVia2;
    RadioGroup llBookNowTodayTomorrow;
    CoordinatorLayout clSearch;
    TimePickerDialog mTimePicker;
    private AlertDialog alert;
    Boolean check = false;
    Boolean timeChange;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book, container, false);

        tvBookFrom = (TextView) rootView.findViewById(R.id.tvBookFrom);
        tvBookTo = (TextView) rootView.findViewById(R.id.tvBookTo);
        tvBookvia1 = (TextView) rootView.findViewById(R.id.tvBookvia1);
        tvBookvia2 = (TextView) rootView.findViewById(R.id.tvBookvia2);
        tvBookDateTime = (TextView) rootView.findViewById(R.id.tvBookDateTime);
        tvBookLuggage = (TextView) rootView.findViewById(R.id.tvBookLuggage);
        tvBookPassenger = (TextView) rootView.findViewById(R.id.tvBookPassenger);
        tvBookSearchCars = (TextView) rootView.findViewById(R.id.tvBookSearchCars);
        rbBookNow = (RadioButton) rootView.findViewById(R.id.rbBookNow);
        rbBookNow.setChecked(true);
        rbBookToday = (RadioButton) rootView.findViewById(R.id.rbBookToday);
        rbBookTomorrow = (RadioButton) rootView.findViewById(R.id.rbBookTomorrow);
        imgBookFromToReverse = (ImageView) rootView.findViewById(R.id.imgBookFromToReverse);
        ivBookVia = (ImageView) rootView.findViewById(R.id.ivBookVia);
        llBookVia1 = (LinearLayout) rootView.findViewById(R.id.llBookVia1);
        llBookVia2 = (LinearLayout) rootView.findViewById(R.id.llBookVia2);
        ivBookDeleteVia1 = (ImageView) rootView.findViewById(R.id.ivBookDeleteVia1);
        ivBookDeleteVia2 = (ImageView) rootView.findViewById(R.id.ivBookDeleteVia2);
        clSearch = (CoordinatorLayout) getActivity().findViewById(R.id.clSearch);
        llBookNowTodayTomorrow = (RadioGroup) rootView.findViewById(R.id.llBookNowTodayTomorrow);

        tvBookFrom.setOnClickListener(this);
        tvBookTo.setOnClickListener(this);
        tvBookvia1.setOnClickListener(this);
        tvBookvia2.setOnClickListener(this);
        tvBookLuggage.setOnClickListener(this);
        tvBookPassenger.setOnClickListener(this);
        rbBookNow.setOnClickListener(this);
        rbBookToday.setOnClickListener(this);
        rbBookTomorrow.setOnClickListener(this);
        tvBookSearchCars.setOnClickListener(this);
        tvBookDateTime.setOnClickListener(this);
        ivBookVia.setOnClickListener(this);
        imgBookFromToReverse.setOnClickListener(this);
        llBookVia1.setOnClickListener(this);
        llBookVia2.setOnClickListener(this);
        ivBookDeleteVia1.setOnClickListener(this);
        ivBookDeleteVia2.setOnClickListener(this);

        timeChange = true;
        setDefaultValues();
        getServerDateTime();

        return rootView;
    }

    private String updateTime(int hours, int mins) {

        String timeSet;
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes;
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        String hour = "";
        if (hours < 10)
            hour = "0" + hours;
        else
            hour = String.valueOf(hours);

        String hrs = null, minute = null, timeSlice = null;
        String daTime = tvBookDateTime.getText().toString();

        if (rbBookTomorrow.isChecked()) {
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

        if (rbBookToday.isChecked()) {
            Date dateSet, dateGet;
            try {
                dateSet = Constants.DATE_FORMAT_SET.parse(daTime);
                dateGet = Constants.DATE_FORMAT_SET.parse((daTime.substring(0, daTime.indexOf(' '))) + " " + hour + ':' + minutes + " " + timeSet);
                long millisecondSet = dateSet.getTime();
                long millisecondGet = dateGet.getTime();
                if (millisecondGet < millisecondSet) {
                    Toast.makeText(getActivity(), "Invalid Time", Toast.LENGTH_SHORT).show();
                } else {
                    hrs = hour;
                    minute = minutes;
                    timeSlice = timeSet;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return hrs + ':' + minute + " " + timeSlice;
    }

    public void setDateTime(Boolean check, String dateTime) {
        final int id = llBookNowTodayTomorrow.getCheckedRadioButtonId();
        Date Now = new Date();
        Calendar calendar;
        final String finalDateNow1 = Constants.DATE_FORMAT_SET.format(Now);
        if (rbBookNow.getId() != id) {
            int hour = 0, minute = 0;
            try {
                hour = Integer.valueOf(Constants.DATE_FORMAT_BIG_TIME_HOUR.format(Constants.DATE_FORMAT_SET.parse(dateTime)));
                minute = Integer.valueOf(Constants.DATE_FORMAT_TIME_MIN.format(Constants.DATE_FORMAT_SET.parse(dateTime)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            final String finalDateNow2 = Constants.DATE_FORMAT_ONLY_DATE.format(Now);
            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = calendar.getTime();
            final String finalDateTomorrow = Constants.DATE_FORMAT_ONLY_DATE.format(tomorrow);
            if (rbBookToday.getId() == id) {
                tvBookDateTime.setText(finalDateNow2 + " " + updateTime(hour, minute));
            } else if (rbBookTomorrow.getId() == id) {
                tvBookDateTime.setText(finalDateTomorrow + " " + updateTime(hour, minute));
            }
            if (check) {
                timePicker(id, finalDateNow2, finalDateTomorrow, hour, minute);
            }
        } else {
            tvBookDateTime.setText(finalDateNow1);
        }
    }

    public void timePicker(final int id, final String finalDateNow2, final String finalDateTomorrow, int hour, int minute) {
        mTimePicker = new TimePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (rbBookToday.getId() == id) {
                    tvBookDateTime.setText(finalDateNow2 + " " + updateTime(selectedHour, selectedMinute));
                } else if (rbBookTomorrow.getId() == id) {
                    tvBookDateTime.setText(finalDateTomorrow + " " + updateTime(selectedHour, selectedMinute));
                }
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
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

        CommonUtil.jsonRequestGET(getActivity(), getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.GET_SERVER_DATE_TIME), tag, this, this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rbBookNow:
                check = false;
                timeChange = true;
                getServerDateTime();
                break;

            case R.id.rbBookToday:
                check = false;
                getServerDateTime();
                break;

            case R.id.rbBookTomorrow:
                getServerDateTime();
                check = false;
                break;

            case R.id.tvBookDateTime:
                if (rbBookNow.isChecked())
                    timeChange = true;
                getServerDateTime();
                check = true;
                break;

            case R.id.tvBookSearchCars:
                checkCondition();
                break;

            case R.id.ivBookVia:
                if (llBookVia1.getVisibility() == View.VISIBLE)
                    llBookVia2.setVisibility(View.VISIBLE);
                llBookVia1.setVisibility(View.VISIBLE);
                break;

            case R.id.ivBookDeleteVia1:
                llBookVia1.setVisibility(View.GONE);
                tvBookvia1.setText("");
                SharedPreferenceUtil.putValue(Constants.VIA_ADDRESS, "");
                break;

            case R.id.ivBookDeleteVia2:
                llBookVia2.setVisibility(View.GONE);
                tvBookvia2.setText("");
                SharedPreferenceUtil.putValue(Constants.VIA2_ADDRESS, "");
                break;

            case R.id.tvBookFrom:
                startActivityForResult(new Intent(getActivity(), GooglePlaces.class).putExtra("typeAddress", 1), 1);
                break;

            case R.id.tvBookTo:
                startActivityForResult(new Intent(getActivity(), GooglePlaces.class).putExtra("typeAddress", 2), 2);
                break;

            case R.id.tvBookvia1:
                startActivityForResult(new Intent(getActivity(), GooglePlaces.class).putExtra("typeAddress", 3), 3);
                break;

            case R.id.tvBookvia2:
                startActivityForResult(new Intent(getActivity(), GooglePlaces.class).putExtra("typeAddress", 4), 4);
                break;
            case R.id.tvBookPassenger:
                openQuestionSelectPopup(false);
                break;
            case R.id.tvBookLuggage:
                openQuestionSelectPopup(true);
                break;
            case R.id.imgBookFromToReverse:
                if (TextUtils.isEmpty(tvBookFrom.getText().toString()) || TextUtils.isEmpty(tvBookTo.getText().toString()))
                    CommonUtil.showSnackBar(getString(R.string.enter_fields_below), clSearch);
                else {
                    String reverseFrom = tvBookFrom.getText().toString();
                    tvBookFrom.setText(tvBookTo.getText().toString());
                    tvBookTo.setText(reverseFrom);
                    SharedPreferenceUtil.putValue(Constants.REVERSE_FROM_TO, SharedPreferenceUtil.getString(Constants.FROM_ADDRESS, ""));
                    SharedPreferenceUtil.putValue(Constants.FROM_ADDRESS, SharedPreferenceUtil.getString(Constants.TO_ADDRESS, ""));
                    SharedPreferenceUtil.putValue(Constants.TO_ADDRESS, SharedPreferenceUtil.getString(Constants.REVERSE_FROM_TO, ""));
                    SharedPreferenceUtil.save();
                }
                break;
        }
    }

    public void checkCondition() {
        if (TextUtils.isEmpty(tvBookFrom.getText().toString()) && TextUtils.isEmpty(tvBookTo.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.enter_fields_below), clSearch);
        else if (TextUtils.isEmpty(tvBookFrom.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.pls_enter_pick), clSearch);
        else if (TextUtils.isEmpty(tvBookTo.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.pls_enter_drop), clSearch);
        else if (llBookVia1.getVisibility() == View.VISIBLE && llBookVia2.getVisibility() == View.VISIBLE && TextUtils.isEmpty(tvBookvia1.getText().toString()) && TextUtils.isEmpty(tvBookvia2.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.pls_ent_via_fields), clSearch);
        else if (llBookVia1.getVisibility() == View.VISIBLE && llBookVia2.getVisibility() == View.VISIBLE && TextUtils.isEmpty(tvBookvia1.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.pls_ent_via1_loc), clSearch);
        else if (llBookVia1.getVisibility() == View.VISIBLE && llBookVia2.getVisibility() == View.VISIBLE && TextUtils.isEmpty(tvBookvia2.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.pls_ent_via2_loc), clSearch);
        else if (llBookVia1.getVisibility() == View.VISIBLE && TextUtils.isEmpty(tvBookvia1.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.pls_ent_via1_loc), clSearch);
        else if (llBookVia2.getVisibility() == View.VISIBLE && TextUtils.isEmpty(tvBookvia2.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.pls_ent_via2_loc), clSearch);
        else
            try {
                startActivity(new Intent(getActivity(), SearchCars.class)
                        .putExtra("tvBookDateTime", Constants.DATE_FORMAT_SEND.format(Constants.DATE_FORMAT_SET.parse(tvBookDateTime.getText().toString())))
                        .putExtra("tvBookLuggage", (String) tvBookLuggage.getTag())
                        .putExtra("luggageDescription", tvBookLuggage.getText().toString())
                        .putExtra("passengerDescription", tvBookPassenger.getText().toString())
                        .putExtra("duration", rbBookNow.isChecked() ? "1" : rbBookToday.isChecked() ? "2" : "3")
                        .putExtra("tvBookPassenger", (String) tvBookPassenger.getTag()));
            } catch (ParseException e) {
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
                    if (tvBookTo.getText().toString().equalsIgnoreCase(message) || tvBookvia1.getText().toString().equalsIgnoreCase(message) || tvBookvia2.getText().toString().equalsIgnoreCase(message))
                        CommonUtil.showSnackBar(getString(R.string.same_filled_error), clSearch);
                    else {
                        tvBookFrom.setText(message);
                        SharedPreferenceUtil.putValue(Constants.FROM_ADDRESS, data.getStringExtra("address"));
                    }
                    break;
                case 2:
                    if (tvBookFrom.getText().toString().equalsIgnoreCase(message) || tvBookvia1.getText().toString().equalsIgnoreCase(message) || tvBookvia2.getText().toString().equalsIgnoreCase(message))
                        CommonUtil.showSnackBar(getString(R.string.same_filled_error), clSearch);
                    else {
                        tvBookTo.setText(message);
                        SharedPreferenceUtil.putValue(Constants.TO_ADDRESS, data.getStringExtra("address"));
                    }
                    break;
                case 3:
                    if (tvBookTo.getText().toString().equalsIgnoreCase(message) || tvBookFrom.getText().toString().equalsIgnoreCase(message) || tvBookvia2.getText().toString().equalsIgnoreCase(message))
                        CommonUtil.showSnackBar(getString(R.string.same_filled_error), clSearch);
                    else {
                        tvBookvia1.setText(message);
                        SharedPreferenceUtil.putValue(Constants.VIA_ADDRESS, data.getStringExtra("address"));
                    }
                    break;
                case 4:
                    if (tvBookTo.getText().toString().equalsIgnoreCase(message) || tvBookvia1.getText().toString().equalsIgnoreCase(message) || tvBookFrom.getText().toString().equalsIgnoreCase(message))
                        CommonUtil.showSnackBar(getString(R.string.same_filled_error), clSearch);
                    else {
                        tvBookvia2.setText(message);
                        SharedPreferenceUtil.putValue(Constants.VIA2_ADDRESS, data.getStringExtra("address"));
                    }
                    break;
            }
            SharedPreferenceUtil.save();
        }
    }

    public void setDefaultValues() {
        String str;
        JSONArray jsonArray;
        try {

            str = SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_LUGGAGE, "");
            jsonArray = new JSONArray(str);
            if (jsonArray.length() > 0) {
                tvBookLuggage.setTag(jsonArray.optJSONObject(0).optString("luggageId"));
                tvBookLuggage.setText(jsonArray.optJSONObject(0).optString("name"));
                SharedPreferenceUtil.putValue(Constants.PrefKeys.LUGGAGE_VALUE, "2");
                SharedPreferenceUtil.save();
            }
            str = SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PASSENGER, "");
            jsonArray = new JSONArray(str);
            if (jsonArray.length() > 0) {
                if (jsonArray.optJSONObject(0).optString("name").equalsIgnoreCase("1"))
                    tvBookPassenger.setText(jsonArray.optJSONObject(0).optString("name") + " passenger");
                else
                    tvBookPassenger.setText(jsonArray.optJSONObject(0).optString("name") + " passengers");
                tvBookPassenger.setTag(jsonArray.optJSONObject(0).optString("passengerCountId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openQuestionSelectPopup(Boolean check) {
        try {

            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_select_question, null);

            TextView tvCityDialogHead = (TextView) dialog.findViewById(R.id.tvCityDialogHead);
            String str = "";
            if (check) {
                str = SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_LUGGAGE, "");
                tvCityDialogHead.setText("Select Luggage");
            } else {
                str = SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PASSENGER, "");
                tvCityDialogHead.setText("Select Passenger/Passengers");
            }
            JSONArray jsonArray = new JSONArray(str);
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);
            AdapterSelectionPassengerOrLuggage adapterSelectionPassengerOrLuggage = new AdapterSelectionPassengerOrLuggage(getActivity(), jsonArray, check);
            list_view.setAdapter(adapterSelectionPassengerOrLuggage);
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(true);
            alert = alertDialogs.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class AdapterSelectionPassengerOrLuggage extends BaseAdapter {
        private final Boolean check;
        private JSONArray data;
        private LayoutInflater inflater = null;
        Activity a;

        public AdapterSelectionPassengerOrLuggage(Activity a, JSONArray data, Boolean check) {
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
            final JSONObject object = data.optJSONObject(position);
            if (object.optInt("isEnable") == 1) {
                if (check) {
                    cbElementClassName.setTag(object.optString("luggageId"));
                    cbElementClassName.setText(object.optString("name"));
                } else {
                    if (object.optString("name").equalsIgnoreCase("1"))
                        cbElementClassName.setText(object.optString("name") + " passenger");
                    else
                        cbElementClassName.setText(object.optString("name") + " passengers");
                    cbElementClassName.setTag(object.optString("passengerCountId"));
                }
                cbElementClassName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (check) {
                            tvBookLuggage.setText(cbElementClassName.getText().toString());
                            tvBookLuggage.setTag(cbElementClassName.getTag());
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.LUGGAGE_VALUE, object.optInt("value") + "");
                            SharedPreferenceUtil.save();
                        } else {
                            tvBookPassenger.setText(cbElementClassName.getText().toString());
                            tvBookPassenger.setTag(cbElementClassName.getTag());
                        }
                        alert.dismiss();
                    }
                });
            }
            return vi;
        }
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.cancelProgressDialog();
        CommonUtil.errorToastShowing(getActivity());
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        CommonUtil.cancelProgressDialog();
        if (jsonObject != null) {
            if (jsonObject.optInt("status") == Constants.STATUS_SUCCESS) {
                try {
                    String dateTime = Constants.DATE_FORMAT_SET.format(Constants.DATE_FORMAT_GET.parse(jsonObject.optJSONObject("json").optString("serverTime")));
                    if (timeChange) {
                        tvBookDateTime.setText(dateTime);
                        tvBookDateTime.setTag(dateTime);
                        timeChange = false;
                    }
                    if (!rbBookNow.isChecked())
                        setDateTime(check, dateTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else
                CommonUtil.conditionAuthentication(getActivity(), jsonObject);
        } else
            CommonUtil.jsonNullError(getActivity());
    }
}
