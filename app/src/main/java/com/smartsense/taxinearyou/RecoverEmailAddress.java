package com.smartsense.taxinearyou;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.DataRequest;
import com.smartsense.taxinearyou.utill.JsonErrorShow;

import org.json.JSONException;
import org.json.JSONObject;

public class RecoverEmailAddress extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {

    RadioButton rbRecoverEmailAlternateEmail, rbRecoverEmailRememberEmail;
    EditText etRecoverEmailAlternateEmail, etRecoverEmailFirstName, etRecoverEmailLastName, etRecoverEmailContact, etRecoverEmailAddress;
    LinearLayout lyRadioButton1, lyRadioButton2;
    AlertDialog alert;
    Button btRecoverEmailSubmit;
    CoordinatorLayout clRecoverEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_email_address);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rbRecoverEmailAlternateEmail = (RadioButton) findViewById(R.id.rbRecoverEmailAlternateEmail);
        rbRecoverEmailRememberEmail = (RadioButton) findViewById(R.id.rbRecoverEmailRememberEmail);
        etRecoverEmailAlternateEmail = (EditText) findViewById(R.id.etRecoverEmailAlternateEmail);
        etRecoverEmailFirstName = (EditText) findViewById(R.id.etRecoverEmailFirstName);
        etRecoverEmailLastName = (EditText) findViewById(R.id.etRecoverEmailLastName);
        etRecoverEmailContact = (EditText) findViewById(R.id.etRecoverEmailContact);
        etRecoverEmailAddress = (EditText) findViewById(R.id.etRecoverEmailAddress);
        lyRadioButton1 = (LinearLayout) findViewById(R.id.lyRadioButton1);
        lyRadioButton2 = (LinearLayout) findViewById(R.id.lyRadioButton2);
        btRecoverEmailSubmit = (Button) findViewById(R.id.btRecoverEmailSubmit);
        clRecoverEmail = (CoordinatorLayout) findViewById(R.id.clRecoverEmail);

        rbRecoverEmailAlternateEmail.setOnClickListener(this);
        rbRecoverEmailRememberEmail.setOnClickListener(this);
        btRecoverEmailSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rbRecoverEmailAlternateEmail:
                lyRadioButton2.setVisibility(View.GONE);
                lyRadioButton1.setVisibility(View.VISIBLE);
                etRecoverEmailFirstName.setText("");
                etRecoverEmailLastName.setText("");
                etRecoverEmailContact.setText("");
                etRecoverEmailAddress.setText("");

                break;
            case R.id.rbRecoverEmailRememberEmail:
                lyRadioButton1.setVisibility(View.GONE);
                lyRadioButton2.setVisibility(View.VISIBLE);
                etRecoverEmailAlternateEmail.setText("");
                break;
            case R.id.btRecoverEmailSubmit:
                if (rbRecoverEmailAlternateEmail.isChecked()) {
                    if (TextUtils.isEmpty(etRecoverEmailAlternateEmail.getText().toString()))
                        CommonUtil.showSnackBar(RecoverEmailAddress.this, getString(R.string.enter_alternate_email), clRecoverEmail);
                    else if (!CommonUtil.isValidEmail(etRecoverEmailAlternateEmail.getText().toString()))
                        CommonUtil.showSnackBar(RecoverEmailAddress.this, getString(R.string.enter_valid_alternate_email), clRecoverEmail);
                    else
//                        CommonUtil.openDialogs(RecoverEmailAddress.this, "Recover Email", R.id.lyPopUpAfterRecovery, R.id.btPopupThankingRecoveryBack);
                        doResetEmail();
                } else {
                    if (TextUtils.isEmpty(etRecoverEmailFirstName.getText().toString()))
                        CommonUtil.showSnackBar(RecoverEmailAddress.this, getString(R.string.enter_first_name), clRecoverEmail);
                    else if (TextUtils.isEmpty(etRecoverEmailLastName.getText().toString()))
                        CommonUtil.showSnackBar(RecoverEmailAddress.this, getString(R.string.enter_last_name), clRecoverEmail);
                    else if (TextUtils.isEmpty(etRecoverEmailContact.getText().toString()))
                        CommonUtil.showSnackBar(RecoverEmailAddress.this, getString(R.string.enter_contact_no), clRecoverEmail);
                    else if (etRecoverEmailContact.length() < 7 || etRecoverEmailContact.length() > 13)
                        CommonUtil.showSnackBar(RecoverEmailAddress.this, getString(R.string.enter_valid_contact_no), clRecoverEmail);
                    else if (TextUtils.isEmpty(etRecoverEmailAddress.getText().toString()))
                        CommonUtil.showSnackBar(RecoverEmailAddress.this, getString(R.string.enter_email), clRecoverEmail);
                    else if (!CommonUtil.isValidEmail(etRecoverEmailAddress.getText().toString()))
                        CommonUtil.showSnackBar(RecoverEmailAddress.this, getString(R.string.enter_valid_email), clRecoverEmail);
                    else
                        doResetEmail();
//                        CommonUtil.openDialogs(RecoverEmailAddress.this, "Recover Email", R.id.lyPopUpAfterRecovery, R.id.btPopupThankingRecoveryBack);
                }
                break;
        }
    }

    private void doResetEmail() {
        final String tag = "doResetEmail";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();
        if (rbRecoverEmailAlternateEmail.isChecked()) {
            try {
                builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.EVENT_FORGOT_EMAIL + "&json="
                        + jsonData.put("emailId", etRecoverEmailAlternateEmail.getText().toString().trim()).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.EVENT_FORGOT_EMAIL_WITHOUT + "&json="
                        + jsonData.put("firstName", etRecoverEmailFirstName.getText().toString().trim()).put("lastName", etRecoverEmailLastName.getText().toString().trim()).put("mobileNo", etRecoverEmailContact.getText().toString().trim()).put("emailId", etRecoverEmailAddress.getText().toString().trim()).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        CommonUtil.showProgressDialog(this, "Login...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(this, "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (response.getInt("__eventid")) {
                        case Constants.Events.EVENT_FORGOT_EMAIL:
                            openDialog();
//                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONObject("data").getString("userId"));
//                            SharedPreferenceUtil.save();
//                            startActivity(new Intent(this, OTPActivity.class).putExtra("mobile", etMobileNo.getText().toString()).putExtra("code", etCountryCode.getText().toString()).putExtra("tag", (String) etCountryCode.getTag()));
//                            finish();
                            break;
                        case Constants.Events.EVENT_FORGOT_EMAIL_WITHOUT:
                            CommonUtil.openDialogs(RecoverEmailAddress.this, "Recover Email", R.id.lyPopUpAfterRecovery, R.id.btPopupThankingRecoveryBack);
//                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONObject("data").getString("userId"));
//                            SharedPreferenceUtil.save();
//                            startActivity(new Intent(this, OTPActivity.class).putExtra("mobile", etMobileNo.getText().toString()).putExtra("code", etCountryCode.getText().toString()).putExtra("tag", (String) etCountryCode.getTag()));
//                            finish();
                            break;
                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, this, clRecoverEmail);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void openDialog() {

        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View dialog = inflater.inflate(R.layout.dialog_all, null);
            LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.lyPopUpLocatedAccount);
            TextView tvPopupLocatedEmail = (TextView) dialog.findViewById(R.id.tvPopupLocatedEmail);
            tvPopupLocatedEmail.setText(etRecoverEmailAlternateEmail.getText().toString());

            Button button1 = (Button) dialog.findViewById(R.id.btPopupLocatedBack);

            linearLayout.setVisibility(View.VISIBLE);

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    finish();
                }
            });
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(true);
            alert = alertDialogs.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
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
}
