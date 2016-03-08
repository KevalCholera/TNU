package com.smartsense.taxinearyou;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.smartsense.taxinearyou.utill.CommonUtil;

public class RecoverEmailAddress extends AppCompatActivity implements View.OnClickListener {

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
                break;
            case R.id.rbRecoverEmailRememberEmail:
                lyRadioButton1.setVisibility(View.GONE);
                lyRadioButton2.setVisibility(View.VISIBLE);
                break;
            case R.id.btRecoverEmailSubmit:
                if (rbRecoverEmailAlternateEmail.isChecked()) {
                    if (TextUtils.isEmpty(etRecoverEmailAlternateEmail.getText().toString()))
                        CommonUtil.showSnackBar(RecoverEmailAddress.this, getString(R.string.enter_alternate_email), clRecoverEmail);
                    else if (!CommonUtil.isValidEmail(etRecoverEmailAlternateEmail.getText().toString()))
                        CommonUtil.showSnackBar(RecoverEmailAddress.this, getString(R.string.enter_valid_alternate_email), clRecoverEmail);
                    else
                        CommonUtil.openDialogs(RecoverEmailAddress.this, "Recover Email", R.id.lyPopUpAfterRecovery, R.id.btPopupThankingRecoveryBack);
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
                        CommonUtil.openDialogs(RecoverEmailAddress.this, "Recover Email", R.id.lyPopUpAfterRecovery, R.id.btPopupThankingRecoveryBack);
                }
                break;
        }
    }
}
