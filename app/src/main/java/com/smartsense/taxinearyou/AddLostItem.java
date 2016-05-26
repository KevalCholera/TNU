package com.smartsense.taxinearyou;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.DataRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class AddLostItem extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    EditText etYourDetailName, etYourDetailNumber, etYourDetailID, etYourDetailAddress, etYourDetailPostcode, etYourDetailColor, etYourDetailDescription;
    AlertDialog alert;
    Button btYourDetailSubmit;
    CoordinatorLayout clYourDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lost_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etYourDetailName = (EditText) findViewById(R.id.etYourDetailName);
        etYourDetailNumber = (EditText) findViewById(R.id.etYourDetailNumber);
        etYourDetailID = (EditText) findViewById(R.id.etYourDetailID);
        etYourDetailAddress = (EditText) findViewById(R.id.etYourDetailAddress);
        etYourDetailPostcode = (EditText) findViewById(R.id.etYourDetailPostcode);
        etYourDetailColor = (EditText) findViewById(R.id.etYourDetailColor);
        etYourDetailDescription = (EditText) findViewById(R.id.etYourDetailDescription);
        btYourDetailSubmit = (Button) findViewById(R.id.btYourDetailSubmit);
        clYourDetails = (CoordinatorLayout) findViewById(R.id.clYourDetails);

        btYourDetailSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btYourDetailSubmit:
                if (TextUtils.isEmpty(etYourDetailAddress.getText().toString()))
                    CommonUtil.showSnackBar(AddLostItem.this, getString(R.string.enter_item_add), clYourDetails);
                else if (TextUtils.isEmpty(etYourDetailPostcode.getText().toString()))
                    CommonUtil.showSnackBar(AddLostItem.this, getString(R.string.enter_postcode), clYourDetails);
                else if (TextUtils.isEmpty(etYourDetailColor.getText().toString()))
                    CommonUtil.showSnackBar(AddLostItem.this, getString(R.string.enter_color), clYourDetails);
                else if (TextUtils.isEmpty(etYourDetailColor.getText().toString()))
                    CommonUtil.showSnackBar(AddLostItem.this, getString(R.string.enter_color), clYourDetails);
                else if (TextUtils.isEmpty(etYourDetailDescription.getText().toString()))
                    CommonUtil.showSnackBar(AddLostItem.this, getString(R.string.enter_item_dec), clYourDetails);
                else
                    addLostItem();
                break;

        }
    }

    private void addLostItem() {
        final String tag = "Add Lost Item";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.ADD_LOST_ITEM + "&json=")
                    .append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "")
                            + jsonData.put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "").trim())
                            + jsonData.put("rideId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_RIDE_ID, "").trim())
                            + jsonData.put("color", etYourDetailColor.getText().toString().trim())
                            + jsonData.put("pinCode", etYourDetailPostcode.getText().toString().trim())
                            + jsonData.put("itemDescription", etYourDetailDescription.getText().toString().trim())
                            + jsonData.put("address", etYourDetailAddress.getText().toString().trim())));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.showProgressDialog(this, "getting data...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
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
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        CommonUtil.cancelProgressDialog();
        if (jsonObject.length() > 0 && jsonObject.optInt("status") == Constants.STATUS_SUCCESS)
            CommonUtil.successToastShowing(this, jsonObject);
        else
            CommonUtil.successToastShowing(this, jsonObject);

    }
}
