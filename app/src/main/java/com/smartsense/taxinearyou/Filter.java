package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Filter extends AppCompatActivity implements View.OnClickListener , Response.Listener<JSONObject>, Response.ErrorListener {

    RadioGroup rgFilterPrice, rgFilterBookingType;
    TableLayout rgFilterDistance,
            rgFilterVehicleType;
    RadioButton rbFilterRating5, rbFilterRating4, rbFilterRating3, rbFilterRating2, rbFilterRating1, rbFilterRatingAll;
    Button btFilterDone, btFilterResetAll;
    ImageView ivFilterCancel;
    CheckBox cbFilterRecommend;
    private RadioButton rbFilterReturn;
    private RadioButton rbFilterSingle;
    private RadioButton rbFilterHigh;
    private RadioButton rbFilterLow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        rgFilterPrice = (RadioGroup) findViewById(R.id.rgFilterPrice);
        rgFilterVehicleType = (TableLayout) findViewById(R.id.rgFilterVehicleType1);
        rgFilterBookingType = (RadioGroup) findViewById(R.id.rgFilterBookingType);
        rgFilterDistance = (TableLayout) findViewById(R.id.rgFilterDistance1);
        rbFilterLow = (RadioButton) findViewById(R.id.rbFilterLow);
        rbFilterHigh = (RadioButton) findViewById(R.id.rbFilterHigh);
        rbFilterSingle = (RadioButton) findViewById(R.id.rbFilterSingle);
        rbFilterReturn = (RadioButton) findViewById(R.id.rbFilterReturn);
        rbFilterRating5 = (RadioButton) findViewById(R.id.rbFilterRating5);
        rbFilterRating4 = (RadioButton) findViewById(R.id.rbFilterRating4);
        rbFilterRating3 = (RadioButton) findViewById(R.id.rbFilterRating3);
        rbFilterRating2 = (RadioButton) findViewById(R.id.rbFilterRating2);
        rbFilterRating1 = (RadioButton) findViewById(R.id.rbFilterRating1);
        rbFilterRatingAll = (RadioButton) findViewById(R.id.rbFilterRatingAll);
        cbFilterRecommend = (CheckBox) findViewById(R.id.cbFilterRecommend);
        btFilterDone = (Button) findViewById(R.id.btFilterDone);
        btFilterResetAll = (Button) findViewById(R.id.btFilterResetAll);
        ivFilterCancel = (ImageView) findViewById(R.id.ivFilterCancel);


        cbFilterRecommend.setOnClickListener(this);
        rbFilterLow.setOnClickListener(this);
        rbFilterHigh.setOnClickListener(this);
        rbFilterSingle.setOnClickListener(this);
        rbFilterReturn.setOnClickListener(this);


        btFilterDone.setOnClickListener(this);
        btFilterResetAll.setOnClickListener(this);
        ivFilterCancel.setOnClickListener(this);

        addRadioVehical();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
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

    public void reset() {
        rbFilterSingle.setChecked(true);
        cbFilterRecommend.setChecked(false);
        rbFilterHigh.setChecked(true);
        rbFilterRatingAll.setChecked(true);

    }

    public void addRadioVehical() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.setMargins(5, 0, 0, 0);
        try {
            JSONArray taxiTypeArray = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_TAXI_TYPE, ""));

            JSONObject filterObj = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_FILTER_REQUEST, ""));
            RadioButton[] radioButton = new RadioButton[taxiTypeArray.length()];
            TableRow[] tableRow = new TableRow[(int) Math.ceil(taxiTypeArray.length() / 2)];
            int j = -1;
            for (int i = 0; i < taxiTypeArray.length(); i++) {

                if ((i + 1) % 2 != 0) {
                    j++;
                    Log.i("Yes", j + "" + i);
                    tableRow[j] = new TableRow(this);
                    tableRow[j].setId(j);
                    tableRow[j].setLayoutParams(params1);
                }
                radioButton[i] = new RadioButton(this);
                radioButton[i].setLayoutParams(params);
                radioButton[i].setText(taxiTypeArray.optJSONObject(i).getString("taxiName"));
                radioButton[i].setTextColor(getResources().getColor(R.color.white));
                radioButton[i].setId(taxiTypeArray.optJSONObject(i).getInt("taxiTypeId"));
                radioButton[i].setGravity(Gravity.CENTER);
                radioButton[i].setPadding(15, 15, 15, 15); // android:checked="true"
                if (filterObj.optInt("vehicleType") == (taxiTypeArray.optJSONObject(i).getInt("taxiTypeId")))
                    radioButton[i].setClickable(true);
//                radioButton[i].setChecked(true);
                tableRow[j].addView(radioButton[i]);
                if ((i + 1) % 2 == 0) {
                    Log.i("Yes1", j + "" + i);
                    rgFilterVehicleType.addView(tableRow[j], new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                }
            }

            j = -1;
            JSONArray jsonArray = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_DISTANCE_LIST, ""));

            radioButton = new RadioButton[jsonArray.length()];
            tableRow = new TableRow[(int) Math.floor(jsonArray.length() / 2)+1];
            Log.i("Yes", jsonArray.length() + "" + tableRow.length);
            for (int i = 0; i < jsonArray.length(); i++) {
                if ((i + 1) % 2 != 0) {
                    j++;
                    Log.i("Yes", j + "" + i);
                    tableRow[j] = new TableRow(this);
                    tableRow[j].setId(j);
                    tableRow[j].setLayoutParams(params1);
                }
                radioButton[i] = new RadioButton(this);
                radioButton[i].setLayoutParams(params);
                radioButton[i].setText(jsonArray.optJSONObject(i).getString("name"));
                radioButton[i].setTextColor(getResources().getColor(R.color.white));
                radioButton[i].setId(jsonArray.optJSONObject(i).getInt("distanceId"));
                radioButton[i].setGravity(Gravity.CENTER);
                radioButton[i].setPadding(15, 15, 15, 15); // android:checked="true"
                if (filterObj.optInt("distance") == (jsonArray.optJSONObject(i).getInt("distanceId")))
                    radioButton[i].setClickable(true);
//                radioButton[i].setChecked(true);
                tableRow[j].addView(radioButton[i]);
                if ((i + 1) % 2 == 0) {
                    Log.i("Yes1", j + "" + i);
                    rgFilterDistance.addView(tableRow[j], new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(this, "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
//        NetworkResponse response = error.networkResponse;
//        if (error instanceof ServerError && response != null) {
//            try {
//                String res = new String(response.data,
//                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                // Now you can use any deserializer to make sense of data
//                JSONObject obj = new JSONObject(res);
//            } catch (UnsupportedEncodingException e1) {
//                // Couldn't properly decode data to string
//                e1.printStackTrace();
//            } catch (JSONException e2) {
//                // returned data is not JSONObject?
//                e2.printStackTrace();
//            }
//        }
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (response.getInt("__eventid")) {
                        case Constants.Events.EVENT_PARTNER_LIST:
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_TAXI_TYPE, response.optJSONObject("json").optJSONArray("taxiTypeArray").toString());
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_DISTANCE_LIST, response.optJSONObject("json").optJSONArray("distanceList").toString());
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_PARTNER_ARRAY, response.optJSONObject("json").optJSONArray("partnerArray").toString());
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_FILTER_REQUEST, response.optJSONObject("filterRequest").toString());
                            SharedPreferenceUtil.save();
                            startActivity(new Intent(this, SearchCars.class));
                            break;
                    }
                } else {
//                    JsonErrorShow.jsonErrorShow(response, this, clSearch);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

//    private void doPartnerList() {
//        final String tag = "doPartnerList";
//        StringBuilder builder = new StringBuilder();
//        String strDate = (String) tvBookDateTime.getTag();
////        strDate = strDate.replace(" ", "%20");
//        tvBookDateTime.setTag(strDate);
//        JSONObject jsonData = new JSONObject();
//
//        try {
//            JSONObject fromData = new JSONObject(SharedPreferenceUtil.getString(Constants.FROM_ADDRESS, ""));
//            JSONObject toData = new JSONObject(SharedPreferenceUtil.getString(Constants.TO_ADDRESS, ""));
//            JSONObject filterRequest = new JSONObject();
//            JSONArray viaArea = new JSONArray();
//            if (SharedPreferenceUtil.contains(Constants.VIA_ADDRESS))
//                viaArea.put(new JSONObject(SharedPreferenceUtil.getString(Constants.VIA_ADDRESS, "")));
//            if (SharedPreferenceUtil.contains(Constants.VIA2_ADDRESS))
//                viaArea.put(new JSONObject(SharedPreferenceUtil.getString(Constants.VIA2_ADDRESS, "")));
//
//            builder.append(jsonData.put("fromAreaName", fromData.optString("AreaName")).put("fromAreaLat", fromData.optString("AreaLat")).put("fromAreaLong", fromData.optString("AreaLong")).put("fromAreaPlaceid", fromData.optString("AreaPlaceid")).put("fromAreaAddress", fromData.optString("AreaAddress")).put("fromAreaCity", fromData.optString("AreaCity")).put("fromAreaPostalCode", fromData.optString("AreaPostalCode")).put("toAreaName", toData.optString("AreaName")).put("toAreaLat", toData.optString("AreaLat")).put("toAreaLong", toData.optString("AreaLong")).put("toAreaPlaceid", toData.optString("AreaPlaceid")).put("toAreaAddress", toData.optString("AreaAddress")).put("toAreaCity", toData.optString("AreaCity")).put("toAreaPostalCode", toData.optString("AreaPostalCode")).put("viaArea", viaArea)
//                    .put("journeyDatetime", (String) tvBookDateTime.getTag()).put("luggageId", (String) tvBookLuggage.getTag()).put("passanger", (String) tvBookPassenger.getTag()).put("bookingduration", "1").put("pageSize", "1").put("pageNumber", "1").put("sortField", "rating").put("sortOrder", "asc").put("filterRequest", filterRequest).put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "")).put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        String url = "";
//        try {
//            url = Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.EVENT_PARTNER_LIST + "&json="
//                    + URLEncoder.encode(builder.toString(), "UTF8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        CommonUtil.showProgressDialog(getActivity(), "Login...");
//        DataRequest dataRequest = new DataRequest(Request.Method.GET, url, null, this, this);
//        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
//    }
}