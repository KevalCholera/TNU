package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.TimeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class BookingInfo extends TimeActivity {

    TextView tvBookInfoDate, tvBookInfoTime, tvBookInfoFrom, tvBookInfoVia1, tvBookInfoVia2, tvBookInfoTo, tvBookInfoVehicleType,
            tvBookInfoProvider, tvBookInfoPassengers, tvBookInfoLugguages, tvBookInfoETA, tvBookInfoDistance, tvBookInfoRideType,
            tvBookInfoCost, tvBookingInfoVia2ChangeName;
    Button btBookingInfoConfirm;
    LinearLayout lyBookingInfoVia1, lyBookingInfoVia2, lyBookingInfoETA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_booking_info);
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
        tvBookingInfoVia2ChangeName = (TextView) findViewById(R.id.tvBookingInfoVia2ChangeName);
        btBookingInfoConfirm = (Button) findViewById(R.id.btBookingInfoConfirm);
        lyBookingInfoVia2 = (LinearLayout) findViewById(R.id.lyBookingInfoVia2);
        lyBookingInfoVia1 = (LinearLayout) findViewById(R.id.lyBookingInfoVia1);
        lyBookingInfoETA = (LinearLayout) findViewById(R.id.lyBookingInfoETA);

        lyBookingInfoETA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.alertBox(BookingInfo.this, getResources().getString(R.string.eta_err));
            }
        });

        try {
            JSONObject mainData = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.MAIN_DATA, ""));
            JSONObject jsonObject = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_CUSTOMER_SELECTION, ""));
            try {
                tvBookInfoDate.setText(Constants.DATE_FORMAT_ONLY_DATE.format(Constants.DATE_FORMAT_SEND.parse(mainData.optString("journeyDatetime"))));
                tvBookInfoTime.setText(Constants.DATE_FORMAT_ONLY_TIME.format(Constants.DATE_FORMAT_SEND.parse(mainData.optString("journeyDatetime"))));
                tvBookInfoFrom.setText(mainData.optString("fromAreaAddress"));
                tvBookInfoTo.setText(mainData.optString("toAreaAddress"));

                if (!SharedPreferenceUtil.getString(Constants.VIA_ADDRESS, "").equalsIgnoreCase("")) {
                    JSONObject viaField1 = new JSONObject(SharedPreferenceUtil.getString(Constants.VIA_ADDRESS, ""));
                    lyBookingInfoVia1.setVisibility(View.VISIBLE);
                    tvBookInfoVia1.setText(viaField1.optString("viaAreaAddress"));
                }
                if (!SharedPreferenceUtil.getString(Constants.VIA2_ADDRESS, "").equalsIgnoreCase("")) {
                    JSONObject viaField2 = new JSONObject(SharedPreferenceUtil.getString(Constants.VIA2_ADDRESS, ""));
                    lyBookingInfoVia2.setVisibility(View.VISIBLE);
                    tvBookInfoVia2.setText(viaField2.optString("viaAreaAddress"));
                }

                if (lyBookingInfoVia1.getVisibility() == View.GONE && lyBookingInfoVia2.getVisibility() == View.VISIBLE) {
                    tvBookingInfoVia2ChangeName.setText(getResources().getString(R.string.via_1));
                }
                tvBookInfoVehicleType.setTag(mainData.optJSONObject("filterRequest").optString("vehicleType"));
                tvBookInfoVehicleType.setText(mainData.optJSONObject("json").optJSONArray("partnerArray").optJSONObject(0).optJSONObject("taxiType").optString("taxiTypeName"));
                tvBookInfoProvider.setText(jsonObject.optString("partnerName"));
                tvBookInfoPassengers.setText(mainData.optString("passanger").equalsIgnoreCase("1") ? mainData.optString("passanger") + " Passenger" : mainData.optString("passanger") + " Passengers");
                tvBookInfoLugguages.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.LUGGAGE_VALUE, ""));
                tvBookInfoRideType.setText(mainData.optJSONObject("filterRequest").optString("bookingType").equalsIgnoreCase("1") ? "Single" : "Return");
                tvBookInfoCost.setText("£" + CommonUtil.getDecimal(Double.valueOf(SharedPreferenceUtil.getString(Constants.PrefKeys.FARE_COST, ""))));

                int hours = mainData.optJSONObject("json").optJSONObject("distanceMatrix").optInt("duration") / 3600;
                String hour;
                int mins = (mainData.optJSONObject("json").optJSONObject("distanceMatrix").optInt("duration") / 60) % 60;
                String min;

                if (hours < 10)
                    hour = "0" + hours;
                else
                    hour = "" + hours;

                if (mins < 10)
                    min = "0" + mins;
                else
                    min = "" + mins;

                tvBookInfoETA.setText(hour + ":" + min + " hours");


                tvBookInfoDistance.setText(CommonUtil.getDecimal(mainData.optJSONObject("json").optJSONObject("distanceMatrix").optDouble("distance")) + " miles");

            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        btBookingInfoConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingInfo.this, PersonalInfo.class));
            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_booking_info;
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
}
