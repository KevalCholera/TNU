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

import com.smartsense.taxinearyou.utill.CommonUtil;

public class YourDetails extends AppCompatActivity implements View.OnClickListener {

    EditText etYourDetailName, etYourDetailNumber, etYourDetailID, etYourDetailAddress, etYourDetailPostcode, etYourDetailColor, etYourDetailDescription;
    AlertDialog alert;
    Button btYourDetailSubmit;
    CoordinatorLayout clYourDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etYourDetailName = (EditText) findViewById(R.id.etYourDetailName);
        etYourDetailNumber = (EditText) findViewById(R.id.etYourDetailNumber);
        etYourDetailID = (EditText) findViewById(R.id.etYourDetailID);
        etYourDetailAddress = (EditText) findViewById(R.id.etYourDetailAddress);
        etYourDetailPostcode = (EditText) findViewById(R.id.etYourDetailPostcode);
        etYourDetailColor = (EditText) findViewById(R.id.etYourDetailColor);
        etYourDetailDescription = (EditText) findViewById(R.id.etYourDetailDescription);
        btYourDetailSubmit = (Button) findViewById(R.id.btYourDetailSubmit);
        clYourDetails = (CoordinatorLayout) findViewById(R.id.clYourDetails);

        btYourDetailSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btYourDetailSubmit:
                if (TextUtils.isEmpty(etYourDetailAddress.getText().toString()))
                    CommonUtil.showSnackBar(YourDetails.this, getString(R.string.enter_item_add), clYourDetails);
                else if (TextUtils.isEmpty(etYourDetailPostcode.getText().toString()))
                    CommonUtil.showSnackBar(YourDetails.this, getString(R.string.enter_postcode), clYourDetails);
                else if (TextUtils.isEmpty(etYourDetailColor.getText().toString()))
                    CommonUtil.showSnackBar(YourDetails.this, getString(R.string.enter_color), clYourDetails);
                else if (TextUtils.isEmpty(etYourDetailColor.getText().toString()))
                    CommonUtil.showSnackBar(YourDetails.this, getString(R.string.enter_color), clYourDetails);
                else if (TextUtils.isEmpty(etYourDetailDescription.getText().toString()))
                    CommonUtil.showSnackBar(YourDetails.this, getString(R.string.enter_item_dec), clYourDetails);
                else
                    CommonUtil.openDialogs(YourDetails.this, "Your Details", R.id.lyPopUpYourDetail, R.id.btPopupYourDetailOk);
                break;

        }
    }
}
