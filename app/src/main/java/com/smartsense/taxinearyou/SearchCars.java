package com.smartsense.taxinearyou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.Adapters.AdapterSearchCar;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.DataRequest;
import com.smartsense.taxinearyou.utill.JsonErrorShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class SearchCars extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {

    ListView lvSearchCarsLine1;
    TextView tvSearchCarsFilter, tvSearchCarsDateTime;
    RadioButton rbSearchCarsRating, rbSearchCarsAvailability, rbSearchCarsPriceRange;
    RadioGroup rbgSearchCars;
    ImageView ivSearchCarsFront, ivSearchCarsBack;
    LinearLayout llSearchCarsFilter, lySearchCarsDateTime;

    String a = "Rating" + (char) 0x2191;
    String b = "Price Range" + (char) 0x2191;
    String c = "Availability" + (char) 0x2191;
    Toolbar toolbarAll;
    CoordinatorLayout clSearchCars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_cars);
        toolbarAll = (Toolbar) findViewById(R.id.toolbarAll);
        setSupportActionBar(toolbarAll);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        clSearchCars = (CoordinatorLayout) findViewById(R.id.clSearchCars);
        lvSearchCarsLine1 = (ListView) findViewById(R.id.lvSearchCarsLine1);
        tvSearchCarsFilter = (TextView) findViewById(R.id.tvSearchCarsFilter);
        rbgSearchCars = (RadioGroup) findViewById(R.id.rbgSearchCars);
        rbSearchCarsRating = (RadioButton) findViewById(R.id.rbSearchCarsRating);
        rbSearchCarsAvailability = (RadioButton) findViewById(R.id.rbSearchCarsAvailability);
        rbSearchCarsPriceRange = (RadioButton) findViewById(R.id.rbSearchCarsPriceRange);
        tvSearchCarsDateTime = (TextView) findViewById(R.id.tvSearchCarsDateTime);
        ivSearchCarsFront = (ImageView) findViewById(R.id.ivSearchCarsFront);
        ivSearchCarsBack = (ImageView) findViewById(R.id.ivSearchCarsBack);
        llSearchCarsFilter = (LinearLayout) findViewById(R.id.llSearchCarsFilter);
        lySearchCarsDateTime = (LinearLayout) findViewById(R.id.lySearchCarsDateTime);

//        String data = "[{\"name\":\"Keval Cholera\",\"time\":\"(pls wait 40 min)\",\"address\":\"Hello Keval\",\"price\":40000,\"submit\":\"Book Now\"},{\"name\":\"Keval Cholera\",\"time\":\"(pls wait 40 min)\",\"address\":\"Hello Keval\",\"price\":40000,\"submit\":\"Book Now\"},{\"name\":\"Keval Cholera\",\"time\":\"(pls wait 40 min)\",\"address\":\"Hello Keval\",\"price\":40000,\"submit\":\"Book Now\"},{\"name\":\"Keval Cholera\",\"time\":\"(pls wait 40 min)\",\"address\":\"Hello Keval\",\"price\":40000,\"submit\":\"Book Now\"}]";


        llSearchCarsFilter.setOnClickListener(this);
        rbSearchCarsRating.setOnClickListener(this);
        rbSearchCarsAvailability.setOnClickListener(this);
        rbSearchCarsPriceRange.setOnClickListener(this);
        tvSearchCarsFilter.setOnClickListener(this);
        lySearchCarsDateTime.setOnClickListener(this);
        rbSearchCarsPriceRange.setChecked(true);
        rbSearchCarsPriceRange.setText("Price Range" + (char) 0x2191);

        doPartnerList();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSearchCarsFilter:
                startActivityForResult(new Intent(SearchCars.this, Filter.class), 0);
                break;
            case R.id.llSearchCarsFilter:
                startActivityForResult(new Intent(SearchCars.this, Filter.class), 0);
                break;
            case R.id.rbSearchCarsRating:
                rbSearchCarsPriceRange.setText("Price Range");
                rbSearchCarsAvailability.setText("Availability");
                if (rbSearchCarsRating.getText().toString().equalsIgnoreCase(a))
                    rbSearchCarsRating.setText("Rating" + (char) 0x2193);
                else
                    rbSearchCarsRating.setText("Rating" + (char) 0x2191);
                doPartnerList();
                break;
            case R.id.rbSearchCarsAvailability:
                rbSearchCarsPriceRange.setText("Price Range");
                rbSearchCarsRating.setText("Rating");
                if (rbSearchCarsAvailability.getText().toString().equalsIgnoreCase(c))
                    rbSearchCarsAvailability.setText("Availability" + (char) 0x2193);
                else
                    rbSearchCarsAvailability.setText("Availability" + (char) 0x2191);
                doPartnerList();
                break;
            case R.id.rbSearchCarsPriceRange:
                rbSearchCarsRating.setText("Rating");
                rbSearchCarsAvailability.setText("Availability");
                if (rbSearchCarsPriceRange.getText().toString().equalsIgnoreCase(b))
                    rbSearchCarsPriceRange.setText("Price Range" + (char) 0x2193);
                else
                    rbSearchCarsPriceRange.setText("Price Range" + (char) 0x2191);
                doPartnerList();
                break;
            case R.id.lySearchCarsDateTime:
                finish();
                break;
        }
    }

    private void doPartnerList() {
        final String tag = "doPartnerList";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();
        try {
            JSONObject fromData = new JSONObject(SharedPreferenceUtil.getString(Constants.FROM_ADDRESS, ""));
            JSONObject toData = new JSONObject(SharedPreferenceUtil.getString(Constants.TO_ADDRESS, ""));
            JSONObject filterRequest;
            if (SharedPreferenceUtil.contains(Constants.PrefKeys.PREF_FILTER_REQUEST))
                filterRequest = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_FILTER_REQUEST, ""));
            else
                filterRequest = new JSONObject();
            String sortOrder = "", sortField = "", bookingduration = "0";
            if (filterRequest.has("bookingType"))
                bookingduration = filterRequest.optString("bookingType");
            else
                bookingduration = "0";
            JSONArray viaArea = new JSONArray();
            if (SharedPreferenceUtil.contains(Constants.VIA_ADDRESS))
                viaArea.put(new JSONObject(SharedPreferenceUtil.getString(Constants.VIA_ADDRESS, "")));
            if (SharedPreferenceUtil.contains(Constants.VIA2_ADDRESS))
                viaArea.put(new JSONObject(SharedPreferenceUtil.getString(Constants.VIA2_ADDRESS, "")));
            int selectedId2 = rbgSearchCars.getCheckedRadioButtonId();
            RadioButton radioButton2 = (RadioButton) findViewById(selectedId2);
            String s = "" + (char) 0x2193;
            if (radioButton2.getText().toString().contains(s))
                sortOrder = "desc";
            else
                sortOrder = "asc";
            switch (rbgSearchCars.getCheckedRadioButtonId()) {
                case R.id.rbSearchCarsRating:
                    sortField = "rating";
                    break;
                case R.id.rbSearchCarsAvailability:
                    sortField = "availability";
                    break;
                case R.id.rbSearchCarsPriceRange:
                    sortField = "ETA";
                    break;
            }
            builder.append(jsonData.put("fromAreaName", fromData.optString("viaAreaName")).put("fromAreaLat", fromData.optString("viaAreaLat"))
                    .put("fromAreaLong", fromData.optString("viaAreaLong")).put("fromAreaPlaceid", fromData.optString("viaAreaPlaceid"))
                    .put("fromAreaAddress", fromData.optString("viaAreaAddress")).put("fromAreaCity", fromData.optString("viaAreaCity"))
                    .put("fromAreaPostalCode", fromData.optString("viaAreaPostalCode")).put("toAreaName", toData.optString("viaAreaName"))
                    .put("toAreaLat", toData.optString("viaAreaLat")).put("toAreaLong", toData.optString("viaAreaLong"))
                    .put("toAreaPlaceid", toData.optString("viaAreaPlaceid")).put("toAreaAddress", toData.optString("viaAreaAddress"))
                    .put("toAreaCity", toData.optString("viaAreaCity")).put("toAreaPostalCode", toData.optString("viaAreaPostalCode"))
                    .put("viaArea", viaArea)
                    .put("journeyDatetime", getIntent().getStringExtra("tvBookDateTime")).put("luggageId", getIntent().getStringExtra("tvBookLuggage")).put("passanger", getIntent().getStringExtra("tvBookPassenger")).put("bookingduration", bookingduration).put("sortField", sortField).put("sortOrder", sortOrder).put("filterRequest", filterRequest).put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "")).put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")).put("offset", 0).put("pageSize", "10"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "";
        try {
            url = Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.EVENT_PARTNER_LIST + "&json="
                    + URLEncoder.encode(builder.toString(), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CommonUtil.showProgressDialog(SearchCars.this, "Wait...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
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
//                            JSONArray jsonArray = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PARTNER_ARRAY, ""));
                            AdapterSearchCar adapterSearchCar = new AdapterSearchCar(this, response.optJSONObject("json").optJSONArray("partnerArray"));
                            lvSearchCarsLine1.setAdapter(adapterSearchCar);
                            break;
                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, SearchCars.this, clSearchCars);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(SearchCars.this, "", getResources().getString(R.string.nointernet_try_again_msg));
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
//            }ese
//        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            doPartnerList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ratingforsearch, menu);
        return true;
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
