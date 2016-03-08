package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.smartsense.taxinearyou.utill.CommonUtil;

public class PersonalInfo extends AppCompatActivity {

    EditText etPersonalInfoName, etPersonalInfolastName, etPersonalInfoNo, etPersonalInfoEmail, etPersonalInfoAddInfo, etInfoSocial;
    CheckBox cbInfoSms;
    CoordinatorLayout clPersonalInfo;
    Button btInfoConfirmNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btInfoConfirmNext = (Button) findViewById(R.id.btInfoConfirmNext);
        etPersonalInfoName = (EditText) findViewById(R.id.etPersonalInfoName);
        etPersonalInfolastName = (EditText) findViewById(R.id.etPersonalInfolastName);
        etPersonalInfoNo = (EditText) findViewById(R.id.etPersonalInfoNo);
        etPersonalInfoEmail = (EditText) findViewById(R.id.etPersonalInfoEmail);
        etPersonalInfoAddInfo = (EditText) findViewById(R.id.etPersonalInfoAddInfo);
        cbInfoSms = (CheckBox) findViewById(R.id.cbInfoSms);
        etInfoSocial = (EditText) findViewById(R.id.etInfoSocial);
        clPersonalInfo = (CoordinatorLayout) findViewById(R.id.clPersonalInfo);

        btInfoConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etPersonalInfoName.getText().toString()))
                    CommonUtil.showSnackBar(PersonalInfo.this, getString(R.string.enter_first_name), clPersonalInfo);
                else if (TextUtils.isEmpty(etPersonalInfolastName.getText().toString()))
                    CommonUtil.showSnackBar(PersonalInfo.this, getString(R.string.enter_last_name), clPersonalInfo);
                else if (TextUtils.isEmpty(etPersonalInfoNo.getText().toString()))
                    CommonUtil.showSnackBar(PersonalInfo.this, getString(R.string.enter_contact_no), clPersonalInfo);
                else if (etPersonalInfoNo.length() < 7 || etPersonalInfoNo.length() > 13)
                    CommonUtil.showSnackBar(PersonalInfo.this, getString(R.string.enter_valid_contact_no), clPersonalInfo);
                else if (TextUtils.isEmpty(etPersonalInfoEmail.getText().toString()))
                    CommonUtil.showSnackBar(PersonalInfo.this, getString(R.string.enter_email), clPersonalInfo);
                else if (!CommonUtil.isValidEmail(etPersonalInfoEmail.getText().toString()))
                    CommonUtil.showSnackBar(PersonalInfo.this, getString(R.string.enter_valid_email_id), clPersonalInfo);
                else
                    startActivity(new Intent(PersonalInfo.this, PaymentDetails.class));
            }
        });

    }

}
