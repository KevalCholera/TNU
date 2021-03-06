package com.smartsense.taxinearyou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.onesignal.OneSignal;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.WakeLocker;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountSecurity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {

    TextView tvAccountSecurityNote, tvAccountSecurityQuestion1, tvAccountSecurityQuestion2, tvPopupSecurityQuestion1,
            tvPopupSecurityQuestion2;

    CheckBox cbAccountSecurityOrganization, cbAccountSecurityTaxinearu;
    String important, privacy, text;
    private AlertDialog alert;
    AlertDialog.Builder alertDialogs;
    View dialog;
    int clicked = 0;
    EditText etAccountSecurityEmail, etAccountSecurityAlternateEmail;
    LinearLayout lyPopUpAlternateEmail, lyPopUpSecurityOptions, lyPopUpPassword, lyPopUpQuestion, lyPopUpQuestionChanges;

    EditText etPopupSecurityAlternateEmail, etPopupSecurityConfirmAlternateEmail, etPopupSecurityEmail,
            etPopupSecurityConfirmEmail, etPopupSecurityPassword, etPopupSecurityConfirmPassword,
            etPopupSecurityQuestionAnswer1, etPopupSecurityQuestionAnswer2, etPopupSecurityQuestionChangeAnswer1,
            etPopupSecurityQuestionChangeAnswer2, etPopupSecurityQuestionChangeQuestion1, etPopupSecurityQuestionChangeQuestion2;

    Button btAccountSecurityChangeEmail, btAccountSecurityUpdate, btAccountSecurityChangeAlternateEmail,
            btAccountSecurityChangePassword, btAccountSecurityChangeQuestion, btAccountSecurityRemoveAlternateEmail;

    RadioButton rbPopupSecurityOptionsEmail, rbPopupSecurityOptionsAlternateEmail, rbPopupSecurityOptionsQuestions;

    Button btPopupSecurityAlternateEmailSubmit, btPopupSecurityEmailSubmit, btPopupSecurityOptionsSubmit,
            btPopupSecurityQuestionChangeConfirm, btPopupSecurityPasswordSubmit, btPopupSecurityQuestionConfirm;

    CoordinatorLayout clPopUpMain;
    LinearLayout lyPopUpEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_security);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerReceiver(tripMessageReceiver, new IntentFilter(String.valueOf(Constants.Events.UPDATE_PROMOTIONS)));
        registerReceiver(tripMessageReceiver, new IntentFilter(String.valueOf(Constants.Events.UPDATE_EMAIL)));
        registerReceiver(tripMessageReceiver, new IntentFilter(String.valueOf(Constants.Events.UPDATE_GENERAL_INFO)));

        tvAccountSecurityNote = (TextView) findViewById(R.id.tvAccountSecurityNote);
        btAccountSecurityChangeEmail = (Button) findViewById(R.id.btAccountSecurityChangeEmail);
        btAccountSecurityChangeAlternateEmail = (Button) findViewById(R.id.btAccountSecurityChangeAlternateEmail);
        tvAccountSecurityQuestion1 = (TextView) findViewById(R.id.tvAccountSecurityQuestion1);
        tvAccountSecurityQuestion2 = (TextView) findViewById(R.id.tvAccountSecurityQuestion2);
        btAccountSecurityChangeQuestion = (Button) findViewById(R.id.btAccountSecurityChangeQuestion);
        btAccountSecurityChangePassword = (Button) findViewById(R.id.btAccountSecurityChangePassword);
        btAccountSecurityUpdate = (Button) findViewById(R.id.btAccountSecurityUpdate);
        btAccountSecurityRemoveAlternateEmail = (Button) findViewById(R.id.btAccountSecurityRemoveAlternateEmail);
        cbAccountSecurityOrganization = (CheckBox) findViewById(R.id.cbAccountSecurityOrganization);
        cbAccountSecurityTaxinearu = (CheckBox) findViewById(R.id.cbAccountSecurityTaxinearu);
        etAccountSecurityEmail = (EditText) findViewById(R.id.etAccountSecurityEmail);
        etAccountSecurityAlternateEmail = (EditText) findViewById(R.id.etAccountSecurityAlternateEmail);

        important = "<font color='" + ContextCompat.getColor(this, R.color.dark_gray) + "'><b>Important: </b></font>";
        text = "<font color='" + ContextCompat.getColor(this, R.color.text_color) + "'>Tick the boxes below, " +
                "to receive offers and information (by email, phone and text) about product and services from " +
                "various organizations. For more information please view our </font>";

        privacy = "<font color='" + ContextCompat.getColor(this, R.color.purple) + "'>Privacy policy</font>.";

        btAccountSecurityChangeEmail.setOnClickListener(this);
        btAccountSecurityChangeAlternateEmail.setOnClickListener(this);
        btAccountSecurityChangeQuestion.setOnClickListener(this);
        btAccountSecurityChangePassword.setOnClickListener(this);
        btAccountSecurityUpdate.setOnClickListener(this);
        btAccountSecurityRemoveAlternateEmail.setOnClickListener(this);

        if (TextUtils.isEmpty(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ALTERNATE_EMAIL, ""))) {
            btAccountSecurityChangeAlternateEmail.setText("Add Alternate Email Address"); // Add Alternative Email Change
            btAccountSecurityRemoveAlternateEmail.setVisibility(View.GONE);
        } else {
            btAccountSecurityChangeAlternateEmail.setText("Change Alternate Email Address");
            btAccountSecurityRemoveAlternateEmail.setVisibility(View.VISIBLE);
        }

        Spannable span = Spannable.Factory.getInstance().newSpannable(Html.fromHtml(important + text + privacy));
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountSecurity.this, AboutUs.class).putExtra("privacy", "privacy"));
            }
        }, 186, 201, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvAccountSecurityNote.setText(span);
        tvAccountSecurityNote.setMovementMethod(LinkMovementMethod.getInstance());

        setValue();
    }

    public void openOccasionsPopupOptions() {
        try {
            alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dialog = inflater.inflate(R.layout.dialog_all, null);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                dialog.setBackground(new ColorDrawable(Color.TRANSPARENT));
            } else {
                dialog.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

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
            clPopUpMain = (CoordinatorLayout) dialog.findViewById(R.id.clPopUpMain);
            etPopupSecurityQuestionChangeQuestion1 = (EditText) dialog.findViewById(R.id.etPopupSecurityQuestionChangeQuestion1);
            etPopupSecurityQuestionChangeQuestion2 = (EditText) dialog.findViewById(R.id.etPopupSecurityQuestionChangeQuestion2);

            lyPopUpSecurityOptions.setVisibility(View.VISIBLE);
            tvPopupSecurityQuestion1.setText(dialog.getResources().getString(R.string.q1) + " " + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_QUESTION1, ""));
            tvPopupSecurityQuestion2.setText(dialog.getResources().getString(R.string.q2) + " " + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_QUESTION2, ""));

            if (clicked == Constants.AccountSecurity.SECURITY_QUESTION)
                rbPopupSecurityOptionsQuestions.setVisibility(View.GONE);

            rbPopupSecurityOptionsEmail.setText(getResources().getString(R.string.send_email_to_primary_email_address) + "\n" + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_EMAIL, ""));

            if (TextUtils.isEmpty(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ALTERNATE_EMAIL, "")))
                rbPopupSecurityOptionsAlternateEmail.setVisibility(View.GONE);

            rbPopupSecurityOptionsAlternateEmail.setText(getResources().getString(R.string.send_email_to_alternate_email_address) + "\n" + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ALTERNATE_EMAIL, ""));

            btPopupSecurityOptionsSubmit.setOnClickListener(this);
            etPopupSecurityQuestionChangeQuestion1.setOnClickListener(this);
            etPopupSecurityQuestionChangeQuestion2.setOnClickListener(this);

            alertDialogs.setView(dialog);
            alert = alertDialogs.create();
            alert.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btAccountSecurityChangeEmail:
                clicked = Constants.AccountSecurity.CHANGE_EMAIL;
                openOccasionsPopupOptions();
                break;
            case R.id.btAccountSecurityChangeAlternateEmail:
                clicked = Constants.AccountSecurity.CHANGE_ALTERNATE_EMAIL;
                openOccasionsPopupOptions();
                break;
            case R.id.btAccountSecurityChangePassword:
                clicked = Constants.AccountSecurity.CHANGE_PASSWORD;
                openOccasionsPopupOptions();
                break;
            case R.id.btAccountSecurityChangeQuestion:
                clicked = Constants.AccountSecurity.SECURITY_QUESTION;
                openOccasionsPopupOptions();
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
            case R.id.btPopupSecurityPasswordSubmit:
                passwordChanged();
                break;
            case R.id.btAccountSecurityUpdate:
                updatePromotions();
                break;
            case R.id.btAccountSecurityRemoveAlternateEmail:
                dialogRemoveAlternativeEmail();
                break;
        }
    }

    public void dialogRemoveAlternativeEmail() {
        try {

            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialog = inflater.inflate(R.layout.dialog_all, null);
            LinearLayout lyPopUpCancelRide = (LinearLayout) dialog.findViewById(R.id.lyPopUpCancelRide);
            lyPopUpCancelRide.setVisibility(View.VISIBLE);

            TextView tvPopupCancelRideOk, tvPopupCancelRideCancel, tvDialogCancelText, tvDialogCancelHeading;

            tvDialogCancelHeading = (TextView) dialog.findViewById(R.id.tvDialogCancelHeading);
            tvPopupCancelRideOk = (TextView) dialog.findViewById(R.id.tvPopupCancelRideOk);
            tvPopupCancelRideCancel = (TextView) dialog.findViewById(R.id.tvPopupCancelRideCancel);
            tvDialogCancelText = (TextView) dialog.findViewById(R.id.tvDialogCancelText);

            tvDialogCancelText.setText("Sure to remove alternative email?");
            tvDialogCancelHeading.setText("Remove Alternative Email?");

            tvPopupCancelRideOk.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    removeAlternativeEmail();

                }
            });
            tvPopupCancelRideCancel.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(false);
            alert = alertDialogs.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeAlternativeEmail() {
        final String tag = "Account Security";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.REMOVE_ALTERNATIVE_EMAIL), tag, this, this);

    }

    private void changeByLinkAPI() {
        final String tag = "Account Security";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("actionType", rbPopupSecurityOptionsEmail.isChecked() ? 1 : 2)
                    .put("reqType", clicked));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.SEND_UPDATE_EMAIL), tag, this, this);
    }

    private void updatePromotions() {
        final String tag = "Account Security";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("promotionStatus", cbAccountSecurityTaxinearu.isChecked() ? 1 : 0)
                    .put("thirdPartyPomotionStatus", cbAccountSecurityOrganization.isChecked() ? 1 : 0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.UPDATE_PROMOTIONS), tag, this, this);
    }

    private void changeEmailAPI() {
        final String tag = "Account Security";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("reqType", clicked)
                    .put("emailId", clicked == 1 ? etPopupSecurityEmail.getText().toString() : etPopupSecurityAlternateEmail.getText().toString())
                    .put("confirmEmailId", clicked == 1 ? etPopupSecurityConfirmEmail.getText().toString() : etPopupSecurityConfirmAlternateEmail.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.UPDATE_EMAIL), tag, this, this);
    }

    private void changePasswordAPI() {
        final String tag = "Account Security";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("confPassword", etPopupSecurityPassword.getText().toString())
                    .put("newPassword", etPopupSecurityConfirmPassword.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.UPDATE_PASSWORD), tag, this, this);
    }

    public void setValue() {

        if (!TextUtils.isEmpty(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ALTERNATE_EMAIL, "")))
            btAccountSecurityRemoveAlternateEmail.setVisibility(View.VISIBLE);
        else
            btAccountSecurityRemoveAlternateEmail.setVisibility(View.GONE);

        etAccountSecurityEmail.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_EMAIL, ""));

        if (!TextUtils.isEmpty(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ALTERNATE_EMAIL, "")))
            etAccountSecurityAlternateEmail.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ALTERNATE_EMAIL, ""));
        else
            etAccountSecurityAlternateEmail.setText("--");

        tvAccountSecurityQuestion1.setText("Q1)  " + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_QUESTION1, ""));
        tvAccountSecurityQuestion2.setText("Q2)  " + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_QUESTION2, ""));
        cbAccountSecurityOrganization.setChecked(SharedPreferenceUtil.getString(Constants.PrefKeys.RECEIVE_ORG_OFFERS, "0").equalsIgnoreCase("1") ? true : false);
        cbAccountSecurityTaxinearu.setChecked(SharedPreferenceUtil.getString(Constants.PrefKeys.RECEIVE_TNU_OFFERS, "0").equalsIgnoreCase("1") ? true : false);
    }

    public void securityQuestionConfirm() {
        alert.dismiss();
        lyPopUpQuestion.setVisibility(View.GONE);
        if (clicked == Constants.AccountSecurity.CHANGE_EMAIL) {
            CommonUtil.handlerDialog(alert, lyPopUpEmail);
            btPopupSecurityEmailSubmit.setOnClickListener(AccountSecurity.this);
        } else if (clicked == Constants.AccountSecurity.CHANGE_ALTERNATE_EMAIL) {
            CommonUtil.handlerDialog(alert, lyPopUpAlternateEmail);
            btPopupSecurityAlternateEmailSubmit.setOnClickListener(AccountSecurity.this);
        } else if (clicked == Constants.AccountSecurity.SECURITY_QUESTION) {
            CommonUtil.handlerDialog(alert, lyPopUpQuestionChanges);
            btPopupSecurityQuestionChangeConfirm.setOnClickListener(AccountSecurity.this);
        } else if (clicked == Constants.AccountSecurity.CHANGE_PASSWORD) {
            CommonUtil.handlerDialog(alert, lyPopUpPassword);
            btPopupSecurityPasswordSubmit.setOnClickListener(AccountSecurity.this);
        }
    }

    public void securityOptionSelection() {
        if (rbPopupSecurityOptionsEmail.isChecked() || rbPopupSecurityOptionsAlternateEmail.isChecked()) {
            changeByLinkAPI();
        } else {
            alert.dismiss();
            lyPopUpSecurityOptions.setVisibility(View.GONE);
            CommonUtil.handlerDialog(alert, lyPopUpQuestion);
            btPopupSecurityQuestionConfirm.setOnClickListener(this);
        }
    }

    public void emailChanged() {
        if (etPopupSecurityEmail.getText().toString().equalsIgnoreCase("") || etPopupSecurityConfirmEmail.getText().toString().equalsIgnoreCase(""))
            CommonUtil.showSnackBar(getString(R.string.enter_fields_below), clPopUpMain);
        else if (!CommonUtil.isValidEmail(etPopupSecurityEmail.getText().toString()) || !CommonUtil.isValidEmail(etPopupSecurityConfirmEmail.getText().toString()))
            CommonUtil.showSnackBar(getString(R.string.pls_email), clPopUpMain);
        else if (!etPopupSecurityEmail.getText().toString().equalsIgnoreCase(etPopupSecurityConfirmEmail.getText().toString()))
            CommonUtil.showSnackBar(getString(R.string.enter_not_same), clPopUpMain);
        else
            changeEmailAPI();
    }

    public void alternateEmailChanged() {
        if (etPopupSecurityAlternateEmail.getText().toString().equalsIgnoreCase("") || etPopupSecurityAlternateEmail.getText().toString().equalsIgnoreCase(""))
            CommonUtil.showSnackBar(getString(R.string.enter_fields_below), clPopUpMain);
        else if (!CommonUtil.isValidEmail(etPopupSecurityAlternateEmail.getText().toString()) || !CommonUtil.isValidEmail(etPopupSecurityConfirmAlternateEmail.getText().toString())) {
            CommonUtil.showSnackBar(getString(R.string.pls_ent_alt), clPopUpMain);
        } else if (!etPopupSecurityAlternateEmail.getText().toString().equalsIgnoreCase(etPopupSecurityConfirmAlternateEmail.getText().toString())) {
            CommonUtil.showSnackBar(getString(R.string.enter_alt_not_same), clPopUpMain);
        } else
            changeEmailAPI();
    }

    public void passwordChanged() {
        if ((etPopupSecurityPassword.length() < 7 || etPopupSecurityPassword.length() > 15) || !CommonUtil.isLegalPassword(etPopupSecurityPassword.getText().toString()) || CommonUtil.isSpecialChar(etPopupSecurityPassword.getText().toString()))
            CommonUtil.showSnackBar(getString(R.string.enter_valid_pass), clPopUpMain);
        else if (!etPopupSecurityConfirmPassword.getText().toString().equals(etPopupSecurityPassword.getText().toString()))
            CommonUtil.showSnackBar(getString(R.string.conpass_pass_same), clPopUpMain);
        else
            changePasswordAPI();
    }

    public void verifyingAnswers() {

        if (TextUtils.isEmpty(etPopupSecurityQuestionAnswer1.getText().toString()) || TextUtils.isEmpty(etPopupSecurityQuestionAnswer2.getText().toString()))
            CommonUtil.showSnackBar(getResources().getString(R.string.enter_fields_below), clPopUpMain);
        else if (!etPopupSecurityQuestionAnswer1.getText().toString().equalsIgnoreCase(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ANSWER1, "")))
            CommonUtil.showSnackBar(getResources().getString(R.string.answer_not_same), clPopUpMain);
        else if (!etPopupSecurityQuestionAnswer2.getText().toString().equalsIgnoreCase(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ANSWER2, "")))
            CommonUtil.showSnackBar(getResources().getString(R.string.answer_not_same), clPopUpMain);
        else {
            securityQuestionConfirm();
        }
    }

    public void alertBox(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(msg);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(AccountSecurity.this, SignIn.class));
                SharedPreferenceUtil.clear();
                SharedPreferenceUtil.save();
                finish();
                OneSignal.sendTag("emailId", "");
            }
        });
        builder.create();
        builder.show();
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
        CommonUtil.closeKeyboard(this);
        if (jsonObject != null) {
            if (jsonObject.optInt("status") == Constants.STATUS_SUCCESS) {
                if (alert != null)
                    alert.dismiss();

                if (clicked == Constants.AccountSecurity.CHANGE_EMAIL && jsonObject.optInt("__eventid") == Constants.Events.UPDATE_EMAIL)
                    alertBox(jsonObject.optString("msg"));
                else if (jsonObject.optInt("__eventid") == Constants.Events.REMOVE_ALTERNATIVE_EMAIL) {
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ALTERNATE_EMAIL, "");
                    SharedPreferenceUtil.save();
                    setValue();
                    CommonUtil.alertBox(this, jsonObject.optString("msg"));
                } else if (clicked == Constants.AccountSecurity.CHANGE_ALTERNATE_EMAIL && !TextUtils.isEmpty(etPopupSecurityAlternateEmail.getText().toString())) {
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ALTERNATE_EMAIL, etPopupSecurityAlternateEmail.getText().toString());
                    SharedPreferenceUtil.save();
                    setValue();
                    CommonUtil.alertBox(this, jsonObject.optString("msg"));
                } else if (jsonObject.optInt("__eventid") == Constants.Events.UPDATE_PROMOTIONS) {
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.RECEIVE_ORG_OFFERS, cbAccountSecurityOrganization.isChecked() ? "1" : "0");
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.RECEIVE_TNU_OFFERS, cbAccountSecurityTaxinearu.isChecked() ? "1" : "0");
                    SharedPreferenceUtil.save();
                    CommonUtil.alertBox(this, jsonObject.optString("msg"));
                } else
                    CommonUtil.alertBox(this, jsonObject.optString("msg"));

                if (TextUtils.isEmpty(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ALTERNATE_EMAIL, ""))) {
                    btAccountSecurityChangeAlternateEmail.setText("Add Alternate Email Address"); // Add Alternative Email Change
                    btAccountSecurityRemoveAlternateEmail.setVisibility(View.GONE);
                } else {
                    btAccountSecurityChangeAlternateEmail.setText("Change Alternate Email Address");
                    btAccountSecurityRemoveAlternateEmail.setVisibility(View.VISIBLE);
                }

            } else
                CommonUtil.conditionAuthentication(this, jsonObject);
        } else
            CommonUtil.jsonNullError(this);
    }

    private final BroadcastReceiver tripMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WakeLocker.acquire(context);
            Log.i("Push ", intent.getStringExtra(Constants.EXTRAS));
            try {
//                JSONObject pushData = new JSONObject(intent.getStringExtra(Constants.EXTRAS));
//                CommonUtil.storeUserData(pushData.optJSONObject("user"));
                setValue();
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
