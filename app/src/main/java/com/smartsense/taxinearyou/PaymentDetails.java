package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.DataRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PaymentDetails extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    TextView tvPaymentTNUCredit, tvPaymentCard, tvPaymentCash, tvPaymentAmount;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvPaymentTNUCredit = (TextView) findViewById(R.id.tvPaymentTNUCredit);
        tvPaymentCard = (TextView) findViewById(R.id.tvPaymentCard);
        tvPaymentCash = (TextView) findViewById(R.id.tvPaymentCash);
        tvPaymentAmount = (TextView) findViewById(R.id.tvPaymentAmount);

        tvPaymentAmount.setText("£" + SharedPreferenceUtil.getString(Constants.PrefKeys.FARE_COST, ""));

        tvPaymentCash.setOnClickListener(this);
        tvPaymentCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPaymentCash:
                finalBooking();
                break;
            case R.id.tvPaymentCard:
                startActivity(new Intent(PaymentDetails.this, Card.class));
                break;
        }
    }

    public void finalBooking() {
        final String tag = "Final Booking";
        StringBuilder builder = new StringBuilder();
        JSONObject mainData = new JSONObject();
        String url = "";

        try {
            JSONObject jsonObject1 = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.BOOKING_INFO, ""));
            JSONObject jsonObject2 = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_DISTANCE_MATRIX, ""));
            JSONObject jsonObject3 = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_REQUEST_JSON, ""));
            JSONObject jsonObject4 = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_CUSTOMER_SELECTION, ""));

            builder.append(mainData.put("bookingInfo", jsonObject1)
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("distanceMatrix", jsonObject2)
                    .put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("requestJson", jsonObject3)
                    .put("customerSelection", jsonObject4));

            try {
                url = Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.BookRide + "&json="
                        + URLEncoder.encode(builder.toString(), "UTF8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.showProgressDialog(this, "Booking your ride...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
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

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.cancelProgressDialog();
        CommonUtil.errorToastShowing(this);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        CommonUtil.cancelProgressDialog();
        if (jsonObject != null && jsonObject.optInt("status") == Constants.STATUS_SUCCESS)
            CommonUtil.openDialogs(PaymentDetails.this, "Payment Details", R.id.lyPopupBookSuccess, R.id.btPopupBookSuccessOk);
        else CommonUtil.successToastShowing(this, jsonObject);
    }
}
