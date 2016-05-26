package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.DataRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    EditText etSignUpFirstName, etSignUpLastName, etSignUpContact, etSignUpEmail,
            etSignUpPassword, etSignUpConfirmPassword, etSignUpAlternateEmail;
    CoordinatorLayout clSignUp;
    Button btSignUpSaveNext;
    ImageButton btSignUpBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etSignUpFirstName = (EditText) findViewById(R.id.etSignUpFirstName);
        etSignUpLastName = (EditText) findViewById(R.id.etSignUpLastName);
        etSignUpContact = (EditText) findViewById(R.id.etSignUpContact);
        etSignUpEmail = (EditText) findViewById(R.id.etSignUpEmail);
        etSignUpPassword = (EditText) findViewById(R.id.etSignUpPassword);
        etSignUpConfirmPassword = (EditText) findViewById(R.id.etSignUpConfirmPassword);
        etSignUpAlternateEmail = (EditText) findViewById(R.id.etSignUpAlternateEmail);
        btSignUpSaveNext = (Button) findViewById(R.id.btSignUpSaveNext);
        clSignUp = (CoordinatorLayout) findViewById(R.id.clSignUp);
        btSignUpBack = (ImageButton) findViewById(R.id.btSignUpBack);

        etSignUpContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    contactAvailability();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etSignUpEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (CommonUtil.isValidEmail(s)) {
                    emailAvailability();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btSignUpSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etSignUpFirstName.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_first_name), clSignUp);
                else if (TextUtils.isEmpty(etSignUpLastName.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_last_name), clSignUp);
                else if (TextUtils.isEmpty(etSignUpContact.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_contact_no), clSignUp);
                else if (etSignUpContact.length() < 7 || etSignUpContact.length() > 13)
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_valid_contact_no), clSignUp);
                else if (TextUtils.isEmpty(etSignUpEmail.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_email_id), clSignUp);
                else if (!CommonUtil.isValidEmail(etSignUpEmail.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_valid_email_id), clSignUp);
                else if (TextUtils.isEmpty(etSignUpAlternateEmail.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_alternate_email), clSignUp);
                else if (!CommonUtil.isValidEmail(etSignUpAlternateEmail.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_valid_alternate_email), clSignUp);
                else if (TextUtils.equals(etSignUpAlternateEmail.getText().toString(), etSignUpEmail.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.alternate_email_not_same), clSignUp);
                else if (TextUtils.isEmpty(etSignUpPassword.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_pass), clSignUp);
                else if (etSignUpPassword.length() < 7 || etSignUpPassword.length() > 15)
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_valid_pass), clSignUp);
                else if (TextUtils.isEmpty(etSignUpConfirmPassword.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_confirm_pass), clSignUp);
                else if (etSignUpConfirmPassword.length() < 7 || etSignUpPassword.length() > 15)
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.enter_valid_confirm_pass), clSignUp);
                else if (!TextUtils.equals(etSignUpConfirmPassword.getText().toString(), etSignUpPassword.getText().toString()))
                    CommonUtil.showSnackBar(SignUp.this, getString(R.string.conpass_pass_same), clSignUp);
                else
                    startActivity(new Intent(SignUp.this, SecurityQuestion.class).putExtra("firstName", etSignUpFirstName.getText().toString().trim()).putExtra("lastName", etSignUpLastName.getText().toString().trim()).putExtra("contactNo", etSignUpContact.getText().toString().trim()).putExtra("emailId", etSignUpEmail.getText().toString().trim()).putExtra("password", etSignUpPassword.getText().toString().trim()).putExtra("confPassword", etSignUpConfirmPassword.getText().toString().trim()).putExtra("altEmailId", etSignUpAlternateEmail.getText().toString().trim()));
            }
        });
        btSignUpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CommonUtil.closeKeyboard(SignUp.this);
            }
        });
    }

    private void contactAvailability() {
        final String tag = "Contact Availability";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.CHECK_MOBILE_AVAILABILITY + "&json=").append(jsonData.put("mobile", etSignUpContact.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }

    private void emailAvailability() {
        final String tag = "Email Availability";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.CHECK_EMAIL_AVAILABILITY + "&json=").append(jsonData.put("mobile", etSignUpContact.getText().toString()).put("type", "1"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.errorToastShowing(this);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {

        if (jsonObject != null && jsonObject.length() > 0 && jsonObject.optInt("status") == Constants.STATUS_SUCCESS && !jsonObject.optJSONObject("json").optString("isAvailable").equalsIgnoreCase("1"))
            CommonUtil.showSnackBar(this, jsonObject.optString("msg"), clSignUp);
    }
}
