package com.smartsense.taxinearyou;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Filter extends AppCompatActivity implements View.OnClickListener {

    RadioGroup rgFilterPrice, rgFilterBookingType, rgFilterDistance1, rgFilterDistance2,
            rgFilterVehicleType1, rgFilterVehicleType2;
    RadioButton rbFilter1, rbFilter2, rbFilter3, rbFilter4, rbFilterLow, rbFilterHigh, rbFilterSingle,
            rbFilterReturn, rbFilterAll, rbFilter3Miles, rbFilter5Miles, rbFilter6Miles, rbFilter9Miles,
            rbFilter5, rbFilter6, rbFilter7, rbFilter8, rbFilterRating5, rbFilterRating4, rbFilterRating3, rbFilterRating2, rbFilterRating1, rbFilterRatingAll;
    Button btFilterDone, btFilterResetAll;
    ImageView ivFilterCancel;
    CheckBox cbFilterRecommend;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void reset() {
        rgFilterDistance1.clearCheck();
        rgFilterDistance2.clearCheck();
        rgFilterVehicleType2.clearCheck();
        rgFilterVehicleType1.clearCheck();
        rbFilter1.setChecked(true);
        rbFilterSingle.setChecked(true);
        cbFilterRecommend.setChecked(false);
        rbFilterHigh.setChecked(true);
        rbFilterRatingAll.setChecked(true);
        rbFilterAll.setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        rgFilterPrice = (RadioGroup) findViewById(R.id.rgFilterPrice);
        rgFilterVehicleType1 = (RadioGroup) findViewById(R.id.rgFilterVehicleType1);
        rgFilterVehicleType2 = (RadioGroup) findViewById(R.id.rgFilterVehicleType2);
        rgFilterBookingType = (RadioGroup) findViewById(R.id.rgFilterBookingType);
        rgFilterDistance1 = (RadioGroup) findViewById(R.id.rgFilterDistance1);
        rgFilterDistance2 = (RadioGroup) findViewById(R.id.rgFilterDistance2);
        rbFilter1 = (RadioButton) findViewById(R.id.rbFilter1);
        rbFilter2 = (RadioButton) findViewById(R.id.rbFilter2);
        rbFilter3 = (RadioButton) findViewById(R.id.rbFilter3);
        rbFilter4 = (RadioButton) findViewById(R.id.rbFilter4);
        rbFilterLow = (RadioButton) findViewById(R.id.rbFilterLow);
        rbFilterHigh = (RadioButton) findViewById(R.id.rbFilterHigh);
        rbFilterSingle = (RadioButton) findViewById(R.id.rbFilterSingle);
        rbFilterReturn = (RadioButton) findViewById(R.id.rbFilterReturn);
        rbFilterAll = (RadioButton) findViewById(R.id.rbFilterAll);
        rbFilter3Miles = (RadioButton) findViewById(R.id.rbFilter3Miles);
        rbFilter5Miles = (RadioButton) findViewById(R.id.rbFilter5Miles);
        rbFilter6Miles = (RadioButton) findViewById(R.id.rbFilter6Miles);
        rbFilter9Miles = (RadioButton) findViewById(R.id.rbFilter9Miles);
        btFilterDone = (Button) findViewById(R.id.btFilterDone);
        btFilterResetAll = (Button) findViewById(R.id.btFilterResetAll);
        ivFilterCancel = (ImageView) findViewById(R.id.ivFilterCancel);
        rbFilter5 = (RadioButton) findViewById(R.id.rbFilter5);
        rbFilter6 = (RadioButton) findViewById(R.id.rbFilter6);
        rbFilter7 = (RadioButton) findViewById(R.id.rbFilter7);
        rbFilter8 = (RadioButton) findViewById(R.id.rbFilter8);
        rbFilterRating5 = (RadioButton) findViewById(R.id.rbFilterRating5);
        rbFilterRating4 = (RadioButton) findViewById(R.id.rbFilterRating4);
        rbFilterRating3 = (RadioButton) findViewById(R.id.rbFilterRating3);
        rbFilterRating2 = (RadioButton) findViewById(R.id.rbFilterRating2);
        rbFilterRating1 = (RadioButton) findViewById(R.id.rbFilterRating1);
        cbFilterRecommend = (CheckBox) findViewById(R.id.cbFilterRecommend);
        rbFilterRatingAll = (RadioButton) findViewById(R.id.rbFilterRatingAll);

        rbFilter1.setOnClickListener(this);
        rbFilter2.setOnClickListener(this);
        rbFilter3.setOnClickListener(this);
        rbFilter4.setOnClickListener(this);
        rbFilter5.setOnClickListener(this);
        rbFilter6.setOnClickListener(this);
        rbFilter7.setOnClickListener(this);
        rbFilter8.setOnClickListener(this);
        cbFilterRecommend.setOnClickListener(this);
        rbFilterLow.setOnClickListener(this);
        rbFilterHigh.setOnClickListener(this);
        rbFilterSingle.setOnClickListener(this);
        rbFilterReturn.setOnClickListener(this);
        rbFilterAll.setOnClickListener(this);
        rbFilter3Miles.setOnClickListener(this);
        rbFilter5Miles.setOnClickListener(this);
        rbFilter6Miles.setOnClickListener(this);
        rbFilter9Miles.setOnClickListener(this);
        btFilterDone.setOnClickListener(this);
        btFilterResetAll.setOnClickListener(this);
        ivFilterCancel.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rbFilter1:
                rgFilterVehicleType2.clearCheck();
                break;
            case R.id.rbFilter2:
                rgFilterVehicleType2.clearCheck();
                break;
            case R.id.rbFilter3:
                rgFilterVehicleType2.clearCheck();
                break;
            case R.id.rbFilter4:
                rgFilterVehicleType2.clearCheck();
                break;
            case R.id.rbFilter5:
                rgFilterVehicleType1.clearCheck();
                break;

            case R.id.rbFilter6:
                rgFilterVehicleType1.clearCheck();
                break;
            case R.id.rbFilter7:
                rgFilterVehicleType1.clearCheck();
                break;
            case R.id.rbFilter8:
                rgFilterVehicleType1.clearCheck();
                break;
            case R.id.rbFilterAll:
                rgFilterDistance2.clearCheck();
                break;
            case R.id.rbFilter3Miles:
                rgFilterDistance2.clearCheck();
                break;
            case R.id.rbFilter5Miles:
                rgFilterDistance2.clearCheck();
                break;
            case R.id.rbFilter6Miles:
                rgFilterDistance1.clearCheck();
                break;
            case R.id.rbFilter9Miles:
                rgFilterDistance1.clearCheck();
                break;
            case R.id.ivFilterCancel:
                startActivity(new Intent(Filter.this, SearchCars.class));
                break;
            case R.id.btFilterDone:
                startActivity(new Intent(Filter.this, SearchCars.class));
                break;
            case R.id.btFilterResetAll:
                reset();
                break;
        }

    }
}