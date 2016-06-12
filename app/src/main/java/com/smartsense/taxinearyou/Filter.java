package com.smartsense.taxinearyou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.TimeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class Filter extends TimeActivity implements View.OnClickListener {

    RadioGroup rgFilterBookingType, rgFilterDistance, rgFilterVehicleType, rbgFilterRating;
    RadioButton rbFilterRating5, rbFilterRating4, rbFilterRating3, rbFilterRating2, rbFilterRating1, rbFilterRatingAll;
    Button btFilterDone, btFilterResetAll;
    //    ImageView ivFilterCancel;
    CheckBox cbFilterRecommend;
    LinearLayout rbFeedbackRatingForDriver5, rbFeedbackRatingForDriver4, rbFeedbackRatingForDriver3, rbFeedbackRatingForDriver2, rbFeedbackRatingForDriver1;
    private RadioButton rbFilterSingle;
    private JSONObject filterObj;
    TextView tvFilterRating1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_filter);

//        Toolbar tbFilter = (Toolbar) findViewById(R.id.tbFilter);
//        setSupportAct(tbFilter);
        getSupportActionBar().setTitle("");

        rgFilterVehicleType = (RadioGroup) findViewById(R.id.rgFilterVehicleType1);
        rgFilterBookingType = (RadioGroup) findViewById(R.id.rgFilterBookingType);
        rgFilterDistance = (RadioGroup) findViewById(R.id.rgFilterDistance1);
        rbgFilterRating = (RadioGroup) findViewById(R.id.rbgFilterRating);
        rbFilterSingle = (RadioButton) findViewById(R.id.rbFilterSingle);
        RadioButton rbFilterReturn = (RadioButton) findViewById(R.id.rbFilterReturn);
        rbFilterRating5 = (RadioButton) findViewById(R.id.rbFilterRating5);
        rbFilterRating4 = (RadioButton) findViewById(R.id.rbFilterRating4);
        rbFilterRating3 = (RadioButton) findViewById(R.id.rbFilterRating3);
        rbFilterRating2 = (RadioButton) findViewById(R.id.rbFilterRating2);
        rbFilterRating1 = (RadioButton) findViewById(R.id.rbFilterRating1);
        rbFilterRatingAll = (RadioButton) findViewById(R.id.rbFilterRatingAll);
        rbFeedbackRatingForDriver5 = (LinearLayout) findViewById(R.id.rbFeedbackRatingForDriver5);
        rbFeedbackRatingForDriver4 = (LinearLayout) findViewById(R.id.rbFeedbackRatingForDriver4);
        rbFeedbackRatingForDriver3 = (LinearLayout) findViewById(R.id.rbFeedbackRatingForDriver3);
        rbFeedbackRatingForDriver2 = (LinearLayout) findViewById(R.id.rbFeedbackRatingForDriver2);
        rbFeedbackRatingForDriver1 = (LinearLayout) findViewById(R.id.rbFeedbackRatingForDriver1);
        cbFilterRecommend = (CheckBox) findViewById(R.id.cbFilterRecommend);
        btFilterDone = (Button) findViewById(R.id.btFilterDone);
        btFilterResetAll = (Button) findViewById(R.id.btFilterResetAll);
        tvFilterRating1 = (TextView) findViewById(R.id.tvFilterRating1);

        cbFilterRecommend.setOnClickListener(this);
        rbFilterSingle.setOnClickListener(this);
        rbFilterReturn.setOnClickListener(this);
        btFilterDone.setOnClickListener(this);
        btFilterResetAll.setOnClickListener(this);
        rbFeedbackRatingForDriver5.setOnClickListener(this);
        rbFeedbackRatingForDriver4.setOnClickListener(this);
        rbFeedbackRatingForDriver3.setOnClickListener(this);
        rbFeedbackRatingForDriver2.setOnClickListener(this);
        rbFeedbackRatingForDriver1.setOnClickListener(this);
        tvFilterRating1.setOnClickListener(this);

        try {
            filterObj = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_FILTER_REQUEST, ""));
            if (filterObj.has("bookingType")) {
                if (filterObj.optInt("bookingType") == 1)
                    rbFilterSingle.setChecked(true);
                else
                    rbFilterReturn.setChecked(true);
            }
            if (filterObj.has("rating")) {
                if (-1 == filterObj.optInt("rating"))
                    rbFilterRatingAll.setChecked(true);
                else if (5 == filterObj.optInt("rating"))
                    rbFilterRating5.setChecked(true);
                else if (4 == filterObj.optInt("rating"))
                    rbFilterRating4.setChecked(true);
                else if (3 == filterObj.optInt("rating"))
                    rbFilterRating3.setChecked(true);
                else if (2 == filterObj.optInt("rating"))
                    rbFilterRating2.setChecked(true);
                else if (filterObj.optInt("rating") == 1)
                    rbFilterRating1.setChecked(true);
            }
            if (filterObj.has("isRecommended")) {
                if (filterObj.optInt("isRecommended") == 1)
                    cbFilterRecommend.setChecked(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        addRadioVehical();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_partner_details;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btFilterResetAll:
                SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_FILTER_REQUEST);
                SharedPreferenceUtil.save();
                setResult(Activity.RESULT_OK, new Intent());
                finish();
                break;
            case R.id.rbFeedbackRatingForDriver5:
                rbFilterRating5.performClick();
                break;
            case R.id.rbFeedbackRatingForDriver4:
                rbFilterRating4.performClick();
                break;
            case R.id.rbFeedbackRatingForDriver3:
                rbFilterRating3.performClick();
                break;
            case R.id.rbFeedbackRatingForDriver2:
                rbFilterRating2.performClick();
                break;
            case R.id.rbFeedbackRatingForDriver1:
                rbFilterRating1.performClick();
                break;
            case R.id.tvFilterRating1:
                rbFilterRatingAll.performClick();
                break;
            case R.id.btFilterDone:
                int selectedId2 = rgFilterVehicleType.getCheckedRadioButtonId();
                RadioButton radioButton2 = (RadioButton) findViewById(selectedId2);
                int selectedId1 = rgFilterDistance.getCheckedRadioButtonId();
                RadioButton radioButton1 = (RadioButton) findViewById(selectedId1);
                int selectedId = rgFilterBookingType.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                int selectedId3 = rbgFilterRating.getCheckedRadioButtonId();
                RadioButton radioButton3 = (RadioButton) findViewById(selectedId3);
                try {
                    if (filterObj.has("vehicleType"))
                        filterObj.remove("vehicleType");
                    filterObj.put("vehicleType", String.valueOf(radioButton2.getId()));
                    if (filterObj.has("distance"))
                        filterObj.remove("distance");
                    filterObj.put("distance", String.valueOf(radioButton1.getId() == 0 ? -1 : radioButton1.getId()));
                    if (filterObj.has("bookingType"))
                        filterObj.remove("bookingType");
                    filterObj.put("bookingType", String.valueOf(radioButton.getId() == R.id.rbFilterSingle ? 1 : 2));
                    if (filterObj.has("rating"))
                        filterObj.remove("rating");
                    int ratingId = -1;
                    if (radioButton3.getId() == R.id.rbFilterRatingAll)
                        ratingId = -1;
                    else if (radioButton3.getId() == R.id.rbFilterRating5)
                        ratingId = 5;
                    else if (radioButton3.getId() == R.id.rbFilterRating4)
                        ratingId = 4;
                    else if (radioButton3.getId() == R.id.rbFilterRating3)
                        ratingId = 3;
                    else if (radioButton3.getId() == R.id.rbFilterRating2)
                        ratingId = 2;
                    else if (radioButton3.getId() == R.id.rbFilterRating1)
                        ratingId = 1;
                    filterObj.put("rating", String.valueOf(ratingId));
                    if (filterObj.has("isRecommended"))
                        filterObj.remove("isRecommended");
                    filterObj.put("isRecommended", String.valueOf(cbFilterRecommend.isChecked() ? 1 : 0));
                    Log.i("filterObj", filterObj.toString());
                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_FILTER_REQUEST, filterObj.toString());
                    SharedPreferenceUtil.save();
                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rgFilterDistance1:

                break;
            case R.id.rgFilterBookingType:

                break;
        }
    }

    public void reset() {
        rbFilterSingle.setChecked(true);
        cbFilterRecommend.setChecked(false);

        rbFilterRatingAll.setChecked(true);

    }

    public void addRadioVehical() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.setMargins(5, 0, 0, 0);
        try {
            JSONArray taxiTypeArray = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_TAXI_TYPE, ""));
            RadioButton[] radioButton = new RadioButton[taxiTypeArray.length() % 2 == 0 ? taxiTypeArray.length() : taxiTypeArray.length() + 1];
            int typeLen = (int) Math.floor(taxiTypeArray.length() / 2);
            TableRow[] tableRow = new TableRow[typeLen + 1];
            int j = -1;
            Boolean check = false;
            for (int i = 0; i < taxiTypeArray.length(); i++) {
                if ((i + 1) % 2 != 0) {
                    j++;
                    tableRow[j] = new TableRow(this);
                    tableRow[j].setId(taxiTypeArray.optJSONObject(i).getInt("taxiTypeId"));
                    tableRow[j].setLayoutParams(params1);
                }
                radioButton[i] = new RadioButton(this);
                radioButton[i].setText(taxiTypeArray.optJSONObject(i).getString("taxiName"));
                radioButton[i].setTextSize(12f);
                radioButton[i].setTextColor(getResources().getColor(R.color.white));
                radioButton[i].setButtonDrawable(R.drawable.radio_button_filter);
                radioButton[i].setId(taxiTypeArray.optJSONObject(i).getInt("taxiTypeId"));
                radioButton[i].setGravity(Gravity.CENTER);
                radioButton[i].setPadding(15, 15, 15, 15); // android:checked="true"
                if (filterObj.optInt("vehicleType") == taxiTypeArray.optJSONObject(i).getInt("taxiTypeId"))
                    radioButton[i].setChecked(true);
                radioButton[i].setClickable(true);
                radioButton[i].setLayoutParams(params);
//                tableRow[j].addView(radioButton[i]);
                rgFilterVehicleType.addView(radioButton[i]);
                if (taxiTypeArray.length() == i + 1) {
                    Log.i("if", j + "" + i);
                    check = (taxiTypeArray.length() % 2) == 0 ? false : true;
                }
//                if ((i + 1) % 2 == 0 || check) {
//                    if (check) {
//                        radioButton[i + 1] = new RadioButton(this);
//                        radioButton[ i + 1].setLayoutParams(params);
//                        radioButton[i + 1].setText(taxiTypeArray.optJSONObject(i).getString("taxiName"));
//                        radioButton[i + 1].setVisibility(View.INVISIBLE);
//                        tableRow[j].addView(radioButton[i + 1]);
//                    }
//                    check = false;
//                    Log.i("Yes1", j + "" + i);
//                    rgFilterVehicleType.addView(tableRow[j], new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//
//                }
            }

            j = -1;
            JSONArray jsonArray = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_DISTANCE_LIST, ""));
            radioButton = new RadioButton[jsonArray.length() % 2 == 0 ? jsonArray.length() : jsonArray.length() + 1];
            tableRow = new TableRow[(int) Math.floor(jsonArray.length() / 2) + 1];
            Log.i("Yes", jsonArray.length() + "" + tableRow.length);
            for (int i = 0; i < jsonArray.length(); i++) {
                if ((i + 1) % 2 != 0) {
                    j++;
                    Log.i("Yes", j + "" + i);
                    tableRow[j] = new TableRow(this);
                    tableRow[j].setId(jsonArray.optJSONObject(i).getInt("distanceId"));
                    tableRow[j].setLayoutParams(params1);
                }
                radioButton[i] = new RadioButton(this);
                radioButton[i].setLayoutParams(params);
                radioButton[i].setButtonDrawable(R.drawable.radio_button_filter);
                radioButton[i].setTextSize(12f);
                radioButton[i].setText(jsonArray.optJSONObject(i).getString("name"));
                radioButton[i].setTextColor(ContextCompat.getColor(Filter.this, R.color.white));
                int id1 = jsonArray.optJSONObject(i).getInt("distanceId") == -1 ? 0 : jsonArray.optJSONObject(i).getInt("distanceId");
                radioButton[i].setId(id1);
                radioButton[i].setGravity(Gravity.CENTER);
                radioButton[i].setPadding(15, 15, 15, 15); // android:checked="true"
                if (filterObj.optInt("distance") == (jsonArray.optJSONObject(i).getInt("distanceId")))
                    radioButton[i].setChecked(true);
                radioButton[i].setClickable(true);

                if (jsonArray.length() == i + 1) {
                    Log.i("if", j + "" + i);
                    check = (jsonArray.length() % 2) == 0 ? false : true;
                }
//                tableRow[j].addView(radioButton[i]);
                rgFilterDistance.addView(radioButton[i]);
//                if ((i + 1) % 2 == 0 || check) {
//                    if (check) {
//                        radioButton[i + 1] = new RadioButton(this);
//                        radioButton[i + 1].setVisibility(View.INVISIBLE);
//                        radioButton[i + 1].setLayoutParams(params);
//                        radioButton[i + 1].setText(jsonArray.optJSONObject(i).getString("name"));
//                        tableRow[j].addView(radioButton[i + 1]);
//                    }
//                    check = false;
//                    Log.i("Yes1", j + "" + i);
//                    rgFilterDistance.addView(tableRow[j], new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel_action:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}