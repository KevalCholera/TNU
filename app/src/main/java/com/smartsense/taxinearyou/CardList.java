package com.smartsense.taxinearyou;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.Adapters.AdapterCardList;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CardList extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    ListView lvCardList;
    private AlertDialog alert;
    CoordinatorLayout clPaymentDetails;
    private AdapterCardList adapterCardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvCardList = (ListView) findViewById(R.id.lvCardList);
        clPaymentDetails = (CoordinatorLayout) findViewById(R.id.clPaymentDetails);


        lvCardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                AdapterCardList.pos = i;
                adapterCardList.adapterCardList();
//                alertBoxTripDetails("Are you sure you want to delete?");
            }
        });

        isCardList();
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
            Button tvPopupCancelRideCancel = (Button) dialog.findViewById(R.id.btPopupGen1);
            tvPopupCancelRideCancel.setVisibility(View.VISIBLE);
            TextView tvDialogCancelText = (TextView) dialog.findViewById(R.id.tvPopupGen);


            tvDialogCancelText.setText(msg);


            tvPopupCancelRideOk.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alert.dismiss();


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPaymentCash:

                break;
        }
    }

    private void isCardList() {

        final String tag = "Card List";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();
        try {
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("builder", builder.toString());

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.EVENT_CARD_LIST), tag, this, this);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.errorToastShowing(this);
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (response.getInt("__eventid")) {
                        case Constants.Events.EVENT_CARD_LIST:
                            String data = "[{\"Card_No\":\"4567\",\"Expiry_Date\":\"16/20\"},{\"Card_No\":\"4521\",\"Expiry_Date\":\"30/25\"},{\"Card_No\":\"7854\",\"Expiry_Date\":\"10/29\"},{\"Card_No\":\"9854\",\"Expiry_Date\":\"15/45\"}]";

                            JSONArray jsonArray = response.optJSONObject("json").getJSONArray("jCardList");
                            adapterCardList = new AdapterCardList(this, jsonArray);
                            lvCardList.setAdapter(adapterCardList);

                            break;
                    }
                } else {
                    CommonUtil.conditionAuthentication(this, response);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            CommonUtil.jsonNullError(this);
    }
}
