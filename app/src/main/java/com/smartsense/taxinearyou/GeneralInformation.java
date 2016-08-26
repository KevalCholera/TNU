package com.smartsense.taxinearyou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.smartsense.taxinearyou.utill.WakeLocker;

import org.json.JSONException;
import org.json.JSONObject;

public class GeneralInformation extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    EditText etGeneralFirstName, etGeneralLastName, etGeneralMobile;
    Button btGeneralUpdateProfile;
    CoordinatorLayout clGeneralInfo;
    int check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_information);
        registerReceiver(tripMessageReceiver, new IntentFilter(String.valueOf(Constants.Events.UPDATE_GENERAL_INFO)));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etGeneralFirstName = (EditText) findViewById(R.id.etGeneralFirstName);
        etGeneralLastName = (EditText) findViewById(R.id.etGeneralLastName);
        etGeneralMobile = (EditText) findViewById(R.id.etGeneralMobile);
        btGeneralUpdateProfile = (Button) findViewById(R.id.btGeneralUpdateProfile);
        clGeneralInfo = (CoordinatorLayout) findViewById(R.id.clGeneralInfo);

//        clGeneralInfo.setFitsSystemWindows(true);

//        clGeneralInfo.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        etGeneralFirstName.setFilters(new InputFilter[]{CommonUtil.textFilter});
        etGeneralLastName.setFilters(new InputFilter[]{CommonUtil.textFilter});

        setDataInActivity();

        etGeneralMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                check = 1;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btGeneralUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.closeKeyboard(GeneralInformation.this);
                if (TextUtils.isEmpty(etGeneralFirstName.getText().toString()) && TextUtils.isEmpty(etGeneralLastName.getText().toString()) && TextUtils.isEmpty(etGeneralMobile.getText().toString()))
                    CommonUtil.showSnackBar(getResources().getString(R.string.enter_fields_below), clGeneralInfo);
                else if (TextUtils.isEmpty(etGeneralFirstName.getText().toString()))
                    CommonUtil.showSnackBar(getString(R.string.enter_first_name), clGeneralInfo);
                else if (TextUtils.isEmpty(etGeneralLastName.getText().toString()))
                    CommonUtil.showSnackBar(getString(R.string.enter_last_name), clGeneralInfo);
                else if (etGeneralMobile.length() != 10)
                    CommonUtil.showSnackBar(getString(R.string.enter_valid_contact), clGeneralInfo);
                else {
                    if (check == 1 && !etGeneralMobile.getText().toString().equalsIgnoreCase(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_MNO, "")))
                        contactAvailability();
                    else
                        generalInfo();
                }
            }
        });
    }

    public void setDataInActivity() {
        etGeneralFirstName.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_FIRST, ""));
        etGeneralLastName.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_LAST, ""));
        etGeneralMobile.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_MNO, ""));
    }

    private void contactAvailability() {
        final String tag = "Contact Availability";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("mobileNo", etGeneralMobile.getText().toString().trim()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.CHECK_MOBILE_AVAILABILITY), tag, this, this);
    }

    private void generalInfo() {
        final String tag = "generalInfo";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("firstName", etGeneralFirstName.getText().toString().trim())
                    .put("lastName", etGeneralLastName.getText().toString().trim())
                    .put("contactNo", etGeneralMobile.getText().toString().trim())
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.upload_info), CommonUtil.utf8Convert(builder, Constants.Events.UPDATE_GENERAL_INFO), tag, this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CommonUtil.closeKeyboard(GeneralInformation.this);
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

                if (jsonObject.optString("__eventid").equalsIgnoreCase(Constants.Events.CHECK_MOBILE_AVAILABILITY + ""))

                    if (!jsonObject.optJSONObject("json").optString("isAvailable").equalsIgnoreCase("1"))
//                        CommonUtil.showSnackBar(jsonObject.optString("msg"), clGeneralInfo);
                        CommonUtil.successToastShowing(this, jsonObject);
                    else
                        generalInfo();

                if (jsonObject.optString("__eventid").equalsIgnoreCase(Constants.Events.UPDATE_GENERAL_INFO + "")) {

                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_FIRST, etGeneralFirstName.getText().toString());
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_LAST, etGeneralLastName.getText().toString());
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_FULLNAME, etGeneralFirstName.getText().toString() + " " + etGeneralLastName.getText().toString());
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_MNO, etGeneralMobile.getText().toString());
                    SharedPreferenceUtil.save();
                    check = 0;
                    CommonUtil.alertBox(this, jsonObject.optString("msg"));

                }

            } else {
//                clGeneralInfo.setFitsSystemWindows(false);
                CommonUtil.conditionAuthentication(this, jsonObject);
            }
        else CommonUtil.jsonNullError(this);
    }

    private final BroadcastReceiver tripMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WakeLocker.acquire(context);
            Log.i("Push ", intent.getStringExtra(Constants.EXTRAS));
            try {
//                JSONObject pushData = new JSONObject(intent.getStringExtra(Constants.EXTRAS));
//                CommonUtil.storeUserData(pushData.optJSONObject("user"));
                setDataInActivity();
            } catch (Exception e) {
                e.printStackTrace();
            }
            WakeLocker.release();
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