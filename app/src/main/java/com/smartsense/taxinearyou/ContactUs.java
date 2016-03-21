package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.smartsense.taxinearyou.utill.CommonUtil;

public class ContactUs extends AppCompatActivity {

    EditText etContactUsName, etContactUsMobile, etContactUsEmail, etContactUsMessage;
    Button btContactSubmit;
    CoordinatorLayout clContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etContactUsName = (EditText) findViewById(R.id.etContactUsName);
        etContactUsMobile = (EditText) findViewById(R.id.etContactUsMobile);
        etContactUsEmail = (EditText) findViewById(R.id.etContactUsEmail);
        etContactUsMessage = (EditText) findViewById(R.id.etContactUsMessage);
        btContactSubmit = (Button) findViewById(R.id.btContactSubmit);
        clContactUs = (CoordinatorLayout) findViewById(R.id.clContactUs);
    }

    public void submitContact(View view) {
        if (TextUtils.isEmpty(etContactUsName.getText().toString()))
            CommonUtil.showSnackBar(ContactUs.this, getResources().getString(R.string.enter_first_name), clContactUs);
        else if (TextUtils.isEmpty(etContactUsMobile.getText().toString()))
            CommonUtil.showSnackBar(ContactUs.this, getResources().getString(R.string.enter_contact_no), clContactUs);
        else if (TextUtils.isEmpty(etContactUsEmail.getText().toString()))
            CommonUtil.showSnackBar(ContactUs.this, getResources().getString(R.string.enter_email_id), clContactUs);
        else if (TextUtils.isEmpty(etContactUsMessage.getText().toString()))
            CommonUtil.showSnackBar(ContactUs.this, getResources().getString(R.string.enter_message), clContactUs);
        else
            startActivity(new Intent(ContactUs.this, Search.class));
    }
}
