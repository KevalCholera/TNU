package com.smartsense.taxinearyou;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

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
                if (!CommonUtil.isValidEmail(etResetPasswordEmailAddress.getText().toString()))
                    CommonUtil.showSnackBar(getString(R.string.enter_email_id), clResetPassword);
                else {
                    CommonUtil.closeKeyboard(ResetPassword.this);
                    doResetPass();
                }
            }
        });
        tvResetPasswordForgotEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPassword.this, RecoverEmailAddress.class));
                finish();
            }
        });
    }

    private void doResetPass() {
        final String tag = "doResetPass";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();
        try {
            builder.append(jsonData.put("username", etResetPasswordEmailAddress.getText().toString().trim()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.EVENT_FORGOT_PASS), tag, this, this);
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
            tvPopupLocatedDetail.setText("Please follow the instructions to recover your password. If you can not find the email we have sent you, please check your junk mail.");
            tvPopupLocatedEmail.setText(etResetPasswordEmailAddress.getText().toString().trim());
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CommonUtil.closeKeyboard(ResetPassword.this);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.errorToastShowing(this);
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            if (response.optInt("status") == Constants.STATUS_SUCCESS) {
                openDialog();
            } else
                CommonUtil.conditionAuthentication(this, response);
        } else CommonUtil.jsonNullError(this);
    }
}
