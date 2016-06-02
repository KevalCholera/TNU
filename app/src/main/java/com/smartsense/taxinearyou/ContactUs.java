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

public class ContactUs extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    EditText etContactUsName, etContactUsMobile, etContactUsEmail, etContactUsMessage, etContactUsSubject;
    Button btContactSubmit;
    CoordinatorLayout clContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etContactUsName = (EditText) findViewById(R.id.etContactUsName);
        etContactUsMobile = (EditText) findViewById(R.id.etContactUsMobile);
        etContactUsSubject = (EditText) findViewById(R.id.etContactUsSubject);
        etContactUsEmail = (EditText) findViewById(R.id.etContactUsEmail);
        etContactUsMessage = (EditText) findViewById(R.id.etContactUsMessage);
        btContactSubmit = (Button) findViewById(R.id.btContactSubmit);
        clContactUs = (CoordinatorLayout) findViewById(R.id.clContactUs);
    }

    public void submitContact(View view) {
        if (TextUtils.isEmpty(etContactUsName.getText().toString()))
            CommonUtil.showSnackBar(ContactUs.this, getResources().getString(R.string.enter_first_name), clContactUs);
        else if (TextUtils.isEmpty(etContactUsMobile.getText().toString()))
            CommonUtil.showSnackBar(ContactUs.this, getResources().getString(R.string.enter_contact_no), clContactUs);
        else if (TextUtils.isEmpty(etContactUsEmail.getText().toString()))
            CommonUtil.showSnackBar(ContactUs.this, getResources().getString(R.string.enter_email_id), clContactUs);
        else if (TextUtils.isEmpty(etContactUsMessage.getText().toString()))
            CommonUtil.showSnackBar(ContactUs.this, getResources().getString(R.string.enter_message), clContactUs);
        else
            contactUs();
    }

    private void contactUs() {
        final String tag = "Contact Us";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.CONTACT_US + "&json=")
                    .append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                            .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                            .put("mobileNo", etContactUsMobile.getText().toString().trim())
                            .put("subject", etContactUsSubject.getText().toString().trim())
                            .put("message", etContactUsMessage.getText().toString().trim())
                            .put("emailId", etContactUsEmail.getText().toString().trim())
                            .put("name", etContactUsName.getText().toString().trim()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), builder.toString(), tag, this, this);
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
                if (!jsonObject.optJSONObject("json").optString("isAvailable").equalsIgnoreCase("1"))
                    CommonUtil.showSnackBar(this, jsonObject.optString("msg"), clContactUs);
                else if (jsonObject.optString("__eventId").equalsIgnoreCase(Constants.Events.CONTACT_US + ""))
                    CommonUtil.showSnackBar(this, jsonObject.optString("msg"), clContactUs);
            } else
                CommonUtil.conditionAuthentication(this, jsonObject);
        else
            CommonUtil.jsonNullError(this);
    }
}
