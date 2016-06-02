package com.smartsense.taxinearyou;

import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

public class LostItem extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    ListView lvLostItemList;
    RadioButton rbLostItemOpen, rbLostItemInprogress, rbLostItemClosed;
    LinearLayout llLostItemNoItemAvailable, llLostItemItemsAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvLostItemList = (ListView) findViewById(R.id.lvLostItemList);

        rbLostItemClosed = (RadioButton) findViewById(R.id.rbLostItemClosed);
        rbLostItemOpen = (RadioButton) findViewById(R.id.rbLostItemOpen);
        rbLostItemInprogress = (RadioButton) findViewById(R.id.rbLostItemInprogress);
        llLostItemNoItemAvailable = (LinearLayout) findViewById(R.id.llLostItemNoItemAvailable);
        llLostItemItemsAvailable = (LinearLayout) findViewById(R.id.llLostItemItemsAvailable);

        rbLostItemClosed.setOnClickListener(this);
        rbLostItemOpen.setOnClickListener(this);
        rbLostItemInprogress.setOnClickListener(this);

        lostItem();
    }

    private void lostItem() {
        final String tag = "Lost Item";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.LOST_ITEM + "&json=")
                    .append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                            .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), builder.toString(), tag, this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rbLostItemOpen:
                break;
            case R.id.rbLostItemInprogress:
                break;
            case R.id.rbLostItemClosed:
                break;
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
        llLostItemItemsAvailable.setVisibility(View.GONE);
        llLostItemNoItemAvailable.setVisibility(View.GONE);
        CommonUtil.errorToastShowing(this);
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        CommonUtil.cancelProgressDialog();
        if (jsonObject != null) {

            if (jsonObject.optInt("status") == Constants.STATUS_SUCCESS)

                if (jsonObject.optJSONObject("json").optJSONArray("lostItemInfoArray").length() > 0) {
                    llLostItemItemsAvailable.setVisibility(View.VISIBLE);
                    llLostItemNoItemAvailable.setVisibility(View.GONE);
                    AdapterLostItem adapterLostItem = new AdapterLostItem(this, jsonObject.optJSONObject("json").optJSONArray("lostItemInfoArray"));
                    lvLostItemList.setAdapter(adapterLostItem);
                    CommonUtil.successToastShowing(this, jsonObject);
                } else {
                    llLostItemItemsAvailable.setVisibility(View.GONE);
                    llLostItemNoItemAvailable.setVisibility(View.VISIBLE);
                }
            else
                CommonUtil.conditionAuthentication(this, jsonObject);
        } else
            CommonUtil.jsonNullError(this);
    }
}
