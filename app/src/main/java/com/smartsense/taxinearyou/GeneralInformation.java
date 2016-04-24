package com.smartsense.taxinearyou;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.smartsense.taxinearyou.utill.CommonUtil;

public class GeneralInformation extends AppCompatActivity {

    EditText etGeneralFirstName, etGeneralLastName, etGeneralMobile;
    Button btGeneralUpdateProfile;
    CoordinatorLayout clGeneralInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_information);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etGeneralFirstName = (EditText) findViewById(R.id.etGeneralFirstName);
        etGeneralLastName = (EditText) findViewById(R.id.etGeneralLastName);
        etGeneralMobile = (EditText) findViewById(R.id.etGeneralMobile);
        btGeneralUpdateProfile = (Button) findViewById(R.id.btGeneralUpdateProfile);
        clGeneralInfo = (CoordinatorLayout) findViewById(R.id.clGeneralInfo);

        btGeneralUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etGeneralMobile.getText().toString()))
                    CommonUtil.showSnackBar(GeneralInformation.this, getString(R.string.enter_contact_no), clGeneralInfo);
                else if (etGeneralMobile.length() < 7 || etGeneralMobile.length() > 13)
                    CommonUtil.showSnackBar(GeneralInformation.this, getString(R.string.enter_valid_contact_no), clGeneralInfo);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ratingforsearch, menu);
        return false;
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
