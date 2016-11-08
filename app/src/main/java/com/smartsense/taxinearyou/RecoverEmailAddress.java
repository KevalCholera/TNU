package com.smartsense.taxinearyou;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class RecoverEmailAddress extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {

    RadioButton rbRecoverEmailAlternateEmail, rbRecoverEmailRememberEmail;
    EditText etRecoverEmailAlternateEmail, etRecoverEmailFirstName, etRecoverEmailLastName,
            etRecoverEmailContact, etRecoverEmailAddress;
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
//        etRecoverEmailLastName.setNextFocusForwardId(R.id.etRecoverEmailContact);
        etRecoverEmailAddress = (EditText) findViewById(R.id.etRecoverEmailAddress);
        lyRadioButton1 = (LinearLayout) findViewById(R.id.lyRadioButton1);
        lyRadioButton2 = (LinearLayout) findViewById(R.id.lyRadioButton2);
        btRecoverEmailSubmit = (Button) findViewById(R.id.btRecoverEmailSubmit);
        clRecoverEmail = (CoordinatorLayout) findViewById(R.id.clRecoverEmail);

        etRecoverEmailFirstName.setFilters(new InputFilter[]{CommonUtil.textFilter});
        etRecoverEmailLastName.setFilters(new InputFilter[]{CommonUtil.textFilter});

        rbRecoverEmailAlternateEmail.setOnClickListener(this);
        rbRecoverEmailRememberEmail.setOnClickListener(this);
        btRecoverEmailSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        CommonUtil.closeKeyboard(this);
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
                    if (!CommonUtil.isValidEmail(etRecoverEmailAlternateEmail.getText().toString()))
                        CommonUtil.showSnackBar(getString(R.string.enter_alternate), clRecoverEmail);
                    else
                        doResetEmail();
                } else {
                    if (TextUtils.isEmpty(etRecoverEmailFirstName.getText().toString()))
                        CommonUtil.showSnackBar(getString(R.string.enter_first_name), clRecoverEmail);
                    else if (TextUtils.isEmpty(etRecoverEmailLastName.getText().toString()))
                        CommonUtil.showSnackBar(getString(R.string.enter_last_name), clRecoverEmail);
                    else if (etRecoverEmailContact.length() != 10)
                        CommonUtil.showSnackBar(getString(R.string.enter_con), clRecoverEmail);
                    else if (!CommonUtil.isValidEmail(etRecoverEmailAddress.getText().toString()))
                        CommonUtil.showSnackBar(getString(R.string.enter_email_id), clRecoverEmail);
                    else
                        doResetEmail();
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
                builder.append(jsonData.put("emailId", etRecoverEmailAlternateEmail.getText().toString().trim()).toString());
                CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.EVENT_FORGOT_EMAIL), tag, this, this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                builder.append(jsonData.put("firstName", etRecoverEmailFirstName.getText().toString().trim()).put("lastName", etRecoverEmailLastName.getText().toString().trim()).put("mobileNo", etRecoverEmailContact.getText().toString().trim()).put("emailId", etRecoverEmailAddress.getText().toString().trim()).toString());
                CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.EVENT_FORGOT_EMAIL_WITHOUT), tag, this, this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void openDialog(Boolean check) {

        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View dialog = inflater.inflate(R.layout.dialog_all, null);
            LinearLayout linearLayout;
            Button button1;
            if (check) {
                linearLayout = (LinearLayout) dialog.findViewById(R.id.lyPopUpLocatedAccount);
                TextView tvPopupLocatedEmail = (TextView) dialog.findViewById(R.id.tvPopupLocatedEmail);
                tvPopupLocatedEmail.setText(etRecoverEmailAlternateEmail.getText().toString());
                TextView tvPopupLocatedDetail = (TextView) dialog.findViewById(R.id.tvPopupLocatedDetail);
                tvPopupLocatedDetail.setText("Please follow the instructions to recover your password. If you can not find the email we have sent you, please check your junk mail.");
                button1 = (Button) dialog.findViewById(R.id.btPopupLocatedBack);
            } else {
                button1 = (Button) dialog.findViewById(R.id.btPopupWithOutMail);
                linearLayout = (LinearLayout) dialog.findViewById(R.id.lyPopUpWithOutMail);
            }
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
                CommonUtil.closeKeyboard(this);
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
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (response.getInt("__eventid")) {
                        case Constants.Events.EVENT_FORGOT_EMAIL:
                            openDialog(true);
                            break;
                        case Constants.Events.EVENT_FORGOT_EMAIL_WITHOUT:
                            openDialog(false);
                            break;
                    }
                } else {
                    if (response.getInt("__eventid") == Constants.Events.EVENT_FORGOT_EMAIL) {
                        CommonUtil.showSnackBar(response.optString("msg"), clRecoverEmail);
                    } else
                        CommonUtil.conditionAuthentication(this, response);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            CommonUtil.jsonNullError(this);
    }
}
