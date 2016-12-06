package com.smartsense.taxinearyou;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CircleImageView1;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.WakeLocker;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TripDetails extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    TextView tvTripDetailBookingDate, tvTripDetailBookingTime, tvTripDetailLost, tvTripDetailTaxiProvider, tvTripDetailPickUpDate,
            tvTripDetailPickUpTime, tvTripDetailFare, tvTripDetailFrom, tvTripDetailVia1, tvTripDetailVia2, tvTripDetailTo,
            tvTripDetailTNR, tvTripDetailPassengers, tvTripDetailAdditionsInfo, tvTripDetailLugguages, tvTripDetailMiles,
            tvTripDetailPayment, tvTripDetailRideType, tvTripDetailRideStatus, tvTripDetailVehicle, tvTripDetailCancle,
            tvTripDetailInvoice, tvTripDetailFeedback, tvTripDetailFareLabel, tvTripDetailAdditionsReasonLabel,
            tvTripDetailAdditionsReason;

    LinearLayout lyTripDetailInvoiceFeedback, lyTripDetailsVia2, lyTripDetailsVia1;
    CircleImageView1 cvTripDetailsPartnerLogo;
    AlertDialog alert;
    ImageView ivTripDetailsMap;
    final int requestFeedBack = 1;
    final int requestLostItem = 2;
    int requestRefreshMyTrip = 0;
    Boolean check = false;
    JSONObject tripDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        check = getIntent().getBooleanExtra("check", false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerReceiver(tripMessageReceiver, new IntentFilter(String.valueOf(Constants.Events.EVENT_CHANGE)));
        registerReceiver(tripMessageReceiver, new IntentFilter(String.valueOf(Constants.Events.BookRide)));
        tvTripDetailFareLabel = (TextView) findViewById(R.id.tvTripDetailFareLabel);
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
        tvTripDetailAdditionsInfo = (TextView) findViewById(R.id.tvTripDetailAdditionsInfo);
        tvTripDetailVehicle = (TextView) findViewById(R.id.tvTripDetailVehicle);
        tvTripDetailCancle = (TextView) findViewById(R.id.tvTripDetailCancle);
        tvTripDetailInvoice = (TextView) findViewById(R.id.btTripDetailInvoice);
        tvTripDetailFeedback = (TextView) findViewById(R.id.tvTripDetailFeedback);
        tvTripDetailAdditionsReasonLabel = (TextView) findViewById(R.id.tvTripDetailAdditionsReasonLabel);
        tvTripDetailAdditionsReason = (TextView) findViewById(R.id.tvTripDetailAdditionsReason);
        lyTripDetailInvoiceFeedback = (LinearLayout) findViewById(R.id.lyTripDetailInvoiceFeedback);
        lyTripDetailsVia2 = (LinearLayout) findViewById(R.id.lyTripDetailsVia2);
        lyTripDetailsVia1 = (LinearLayout) findViewById(R.id.lyTripDetailsVia1);
        cvTripDetailsPartnerLogo = (CircleImageView1) findViewById(R.id.cvTripDetailsPartnerLogo);
        ivTripDetailsMap = (ImageView) findViewById(R.id.ivTripDetailsMap);

        try {
            Log.i("Yes", "Here");

//            final JSONObject tripDetails = new JSONObject(getIntent().getStringExtra("key"));
            tripDetails = new JSONObject(SharedPreferenceUtil.getString("key", ""));
            tvTripDetailRideStatus.setText(tripDetails.optString("status"));

            if (tripDetails.optInt("feedbackSts") == 1)
                tvTripDetailFeedback.setVisibility(View.GONE);
            if (tripDetails.optInt("lostFoundExists") == 1)
                tvTripDetailLost.setVisibility(View.GONE);

            if (tvTripDetailRideStatus.getText().toString().equalsIgnoreCase("complete")) {
                tvTripDetailLost.setVisibility(View.VISIBLE);
                lyTripDetailInvoiceFeedback.setVisibility(View.VISIBLE);
            } else if (tvTripDetailRideStatus.getText().toString().equalsIgnoreCase("Open") || tvTripDetailRideStatus.getText().toString().equalsIgnoreCase("Assigned")) {
                tvTripDetailCancle.setVisibility(View.VISIBLE);
            }

            Picasso.with(this)
                    .load(Constants.BASE_URL_IMAGE_POSTFIX + tripDetails.optString("partnerLogo"))
                    .error(R.mipmap.img_place_holder)
                    .placeholder(R.mipmap.img_place_holder)
                    .into(cvTripDetailsPartnerLogo);

            tvTripDetailBookingDate.setText(Constants.DATE_FORMAT_DATE_TIME.format(Constants.DATE_FORMAT_SET.parse(tripDetails.optString("bookingTime").trim())));
            tvTripDetailPickUpDate.setText(Constants.DATE_FORMAT_DATE_TIME.format(Constants.DATE_FORMAT_SET.parse(tripDetails.optString("pickTime").trim())));

            if (tvTripDetailPickUpDate.getText().toString().contains("a.m."))
                tvTripDetailPickUpDate.setText(CommonUtil.changeTimeSmallToCaps(tvTripDetailPickUpDate.getText().toString(), "a.m.", "AM"));
            else
                tvTripDetailPickUpDate.setText(CommonUtil.changeTimeSmallToCaps(tvTripDetailPickUpDate.getText().toString(), "p.m.", "PM"));

            if (tvTripDetailBookingDate.getText().toString().contains("a.m."))
                tvTripDetailBookingDate.setText(CommonUtil.changeTimeSmallToCaps(tvTripDetailBookingDate.getText().toString(), "a.m.", "AM"));
            else
                tvTripDetailBookingDate.setText(CommonUtil.changeTimeSmallToCaps(tvTripDetailBookingDate.getText().toString(), "p.m.", "PM"));


            tvTripDetailTaxiProvider.setText(tripDetails.optString("partner"));
            tvTripDetailTaxiProvider.setTag(tripDetails.optString("rideId"));
            tvTripDetailMiles.setText(CommonUtil.getDecimal(tripDetails.optDouble("estimatedKm")) + " miles");

            tvTripDetailFrom.setText(tripDetails.optString("from"));
            tvTripDetailTo.setText(tripDetails.optString("to"));

            if (tripDetails.optString("addtionalInformation").equalsIgnoreCase(""))
                tvTripDetailAdditionsInfo.setText("-");
            else
                tvTripDetailAdditionsInfo.setText(tripDetails.optString("addtionalInformation"));

            ViewTreeObserver vto = ivTripDetailsMap.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {

                    ivTripDetailsMap.getViewTreeObserver().removeOnPreDrawListener(this);

                    JSONArray jsonArray = tripDetails.optJSONArray("viaArray");

                    String fromLat, fromLong, toLat, toLong, via1Lat = null, via1Long = null, via2Lat, via2Long;

                    fromLat = tripDetails.optString("fromAreaLat");
                    fromLong = tripDetails.optString("fromAreaLng");
                    toLat = tripDetails.optString("toAreaLat");
                    toLong = tripDetails.optString("toAreaLng");

                    if (tripDetails.has("viaArray") && jsonArray.length() > 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.optJSONObject(i);

                            if (i == 0) {
                                JSONObject via1Details = jsonObject.optJSONObject("areaInfo");
                                lyTripDetailsVia1.setVisibility(View.VISIBLE);
                                tvTripDetailVia1.setText(via1Details.optString("areaName"));
                                via1Lat = via1Details.optString("areaLatitude");
                                via1Long = via1Details.optString("areaLongitude");

                                Picasso.with(TripDetails.this)
                                        .load(Uri.parse(Constants.GOOGLE_TRIP_PATH_API + "size=" + ivTripDetailsMap.getMeasuredWidth() + "x" + ivTripDetailsMap.getMeasuredHeight() + "&scale=2&markers=color:" + ContextCompat.getColor(TripDetails.this, R.color.red) + "%7C" + fromLat + "," + fromLong + "%7C" + toLat + "," + toLong + "&path=color:" + ContextCompat.getColor(TripDetails.this, R.color.colorPrimaryDark) + "%7Cweight:4%7C" + fromLat + "," + fromLong + "%7C" + via1Lat + "," + via1Long + "%7C" + toLat + "," + toLong + "&key=" + Constants.GOOGLE_MAP_API))
                                        .error(R.mipmap.img_map)
                                        .placeholder(R.mipmap.img_map)
                                        .into(ivTripDetailsMap);
                            }
                            if (i == 1) {
                                JSONObject via2Details = jsonObject.optJSONObject("areaInfo");
                                lyTripDetailsVia2.setVisibility(View.VISIBLE);
                                tvTripDetailVia2.setText(via2Details.optString("areaName"));
                                via2Lat = via2Details.optString("areaLatitude");
                                via2Long = via2Details.optString("areaLongitude");

                                Picasso.with(TripDetails.this)
                                        .load(Uri.parse(Constants.GOOGLE_TRIP_PATH_API + "size=" + ivTripDetailsMap.getMeasuredWidth() + "x" + ivTripDetailsMap.getMeasuredHeight() + "&scale=2&markers=color:" + ContextCompat.getColor(TripDetails.this, R.color.red) + "%7C" + fromLat + "," + fromLong + "%7C" + toLat + "," + toLong + "&path=color:" + ContextCompat.getColor(TripDetails.this, R.color.colorPrimaryDark) + "%7Cweight:4%7C" + fromLat + "," + fromLong + "%7C" + via1Lat + "," + via1Long + "%7C" + via2Lat + "," + via2Long + "%7C" + toLat + "," + toLong + "&key=" + Constants.GOOGLE_MAP_API))
                                        .error(R.mipmap.img_map)
                                        .placeholder(R.mipmap.img_map)
                                        .into(ivTripDetailsMap);
                            }
                        }
                    } else {
                        Picasso.with(TripDetails.this)
                                .load(Uri.parse(Constants.GOOGLE_TRIP_PATH_API + "size=" + ivTripDetailsMap.getMeasuredWidth() + "x" + ivTripDetailsMap.getMeasuredHeight() + "&scale=2&markers=color:" + ContextCompat.getColor(TripDetails.this, R.color.red) + "%7C" + fromLat + "," + fromLong + "%7C" + toLat + "," + toLong + "&path=color:" + ContextCompat.getColor(TripDetails.this, R.color.colorPrimaryDark) + "%7Cweight:4%7C" + fromLat + "," + fromLong + "%7C" + toLat + "," + toLong + "&key=" + Constants.GOOGLE_MAP_API))
                                .error(R.mipmap.img_map)
                                .placeholder(R.mipmap.img_map)
                                .into(ivTripDetailsMap);
                    }
                    return true;
                }
            });

            if (tripDetails.optInt("feedbackSts") == 1)
                tvTripDetailFeedback.setVisibility(View.GONE);

            if (tripDetails.optInt("lostFoundExists") == 1)
                tvTripDetailLost.setVisibility(View.GONE);

            tvTripDetailTNR.setText(tripDetails.optString("pnr"));
            tvTripDetailPassengers.setText(tripDetails.optString("totalPassenger"));
            tvTripDetailLugguages.setText(tripDetails.optString("totalLuggage"));
            tvTripDetailPayment.setText(tripDetails.optString("paymentType"));
            tvTripDetailRideType.setText(tripDetails.optString("bookingType"));
            tvTripDetailVehicle.setText(tripDetails.optString("vehicleType"));
            tvTripDetailFare.setText("£" + CommonUtil.getDecimal(tripDetails.optDouble("estimatedAmount")));
            if (check) {
                tvTripDetailFare.setText("£" + CommonUtil.getDecimal(tripDetails.optDouble("pendingAmount")));
                tvTripDetailFareLabel.setText("Charge");
                if (tripDetails.optInt("isPaid") == 0) {
                    tvTripDetailLost.setText("Pending Payment");
                    tvTripDetailCancle.setText("Make Payment");
                    tvTripDetailCancle.setVisibility(View.VISIBLE);
                    tvTripDetailLost.setBackgroundColor(ContextCompat.getColor(TripDetails.this, R.color.red));
                } else {
                    tvTripDetailLost.setBackgroundColor(ContextCompat.getColor(TripDetails.this, R.color.dark_green));
                    tvTripDetailLost.setText("Paid");
                }

                tvTripDetailLost.setTag(tripDetails.optString("paymentId"));
                tvTripDetailLost.setVisibility(View.VISIBLE);
                tvTripDetailAdditionsReasonLabel.setVisibility(View.VISIBLE);
                tvTripDetailAdditionsReason.setVisibility(View.VISIBLE);
                if (tripDetails.optString("chargeReason").equalsIgnoreCase(""))
                    tvTripDetailAdditionsReason.setText("-");
                else
                    tvTripDetailAdditionsReason.setText(tripDetails.optString("chargeReason"));
            }
            if (tripDetails.has("refundMsg")) {
                tvTripDetailAdditionsReasonLabel.setText("Refund Information");
                tvTripDetailAdditionsReasonLabel.setVisibility(View.VISIBLE);
                tvTripDetailAdditionsReason.setVisibility(View.VISIBLE);
                tvTripDetailAdditionsReason.setText(tripDetails.optString("refundMsg"));
            }
        } catch (Exception e) {
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
                if (tvTripDetailLost.getText().toString().equalsIgnoreCase("Pending Payment")) {
//                    payPayment();
                    startActivity(new Intent(TripDetails.this, PaymentDetails.class).putExtra("check", true).putExtra("json", tripDetails.toString()));
                } else
                    cancelRideDialog();
                break;
            case R.id.tvTripDetailLost:
                if (tvTripDetailLost.getText().toString().equalsIgnoreCase("Paid")) {

                } else if (tvTripDetailLost.getText().toString().equalsIgnoreCase("Pending Payment")) {
//                    payPayment();
                } else
                    startActivityForResult(new Intent(TripDetails.this, AddLostItem.class).putExtra("rideId", (String) tvTripDetailTaxiProvider.getTag()), requestLostItem);
                break;
            case R.id.tvTripDetailFeedback:
                startActivityForResult(new Intent(TripDetails.this, Feedback.class).putExtra("rideId", (String) tvTripDetailTaxiProvider.getTag()), requestFeedBack);
                break;
            case R.id.btTripDetailInvoice:
                resendInvoice();
                break;
        }
    }

    private void payPayment() {
        final String tag = "payPayment";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("paymentId", (String) tvTripDetailLost.getTag()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.EVENT_PAY_TRIP), tag, this, this);

    }

    private void resendInvoice() {
        final String tag = "Resend Invoice";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("rideId", (String) tvTripDetailTaxiProvider.getTag()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.sending_invoice), CommonUtil.utf8Convert(builder, Constants.Events.RESEND_INVOICE), tag, this, this);

    }

    private void cancelRide() {
        final String tag = "Cancel Ride";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("rideId", (String) tvTripDetailTaxiProvider.getTag()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.CANCEL_RIDE), tag, this, this);
    }

    public void cancelRideDialog() {
        try {

            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialog = inflater.inflate(R.layout.dialog_all, null);
            LinearLayout lyPopUpCancelRide = (LinearLayout) dialog.findViewById(R.id.lyPopUpCancelRide);
            lyPopUpCancelRide.setVisibility(View.VISIBLE);

            TextView tvPopupCancelRideOk, tvPopupCancelRideCancel, tvDialogCancelText;

            tvPopupCancelRideOk = (TextView) dialog.findViewById(R.id.tvPopupCancelRideOk);
            tvPopupCancelRideCancel = (TextView) dialog.findViewById(R.id.tvPopupCancelRideCancel);
            tvDialogCancelText = (TextView) dialog.findViewById(R.id.tvDialogCancelText);

            String important = "<font color='" + ContextCompat.getColor(this, R.color.purple) + "'>" + "Terms and Conditions" + "</font>";
            String text = "<font color='" + ContextCompat.getColor(this, R.color.black) + "'>" + "You may incur charges if you cancel your booking please see " + "</font>";

            Spannable span = Spannable.Factory.getInstance().newSpannable(Html.fromHtml(text + important));
            span.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TripDetails.this, AboutUs.class));
                }
            }, 60, 80, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            tvDialogCancelText.setText(span);
            tvDialogCancelText.setMovementMethod(LinkMovementMethod.getInstance());


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
            alertDialogs.setCancelable(false);
            alert = alertDialogs.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alertBoxTripDetails(final String msg) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
//        builder.setMessage(msg);
//        builder.setTitle(getResources().getString(R.string.cancellation));
//        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                tvTripDetailCancle.setVisibility(View.GONE);
//            }
//        });
//        builder.create();
//        builder.show();

        try {

            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialog = inflater.inflate(R.layout.dialog_all, null);
            LinearLayout lyPopUpCancelRide = (LinearLayout) dialog.findViewById(R.id.lyPopUpGen);
            lyPopUpCancelRide.setVisibility(View.VISIBLE);


            Button tvPopupCancelRideOk = (Button) dialog.findViewById(R.id.btPopupGen);

            TextView tvDialogCancelText = (TextView) dialog.findViewById(R.id.tvPopupGen);


            tvDialogCancelText.setText(msg);


            tvPopupCancelRideOk.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    tvTripDetailCancle.setVisibility(View.GONE);

                }
            });
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(false);
            alert = alertDialogs.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            requestRefreshMyTrip = 1;
            switch (requestCode) {
                case requestFeedBack:

                    tvTripDetailFeedback.setVisibility(View.GONE);
                    break;
                case requestLostItem:
                    tvTripDetailLost.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (requestRefreshMyTrip == 1)
            setResult(RESULT_OK);
        else
            setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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
        if (jsonObject != null)
            if (jsonObject.optInt("status") == Constants.STATUS_SUCCESS) {
                if (jsonObject.optInt("__eventid") == Constants.Events.CANCEL_RIDE) {
                    alert.dismiss();
                    requestRefreshMyTrip = 1;
                    alertBoxTripDetails(jsonObject.optString("msg"));
                } else if (jsonObject.optInt("__eventid") == Constants.Events.EVENT_PAY_TRIP) {
                    tvTripDetailLost.setBackgroundColor(ContextCompat.getColor(TripDetails.this, R.color.dark_green));
                    tvTripDetailLost.setText("Paid");
                    requestRefreshMyTrip = 1;
                    tvTripDetailCancle.setVisibility(View.GONE);
                    CommonUtil.alertBox(this, jsonObject.optString("msg"));
                } else if (jsonObject.optInt("__eventid") == Constants.Events.RESEND_INVOICE) {
                    CommonUtil.alertBox(this, jsonObject.optString("msg"));
                }
            } else {
                CommonUtil.conditionAuthentication(this, jsonObject);
            }
        else
            CommonUtil.jsonNullError(this);
    }

    private final BroadcastReceiver tripMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                JSONObject pushData = new JSONObject(intent.getStringExtra(Constants.EXTRAS));
                if (pushData.has("ride")) {
                    WakeLocker.acquire(context);
                    Log.i("Push ", intent.getStringExtra(Constants.EXTRAS));

                    String taxiProvider = (String) tvTripDetailTaxiProvider.getTag();
                    if (pushData.optJSONObject("ride").optString("rideId").equalsIgnoreCase(taxiProvider)) {
                        requestRefreshMyTrip = 1;
                        if (pushData.optJSONObject("ride").optString("status").equalsIgnoreCase("complete")) {
                            tvTripDetailLost.setVisibility(View.VISIBLE);
                            lyTripDetailInvoiceFeedback.setVisibility(View.VISIBLE);
                            tvTripDetailCancle.setVisibility(View.GONE);
                        }
                        tvTripDetailRideStatus.setText(pushData.optJSONObject("ride").optString("status"));
                    }
                    WakeLocker.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(tripMessageReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}