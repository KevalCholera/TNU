package com.smartsense.taxinearyou;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.Adapters.AdapterLostItem;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LostItem extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    ListView lvLostItemList;
    RadioButton rbLostItemOnGoing, rbLostItemFound, rbLostItemNotFound;
    LinearLayout llLostItemNoItemAvailable, llLostItemItemsAvailable;
    int which_clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvLostItemList = (ListView) findViewById(R.id.lvLostItemList);

        rbLostItemNotFound = (RadioButton) findViewById(R.id.rbLostItemNotFound);
        rbLostItemOnGoing = (RadioButton) findViewById(R.id.rbLostItemOnGoing);
        rbLostItemFound = (RadioButton) findViewById(R.id.rbLostItemFound);
        llLostItemNoItemAvailable = (LinearLayout) findViewById(R.id.llLostItemNoItemAvailable);
        llLostItemItemsAvailable = (LinearLayout) findViewById(R.id.llLostItemItemsAvailable);

        rbLostItemNotFound.setOnClickListener(this);
        rbLostItemOnGoing.setOnClickListener(this);
        rbLostItemFound.setOnClickListener(this);

        rbLostItemOnGoing.performClick();

    }

    private void lostItem() {
        final String tag = "Lost Item";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.LOST_ITEM), tag, this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rbLostItemOnGoing:
                which_clicked = 1;
                lostItem();
                break;
            case R.id.rbLostItemFound:
                which_clicked = 2;
                lostItem();
                break;
            case R.id.rbLostItemNotFound:
                which_clicked = 3;
                lostItem();
                break;
        }
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
        llLostItemItemsAvailable.setVisibility(View.GONE);
        llLostItemNoItemAvailable.setVisibility(View.GONE);
        CommonUtil.errorToastShowing(this);
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        CommonUtil.cancelProgressDialog();
        if (jsonObject != null) {

            if (jsonObject.optInt("status") == Constants.STATUS_SUCCESS) {
                JSONArray jsonArray = jsonObject.optJSONObject("json").optJSONArray("lostItemInfoArray");
                if (jsonArray.length() > 0) {
                    llLostItemItemsAvailable.setVisibility(View.VISIBLE);
                    llLostItemNoItemAvailable.setVisibility(View.GONE);
                    JSONArray jsonArray1 = new JSONArray();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        if (which_clicked == 1 && jsonObject1.optJSONObject("rideInfo").optJSONObject("lostItem").optString("status").equalsIgnoreCase("On Going"))
                            jsonArray1.put(jsonObject1);
                        else if (which_clicked == 2 && jsonObject1.optJSONObject("rideInfo").optJSONObject("lostItem").optString("status").equalsIgnoreCase("Item Found"))
                            jsonArray1.put(jsonObject1);
                        else if (which_clicked == 3 && jsonObject1.optJSONObject("rideInfo").optJSONObject("lostItem").optString("status").equalsIgnoreCase("Item Not Found"))
                            jsonArray1.put(jsonObject1);
                    }

                    AdapterLostItem adapterLostItem = new AdapterLostItem(this, jsonArray1);
                    lvLostItemList.setAdapter(adapterLostItem);

                } else {
                    llLostItemItemsAvailable.setVisibility(View.GONE);
                    llLostItemNoItemAvailable.setVisibility(View.VISIBLE);
                }
            } else
                CommonUtil.conditionAuthentication(this, jsonObject);
        } else
            CommonUtil.jsonNullError(this);
    }
}
