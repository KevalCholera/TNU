package com.smartsense.taxinearyou;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CircleImageView;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.DataRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TripDetails extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    TextView tvTripDetailBookingDate, tvTripDetailBookingTime, tvTripDetailLost, tvTripDetailTaxiProvider, tvTripDetailPickUpDate,
            tvTripDetailPickUpTime, tvTripDetailFare, tvTripDetailFrom, tvTripDetailVia1, tvTripDetailVia2, tvTripDetailTo,
            tvTripDetailTNR, tvTripDetailPassengers, tvTripDetailLugguages, tvTripDetailMiles, tvTripDetailPayment, tvTripDetailRideType,
            tvTripDetailRideStatus, tvTripDetailVehicle, tvTripDetailCancle, tvTripDetailInvoice, tvTripDetailFeedback;
    LinearLayout lyTripDetailInvoiceFeedback;
    CircleImageView cvTripDetailsPartnerLogo;
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
        cvTripDetailsPartnerLogo = (CircleImageView) findViewById(R.id.cvTripDetailsPartnerLogo);

        try {
            JSONObject tripDetails = new JSONObject(getIntent().getStringExtra("key"));
            tvTripDetailRideStatus.setText(tripDetails.optString("status"));

            if (tvTripDetailRideStatus.getText().toString().equalsIgnoreCase("waiting")) {
                tvTripDetailCancle.setVisibility(View.VISIBLE);
            } else if (tvTripDetailRideStatus.getText().toString().equalsIgnoreCase("completed")) {
                tvTripDetailLost.setVisibility(View.VISIBLE);
                lyTripDetailInvoiceFeedback.setVisibility(View.VISIBLE);
            }

            Picasso.with(this)
                    .load(tripDetails.optString("partnerLogo"))
                    .error(R.mipmap.imgtnulogo)
                    .placeholder(R.mipmap.imgtnulogo)
                    .into(cvTripDetailsPartnerLogo);
            tvTripDetailBookingDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(new SimpleDateFormat("dd-MMMM-yyyy HH:mm").parse(tripDetails.optString("bookingTime").trim())));
            tvTripDetailBookingTime.setText(new SimpleDateFormat("hh:mm aa").format(new SimpleDateFormat("dd-MMMM-yyyy HH:mm").parse(tripDetails.optString("bookingTime").trim())));
            tvTripDetailPickUpDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(new SimpleDateFormat("dd-MMMM-yyyy HH:mm").parse(tripDetails.optString("pickTime").trim())));
            tvTripDetailPickUpTime.setText(new SimpleDateFormat("hh:mm aa").format(new SimpleDateFormat("dd-MMMM-yyyy HH:mm").parse(tripDetails.optString("pickTime").trim())));

            tvTripDetailTaxiProvider.setText(tripDetails.optString("partner"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_RIDE_ID, tripDetails.optString("rideId"));
            tvTripDetailFare.setText("$" + tripDetails.optInt("estimatedAmount"));
            tvTripDetailFrom.setText(tripDetails.optString("from"));
            tvTripDetailTo.setText(tripDetails.optString("to"));

            if (tripDetails.optJSONArray("viaArray").length() > 0) {
                tvTripDetailVia1.setText(tripDetails.optJSONArray("viaArea").optJSONObject(0).optString("viaAreaAddress"));
//                tvTripDetailVia1.setTag(tripDetails.optJSONArray("viaArea").optJSONObject(0).optString("viaAreaPlaceid"));
                if (tripDetails.optJSONArray("viaArray").length() == 2) {
                    tvTripDetailVia2.setText(tripDetails.optJSONArray("viaArea").optJSONObject(1).optString("viaAreaAddress"));
//                    tvTripDetailVia2.setTag(tripDetails.optJSONArray("viaArea").optJSONObject(1).optString("viaAreaPlaceid"));
                }
            }
            tvTripDetailTNR.setText(tripDetails.optString("pnr"));
            tvTripDetailPassengers.setText(tripDetails.optInt("totalPassenger") + "");
            tvTripDetailLugguages.setText(tripDetails.optString("totalLuggage"));
            tvTripDetailMiles.setText(tripDetails.optInt("estimatedKm") + "");
            tvTripDetailPayment.setText(tripDetails.optString("paymentType"));
            tvTripDetailRideType.setText(tripDetails.optString("bookingType"));
            tvTripDetailVehicle.setText(tripDetails.optString("vehicleType"));

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        tvTripDetailCancle.setOnClickListener(this);
        tvTripDetailLost.setOnClickListener(this);
        tvTripDetailFeedback.setOnClickListener(this);
        tvTripDetailInvoice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvTripDetailCancle:
                openOccasionsPopup();
                break;
            case R.id.tvTripDetailLost:
                startActivity(new Intent(TripDetails.this, AddLostItem.class));
                break;
            case R.id.tvTripDetailFeedback:
                startActivity(new Intent(TripDetails.this, Feedback.class));
                break;
            case R.id.btTripDetailInvoice:
                tripDetails();
                break;
        }
    }

    private void tripDetails() {
        final String tag = "Resend Invoice";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.RESEND_INVOICE + "&json=").append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "") + jsonData.put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")) + jsonData.put("rideId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_RIDE_ID, ""))));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.showProgressDialog(this, "getting data...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }

    private void cancelRide() {
        final String tag = "Cancel Ride";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.CANCEL_RIDE + "&json=")
                    .append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                            .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                            .put("rideId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_RIDE_ID, "")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.showProgressDialog(this, "getting data...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
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

                    cancelRide();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.ratingforsearch, menu);
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

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.errorToastShowing(this);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        CommonUtil.cancelProgressDialog();
        if (jsonObject.length() > 0 && jsonObject.optInt("status") == Constants.STATUS_SUCCESS)
            CommonUtil.successToastShowing(this, jsonObject);
        else
            CommonUtil.successToastShowing(this, jsonObject);
    }
}
