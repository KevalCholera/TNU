package com.smartsense.taxinearyou;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AddLostItem extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    EditText etYourDetailName, etYourDetailNumber, etYourDetailID, etYourDetailAddress, etYourDetailPostcode, etYourDetailColor, etYourDetailDescription;
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

        etYourDetailName.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_FULLNAME, ""));
        etYourDetailNumber.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_MNO, ""));
        etYourDetailID.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_EMAIL, ""));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btYourDetailSubmit:
                CommonUtil.closeKeyboard(this);
                if (TextUtils.isEmpty(etYourDetailAddress.getText().toString()))
                    CommonUtil.showSnackBar(getString(R.string.enter_add), clYourDetails);
                else if (TextUtils.isEmpty(etYourDetailPostcode.getText().toString()))
                    CommonUtil.showSnackBar(getString(R.string.enter_postcode), clYourDetails);
                else if (TextUtils.isEmpty(etYourDetailColor.getText().toString()))
                    CommonUtil.showSnackBar(getString(R.string.enter_color), clYourDetails);
                else if (TextUtils.isEmpty(etYourDetailDescription.getText().toString()))
                    CommonUtil.showSnackBar(getString(R.string.enter_item_dec), clYourDetails);
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
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "").trim())
                    .put("rideId", getIntent().getStringExtra("rideId").trim())
                    .put("color", etYourDetailColor.getText().toString().trim())
                    .put("pinCode", etYourDetailPostcode.getText().toString().trim())
                    .put("itemDescription", etYourDetailDescription.getText().toString().trim())
                    .put("address", etYourDetailAddress.getText().toString().trim()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.send_data), CommonUtil.utf8Convert(builder, Constants.Events.ADD_LOST_ITEM), tag, this, this);
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
        CommonUtil.cancelProgressDialog();
        CommonUtil.errorToastShowing(this);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        CommonUtil.cancelProgressDialog();
        if (jsonObject != null)
            if (jsonObject.optInt("status") == Constants.STATUS_SUCCESS) {
                CommonUtil.alertBox(this, jsonObject.optString("msg"), false, true);
                setResult(RESULT_OK);
            } else {
                CommonUtil.conditionAuthentication(this, jsonObject);
            }
        else
            CommonUtil.jsonNullError(this);
    }
}
