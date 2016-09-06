package com.smartsense.taxinearyou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cn.qqtheme.framework.picker.OptionPicker;

public class CardPayment extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {

    LinearLayout llWithCardPaymentOld;
    CheckBox cbCardSave;
    Button btCardSavePay;
    CoordinatorLayout clRecoverEmail;
    EditText etCardSaveNo, etCardSaveMonth, etCardSaveYear, etCardSaveSecurity;
    private int year;
    private String currentMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etCardSaveNo = (EditText) findViewById(R.id.etCardSaveNo);
        etCardSaveMonth = (EditText) findViewById(R.id.etCardSaveMonth);
        etCardSaveMonth.setOnClickListener(this);
        etCardSaveYear = (EditText) findViewById(R.id.etCardSaveYear);
        etCardSaveYear.setOnClickListener(this);
        etCardSaveSecurity = (EditText) findViewById(R.id.etCardSaveSecurity);
        btCardSavePay = (Button) findViewById(R.id.btCardSavePay);
        cbCardSave = (CheckBox) findViewById(R.id.cbCardSave);
        clRecoverEmail = (CoordinatorLayout) findViewById(R.id.clRecoverEmail);
        btCardSavePay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        CommonUtil.closeKeyboard(this);
        switch (v.getId()) {
            case R.id.etCardSaveMonth:
                selectPassenger(true);
                break;
            case R.id.etCardSaveYear:
                selectPassenger(false);
                break;
            case R.id.btCardSavePay:

                if (TextUtils.isEmpty(etCardSaveMonth.getText().toString()) && TextUtils.isEmpty(etCardSaveYear.getText().toString()) && TextUtils.isEmpty(etCardSaveNo.getText().toString()))
                    CommonUtil.showSnackBar(getString(R.string.enter_fields_below), clRecoverEmail);
                else if (etCardSaveNo.length() < 13 || TextUtils.isEmpty(etCardSaveNo
                        .getText().toString()))
                    CommonUtil.showSnackBar(getString(R.string.enter_valid_card), clRecoverEmail);
                else if (TextUtils.isEmpty(etCardSaveMonth.getText().toString()))
                    CommonUtil.showSnackBar(getString(R.string.select_month), clRecoverEmail);
                else if (TextUtils.isEmpty(etCardSaveYear.getText().toString()))
                    CommonUtil.showSnackBar(getString(R.string.select_year), clRecoverEmail);
                else if (Integer.valueOf(etCardSaveYear.getText().toString()) == year && Integer.valueOf(etCardSaveMonth.getText().toString()) < Integer.valueOf(currentMonth))
                    CommonUtil.showSnackBar(getString(R.string.select_valid_month), clRecoverEmail);
                else if (etCardSaveSecurity.length() < 3 || CommonUtil.isSpecialChar(etCardSaveSecurity.getText().toString()))
                    CommonUtil.showSnackBar(getString(R.string.enter_sequ_valid_card), clRecoverEmail);
                else
                    doRideBook(etCardSaveNo.getText().toString(), etCardSaveMonth.getText().toString(), etCardSaveYear.getText().toString(), etCardSaveSecurity.getText().toString());
                break;

        }
    }

    private void doRideBook(String no, String month, String year, String security) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cardNumber",no);
            jsonObject.put("saveCard",cbCardSave.isChecked());
            jsonObject.put("expYear",year);
            jsonObject.put("expMonth",month);
            jsonObject.put("cardCVV",security);
            Intent i = new Intent();
            i.putExtra("obj", jsonObject.toString());
            setResult(Activity.RESULT_OK, i);
            finish();
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
                onBackPressed();
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


    public void selectPassenger(final Boolean check) {
        try {
            String[] stringArr;
            if (check) {
                stringArr = new String[]{
                        "01",
                        "02",
                        "03",
                        "04",
                        "05",
                        "06",
                        "07",
                        "08",
                        "09",
                        "10",
                        "11",
                        "12"
                };
            } else {
                stringArr = new String[15];
                try {
                    Log.i("yes",SharedPreferenceUtil.getString(Constants.YEAR_MONTH, ""));
                    String[] date = SharedPreferenceUtil.getString(Constants.YEAR_MONTH, "").split("_");
                    year = Integer.valueOf(date[0]);
                    currentMonth = date[1];
                } catch (Exception e) {
                    Calendar c = Calendar.getInstance();
                    year = c.getTime().getYear();
                    currentMonth = "" + c.getTime().getMonth();
                }
                int year1=year;
                for (int i = 0; i < 15; i++) {
                    if (i == 0)
                        stringArr[i] = "" + (year);
                    else
                        stringArr[i] = "" + (++year1);
                }
            }
            OptionPicker picker = new OptionPicker(this, stringArr);
//            picker.setOffset(2);
//            picker.setSelectedIndex(1);
            picker.setTextSize(15);

            if (check)
                picker.setTitleText("Select Month");
            else
                picker.setTitleText("Select Year");
            picker.setTitleTextColor(ActivityCompat.getColor(this, R.color.white));
            picker.setTopBackgroundColor(ActivityCompat.getColor(this, R.color.colorAccent));
            picker.setTopLineColor(ActivityCompat.getColor(this, R.color.colorAccent));
            picker.setCancelTextColor(ActivityCompat.getColor(this, R.color.white));
            picker.setSubmitTextColor(ActivityCompat.getColor(this, R.color.white));
            picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                @Override
                public void onOptionPicked(String option) {
//                    final String id1 = spinnerMap.get(option);
                    if (check) {
                        etCardSaveMonth.setText(option);
//                        etCardSaveMonth.setTag(id1);
                    } else {
                        etCardSaveYear.setText(option);
//                        etCardSaveYear.setTag(id1);
                    }

                }
            });
            picker.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        CommonUtil.closeKeyboard(this);
        Intent i = new Intent();
        setResult(Activity.RESULT_CANCELED, i);
        finish();
        super.onBackPressed();
    }
}
