package com.smartsense.taxinearyou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

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
    LinearLayout llCardList;
    private AlertDialog alert;
    Button btCardSavePay;
    CoordinatorLayout clPaymentDetails;

    private AdapterCardList adapterCardList;
    String cardId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getBooleanExtra("pending Amount", false))
            getSupportActionBar().setTitle("Pay with existing card");

        lvCardList = (ListView) findViewById(R.id.lvCardList);
        llCardList = (LinearLayout) findViewById(R.id.llCardList);
        btCardSavePay = (Button) findViewById(R.id.btCardSavePay1);

        clPaymentDetails = (CoordinatorLayout) findViewById(R.id.clRecoverEmail);
        lvCardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                JSONObject jsonObject = (JSONObject) adapterView.getItemAtPosition(i);
                if (AdapterCardList.pos != i) {
                    cardId = jsonObject.optString("id");
                    Log.i("Yes", "Here" + cardId);
                    AdapterCardList.pos = i;

                } else {
                    cardId = "";
                    AdapterCardList.pos = -1;

                }
                adapterCardList.adapterCardList();
//                alertBoxTripDetails("Are you sure you want to delete?");
            }
        });


        btCardSavePay.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btCardSavePay1:
                try {
                    Log.i("Yes", "Here");
                    CommonUtil.closeKeyboard(this);
                    if (cardId.equalsIgnoreCase("")) {
                        Log.i("Yes1", "Here");
                        CommonUtil.showSnackBar("Please select card", clPaymentDetails);
                    } else {
                        Log.i("Yes", "1Here");
                        Intent i = new Intent();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("card_id", cardId);
                        i.putExtra("obj", jsonObject.toString());
                        setResult(Activity.RESULT_OK, i);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Yes", "Here1");
                }
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
//                            Log.i("Yes1","Here");
                            JSONArray jsonArray = response.optJSONObject("json").getJSONArray("jCardList");
                            if (jsonArray.length() > 0) {
//                                Log.i("Yes","Here");
//                            String s = "{\"msg\":\"\",\"json\":{\"jCardList\":[{\"last4\":\"4242\",\"exp_month\":4,\"id\":\"card_18qmPlCYlFwHDlzNCVz1iAwj\",\"exp_year\":2018,\"brand\":\"Visa\"}]},\"__eventid\":\"1151\",\"userId\":\"271\",\"token\":\"A0G4LsWglNkNEtfAr0U7\",\"status\":200}";
//                            JSONObject response1 = new JSONObject(s);
//                            JSONArray jsonArray = response1.optJSONObject("json").getJSONArray("jCardList");

                                llCardList.setVisibility(View.VISIBLE);
                                adapterCardList = new AdapterCardList(this, jsonArray);
                                lvCardList.setAdapter(adapterCardList);
                            } else {
                                llCardList.setVisibility(View.GONE);
                            }
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

    @Override
    public void onBackPressed() {
        CommonUtil.closeKeyboard(this);
        Intent i = new Intent();
        setResult(Activity.RESULT_CANCELED, i);
        finish();
        super.onBackPressed();
    }
}
