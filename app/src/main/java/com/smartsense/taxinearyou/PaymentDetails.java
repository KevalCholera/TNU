package com.smartsense.taxinearyou;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.TimeActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends TimeActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    TextView tvPaymentTNUCredit, tvPaymentCard, tvPaymentCash, tvPaymentAmount;
    private AlertDialog alert;
    JSONObject jsonObject3;
    private String pType = Constants.PAYMENT_TYPE_CASH;
    private final int paymentRequest = 1;
    String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_payment_details);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            jsonObject3 = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_REQUEST_JSON, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvPaymentTNUCredit = (TextView) findViewById(R.id.tvPaymentTNUCredit);
        tvPaymentCard = (TextView) findViewById(R.id.tvPaymentCard);
        tvPaymentCash = (TextView) findViewById(R.id.tvPaymentCash);
        tvPaymentAmount = (TextView) findViewById(R.id.tvPaymentAmount);

        tvPaymentAmount.setText("£" + CommonUtil.getDecimal(Double.valueOf(SharedPreferenceUtil.getString(Constants.PrefKeys.FARE_COST, ""))));

        tvPaymentCash.setOnClickListener(this);
        tvPaymentCard.setOnClickListener(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_payment_details;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPaymentCash:
                pType = Constants.PAYMENT_TYPE_CASH;
                isPartnerAvailable();
                break;
            case R.id.tvPaymentCard:
                pType = Constants.PAYMENT_TYPE_CARD;
                isPartnerAvailable();
                break;
        }
    }

    public void finalBooking(Boolean check) {
        final String tag = "Final Booking";
        StringBuilder builder = new StringBuilder();
        JSONObject mainData = new JSONObject();

        try {
            JSONObject jsonObject1 = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.BOOKING_INFO, ""));
            jsonObject1.put("forceBook", check);
            jsonObject1.put("paymentType", pType);
            JSONObject jsonObject2 = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_DISTANCE_MATRIX, ""));

            JSONObject jsonObject4 = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_CUSTOMER_SELECTION, ""));

            builder.append(mainData.put("bookingInfo", jsonObject1)
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("distanceMatrix", jsonObject2)
                    .put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("requestJson", jsonObject3)

                    .put("customerSelection", jsonObject4));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.BookRide), tag, this, this);
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

    private void isPartnerAvailable() {

        final String tag = "Book Or Not";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();
        try {
            JSONObject jsonObject = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_CUSTOMER_SELECTION, ""));
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")).put("taxiTypeId", jsonObject.optString("taxiTypeId")).put("availabilityStatus", jsonObject.optString("available")).put("partnerId", jsonObject.optString("partnerId")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.EVENT_IS_PARTNER_AVAILABLE), tag, this, this);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.cancelProgressDialog();
        CommonUtil.errorToastShowing(this);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        CommonUtil.cancelProgressDialog();
        if (jsonObject != null)
            if (jsonObject.optInt("status") == Constants.STATUS_SUCCESS) {
                try {
                    switch (jsonObject.getInt("__eventid")) {
                        case Constants.Events.BookRide:
                            if (jsonObject.optJSONObject("json").has("key")) {
                                msg=jsonObject.optString("msg");
                                startActivityForResult(new Intent(this, WebViewActivity.class).putExtra("key", jsonObject.optJSONObject("json").optString("key")).putExtra("vendor", jsonObject.optJSONObject("json").optString("vendor")), paymentRequest);
                            } else
                                CommonUtil.openDialogs(PaymentDetails.this, "Payment Details", R.id.lyPopupBookSuccess, R.id.btPopupBookSuccessOk, jsonObject.optString("msg"), R.id.tvDialogAllSuccess);
                            break;
                        case Constants.Events.EVENT_IS_PARTNER_AVAILABLE:
                            if (jsonObject.optJSONObject("json").optBoolean("available")) {
                                if (!jsonObject.optJSONObject("json").optBoolean("taxiAvailibilityUpdated"))
                                    finalBooking(false);
                                else
                                    openDialog(jsonObject.optJSONObject("json").optString("msg"));
                            } else {
                                CommonUtil.openDialogs(this, "Payment Fail", R.id.lyPopupBookError, R.id.btPopupBookErrorOk, jsonObject.optJSONObject("json").optString("msg"), R.id.tvDialogAllError);
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (jsonObject.has("json") && jsonObject.optJSONObject("json").has("errorCode") && jsonObject.optJSONObject("json").optInt("errorCode") == Constants.ErrorCode.SORRY_INCONVENIENCE || jsonObject.has("json") && jsonObject.optJSONObject("json").has("errorCode") && jsonObject.optJSONObject("json").optInt("errorCode") == Constants.ErrorCode.UNPAID_CANCEL_RIDE_CHARGE || jsonObject.has("json") && jsonObject.optJSONObject("json").has("errorCode") && jsonObject.optJSONObject("json").optInt("errorCode") == Constants.ErrorCode.LOGGEDOUT_PARTNER)
                    CommonUtil.openDialogs(this, "Payment Fail", R.id.lyPopupBookError, R.id.btPopupBookErrorOk, jsonObject.optString("msg"), R.id.tvDialogAllError);
                else
                    CommonUtil.conditionAuthentication(this, jsonObject);
            }
        else
            CommonUtil.jsonNullError(this);
    }

    public void openDialog(String msg) {

        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View dialog = inflater.inflate(R.layout.dialog_all, null);
            LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.lyPopUpForseRide);
            linearLayout.setVisibility(View.VISIBLE);
            TextView tvPopupLocatedEmail = (TextView) dialog.findViewById(R.id.tvPopupCancelForse);
            tvPopupLocatedEmail.setText(msg);
            TextView button1 = (TextView) dialog.findViewById(R.id.tvPopupCancelForseCancel);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    startActivity(new Intent(PaymentDetails.this, SearchCars.class)
                            .putExtra("tvBookDateTime", jsonObject3.optString("journeyDatetime"))
                            .putExtra("tvBookLuggage", jsonObject3.optString("luggageId"))
                            .putExtra("luggageDescription", jsonObject3.optString("luggageDescription"))
                            .putExtra("passengerDescription", jsonObject3.optString("passengerDescription"))
                            .putExtra("duration", Integer.valueOf(jsonObject3.optString("bookingduration")))
                            .putExtra("tvBookPassenger", jsonObject3.optString("passanger")));
                    finish();
                }
            });
            TextView button2 = (TextView) dialog.findViewById(R.id.tvPopupCancelForseOk);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    finalBooking(true);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case paymentRequest:
                if (resultCode == Activity.RESULT_OK)
                    CommonUtil.openDialogs(PaymentDetails.this, "Payment Details", R.id.lyPopupBookSuccess, R.id.btPopupBookSuccessOk, "Success", R.id.tvDialogAllSuccess);
                else
                    CommonUtil.openDialogs(this, "Payment Fail", R.id.lyPopupBookError, R.id.btPopupBookErrorOk, "Fail", R.id.tvDialogAllError);
                break;
        }
    }


}
