package com.smartsense.taxinearyou;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class ResetPassword extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    EditText etResetPasswordEmailAddress;
    Button btResetPasswordSend;
    TextView tvResetPasswordForgotEmail;
    CoordinatorLayout clResetPassword;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvResetPasswordForgotEmail = (TextView) findViewById(R.id.tvResetPasswordForgotEmail);
        btResetPasswordSend = (Button) findViewById(R.id.btResetPasswordSend);
        etResetPasswordEmailAddress = (EditText) findViewById(R.id.etResetPasswordEmailAddress);
        clResetPassword = (CoordinatorLayout) findViewById(R.id.clResetPassword);

        btResetPasswordSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etResetPasswordEmailAddress.getText().toString()))
                    CommonUtil.showSnackBar(ResetPassword.this, getString(R.string.enter_email_id), clResetPassword);
                else if (!CommonUtil.isValidEmail(etResetPasswordEmailAddress.getText().toString()))
                    CommonUtil.showSnackBar(ResetPassword.this, getString(R.string.enter_valid_email_id), clResetPassword);
                else
                    doResetPass();
            }
        });
        tvResetPasswordForgotEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPassword.this, RecoverEmailAddress.class));
            }
        });

    }

    private void doResetPass() {
        final String tag = "doResetPass";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();
        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.EVENT_FORGOT_PASS + "&json="
                    + jsonData.put("emailId", etResetPasswordEmailAddress.getText().toString().trim()).toString());
        } catch (JSONException e) {
            e.printStackTrace();
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
//                    switch (response.getInt("__eventid")) {
//                        case Constants.Events.EVENT_FORGOT_PASS:
                    openDialog();
// SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONObject("data").getString("userId"));
//                            SharedPreferenceUtil.save();
//                            startActivity(new Intent(this, OTPActivity.class).putExtra("mobile", etMobileNo.getText().toString()).putExtra("code", etCountryCode.getText().toString()).putExtra("tag", (String) etCountryCode.getTag()));
//                            finish();
//                            break;
//                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, this, clResetPassword);
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
            tvPopupLocatedEmail.setText(etResetPasswordEmailAddress.getText().toString());
            TextView tvPopupLocatedDetail = (TextView) dialog.findViewById(R.id.tvPopupLocatedDetail);
            tvPopupLocatedEmail.setText("Please follow the instructions to recover your password.If you can\\'t find the email we have send you in your inbox, check your junk mail");
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
}
