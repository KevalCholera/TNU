package com.smartsense.taxinearyou;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.Adapters.AdapterCard;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Card extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    ListView lvCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvCard = (ListView) findViewById(R.id.lvCard);


//        String data = "[{\"Card\":\"4564\"},{\"Card\":\"1256\"},{\"Card\":\"7596\"},{\"Card\":\"4521\"}]";
//        try {
//            JSONArray jsonArray = new JSONArray(data);
//            AdapterCard adapterCard = new AdapterCard(this, jsonArray);
//            lvCard.setAdapter(adapterCard);
//            Log.i("card", jsonArray.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

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
                                AdapterCard adapterCard = new AdapterCard(this, jsonArray);
                                lvCard.setAdapter(adapterCard);
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

}
