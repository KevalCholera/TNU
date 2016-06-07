package com.smartsense.taxinearyou;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class LostItem extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    ListView lvLostItemList;
    RadioButton rbLostItemOnGoing, rbLostItemFound, rbLostItemNotFound;
    LinearLayout llLostItemNoItemAvailable, llLostItemItemsAvailable;
    int which_clicked = Constants.LostItem.ON_GOING;
    private JSONArray jsonArray;
    private SwipeRefreshLayout srLostItemList;
    boolean onCreate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvLostItemList = (ListView) findViewById(R.id.lvLostItemList);
        srLostItemList = (SwipeRefreshLayout) findViewById(R.id.srLostItemList);
        rbLostItemNotFound = (RadioButton) findViewById(R.id.rbLostItemNotFound);
        rbLostItemOnGoing = (RadioButton) findViewById(R.id.rbLostItemOnGoing);
        rbLostItemFound = (RadioButton) findViewById(R.id.rbLostItemFound);
        llLostItemNoItemAvailable = (LinearLayout) findViewById(R.id.llLostItemNoItemAvailable);
        llLostItemItemsAvailable = (LinearLayout) findViewById(R.id.llLostItemItemsAvailable);

        rbLostItemNotFound.setOnClickListener(this);
        rbLostItemOnGoing.setOnClickListener(this);
        rbLostItemFound.setOnClickListener(this);

        srLostItemList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lostItem();
            }
        });

        lostItem();
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
        if (onCreate)
            CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.LOST_ITEM), tag, this, this);
        else
            CommonUtil.jsonRequestNoProgressBar(CommonUtil.utf8Convert(builder, Constants.Events.LOST_ITEM), tag, this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rbLostItemOnGoing:
                which_clicked = Constants.LostItem.ON_GOING;
                lostItemFilter(jsonArray);
                break;
            case R.id.rbLostItemFound:
                which_clicked = Constants.LostItem.FOUND;
                lostItemFilter(jsonArray);
                break;
            case R.id.rbLostItemNotFound:
                which_clicked = Constants.LostItem.NOT_FOUND;
                lostItemFilter(jsonArray);
                break;
        }
    }

    public void lostItemFilter(JSONArray jsonArray) {
        JSONArray jsonArray1 = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

            if (which_clicked == Constants.LostItem.ON_GOING && jsonObject1.optJSONObject("rideInfo").optJSONObject("lostItem").optString("status").equalsIgnoreCase("On Going"))
                jsonArray1.put(jsonObject1);
            else if (which_clicked == Constants.LostItem.FOUND && jsonObject1.optJSONObject("rideInfo").optJSONObject("lostItem").optString("status").equalsIgnoreCase("Item Found"))
                jsonArray1.put(jsonObject1);
            else if (which_clicked == Constants.LostItem.NOT_FOUND && jsonObject1.optJSONObject("rideInfo").optJSONObject("lostItem").optString("status").equalsIgnoreCase("Item Not Found"))
                jsonArray1.put(jsonObject1);
        }

        AdapterLostItem adapterLostItem = new AdapterLostItem(this, jsonArray1);
        lvLostItemList.setAdapter(adapterLostItem);
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
        lvLostItemList.setVisibility(View.GONE);
        llLostItemNoItemAvailable.setVisibility(View.GONE);
        CommonUtil.errorToastShowing(this);
        CommonUtil.cancelProgressDialog();
        if (srLostItemList.isRefreshing()) {
            srLostItemList.setRefreshing(false);
        }
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        CommonUtil.cancelProgressDialog();
        if (srLostItemList.isRefreshing()) {
            srLostItemList.setRefreshing(false);
        }
        if (jsonObject != null) {
            if (jsonObject.optInt("status") == Constants.STATUS_SUCCESS) {
                jsonArray = jsonObject.optJSONObject("json").optJSONArray("lostItemInfoArray");
                if (jsonArray.length() > 0) {
                    lvLostItemList.setVisibility(View.VISIBLE);
                    llLostItemNoItemAvailable.setVisibility(View.GONE);
                    lostItemFilter(jsonArray);

                } else {
                    lvLostItemList.setVisibility(View.GONE);
                    llLostItemNoItemAvailable.setVisibility(View.GONE);
                }
            } else
                CommonUtil.conditionAuthentication(this, jsonObject);
        } else
            CommonUtil.jsonNullError(this);
    }
}
