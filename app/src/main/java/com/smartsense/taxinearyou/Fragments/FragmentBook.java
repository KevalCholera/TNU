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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.GooglePlaces;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.TaxiNearYouApp;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.DataRequest;
import com.smartsense.taxinearyou.utill.JsonErrorShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        setDefaultValues();
        setDateTime(false);
        return rootView;
    }

    private String updateTime(int hours, int mins) {
        String timeSet = "";
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


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        String hour = "";
        if (hours < 10)
            hour = "0" + hours;
        else
            hour = String.valueOf(hours);

        // Append in a StringBuilder
        return new StringBuilder().append(hour).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
    }

    public void setDateTime(Boolean check) {
        final int id = llBookNowTodayTomorrow.getCheckedRadioButtonId();
//        final RadioButton rb1 = (RadioButton) getActivity().findViewById(id);
        SimpleDateFormat timeStampFormat1 = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat timeStampFormat2 = new SimpleDateFormat("dd-MMM-yyyy -- hh:mm aa");
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mmaa");
//        SimpleDateFormat timeStampFormat2 = new SimpleDateFormat("dd-MMM-yyyy");//hh == 12 hours && HH == 24 hours  aa == am/pm
//        SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date Now = new Date();
        Calendar calendar = Calendar.getInstance();
        final String finalDateNow = timeStampFormat2.format(Now);
        final String finalDateNow1 = timeStampFormat.format(Now);
        if (rbBookNow.getId() != id) {
            final int hour = calendar.get(Calendar.HOUR_OF_DAY);
            final int minute = calendar.get(Calendar.MINUTE);
            final String finalDateNow2 = timeStampFormat1.format(Now);
            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = calendar.getTime();
            final String finalDateTomorrow = timeStampFormat1.format(tomorrow);
            final String finalDateTomorrow1 = timeStampFormat.format(tomorrow);
            if (rbBookToday.getId() == id) {
                tvBookDateTime.setText(finalDateNow2 + " -- " + updateTime(hour, minute));
                tvBookDateTime.setTag(finalDateNow2 + " " + updateTime(hour, minute));
            } else if (rbBookTomorrow.getId() == id) {
                tvBookDateTime.setText(finalDateTomorrow + " -- " + updateTime(hour, minute));
                tvBookDateTime.setTag(finalDateTomorrow + " " + updateTime(hour, minute));
            }
            if (check) {
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (rbBookToday.getId() == id) {
                            tvBookDateTime.setText(finalDateNow2 + " -- " + updateTime(selectedHour, selectedMinute));
                            tvBookDateTime.setTag(finalDateNow2 + " " + updateTime(selectedHour, selectedMinute));
                        } else if (rbBookTomorrow.getId() == id) {
                            tvBookDateTime.setText(finalDateTomorrow + " -- " + updateTime(selectedHour, selectedMinute));
                            tvBookDateTime.setTag(finalDateTomorrow + " " + updateTime(selectedHour, selectedMinute));
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        } else {
            tvBookDateTime.setText(finalDateNow);
            tvBookDateTime.setTag(finalDateNow1);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rbBookNow:
                setDateTime(false);
                break;

            case R.id.rbBookToday:
                setDateTime(false);
                break;

            case R.id.rbBookTomorrow:
                setDateTime(false);
                break;

            case R.id.tvBookDateTime:
                setDateTime(true);
                break;

            case R.id.tvBookSearchCars:
                if (TextUtils.isEmpty(tvBookFrom.getText().toString()))
                    CommonUtil.showSnackBar(getActivity(), getString(R.string.enter_from), clSearch);
                else if (TextUtils.isEmpty(tvBookTo.getText().toString()))
                    CommonUtil.showSnackBar(getActivity(), getString(R.string.enter_to), clSearch);
                else
//                startActivity(new Intent(getActivity(), SearchCars.class));
                    doPartnerList();
                break;

            case R.id.ivBookVia:
                if (llBookVia1.getVisibility() == View.VISIBLE)
                    llBookVia2.setVisibility(View.VISIBLE);
                llBookVia1.setVisibility(View.VISIBLE);
                break;

            case R.id.ivBookDeleteVia1:
                llBookVia1.setVisibility(View.GONE);
                tvBookvia1.setText("");
                break;

            case R.id.ivBookDeleteVia2:
                llBookVia2.setVisibility(View.GONE);
                tvBookvia2.setText("");
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

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        String message = null;
        if (resultCode == Activity.RESULT_OK) {
            message = data.getStringExtra("AreaName");
            switch (data.getIntExtra("typeAddress", 0)) {
                case 1:
                    tvBookFrom.setText(message);
                    SharedPreferenceUtil.putValue(Constants.FROM_ADDRESS, data.getStringExtra("address"));
                    break;
                case 2:
                    tvBookTo.setText(message);
                    SharedPreferenceUtil.putValue(Constants.TO_ADDRESS, data.getStringExtra("address"));
                    break;
                case 3:
                    tvBookvia1.setText(message);
                    SharedPreferenceUtil.putValue(Constants.VIA_ADDRESS, data.getStringExtra("address"));
                    break;
                case 4:
                    tvBookvia2.setText(message);
                    SharedPreferenceUtil.putValue(Constants.VIA2_ADDRESS, data.getStringExtra("address"));
                    break;
            }
            SharedPreferenceUtil.save();
        }
    }

    public void setDefaultValues() {
        String str = "";
        JSONArray jsonObject;
        try {

            str = SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_LUGGAGE, "");
            jsonObject = new JSONArray(str);
            if (jsonObject.length() > 0) {
                tvBookLuggage.setTag(jsonObject.optJSONObject(0).optString("luggageId"));
                tvBookLuggage.setText(jsonObject.optJSONObject(0).optString("name"));

            }
            str = SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PASSENGER, "");
            jsonObject = new JSONArray(str);
            if (jsonObject.length() > 0) {
                if (jsonObject.optJSONObject(0).optString("name").equalsIgnoreCase("1"))
                    tvBookPassenger.setText(jsonObject.optJSONObject(0).optString("name") + " passenger");
                else
                    tvBookPassenger.setText(jsonObject.optJSONObject(0).optString("name") + " passengers");
                tvBookPassenger.setTag(jsonObject.optJSONObject(0).optString("passengerCountId"));
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
                tvCityDialogHead.setText("Select Luggages");
            } else {
                str = SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PASSENGER, "");
                tvCityDialogHead.setText("Select Passenger/Passengers");
            }
            JSONArray jsonObject = new JSONArray(str);
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);
            AdapterClass adapterClass = new AdapterClass(getActivity(), jsonObject, check);
            list_view.setAdapter(adapterClass);
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(true);
            alert = alertDialogs.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class AdapterClass extends BaseAdapter {
        private final Boolean check;
        private JSONArray data;
        private LayoutInflater inflater = null;
        Activity a;

        public AdapterClass(Activity a, JSONArray data, Boolean check) {
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
                            tvBookLuggage.setTag((String) cbElementClassName.getTag());
                        } else {
                            tvBookPassenger.setText(cbElementClassName.getText().toString());
                            tvBookPassenger.setTag((String) cbElementClassName.getTag());

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
        CommonUtil.alertBox(getActivity(), "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
//                    switch (response.getInt("__eventid")) {
//                        case Constants.Events.EVENT_LOGIN:
//                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_ACCESS_TOKEN, response.optJSONObject("json").optString("token"));
//                    startActivity(new Intent(getActivity(), Search.class));

//                            break;
//                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, getActivity(), clSearch);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void doPartnerList() {
        final String tag = "doPartnerList";
        StringBuilder builder = new StringBuilder();


        JSONObject jsonData = new JSONObject();

        try {
            JSONObject fromData = new JSONObject(SharedPreferenceUtil.getString(Constants.FROM_ADDRESS, ""));
            JSONObject toData = new JSONObject(SharedPreferenceUtil.getString(Constants.TO_ADDRESS, ""));
            JSONObject filterRequest = new JSONObject();
            JSONArray viaArea = new JSONArray();
            if (SharedPreferenceUtil.contains(Constants.VIA_ADDRESS))
                viaArea.put(new JSONObject(SharedPreferenceUtil.getString(Constants.VIA_ADDRESS, "")));
            if (SharedPreferenceUtil.contains(Constants.VIA2_ADDRESS))
                viaArea.put(new JSONObject(SharedPreferenceUtil.getString(Constants.VIA2_ADDRESS, "")));

            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.EVENT_PARTNER_LIST + "&json="
                    + jsonData.put("fromAreaName", fromData.optString("AreaName")).put("fromAreaLat", fromData.optString("AreaLat")).put("fromAreaLong", fromData.optString("AreaLong")).put("fromAreaPlaceid", fromData.optString("AreaPlaceid")).put("fromAreaAddress", fromData.optString("AreaAddress")).put("fromAreaCity", fromData.optString("AreaCity")).put("fromAreaPostalCode", fromData.optString("AreaPostalCode")).put("toAreaName", toData.optString("AreaName")).put("toAreaLat", toData.optString("AreaLat")).put("toAreaLong", toData.optString("AreaLong")).put("toAreaPlaceid", toData.optString("AreaPlaceid")).put("toAreaAddress", toData.optString("AreaAddress")).put("toAreaCity", toData.optString("AreaCity")).put("toAreaPostalCode", toData.optString("AreaPostalCode")).put("viaArea", viaArea)
                    .put("journeyDatetime", (String) tvBookDateTime.getTag()).put("luggageId", (String) tvBookLuggage.getTag()).put("passanger", (String) tvBookPassenger.getTag()).put("bookingduration", "1").put("pageSize", "1").put("pageNumber", "1").put("sortField", "rating").put("sortOrder", "asc").put("filterRequest", filterRequest).put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "")).put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.showProgressDialog(getActivity(), "Login...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }
}
