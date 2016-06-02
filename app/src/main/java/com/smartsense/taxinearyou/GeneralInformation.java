package com.smartsense.taxinearyou;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class GeneralInformation extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    EditText etGeneralFirstName, etGeneralLastName, etGeneralMobile;
    Button btGeneralUpdateProfile;
    CoordinatorLayout clGeneralInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_information);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etGeneralFirstName = (EditText) findViewById(R.id.etGeneralFirstName);
        etGeneralLastName = (EditText) findViewById(R.id.etGeneralLastName);
        etGeneralMobile = (EditText) findViewById(R.id.etGeneralMobile);
        btGeneralUpdateProfile = (Button) findViewById(R.id.btGeneralUpdateProfile);
        clGeneralInfo = (CoordinatorLayout) findViewById(R.id.clGeneralInfo);

        etGeneralFirstName.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_FIRST, ""));
        etGeneralLastName.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_LAST, ""));
        etGeneralMobile.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_MNO, ""));

        btGeneralUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.closeKeyboard(GeneralInformation.this);
                if (TextUtils.isEmpty(etGeneralFirstName.getText().toString()))
                    CommonUtil.showSnackBar(GeneralInformation.this, getString(R.string.enter_first_name), clGeneralInfo);
                else if (TextUtils.isEmpty(etGeneralLastName.getText().toString()))
                    CommonUtil.showSnackBar(GeneralInformation.this, getString(R.string.enter_last_name), clGeneralInfo);
                else if (TextUtils.isEmpty(etGeneralMobile.getText().toString()))
                    CommonUtil.showSnackBar(GeneralInformation.this, getString(R.string.enter_contact_no), clGeneralInfo);
                else if (etGeneralMobile.length() < 7 || etGeneralMobile.length() > 13)
                    CommonUtil.showSnackBar(GeneralInformation.this, getString(R.string.enter_valid_contact_no), clGeneralInfo);
                else {
                    generalInfo();
                }
            }
        });
        etGeneralMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 7) {
                    contactAvailability();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void contactAvailability() {
        final String tag = "Contact Availability";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.CHECK_MOBILE_AVAILABILITY + "&json=")
                    .append(jsonData.put("mobileNo", etGeneralMobile.getText().toString().trim()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), builder.toString(), tag, this, this);
        CommonUtil.jsonRequestNoProgressBar(builder.toString(), tag, this, this);

    }

    private void generalInfo() {
        final String tag = "generalInfo";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.UPDATE_GENERAL_INFO + "&json=")
                    .append(jsonData.put("firstName", etGeneralFirstName.getText().toString().trim())
                            .put("lastName", etGeneralLastName.getText().toString().trim())
                            .put("contactNo", etGeneralMobile.getText().toString().trim())
                            .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                            .put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.upload_info), builder.toString(), tag, this, this);
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
                CommonUtil.successToastShowing(this, jsonObject);
                SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_FIRST, etGeneralFirstName.getText().toString());
                SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_LAST, etGeneralLastName.getText().toString());
                SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_MNO, etGeneralMobile.getText().toString());
                SharedPreferenceUtil.save();
            } else
                CommonUtil.conditionAuthentication(this, jsonObject);
        else CommonUtil.jsonNullError(this);
    }
}