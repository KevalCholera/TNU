package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.Fragments.FragmentBook;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BookingInfo extends AppCompatActivity {

    TextView tvBookInfoDate, tvBookInfoTime, tvBookInfoFrom, tvBookInfoVia1, tvBookInfoVia2, tvBookInfoTo, tvBookInfoVehicleType,
            tvBookInfoProvider, tvBookInfoPassengers, tvBookInfoLugguages, tvBookInfoETA, tvBookInfoDistance, tvBookInfoRideType,
            tvBookInfoCost;
    Button btBookingInfoConfirm;
    LinearLayout lyBookingInfoVia1, lyBookingInfoVia2, lyBookingInfoETA;

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
        lyBookingInfoVia2 = (LinearLayout) findViewById(R.id.lyBookingInfoVia2);
        lyBookingInfoVia1 = (LinearLayout) findViewById(R.id.lyBookingInfoVia1);
        lyBookingInfoETA = (LinearLayout) findViewById(R.id.lyBookingInfoETA);

        lyBookingInfoETA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.alertBox(BookingInfo.this, getResources().getString(R.string.eta_err), false, false);
            }
        });

        try {
            JSONObject mainData = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.MAIN_DATA, ""));
            try {
                tvBookInfoDate.setText(Constants.DATE_FORMAT_ONLY_DATE.format(Constants.DATE_FORMAT_SEND.parse(mainData.optString("journeyDatetime"))));
                tvBookInfoTime.setText(Constants.DATE_FORMAT_ONLY_TIME.format(Constants.DATE_FORMAT_SEND.parse(mainData.optString("journeyDatetime"))));
                tvBookInfoFrom.setText(mainData.optString("fromAreaAddress"));
                tvBookInfoTo.setText(mainData.optString("toAreaAddress"));

                JSONArray jsonArray = mainData.optJSONArray("viaArray");
                if (mainData.has("viaArray") && jsonArray.length() > 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);

                        if (i == 0) {
                            lyBookingInfoVia1.setVisibility(View.VISIBLE);
                            tvBookInfoVia1.setText(jsonObject.optString("viaAreaAddress"));
                        }
                        if (i == 1) {
                            lyBookingInfoVia2.setVisibility(View.VISIBLE);
                            tvBookInfoVia2.setText(jsonObject.optString("viaAreaAddress"));
                        }
                    }
                }

                tvBookInfoVehicleType.setTag(mainData.optJSONObject("filterRequest").optString("vehicleType"));
                tvBookInfoVehicleType.setText(mainData.optJSONObject("json").optJSONArray("partnerArray").optJSONObject(0).optJSONObject("taxiType").optString("taxiTypeName"));
                tvBookInfoProvider.setText(mainData.optJSONObject("json").optJSONArray("partnerArray").optJSONObject(0).optString("partnerName"));
                tvBookInfoPassengers.setText(mainData.optString("passanger") + "Passengers");
                tvBookInfoLugguages.setText("Up to " + SharedPreferenceUtil.getString(Constants.PrefKeys.LUGGAGE_VALUE, "") + " Luggagges");
                tvBookInfoRideType.setText(mainData.optJSONObject("filterRequest").optString("bookingType").equalsIgnoreCase("0") ? "Single" : "Return");
                tvBookInfoCost.setText("£" + mainData.optJSONObject("json").optJSONArray("partnerArray").optJSONObject(0).optString("ETA") + ".00");

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
                double x = mainData.optJSONObject("json").optJSONObject("distanceMatrix").optInt("distance") * 0.000621371192;
                DecimalFormat df = new DecimalFormat("#.##");
                String dx = df.format(x);
                x = Double.valueOf(dx);

                tvBookInfoDistance.setText(x + " miles");
                SharedPreferenceUtil.putValue(Constants.PrefKeys.FARE_COST, mainData.optJSONObject("json").optJSONArray("partnerArray").optJSONObject(0).optString("ETA"));
                SharedPreferenceUtil.save();
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
