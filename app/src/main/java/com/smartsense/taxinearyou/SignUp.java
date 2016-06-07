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
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    EditText etSignUpFirstName, etSignUpLastName, etSignUpContact, etSignUpEmail,
            etSignUpPassword, etSignUpConfirmPassword, etSignUpAlternateEmail;
    CoordinatorLayout clSignUp;
    Button btSignUpSaveNext;
    ImageButton btSignUpBack;
    ImageView ivSignUpAvailableNumber, ivSignUpUnAvailableNumber, ivSignUpAvailableEmail, ivSignUpUnAvailableEmail, ivSignUpUnAvailableAlternateEmail, ivSignUpAvailableAlternateEmail;
    int whichEmail;

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
        ivSignUpUnAvailableEmail = (ImageView) findViewById(R.id.ivSignUpUnAvailableEmail);
        ivSignUpAvailableEmail = (ImageView) findViewById(R.id.ivSignUpAvailableEmail);
        ivSignUpUnAvailableNumber = (ImageView) findViewById(R.id.ivSignUpUnAvailableNumber);
        ivSignUpAvailableNumber = (ImageView) findViewById(R.id.ivSignUpAvailableNumber);
        ivSignUpAvailableAlternateEmail = (ImageView) findViewById(R.id.ivSignUpAvailableAlternateEmail);
        ivSignUpUnAvailableAlternateEmail = (ImageView) findViewById(R.id.ivSignUpUnAvailableAlternateEmail);

        etSignUpContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 10) {
                    whichEmail = 3;
                    contactAvailability();
                } else {
                    ivSignUpAvailableNumber.setVisibility(View.GONE);
                    ivSignUpUnAvailableNumber.setVisibility(View.INVISIBLE);
//                    imageVisibility();
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
                    whichEmail = 1;
                    emailAvailability();
                } else {
                    ivSignUpAvailableEmail.setVisibility(View.GONE);
                    ivSignUpUnAvailableEmail.setVisibility(View.INVISIBLE);
//                    imageVisibility();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etSignUpAlternateEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (CommonUtil.isValidEmail(s)) {
                    whichEmail = 2;
                    emailAvailability();
                } else {
//                    imageVisibility();
                    ivSignUpAvailableAlternateEmail.setVisibility(View.GONE);
                    ivSignUpUnAvailableAlternateEmail.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btSignUpSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitConditions();
            }
        });
        btSignUpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CommonUtil.closeKeyboard(SignUp.this);
            }
        });

        imageVisibility();
    }

    public void submitConditions() {
        if (TextUtils.isEmpty(etSignUpFirstName.getText().toString()) && TextUtils.isEmpty(etSignUpLastName.getText().toString()) && TextUtils.isEmpty(etSignUpContact.getText().toString()) && TextUtils.isEmpty(etSignUpEmail.getText().toString()) && TextUtils.isEmpty(etSignUpAlternateEmail.getText().toString()) && TextUtils.isEmpty(etSignUpPassword.getText().toString()) && TextUtils.isEmpty(etSignUpConfirmPassword.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.enter_fields_below), clSignUp);
        else if (TextUtils.isEmpty(etSignUpFirstName.getText().toString()))
            CommonUtil.showSnackBar(getString(R.string.enter_first_name), clSignUp);
        else if (TextUtils.isEmpty(etSignUpLastName.getText().toString()))
            CommonUtil.showSnackBar(getString(R.string.enter_last_name), clSignUp);
        else if (TextUtils.isEmpty(etSignUpContact.getText().toString()) || etSignUpContact.length() != 10)
            CommonUtil.showSnackBar(getString(R.string.enter_valid_contact), clSignUp);
        else if (ivSignUpUnAvailableNumber.getVisibility() == View.VISIBLE)
            CommonUtil.showSnackBar(getResources().getString(R.string.alred_exist), clSignUp);
        else if (!CommonUtil.isValidEmail(etSignUpEmail.getText().toString()))
            CommonUtil.showSnackBar(getString(R.string.enter_email_id), clSignUp);
        else if (ivSignUpUnAvailableEmail.getVisibility() == View.VISIBLE)
            CommonUtil.showSnackBar(getResources().getString(R.string.email_alred_exist), clSignUp);
        else if (etSignUpPassword.length() < 7 || etSignUpPassword.length() > 15 || !CommonUtil.isLegalPassword(etSignUpPassword.getText().toString()) || CommonUtil.isSpecialChar(etSignUpPassword.getText().toString()))
            CommonUtil.showSnackBar(getString(R.string.enter_valid_pass), clSignUp);
//        else if (!CommonUtil.isLegalPassword(etSignUpPassword.getText().toString()))
//            CommonUtil.showSnackBar(getString(R.string.enter_valid_pass), clSignUp);
//        else if (CommonUtil.isSpecialChar(etSignUpPassword.getText().toString()))
//            CommonUtil.showSnackBar(getString(R.string.enter_valid_pass), clSignUp);
        else if (!etSignUpConfirmPassword.getText().toString().equalsIgnoreCase(etSignUpPassword.getText().toString()))
            CommonUtil.showSnackBar(getString(R.string.conpass_pass_same), clSignUp);
        else if (!CommonUtil.isValidEmail(etSignUpAlternateEmail.getText().toString()))
            CommonUtil.showSnackBar(getString(R.string.enter_alternate_email), clSignUp);
        else if (ivSignUpUnAvailableAlternateEmail.getVisibility() == View.VISIBLE)
            CommonUtil.showSnackBar(getResources().getString(R.string.alt_email_alred_exist), clSignUp);
        else if (TextUtils.equals(etSignUpAlternateEmail.getText().toString(), etSignUpEmail.getText().toString()))
            CommonUtil.showSnackBar(getString(R.string.alternate_email_not_same), clSignUp);
        else
            startActivity(new Intent(SignUp.this, SecurityQuestion.class).putExtra("firstName", etSignUpFirstName.getText().toString().trim()).putExtra("lastName", etSignUpLastName.getText().toString().trim()).putExtra("contactNo", etSignUpContact.getText().toString().trim()).putExtra("emailId", etSignUpEmail.getText().toString().trim()).putExtra("password", etSignUpPassword.getText().toString().trim()).putExtra("confPassword", etSignUpConfirmPassword.getText().toString().trim()).putExtra("altEmailId", etSignUpAlternateEmail.getText().toString().trim()));

    }

    private void contactAvailability() {
        final String tag = "Contact Availability";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("mobileNo", etSignUpContact.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestNoProgressBar(CommonUtil.utf8Convert(builder, Constants.Events.CHECK_MOBILE_AVAILABILITY), tag, this, this);
    }

    public void imageVisibility() {
        ivSignUpAvailableNumber.setVisibility(View.GONE);
        ivSignUpAvailableEmail.setVisibility(View.GONE);
        ivSignUpUnAvailableNumber.setVisibility(View.INVISIBLE);
        ivSignUpUnAvailableEmail.setVisibility(View.INVISIBLE);
        ivSignUpUnAvailableAlternateEmail.setVisibility(View.INVISIBLE);
        ivSignUpAvailableAlternateEmail.setVisibility(View.GONE);
    }

    private void emailAvailability() {
        final String tag = "Email Availability";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("emailId", whichEmail == 1 ? etSignUpEmail.getText().toString() : etSignUpAlternateEmail.getText().toString())
                    .put("type", whichEmail + ""));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        CommonUtil.jsonRequestNoProgressBar(CommonUtil.utf8Convert(builder, Constants.Events.CHECK_EMAIL_AVAILABILITY), tag, this, this);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.errorToastShowing(this);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        if (jsonObject != null) {
            if (jsonObject.optInt("status") == Constants.STATUS_SUCCESS) {
                if (!jsonObject.optJSONObject("json").optString("isAvailable").equalsIgnoreCase("1")) {
                    CommonUtil.showSnackBar(jsonObject.optString("msg"), clSignUp);
                    if (whichEmail == 1) {
                        ivSignUpUnAvailableEmail.setVisibility(View.VISIBLE);
                        ivSignUpAvailableEmail.setVisibility(View.GONE);
                    } else if (whichEmail == 2) {
                        ivSignUpUnAvailableAlternateEmail.setVisibility(View.VISIBLE);
                        ivSignUpAvailableAlternateEmail.setVisibility(View.GONE);
                    } else {
                        ivSignUpUnAvailableNumber.setVisibility(View.VISIBLE);
                        ivSignUpAvailableNumber.setVisibility(View.GONE);
                    }
                } else {
                    if (whichEmail == 1) {
                        ivSignUpUnAvailableEmail.setVisibility(View.GONE);
                        ivSignUpAvailableEmail.setVisibility(View.VISIBLE);
                    } else if (whichEmail == 2) {
                        ivSignUpUnAvailableAlternateEmail.setVisibility(View.GONE);
                        ivSignUpAvailableAlternateEmail.setVisibility(View.VISIBLE);
                    } else {
                        ivSignUpUnAvailableNumber.setVisibility(View.GONE);
                        ivSignUpAvailableNumber.setVisibility(View.VISIBLE);
                    }
                }
            } else
                CommonUtil.conditionAuthentication(this, jsonObject);
        } else
            CommonUtil.jsonNullError(this);
    }
}