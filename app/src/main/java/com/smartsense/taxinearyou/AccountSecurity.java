package com.smartsense.taxinearyou;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.smartsense.taxinearyou.utill.CommonUtil;

public class AccountSecurity extends AppCompatActivity implements View.OnClickListener {

    TextView tvAccountSecurityNote, tvAccountSecurityQuestion1, tvAccountSecurityQuestion2;
    CheckBox cbAccountSecurityOrganization, cbAccountSecurityTaxinearu;
    String important, privacy, text;
    private AlertDialog alert;
    View dialog;
    int clicked = 0;

    LinearLayout lyPopUpAlternateEmail, lyPopUpSecurityOptions, lyPopUpPassword, lyPopUpQuestion, lyPopUpQuestionChanges;
    EditText etPopupSecurityAlternateEmail, etPopupSecurityConfirmAlternateEmail, etPopupSecurityEmail,
            etPopupSecurityConfirmEmail, etPopupSecurityPassword, etPopupSecurityConfirmPassword,
            etPopupSecurityQuestionAnswer1, etPopupSecurityQuestionAnswer2, etPopupSecurityQuestionChangeAnswer1,
            etPopupSecurityQuestionChangeAnswer2;
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

        important = "<font color='#000000'>Important: </font>";
        text = "<font color='#7e7e7e'>Tick the boxes below, to receive offers and information (By Email, Telephone and Text) about products and services from various organization. For more information please view our </font>";
        privacy = "<u><font color='#0000ff'>Privacy Policy</font></u>";

        tvAccountSecurityNote.setText(Html.fromHtml(important + text + privacy));
        btAccountSecurityChangeEmail.setOnClickListener(this);
        btAccountSecurityChangeAlternateEmail.setOnClickListener(this);
        btAccountSecurityChangeQuestion.setOnClickListener(this);
        btAccountSecurityChangePassword.setOnClickListener(this);

    }

    public void openOccasionsPopupOptions() {

        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dialog = inflater.inflate(R.layout.pop_up_all, null);
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
            lyPopUpSecurityOptions.setVisibility(View.VISIBLE);
            clPopUpMain = (CoordinatorLayout) dialog.findViewById(R.id.clPopUpMain);

            btPopupSecurityOptionsSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (rbPopupSecurityOptionsEmail.isChecked()) {
                        Toast.makeText(AccountSecurity.this, "Link has been sent to your Email ID...",
                                Toast.LENGTH_SHORT).show();
                        alert.dismiss();
                    } else if (rbPopupSecurityOptionsAlternateEmail.isChecked()) {
                        Toast.makeText(AccountSecurity.this, "Link has been sent to your Alternate Email ID...",
                                Toast.LENGTH_SHORT).show();
                        alert.dismiss();
                    } else {
                        lyPopUpSecurityOptions.setVisibility(View.GONE);
                        lyPopUpQuestion.setVisibility(View.VISIBLE);
                        btPopupSecurityQuestionConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (clicked == 1) {
                                    String a = String.valueOf(clicked);
                                    Log.i("Email", a);
                                    lyPopUpQuestion.setVisibility(View.GONE);
                                    lyPopUpEmail.setVisibility(View.VISIBLE);
                                    btPopupSecurityEmailSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (TextUtils.isEmpty(etPopupSecurityEmail.getText().toString())) {
                                                CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_email_id), clPopUpMain);
                                            } else if (CommonUtil.isValidEmail(etPopupSecurityEmail.getText().toString())) {
                                                CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_valid_email_id), clPopUpMain);
                                            } else if (TextUtils.isEmpty(etPopupSecurityConfirmEmail.getText().toString())) {
                                                CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_confirm_email_id), clPopUpMain);
                                            } else if (CommonUtil.isValidEmail(etPopupSecurityConfirmEmail.getText().toString())) {
                                                CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_valid_confirm_email_id), clPopUpMain);
                                            } else
                                                alert.dismiss();
                                        }
                                    });
                                } else if (clicked == 2) {
                                    String a = String.valueOf(clicked);
                                    Log.i("Alternate Email", a);
                                    lyPopUpQuestion.setVisibility(View.GONE);
                                    lyPopUpAlternateEmail.setVisibility(View.VISIBLE);
                                    btPopupSecurityAlternateEmailSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (TextUtils.isEmpty(etPopupSecurityAlternateEmail.getText().toString())) {
                                                CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_alternate_email), clPopUpMain);
                                            } else if (CommonUtil.isValidEmail(etPopupSecurityAlternateEmail.getText().toString())) {
                                                CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_valid_alternate_email), clPopUpMain);
                                            } else if (TextUtils.isEmpty(etPopupSecurityConfirmAlternateEmail.getText().toString())) {
                                                CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_confirm_alternate_email), clPopUpMain);
                                            } else if (CommonUtil.isValidEmail(etPopupSecurityConfirmAlternateEmail.getText().toString())) {
                                                CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_valid_confirm_alternate_email), clPopUpMain);
                                            } else
                                                alert.dismiss();
                                        }
                                    });
                                } else if (clicked == 4) {
                                    String a = String.valueOf(clicked);
                                    Log.i("Password", a);
                                    lyPopUpQuestion.setVisibility(View.GONE);
                                    lyPopUpQuestionChanges.setVisibility(View.VISIBLE);
                                    btPopupSecurityQuestionChangeConfirm.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (TextUtils.isEmpty(etPopupSecurityPassword.getText().toString())) {
                                                CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_pass), clPopUpMain);
                                            } else if (etPopupSecurityPassword.length() < 7 || etPopupSecurityPassword.length() >15) {
                                                CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_valid_pass), clPopUpMain);
                                            } else if (TextUtils.isEmpty(etPopupSecurityConfirmPassword.getText().toString())) {
                                                CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_pass), clPopUpMain);
                                            } else if (CommonUtil.isValidEmail(etPopupSecurityPassword.getText().toString())) {
                                                CommonUtil.showSnackBar(AccountSecurity.this, getString(R.string.enter_valid_pass), clPopUpMain);
                                            } else
                                                alert.dismiss();
                                        }
                                    });
                                } else if (clicked == 3) {
                                    String a = String.valueOf(clicked);
                                    Log.i("Question", a);
                                    lyPopUpQuestion.setVisibility(View.GONE);
                                    lyPopUpPassword.setVisibility(View.VISIBLE);
                                    btPopupSecurityPasswordSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alert.dismiss();
                                        }
                                    });
                                }
                            }
                        });
                    }
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
        }

    }
}
