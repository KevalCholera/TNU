package com.smartsense.taxinearyou;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Card extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    ListView lvCard;
    LinearLayout llCard;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvCard = (ListView) findViewById(R.id.lvCard);
        llCard = (LinearLayout) findViewById(R.id.llCard);

        isCardList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

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
//                            Log.i("Yes1","Here");
                            JSONArray jsonArray = response.optJSONObject("json").getJSONArray("jCardList");
                            if (jsonArray.length() > 0) {
                                lvCard.setVisibility(View.VISIBLE);
                                llCard.setVisibility(View.GONE);
                                AdapterCard adapterCard = new AdapterCard(this, jsonArray);
                                lvCard.setAdapter(adapterCard);
                            } else {
                                lvCard.setVisibility(View.GONE);
                                llCard.setVisibility(View.VISIBLE);
                            }
                            break;
                        case Constants.Events.EVENT_CARD_DELETE:
                            if (response.optJSONObject("json").optBoolean("deleteResponse"))
                                isCardList();

                            CommonUtil.successToastShowing(Card.this, response);

//                            if (response.optJSONObject("json").optBoolean("deleteResponse"))
//                                CommonUtil.openDialogs1(Card.this, "Payment", R.id.lyPopupBookSuccess, R.id.btPopupBookSuccessOk, response.optString("msg"), R.id.tvDialogAllSuccess);
//                            else
//                                CommonUtil.openDialogs1(this, "Payment", R.id.lyPopupBookError, R.id.btPopupBookErrorOk, response.optJSONObject("json").optString("msg"), R.id.tvDialogAllError);

                            break;
                    }
                } else {
//                    switch (response.getInt("__eventid")) {
//                        case Constants.Events.EVENT_CARD_DELETE:
//
//                            break;
//                    }
                    CommonUtil.conditionAuthentication(this, response);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            CommonUtil.jsonNullError(this);
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

    public class AdapterCard extends BaseAdapter {
        private JSONArray data;
        private LayoutInflater inflater = null;
        Activity a;

        public AdapterCard(Activity a, JSONArray data) {
            this.data = data;
            this.a = a;
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

                vi = inflater.inflate(R.layout.element_card, null);
            TextView tvElementCardListCardNumber = (TextView) vi.findViewById(R.id.tvElementCardListCardNumber);
            TextView tvElementCardListExpiryDate = (TextView) vi.findViewById(R.id.tvElementCardListExpiryDate);
            TextView tvElementCardListRemoveCard = (TextView) vi.findViewById(R.id.tvElementCardListRemoveCard);

            final JSONObject test = data.optJSONObject(position);
//            Log.i("Test", test.toString());

            tvElementCardListCardNumber.setText("xxxx xxxx xxxx " + test.optString("last4"));
            String expMonth = test.optString("exp_month");

            if (expMonth.length() == 1)
                expMonth = "0" + expMonth;

            tvElementCardListExpiryDate.setText(expMonth + " / " + test.optString("exp_year"));
            tvElementCardListRemoveCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertBoxTripDetails(test.optString("id"));
                }
            });
            return vi;
        }


    }

    private void deleteCard(String cardId) {
        final String tag = "Delete Card";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();
        try {
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")).put("cardId", cardId));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("builder", builder.toString());
        CommonUtil.jsonRequestGET(Card.this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.EVENT_CARD_DELETE), tag, this, this);
    }

    public void alertBoxTripDetails(final String cardId) {
        try {

            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialog = inflater.inflate(R.layout.dialog_all, null);
            LinearLayout lyPopUpCancelRide = (LinearLayout) dialog.findViewById(R.id.lyPopUpGen);
            lyPopUpCancelRide.setVisibility(View.VISIBLE);

            Button tvPopupCancelRideOk = (Button) dialog.findViewById(R.id.btPopupGen);
            Button tvPopupCancelRideCan = (Button) dialog.findViewById(R.id.btPopupGen1);
            tvPopupCancelRideCan.setVisibility(View.VISIBLE);
            TextView tvDialogCancelText = (TextView) dialog.findViewById(R.id.tvPopupGen);
            tvDialogCancelText.setText("Are you sure you want to delete?");
            tvPopupCancelRideCan.setText("No");
            tvPopupCancelRideCan.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {

                    alert.dismiss();


                }
            });
            tvPopupCancelRideOk.setText("Yes");
            tvPopupCancelRideOk.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    deleteCard(cardId);
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
}
