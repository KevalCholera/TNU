package com.smartsense.taxinearyou;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.DataRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountSecurity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    TextView tvAccountSecurityNote, tvAccountSecurityQuestion1, tvAccountSecurityQuestion2, tvPopupSecurityQuestion1, tvPopupSecurityQuestion2;
    CheckBox cbAccountSecurityOrganization, cbAccountSecurityTaxinearu;
    String important, privacy, text;
    private AlertDialog alert;
    View dialog;
    int clicked = 0;

    EditText etAccountSecurityEmail, etAccountSecurityAlternateEmail;

    LinearLayout lyPopUpAlternateEmail, lyPopUpSecurityOptions, lyPopUpPassword, lyPopUpQuestion, lyPopUpQuestionChanges;
    EditText etPopupSecurityAlternateEmail, etPopupSecurityConfirmAlternateEmail, etPopupSecurityEmail,
            etPopupSecurityConfirmEmail, etPopupSecurityPassword, etPopupSecurityConfirmPassword,
            etPopupSecurityQuestionAnswer1, etPopupSecurityQuestionAnswer2, etPopupSecurityQuestionChangeAnswer1,
            etPopupSecurityQuestionChangeAnswer2, etPopupSecurityQuestionChangeQuestion1, etPopupSecurityQuestionChangeQuestion2;
    Button btAccountSecurityChangeEmail, btAccountSecurityChangeAlternateEmail, btAccountSecurityChangePassword, btAccountSecurityChangeQuestion;
    RadioButton rbPopupSecurityOptionsEmail, rbPopupSecurityOptionsAlternateEmail, rbPopupSecurityOptionsQuestions;
    Button btPopupSecurityAlternateEmailSubmit, btPopupSecurityEmailSubmit, btPopupSecurityOptionsSubmit, btPopupSecurityQuestionChangeConfirm,
            btPopupSecurityPasswordSubmit, btPopupSecurityQuestionConfirm;
    CoordinatorLayout clPopUpMain;
    LinearLayout lyPopUpEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_security);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvAccountSecurityNote = (TextView) findViewById(R.id.tvAccountSecurityNote);
        btAccountSecurityChangeEmail = (Button) findViewById(R.id.btAccountSecurityChangeEmail);
        btAccountSecurityChangeAlternateEmail = (Button) findViewById(R.id.btAccountSecurityChangeAlternateEmail);
        tvAccountSecurityQuestion1 = (TextView) findViewById(R.id.tvAccountSecurityQuestion1);
        tvAccountSecurityQuestion2 = (TextView) findViewById(R.id.tvAccountSecurityQuestion2);
        btAccountSecurityChangeQuestion = (Button) findViewById(R.id.btAccountSecurityChangeQuestion);
        btAccountSecurityChangePassword = (Button) findViewById(R.id.btAccountSecurityChangePassword);
        cbAccountSecurityOrganization = (CheckBox) findViewById(R.id.cbAccountSecurityOrganization);
        cbAccountSecurityTaxinearu = (CheckBox) findViewById(R.id.cbAccountSecurityTaxinearu);
        etAccountSecurityEmail = (EditText) findViewById(R.id.etAccountSecurityEmail);
        etAccountSecurityAlternateEmail = (EditText) findViewById(R.id.etAccountSecurityAlternateEmail);

        important = "<font color='#000000'>Important: </font>";
        text = "<font color='#7e7e7e'>Tick the boxes below, to receive offers and information (By Email, Telephone and Text) about products and services from various organization. For more information please view our </font>";
        privacy = "<u><font color='#0000ff'>Privacy Policy</font></u>";

        tvAccountSecurityNote.setText(Html.fromHtml(important + text + privacy));
        btAccountSecurityChangeEmail.setOnClickListener(this);
        btAccountSecurityChangeAlternateEmail.setOnClickListener(this);
        btAccountSecurityChangeQuestion.setOnClickListener(this);
        btAccountSecurityChangePassword.setOnClickListener(this);

        setValue();
    }

    public void openOccasionsPopupOptions() {
        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dialog = inflater.inflate(R.layout.dialog_all, null);
            dialog.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            lyPopUpAlternateEmail = (LinearLayout) dialog.findViewById(R.id.lyPopUpAlternateEmail);
            lyPopUpEmail = (LinearLayout) dialog.findViewById(R.id.lyPopUpEmail);
            lyPopUpSecurityOptions = (LinearLayout) dialog.findViewById(R.id.lyPopUpSecurityOptions);
            lyPopUpPassword = (LinearLayout) dialog.findViewById(R.id.lyPopUpPassword);
            lyPopUpQuestion = (LinearLayout) dialog.findViewById(R.id.lyPopUpQuestion);
            lyPopUpQuestionChanges = (LinearLayout) dialog.findViewById(R.id.lyPopUpQuestionChanges);
            etPopupSecurityAlternateEmail = (EditText) dialog.findViewById(R.id.etPopupSecurityAlternateEmail);
            etPopupSecurityConfirmAlternateEmail = (EditText) dialog.findViewById(R.id.etPopupSecurityConfirmAlternateEmail);
            etPopupSecurityEmail = (EditText) dialog.findViewById(R.id.etPopupSecurityEmail);
            etPopupSecurityPassword = (EditText) dialog.findViewById(R.id.etPopupSecurityPassword);
            etPopupSecurityConfirmPassword = (EditText) dialog.findViewById(R.id.etPopupSecurityConfirmPassword);
            etPopupSecurityConfirmEmail = (EditText) dialog.findViewById(R.id.etPopupSecurityConfirmEmail);
            etPopupSecurityQuestionAnswer1 = (EditText) dialog.findViewById(R.id.etPopupSecurityQuestionAnswer1);
            etPopupSecurityQuestionAnswer2 = (EditText) dialog.findViewById(R.id.etPopupSecurityQuestionAnswer2);
            etPopupSecurityQuestionChangeAnswer1 = (EditText) dialog.findViewById(R.id.etPopupSecurityQuestionChangeAnswer1);
            etPopupSecurityQuestionChangeAnswer2 = (EditText) dialog.findViewById(R.id.etPopupSecurityQuestionChangeAnswer2);
            rbPopupSecurityOptionsEmail = (RadioButton) dialog.findViewById(R.id.rbPopupSecurityOptionsEmail);
            rbPopupSecurityOptionsAlternateEmail = (RadioButton) dialog.findViewById(R.id.rbPopupSecurityOptionsAlternateEmail);
            rbPopupSecurityOptionsQuestions = (RadioButton) dialog.findViewById(R.id.rbPopupSecurityOptionsQuestions);
            btPopupSecurityAlternateEmailSubmit = (Button) dialog.findViewById(R.id.btPopupSecurityAlternateEmailSubmit);
            btPopupSecurityEmailSubmit = (Button) dialog.findViewById(R.id.btPopupSecurityEmailSubmit);
            btPopupSecurityOptionsSubmit = (Button) dialog.findViewById(R.id.btPopupSecurityOptionsSubmit);
            btPopupSecurityQuestionChangeConfirm = (Button) dialog.findViewById(R.id.btPopupSecurityQuestionChangeConfirm);
            btPopupSecurityPasswordSubmit = (Button) dialog.findViewById(R.id.btPopupSecurityPasswordSubmit);
            btPopupSecurityQuestionConfirm = (Button) dialog.findViewById(R.id.btPopupSecurityQuestionConfirm);
            tvPopupSecurityQuestion1 = (TextView) dialog.findViewById(R.id.tvPopupSecurityQuestion1);
            tvPopupSecurityQuestion2 = (TextView) dialog.findViewById(R.id.tvPopupSecurityQuestion2);
            lyPopUpSecurityOptions.setVisibility(View.VISIBLE);
            clPopUpMain = (CoordinatorLayout) dialog.findViewById(R.id.clPopUpMain);
            etPopupSecurityQuestionChangeQuestion1 = (EditText) dialog.findViewById(R.id.etPopupSecurityQuestionChangeQuestion1);
            etPopupSecurityQuestionChangeQuestion2 = (EditText) dialog.findViewById(R.id.etPopupSecurityQuestionChangeQuestion2);

            tvPopupSecurityQuestion1.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_QUESTION1, ""));
            tvPopupSecurityQuestion2.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_QUESTION2, ""));

            if (clicked == 4)
                rbPopupSecurityOptionsQuestions.setVisibility(View.GONE);

            String primaryEmail = "<font color=" + ContextCompat.getColor(this, R.color.dark_gray) + ">" + getResources().getString(R.string.send_email_to_primary_email_address) + "</font>" + "\n" + "<font color=" + ContextCompat.getColor(this, R.color.black) + ">" + "<u>" + (SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_EMAIL, "") + "</u>" + "</font>");
            String alternateEmail = "<font color=" + ContextCompat.getColor(this, R.color.dark_gray) + ">" + getResources().getString(R.string.send_email_to_alternate_email_address) + "</font>" + "\n" + "<font color=" + ContextCompat.getColor(this, R.color.black) + ">" + "<u>" + (SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ALTERNATE_EMAIL, "") + "</u>" + "</font>");

            rbPopupSecurityOptionsEmail.setText(Html.fromHtml(primaryEmail));
            rbPopupSecurityOptionsAlternateEmail.setText(Html.fromHtml(alternateEmail));

            btPopupSecurityOptionsSubmit.setOnClickListener(this);
            etPopupSecurityQuestionChangeQuestion1.setOnClickListener(this);
            etPopupSecurityQuestionChangeQuestion2.setOnClickListener(this);

            alertDialogs.setView(dialog);
            alert = alertDialogs.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btAccountSecurityChangeEmail:
                clicked = 1;
                openOccasionsPopupOptions();
                break;
            case R.id.btAccountSecurityChangeAlternateEmail:
                clicked = 2;
                openOccasionsPopupOptions();
                break;
            case R.id.btAccountSecurityChangePassword:
                clicked = 3;
                openOccasionsPopupOptions();
                break;
            case R.id.btAccountSecurityChangeQuestion:
                clicked = 4;
                openOccasionsPopupOptions();
                break;
            case R.id.etPopupSecurityQuestionChangeQuestion1:
                break;
            case R.id.etPopupSecurityQuestionChangeQuestion2:
                break;
            case R.id.btPopupSecurityQuestionConfirm:
                verifyingAnswers();
                break;
            case R.id.btPopupSecurityOptionsSubmit:
                securityOptionSelection();
                break;
            case R.id.btPopupSecurityEmailSubmit:
                emailChanged();
                break;
            case R.id.btPopupSecurityAlternateEmailSubmit:
                alternateEmailChanged();
                break;
            case R.id.btPopupSecurityQuestionChangeConfirm:
                securityQuestionChanged();
                break;
            case R.id.btPopupSecurityPasswordSubmit:
                passwordChanged();
                break;
        }
    }

    private void changeByLinkAPI() {
        final String tag = "Account Security";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.SEND_UPDATE_EMAIL + "&json=")
                    .append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                            .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                            .put("actionType", rbPopupSecurityOptionsEmail.isChecked() ? 1 : 2)
                            .put("reqType", clicked));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.showProgressDialog(this, "getting data...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }

    private void changeEmailAPI() {
        final String tag = "Account Security";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.UPDATE_EMAIL + "&json=")
                    .append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                            .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                            .put("reqType", clicked)
                            .put("emailId", clicked == 1 ? etPopupSecurityEmail.getText().toString() : etPopupSecurityAlternateEmail.getText().toString())
                            .put("confirmEmailId", clicked == 1 ? etPopupSecurityConfirmEmail.getText().toString() : etPopupSecurityConfirmAlternateEmail.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.showProgressDialog(this, "getting data...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }

    private void changePasswordAPI() {
        final String tag = "Account Security";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.UPDATE_PASSWORD + "&json=")
                    .append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                            .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                            .put("confPassword", etPopupSecurityPassword.getText().toString())
                            .put("newPassword", etPopupSecurityConfirmPassword.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.showProgressDialog(this, "getting data...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }

    public void setValue() {
        etAccountSecurityEmail.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_EMAIL, ""));
        etAccountSecurityAlternateEmail.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ALTERNATE_EMAIL, ""));
        tvAccountSecurityQuestion1.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_QUESTION1, ""));
        tvAccountSecurityQuestion2.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_QUESTION2, ""));
    }

    public void securityQuestionConfirm() {
        lyPopUpQuestion.setVisibility(View.GONE);
        if (clicked == 1) {
            lyPopUpEmail.setVisibility(View.VISIBLE);
            btPopupSecurityEmailSubmit.setOnClickListener(this);
        } else if (clicked == 2) {
            lyPopUpAlternateEmail.setVisibility(View.VISIBLE);
            btPopupSecurityAlternateEmailSubmit.setOnClickListener(this);
        } else if (clicked == 4) {
            lyPopUpQuestionChanges.setVisibility(View.VISIBLE);
            btPopupSecurityQuestionChangeConfirm.setOnClickListener(this);
        } else if (clicked == 3) {
            lyPopUpPassword.setVisibility(View.VISIBLE);
            btPopupSecurityPasswordSubmit.setOnClickListener(this);
        }
    }

    public void securityOptionSelection() {
        if (rbPopupSecurityOptionsEmail.isChecked() || rbPopupSecurityOptionsAlternateEmail.isChecked()) {
            changeByLinkAPI();
        } else {
            lyPopUpSecurityOptions.setVisibility(View.GONE);
            lyPopUpQuestion.setVisibility(View.VISIBLE);
            btPopupSecurityQuestionConfirm.setOnClickListener(this);
        }
    }

    public void emailChanged() {
        if (TextUtils.isEmpty(etPopupSecurityEmail.getText().toString())) {
            CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_email_id), clPopUpMain);
        } else if (CommonUtil.isValidEmail(etPopupSecurityEmail.getText().toString())) {
            CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_valid_email_id), clPopUpMain);
        } else if (TextUtils.isEmpty(etPopupSecurityConfirmEmail.getText().toString())) {
            CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_confirm_email_id), clPopUpMain);
        } else if (!CommonUtil.isValidEmail(etPopupSecurityConfirmEmail.getText().toString())) {
            CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_valid_confirm_email_id), clPopUpMain);
        } else
            changeEmailAPI();
    }

    public void alternateEmailChanged() {
        if (TextUtils.isEmpty(etPopupSecurityAlternateEmail.getText().toString())) {
            CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_alternate_email), clPopUpMain);
        } else if (CommonUtil.isValidEmail(etPopupSecurityAlternateEmail.getText().toString())) {
            CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_valid_alternate_email), clPopUpMain);
        } else if (TextUtils.isEmpty(etPopupSecurityConfirmAlternateEmail.getText().toString())) {
            CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_confirm_alternate_email), clPopUpMain);
        } else if (!CommonUtil.isValidEmail(etPopupSecurityConfirmAlternateEmail.getText().toString())) {
            CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_valid_confirm_alternate_email), clPopUpMain);
        } else
            changeEmailAPI();
    }

    public void securityQuestionChanged() {
        if (TextUtils.isEmpty(etPopupSecurityQuestionChangeAnswer1.getText().toString())) {
            CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_alternate_email), clPopUpMain);
        } else if (TextUtils.isEmpty(etPopupSecurityQuestionChangeAnswer1.getText().toString())) {
            CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_confirm_alternate_email), clPopUpMain);
        } else
            changeByLinkAPI();
    }

    public void passwordChanged() {
        if (TextUtils.isEmpty(etPopupSecurityPassword.getText().toString())) {
            CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_pass), clPopUpMain);
        } else if (etPopupSecurityPassword.length() < 7 || etPopupSecurityPassword.length() > 13) {
            CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_valid_pass), clPopUpMain);
        } else if (TextUtils.isEmpty(etPopupSecurityConfirmPassword.getText().toString())) {
            CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_confirm_pass), clPopUpMain);
        } else if (etPopupSecurityConfirmPassword.length() < 7 || etPopupSecurityConfirmPassword.length() > 13) {
            CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_valid_confirm_pass), clPopUpMain);
        } else
            changePasswordAPI();
    }

    public void verifyingAnswers() {
        if (TextUtils.isEmpty(etPopupSecurityQuestionAnswer1.getText().toString()))
            CommonUtil.showSnackBar(this, getResources().getString(R.string.please_enter_answer), clPopUpMain);
        else if (TextUtils.isEmpty(etPopupSecurityQuestionAnswer2.getText().toString()))
            CommonUtil.showSnackBar(this, getResources().getString(R.string.please_enter_answer), clPopUpMain);
        else if (!etPopupSecurityQuestionAnswer1.getText().toString().equalsIgnoreCase(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ANSWER1, "")))
            CommonUtil.showSnackBar(this, getResources().getString(R.string.answer_not_same), clPopUpMain);
        else if (!etPopupSecurityQuestionAnswer2.getText().toString().equalsIgnoreCase(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ANSWER2, "")))
            CommonUtil.showSnackBar(this, getResources().getString(R.string.answer_not_same), clPopUpMain);
        else
            securityQuestionConfirm();
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
        alert.dismiss();
        CommonUtil.cancelProgressDialog();
        CommonUtil.errorToastShowing(this);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        CommonUtil.cancelProgressDialog();
        if (jsonObject.length() > 0 && jsonObject.optInt("status") == Constants.STATUS_SUCCESS) {
            alert.dismiss();
            CommonUtil.successToastShowing(this, jsonObject);

            if (jsonObject.optString("__eventId").equalsIgnoreCase((Constants.Events.UPDATE_EMAIL) + "")) {

                if (clicked == 1)
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_EMAIL, etPopupSecurityEmail.getText().toString());
                else if (clicked == 2)
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ALTERNATE_EMAIL, etPopupSecurityAlternateEmail.getText().toString());
            }
            SharedPreferenceUtil.save();
            setValue();

        } else
            CommonUtil.successToastShowing(this, jsonObject);
    }
}
