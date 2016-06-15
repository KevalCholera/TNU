package com.smartsense.taxinearyou.Fragments;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.concurrent.TimeUnit;

public class FragmentBook extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {

    TextView tvBookDateTime, tvBookLuggage, tvBookPassenger, tvBookFrom, tvBookTo, tvBookvia1, tvBookvia2, tvBookSearchCars;
    RadioButton rbBookNow, rbBookToday, rbBookTomorrow;
    ImageView imgBookFromToReverse, ivBookVia, ivBookDeleteVia1, ivBookDeleteVia2;
    LinearLayout llBookVia1, llBookVia2;
    RadioGroup llBookNowTodayTomorrow;
    CoordinatorLayout clSearch;
    TimePickerDialog mTimePicker;
    private AlertDialog alert;
    String dateTimeCanChange;
    int whichClick = Constants.FragmentBook.NOW_CLICK;
    private Handler handler;
    Calendar calendar = Calendar.getInstance();
    Calendar calendar1 = Calendar.getInstance();
    Calendar calendarToday;
    public static long timeRemaining = 0;
    Boolean isUserTimeSelect = false;
    boolean session = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_book, container, false);
        handler = new Handler();

        tvBookFrom = (TextView) rootView.findViewById(R.id.tvBookFrom);
        tvBookTo = (TextView) rootView.findViewById(R.id.tvBookTo);
        tvBookvia1 = (TextView) rootView.findViewById(R.id.tvBookvia1);
        tvBookvia2 = (TextView) rootView.findViewById(R.id.tvBookvia2);
        tvBookDateTime = (TextView) rootView.findViewById(R.id.tvBookDateTime);
        tvBookLuggage = (TextView) rootView.findViewById(R.id.tvBookLuggage);
        tvBookPassenger = (TextView) rootView.findViewById(R.id.tvBookPassenger);
        tvBookSearchCars = (TextView) rootView.findViewById(R.id.tvBookSearchCars);
        rbBookNow = (RadioButton) rootView.findViewById(R.id.rbBookNow);
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

        rbBookNow.setChecked(true);
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
        imgBookFromToReverse.setOnClickListener(null);
        llBookVia1.setOnClickListener(this);
        llBookVia2.setOnClickListener(this);
        ivBookDeleteVia1.setOnClickListener(this);
        ivBookDeleteVia2.setOnClickListener(this);

        setDefaultValues();
        getServerDateTime();
        return rootView;
    }

    public void timePicker() {
        int hour = 0;
        int minute = 0;
        try {
            hour = Integer.valueOf(Constants.DATE_FORMAT_BIG_TIME_HOUR.format(Constants.DATE_FORMAT_SET.parse(tvBookDateTime.getText().toString())));
            minute = Integer.valueOf(Constants.DATE_FORMAT_TIME_MIN.format(Constants.DATE_FORMAT_SET.parse(tvBookDateTime.getText().toString())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                try {
                    String time = Constants.DATE_FORMAT_ONLY_TIME.format(Constants.DATE_FORMAT_BIG_TIME_HOUR_MIN.parse(selectedHour + ":" + selectedMinute));
                    if (rbBookToday.isChecked()) {
                        Date dateServerTime, dateGetTime;
                        dateServerTime = Constants.DATE_FORMAT_ONLY_TIME.parse(Constants.DATE_FORMAT_ONLY_TIME.format(Constants.DATE_FORMAT_SET.parse(dateTimeCanChange)));
                        dateGetTime = Constants.DATE_FORMAT_ONLY_TIME.parse(time);
                        long milliServerTime = dateServerTime.getTime() + 1800000;
                        long milliGetTime = dateGetTime.getTime();
                        if (milliGetTime >= milliServerTime) {
                            tvBookDateTime.setText(Constants.DATE_FORMAT_ONLY_DATE.format(Constants.DATE_FORMAT_SET.parse(dateTimeCanChange)) + " " + time);
                            calendarToday = Calendar.getInstance();
                            calendarToday.setTime(Constants.DATE_FORMAT_SET.parse(dateTimeCanChange + " " + time));
                        } else
                            CommonUtil.byToastMessage(getActivity(), getResources().getString(R.string.time_limit));

                    } else {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(Constants.DATE_FORMAT_SET.parse(dateTimeCanChange));
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        tvBookDateTime.setText(Constants.DATE_FORMAT_ONLY_DATE.format(calendar.getTime()) + " " + time);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    isUserTimeSelect = true;
                }
            }
        }, hour, minute, false);

        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
        mTimePicker.setCancelable(false);
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
                whichClick = Constants.FragmentBook.NOW_CLICK;
                isUserTimeSelect = false;
                setTextOnView();
                break;

            case R.id.rbBookToday:
                todayTimeLimitCondition();
                break;

            case R.id.rbBookTomorrow:
                whichClick = Constants.FragmentBook.TOMORROW_CLICK;
                isUserTimeSelect = false;
                setTextOnView();
                break;

            case R.id.tvBookDateTime:
                if (!rbBookNow.isChecked())
                    timePicker();
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
                SharedPreferenceUtil.save();
                break;

            case R.id.ivBookDeleteVia2:
                llBookVia2.setVisibility(View.GONE);
                tvBookvia2.setText("");
                SharedPreferenceUtil.putValue(Constants.VIA2_ADDRESS, "");
                SharedPreferenceUtil.save();
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
                    SharedPreferenceUtil.save();
                    SharedPreferenceUtil.putValue(Constants.FROM_ADDRESS, SharedPreferenceUtil.getString(Constants.TO_ADDRESS, ""));
                    SharedPreferenceUtil.putValue(Constants.TO_ADDRESS, SharedPreferenceUtil.getString(Constants.REVERSE_FROM_TO, ""));
                    SharedPreferenceUtil.save();
                }
                break;
        }
    }

    public void todayTimeLimitCondition() {
        calendar.setTime(calendar1.getTime());

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(calendar1.getTime());
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 30);

        long countingTime = calendar1.getTime().getTime();
        long limitTime = calendar2.getTime().getTime();

        if (countingTime < limitTime) {
            isUserTimeSelect = false;
            setTextOnView();
            session = true;
        } else {
            if (whichClick == Constants.FragmentBook.NOW_CLICK)
                rbBookNow.performClick();
            else
                rbBookTomorrow.performClick();
            session = false;
            CommonUtil.byToastMessage(getActivity(), getResources().getString(R.string.timeLimitError));
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
        else if (TextUtils.isEmpty(tvBookDateTime.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.sel_date), clSearch);
        else if (TextUtils.isEmpty(tvBookLuggage.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.sel_lug), clSearch);
        else if (TextUtils.isEmpty(tvBookPassenger.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.sel_passe), clSearch);
        else
            try {
                startActivity(new Intent(getActivity(), SearchCars.class)
                        .putExtra("tvBookDateTime", Constants.DATE_FORMAT_SEND.format(Constants.DATE_FORMAT_SET.parse(tvBookDateTime.getText().toString())))
                        .putExtra("tvBookLuggage", (String) tvBookLuggage.getTag())
                        .putExtra("luggageDescription", tvBookLuggage.getText().toString())
                        .putExtra("passengerDescription", tvBookPassenger.getText().toString())
                        .putExtra("duration", rbBookNow.isChecked() ? Constants.FragmentBook.NOW_CLICK : rbBookToday.isChecked() ? Constants.FragmentBook.TODAY_CLICK : Constants.FragmentBook.TOMORROW_CLICK)
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

        SharedPreferenceUtil.putValue(Constants.VIA_ADDRESS, "");
        SharedPreferenceUtil.putValue(Constants.VIA2_ADDRESS, "");
        SharedPreferenceUtil.save();

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
            String str;
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.time_get_session_out));
        builder.setPositiveButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getServerDateTime();
            }
        });
        builder.create();
        builder.show();

    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        CommonUtil.cancelProgressDialog();
        if (jsonObject != null) {
            if (jsonObject.optInt("status") == Constants.STATUS_SUCCESS) {
                try {
                    SharedPreferenceUtil.putValue(Constants.SESSION_LIMIT, jsonObject.optJSONObject("json").optInt("sessionLimit"));
                    SharedPreferenceUtil.putValue(Constants.PAGE_LIMIT, jsonObject.optJSONObject("json").optInt("pageLimit"));
                    SharedPreferenceUtil.save();
                    timeRemaining = TimeUnit.MINUTES.toMillis(SharedPreferenceUtil.getInt(Constants.SESSION_LIMIT, 9));
                    dateTimeCanChange = Constants.DATE_FORMAT_SET.format(Constants.DATE_FORMAT_GET.parse(jsonObject.optJSONObject("json").optString("serverTime")));
//                    dateTimeCanChange = Constants.DATE_FORMAT_SET.format(Constants.DATE_FORMAT_GET.parse("15 06 2016 23 29 50"));

                    if (rbBookToday.isChecked()) {
                        calendar.setTime(Constants.DATE_FORMAT_SET.parse(dateTimeCanChange));
                        calendar.add(Calendar.MINUTE, 30);
                        tvBookDateTime.setText(Constants.DATE_FORMAT_SET.format(calendar.getTime()));
                    } else
                        tvBookDateTime.setText(dateTimeCanChange);

                    calendar.setTime(Constants.DATE_FORMAT_SET.parse(dateTimeCanChange));
                    calendar1.setTime(Constants.DATE_FORMAT_SET.parse(dateTimeCanChange));
                    updateClock();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else
                CommonUtil.conditionAuthentication(getActivity(), jsonObject);
        } else
            CommonUtil.jsonNullError(getActivity());
    }

    public void updateClock() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                calendar.add(Calendar.MINUTE, 1);
                calendar1.add(Calendar.MINUTE, 1);
                dateTimeCanChange = Constants.DATE_FORMAT_SET.format(calendar.getTime());

                if (session) {
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(calendar1.getTime());
                    calendar2.set(Calendar.HOUR_OF_DAY, 23);
                    calendar2.set(Calendar.MINUTE, 30);

                    long countingTime = calendar1.getTime().getTime();
                    long limitTime = calendar2.getTime().getTime();
                    if (countingTime < limitTime) {
                        session = true;
                        setTextOnView();
                        handler.postDelayed(this, 60 * 1000);
                    } else {
                        handler.postDelayed(this, 60 * 1000);
                        alertBox();
                        session = false;
                    }
                } else {
                    session = false;
                    setTextOnView();
                    handler.postDelayed(this, 60 * 1000);
                }
            }
        }, 60 * 1000);
    }

    public void alertBox() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.timeLimitError));
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (whichClick == Constants.FragmentBook.NOW_CLICK)
                    rbBookNow.performClick();
                else
                    rbBookTomorrow.performClick();
            }
        });
        builder.create();
        builder.show();
    }

    public void setTextOnView() {
        calendar.setTime(calendar1.getTime());

        if (rbBookToday.isChecked() && calendarToday != null) {
            long milliServerTime = calendar1.getTime().getTime();
            long milliGetTime = calendarToday.getTime().getTime();
            if (milliGetTime >= milliServerTime) {
                isUserTimeSelect = false;
                calendarToday = null;
            }
        }
        if (rbBookTomorrow.isChecked()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (!isUserTimeSelect) {
            if (rbBookToday.isChecked())
                calendar.add(Calendar.MINUTE, 30);
            tvBookDateTime.setText(Constants.DATE_FORMAT_SET.format(calendar.getTime()));// + " " + Constants.DATE_FORMAT_ONLY_TIME.format(Constants.DATE_FORMAT_GET.parse(jsonObject.optJSONObject("json").optString("serverTime")))
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        timeRemaining = TimeUnit.MINUTES.toMillis(SharedPreferenceUtil.getInt(Constants.SESSION_LIMIT, 9));
    }
}
