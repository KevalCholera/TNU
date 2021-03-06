package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.TimeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PersonalInfo extends TimeActivity implements View.OnClickListener {

    TextView etPersonalInfoName, etPersonalInfolastName, etPersonalInfoNo, etPersonalInfoEmail;
    EditText etPersonalInfoAddInfo;
    LinearLayout etInfoSocial;
    CoordinatorLayout clPersonalInfo;
    Spinner spPersonalInfoAd;
    Button btInfoConfirmNext;
    ImageView ivPersonalInfoSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_personal_info);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().getThemedContext();

        btInfoConfirmNext = (Button) findViewById(R.id.btInfoConfirmNext);
        etPersonalInfoName = (TextView) findViewById(R.id.etPersonalInfoName);
        etPersonalInfolastName = (TextView) findViewById(R.id.etPersonalInfolastName);
        etPersonalInfoNo = (TextView) findViewById(R.id.etPersonalInfoNo);
        etPersonalInfoEmail = (TextView) findViewById(R.id.etPersonalInfoEmail);
        etPersonalInfoAddInfo = (EditText) findViewById(R.id.etPersonalInfoAddInfo);
        etInfoSocial = (LinearLayout) findViewById(R.id.etInfoSocial);
        clPersonalInfo = (CoordinatorLayout) findViewById(R.id.clPersonalInfo);
        spPersonalInfoAd = (Spinner) findViewById(R.id.spPersonalInfoAd);
        ivPersonalInfoSpinner = (ImageView) findViewById(R.id.ivPersonalInfoSpinner);

        etPersonalInfoName.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_FIRST, ""));
        etPersonalInfolastName.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_LAST, ""));
        etPersonalInfoNo.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_MNO, ""));
        etPersonalInfoEmail.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_EMAIL, ""));

        List<String> categories = new ArrayList<>();
        categories.add(getResources().getString(R.string.google));
        categories.add(getResources().getString(R.string.ad));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.element_personal_info_spinner, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spPersonalInfoAd.setAdapter(dataAdapter);

        ivPersonalInfoSpinner.setOnClickListener(this);
        etInfoSocial.setOnClickListener(this);
        btInfoConfirmNext.setOnClickListener(this);
    }

    public void checkCondition() {
        try {
            JSONObject jsonData = new JSONObject();
//            .put("hearAboutUs", String.valueOf(spPersonalInfoAd.getSelectedItemPosition() + 1))

            jsonData.put("firstName", etPersonalInfoName.getText().toString().trim())
                    .put("mobileNo", etPersonalInfoNo.getText().toString().trim())
                    .put("freeSms", 1)
                    .put("paymentMode", 1)
                    .put("emailId", etPersonalInfoEmail.getText().toString().trim())
                    .put("addtionalInformation", etPersonalInfoAddInfo.getText().toString().trim())
                    .put("lastName", etPersonalInfolastName.getText().toString().trim());

            SharedPreferenceUtil.putValue(Constants.PrefKeys.BOOKING_INFO, jsonData.toString());
            SharedPreferenceUtil.save();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(PersonalInfo.this, PaymentDetails.class));
//        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_personal_info;
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
                CommonUtil.closeKeyboard(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        CommonUtil.closeKeyboard(this);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        CommonUtil.closeKeyboard(PersonalInfo.this);
        switch (v.getId()) {
            case R.id.btInfoConfirmNext:
                checkCondition();
                break;
            case R.id.ivPersonalInfoSpinner:
                spPersonalInfoAd.performClick();
                break;
            case R.id.etInfoSocial:
                spPersonalInfoAd.performClick();
                break;
        }
    }
}