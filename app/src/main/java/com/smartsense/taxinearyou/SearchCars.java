package com.smartsense.taxinearyou;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.TimeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class SearchCars extends TimeActivity implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {

    ListView lvSearchCarsLine1;
    TextView tvSearchCarsFilter, tvSearchCarsDateTime, tvSearchNoPartnerFound;
    RadioButton rbSearchCarsRating, rbSearchCarsAvailability, rbSearchCarsPriceRange;
    RadioGroup rbgSearchCars;
    ImageView ivSearchCarsFront, ivSearchCarsBack;
    LinearLayout llSearchCarsFilter, lySearchCarsDateTime, llSearchCarsNoPartner;
    private Menu menu;
    String a = "Rating" + (char) 0x2191;
    String b = "Price Range" + (char) 0x2191;
    String c = "Availability" + (char) 0x2191;
    Toolbar toolbarAll;
    CoordinatorLayout clSearchCars;
    private String bookingduration;
    AdapterSearchCar adapterSearchCar;

    int pageNumber = 0;
    int totalRecord = 0;
    int pageSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_cars);
        adapterSearchCar = null;
        pageNumber = 0;
        totalRecord = 0;
        pageSize = SharedPreferenceUtil.getInt(Constants.PAGE_LIMIT,9);
        clSearchCars = (CoordinatorLayout) findViewById(R.id.clSearchCars);
        lvSearchCarsLine1 = (ListView) findViewById(R.id.lvSearchCarsLine1);
        tvSearchCarsFilter = (TextView) findViewById(R.id.tvSearchCarsFilter);
        rbgSearchCars = (RadioGroup) findViewById(R.id.rbgSearchCars);
        rbSearchCarsRating = (RadioButton) findViewById(R.id.rbSearchCarsRating);
        rbSearchCarsAvailability = (RadioButton) findViewById(R.id.rbSearchCarsAvailability);
        rbSearchCarsPriceRange = (RadioButton) findViewById(R.id.rbSearchCarsPriceRange);
        tvSearchCarsDateTime = (TextView) findViewById(R.id.tvSearchCarsDateTime);
        tvSearchNoPartnerFound = (TextView) findViewById(R.id.tvSearchNoPartnerFound);
        ivSearchCarsFront = (ImageView) findViewById(R.id.ivSearchCarsFront);
        ivSearchCarsBack = (ImageView) findViewById(R.id.ivSearchCarsBack);
        llSearchCarsFilter = (LinearLayout) findViewById(R.id.llSearchCarsFilter);
        lySearchCarsDateTime = (LinearLayout) findViewById(R.id.lySearchCarsDateTime);
        llSearchCarsNoPartner = (LinearLayout) findViewById(R.id.llSearchCarsNoPartner);

        try {
            tvSearchCarsDateTime.setText(Constants.DATE_FORMAT_SET.format(Constants.DATE_FORMAT_SEND.parse(getIntent().getStringExtra("tvBookDateTime"))) + " " + (getIntent().getStringExtra("duration").equalsIgnoreCase("1") ? "Now" : getIntent().getStringExtra("duration").equalsIgnoreCase("2") ? "Today" : "Tomorrow"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        llSearchCarsFilter.setOnClickListener(this);
        rbSearchCarsRating.setOnClickListener(this);
        rbSearchCarsAvailability.setOnClickListener(this);
        rbSearchCarsPriceRange.setOnClickListener(this);
        tvSearchCarsFilter.setOnClickListener(this);
        lySearchCarsDateTime.setOnClickListener(this);
        rbSearchCarsPriceRange.setChecked(true);
        rbSearchCarsPriceRange.setText("Price Range" + (char) 0x2191);

        SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_FILTER_REQUEST);
        SharedPreferenceUtil.save();
        doPartnerList(pageNumber);


//        countDownStart(timeRemaining);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_search_cars;
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
                doPartnerList(0);
                break;
            case R.id.rbSearchCarsAvailability:
                rbSearchCarsPriceRange.setText("Price Range");
                rbSearchCarsRating.setText("Rating");
                if (rbSearchCarsAvailability.getText().toString().equalsIgnoreCase(c))
                    rbSearchCarsAvailability.setText("Availability" + (char) 0x2193);
                else
                    rbSearchCarsAvailability.setText("Availability" + (char) 0x2191);
                doPartnerList(0);
                break;
            case R.id.rbSearchCarsPriceRange:
                rbSearchCarsRating.setText("Rating");
                rbSearchCarsAvailability.setText("Availability");
                if (rbSearchCarsPriceRange.getText().toString().equalsIgnoreCase(b))
                    rbSearchCarsPriceRange.setText("Price Range" + (char) 0x2193);
                else
                    rbSearchCarsPriceRange.setText("Price Range" + (char) 0x2191);
                doPartnerList(0);
                break;
            case R.id.lySearchCarsDateTime:
                finish();
                break;
        }
    }

    private void doPartnerList(int pageNumber) {
        if (pageNumber == 0) {
            adapterSearchCar = null;
        }
        final String tag = "doPartnerList";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();
        JSONObject jsonDataForSet = new JSONObject();
        try {
            JSONObject fromData = new JSONObject(SharedPreferenceUtil.getString(Constants.FROM_ADDRESS, ""));
            JSONObject toData = new JSONObject(SharedPreferenceUtil.getString(Constants.TO_ADDRESS, ""));
            JSONObject filterRequest;
            if (SharedPreferenceUtil.contains(Constants.PrefKeys.PREF_FILTER_REQUEST))
                filterRequest = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_FILTER_REQUEST, ""));
            else
                filterRequest = new JSONObject();
            String sortOrder = "", sortField = "";
            if (filterRequest.has("bookingType"))
                bookingduration = filterRequest.optString("bookingType");
            else
                bookingduration = "1";
            JSONArray viaArea = new JSONArray();
            if (SharedPreferenceUtil.contains(Constants.VIA_ADDRESS) && !SharedPreferenceUtil.getString(Constants.VIA_ADDRESS, "").equalsIgnoreCase(""))
                viaArea.put(new JSONObject(SharedPreferenceUtil.getString(Constants.VIA_ADDRESS, "")));
            if (SharedPreferenceUtil.contains(Constants.VIA2_ADDRESS) && !SharedPreferenceUtil.getString(Constants.VIA2_ADDRESS, "").equalsIgnoreCase(""))
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
            builder.append(jsonData.put("fromAreaName", fromData.optString("viaAreaName"))
                    .put("fromAreaLat", fromData.optString("viaAreaLat"))
                    .put("fromAreaLong", fromData.optString("viaAreaLong"))
                    .put("fromAreaPlaceid", fromData.optString("viaAreaPlaceid"))
                    .put("fromAreaAddress", fromData.optString("viaAreaAddress"))
                    .put("fromAreaCity", fromData.optString("viaAreaCity"))
                    .put("fromAreaPostalCode", fromData.optString("viaAreaPostalCode"))
                    .put("toAreaName", toData.optString("viaAreaName"))
                    .put("toAreaLat", toData.optString("viaAreaLat"))
                    .put("toAreaLong", toData.optString("viaAreaLong"))
                    .put("toAreaPlaceid", toData.optString("viaAreaPlaceid"))
                    .put("toAreaAddress", toData.optString("viaAreaAddress"))
                    .put("toAreaCity", toData.optString("viaAreaCity"))
                    .put("toAreaPostalCode", toData.optString("viaAreaPostalCode"))
                    .put("viaArea", viaArea)
                    .put("journeyDatetime", getIntent().getStringExtra("tvBookDateTime"))
                    .put("luggageId", getIntent().getStringExtra("tvBookLuggage"))
                    .put("passanger", getIntent().getStringExtra("tvBookPassenger"))
                    .put("bookingduration", getIntent().getStringExtra("duration"))
                    .put("sortField", sortField)
                    .put("sortOrder", sortOrder)
                    .put("filterRequest", filterRequest)
                    .put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("offset", pageNumber)
                    .put("pageSize", pageSize));

            jsonDataForSet.put("fromAreaName", fromData.optString("viaAreaName"))
                    .put("fromAreaLat", fromData.optString("viaAreaLat"))
                    .put("fromAreaLong", fromData.optString("viaAreaLong"))
                    .put("fromAreaPlaceid", fromData.optString("viaAreaPlaceid"))
                    .put("fromAreaAddress", fromData.optString("viaAreaAddress"))
                    .put("fromAreaCity", fromData.optString("viaAreaCity"))
                    .put("fromAreaPostalCode", fromData.optString("viaAreaPostalCode"))
                    .put("toAreaName", toData.optString("viaAreaName"))
                    .put("toAreaLat", toData.optString("viaAreaLat"))
                    .put("toAreaLong", toData.optString("viaAreaLong"))
                    .put("toAreaPlaceid", toData.optString("viaAreaPlaceid"))
                    .put("toAreaAddress", toData.optString("viaAreaAddress"))
                    .put("toAreaCity", toData.optString("viaAreaCity"))
                    .put("toAreaPostalCode", toData.optString("viaAreaPostalCode"))
                    .put("viaArea", viaArea)
                    .put("journeyDatetime", getIntent().getStringExtra("tvBookDateTime"))
                    .put("luggageId", getIntent().getStringExtra("tvBookLuggage"))
                    .put("passanger", getIntent().getStringExtra("tvBookPassenger"))
                    .put("bookingduration", getIntent().getStringExtra("duration"))
                    .put("luggageDescription", getIntent().getStringExtra("luggageDescription"))
                    .put("passengerDescription", getIntent().getStringExtra("passengerDescription"));

            Log.i("params", jsonDataForSet.toString());
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_REQUEST_JSON, jsonDataForSet.toString());
            SharedPreferenceUtil.save();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        CommonUtil.jsonRequestGET(this, getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.EVENT_PARTNER_LIST), tag, this, this);
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (response.getInt("__eventid")) {
                        case Constants.Events.EVENT_PARTNER_LIST:
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.MAIN_DATA, response.toString());
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.DISTANCE_AFTER_CONVERT, response.optJSONObject("json").optJSONObject("distanceMatrix").optInt("duration") / 3600 + ":" + (response.optJSONObject("json").optJSONObject("distanceMatrix").optInt("duration") / 60) % 60 + " hours");
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_TAXI_TYPE, response.optJSONObject("json").optJSONArray("taxiTypeArray").toString());
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_DISTANCE_LIST, response.optJSONObject("json").optJSONArray("distanceList").toString());
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_PARTNER_ARRAY, response.optJSONObject("json").optJSONArray("partnerArray").toString());
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_FILTER_REQUEST, response.optJSONObject("filterRequest").toString());
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_DISTANCE_MATRIX, response.optJSONObject("json").optJSONObject("distanceMatrix").toString());
                            SharedPreferenceUtil.save();
                            totalRecord = response.optJSONObject("json").optInt("totalRecords");
                            fillPartnerList(response.optJSONObject("json").optJSONArray("partnerArray"));

                            if (response.optJSONObject("json").optJSONArray("partnerArray").length() > 0) {
                                llSearchCarsNoPartner.setVisibility(View.GONE);
                                lvSearchCarsLine1.setVisibility(View.VISIBLE);
                            } else {
                                llSearchCarsNoPartner.setVisibility(View.VISIBLE);
                                lvSearchCarsLine1.setVisibility(View.GONE);
                                tvSearchNoPartnerFound.setText(response.optString("msg"));
                            }

                            break;
                    }
                } else {
                    CommonUtil.conditionAuthentication(this, response);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else CommonUtil.jsonNullError(this);
    }

    private void fillPartnerList(JSONArray jsonArray) {
        if (adapterSearchCar == null) {
            adapterSearchCar = new AdapterSearchCar(SearchCars.this, jsonArray, bookingduration);
            lvSearchCarsLine1.setAdapter(adapterSearchCar);
        } else {
            adapterSearchCar.adapterSearchCar(jsonArray);
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.errorToastShowing(this);
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
            lvSearchCarsLine1.setVisibility(View.VISIBLE);
            llSearchCarsNoPartner.setVisibility(View.GONE);
            rbSearchCarsPriceRange.setText("Price Range" + (char) 0x2193);
            rbSearchCarsAvailability.setText("Availability");
            rbSearchCarsRating.setText("Rating");
            rbSearchCarsPriceRange.setChecked(true);
            rbSearchCarsAvailability.setChecked(false);
            rbSearchCarsRating.setChecked(false);
            doPartnerList(0);
        }
    }


    public class AdapterSearchCar extends BaseAdapter {
        private JSONArray data;
        private LayoutInflater inflater = null;
        String bookingduration;
        Activity a;

        public AdapterSearchCar(Activity a, JSONArray data, String bookingduration) {
            this.data = data;
            this.a = a;
            this.bookingduration = bookingduration;
            inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void adapterSearchCar(JSONArray data) {
            for (int i = 0; i < data.length(); i++) {
                this.data.put(data.optJSONObject(i));
            }
            notifyDataSetChanged();
        }

        public int getCount() {
            return data.length();
        }

        public Object getItem(int position) {
            return this.data.optJSONObject(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, final View convertView, ViewGroup parent) {
            View vi = convertView;
            if (convertView == null)

                vi = inflater.inflate(R.layout.element_search_cars, null);
            final JSONObject test = data.optJSONObject(position);

            final TextView tvElementSearchCarsName = (TextView) vi.findViewById(R.id.tvElementSearchCarsName);
            final TextView tvElementSearchCarsWaitingTime = (TextView) vi.findViewById(R.id.tvElementSearchCarsWaitingTime);
            final TextView tvElementSearchCarsChat = (TextView) vi.findViewById(R.id.tvElementSearchCarsChat);
            final TextView tvElementSearchCarsMoney = (TextView) vi.findViewById(R.id.tvElementSearchCarsMoney);
            TextView tvSearchCarsBookNow = (TextView) vi.findViewById(R.id.tvSearchCarsBookNow);
            LinearLayout llElementSearchCarsMain = (LinearLayout) vi.findViewById(R.id.llElementSearchCarsMain);
            ImageView ivElementSearchCarsOnline = (ImageView) vi.findViewById(R.id.ivElementSearchCarsOnline);
            RatingBar rbElementSearchCars = (RatingBar) vi.findViewById(R.id.rbElementSearchCars);

            final ArrayList<String> rating = new ArrayList<>();

            for (int i = 0; i < test.optJSONArray("partnerRating").length(); i++)
                rating.add(test.optJSONArray("partnerRating").optJSONObject(i).optString("ccount"));

            llElementSearchCarsMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    a.startActivity(new Intent(a, PartnerDetails.class).putExtra("customerSelection", SharedPreferenceUtil.getString(Constants.PrefKeys.DISTANCE_AFTER_CONVERT, ""))
                            .putExtra("ETA", "£" + CommonUtil.getDecimal(test.optDouble("ETA")))
                            .putExtra("partnerName", test.optString("partnerName"))
                            .putExtra("taxiTypeName", test.optJSONObject("taxiType").optString("taxiTypeName"))
                            .putExtra("waitingTime", tvElementSearchCarsWaitingTime.getText().toString())
                            .putExtra("rating", rating)
                            .putExtra("available", test.optInt("availability"))
                            .putExtra("partnerId", (String) tvElementSearchCarsChat.getTag())
                            .putExtra("logoPath", test.optString("logoPath")));

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("partnerTaxiTypeId", test.optJSONObject("taxiType").optInt("partnerTaxiTypeId"));
                        jsonObject.put("partnerName", test.optString("partnerName"));
                        jsonObject.put("distance", test.optString("distance"));
                        jsonObject.put("price", test.optString("ETA"));
                        jsonObject.put("taxiTypeName", test.optJSONObject("taxiType").optString("taxiTypeName"));
                        jsonObject.put("partnerId", test.optJSONObject("taxiType").optString("partnerId"));
                        jsonObject.put("tripType", bookingduration);
                        jsonObject.put("duration", SharedPreferenceUtil.getString(Constants.PrefKeys.DISTANCE_AFTER_CONVERT, ""));
                        jsonObject.put("taxiTypeId", test.optJSONObject("taxiType").optString("taxiTypeId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_CUSTOMER_SELECTION, jsonObject.toString());
                    SharedPreferenceUtil.save();
                }
            });

            tvSearchCarsBookNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("partnerTaxiTypeId", test.optJSONObject("taxiType").optInt("partnerTaxiTypeId"));
                        jsonObject.put("partnerName", test.optString("partnerName"));
                        jsonObject.put("distance", test.optString("distance"));
                        jsonObject.put("price", test.optString("ETA"));
                        jsonObject.put("taxiTypeName", test.optJSONObject("taxiType").optString("taxiTypeName"));
                        jsonObject.put("partnerId", test.optJSONObject("taxiType").optString("partnerId"));
                        jsonObject.put("tripType", bookingduration);
                        jsonObject.put("duration", SharedPreferenceUtil.getString(Constants.PrefKeys.DISTANCE_AFTER_CONVERT, ""));
                        jsonObject.put("taxiTypeId", test.optJSONObject("taxiType").optString("taxiTypeId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_CUSTOMER_SELECTION, jsonObject.toString());
                    SharedPreferenceUtil.save();

                    if (SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_STATUS, "").equalsIgnoreCase("1"))
                        a.startActivity(new Intent(a, BookingInfo.class));
                    else
                        CommonUtil.alertBox(a, a.getResources().getString(R.string.msg_activate_account));
                }
            });

            if (test.optJSONObject("taxiType").optInt("status") == 0)
                tvElementSearchCarsWaitingTime.setText("(10 to 20 minutes for a taxi)");
            else
                tvElementSearchCarsWaitingTime.setText("(20 to 45 minutes for a taxi)");

            tvElementSearchCarsChat.setText(test.optJSONObject("taxiType").optString("taxiTypeName"));
            tvElementSearchCarsChat.setTag(test.optJSONObject("taxiType").optString("partnerId"));

            if (test.optInt("availability") == 0)
                ivElementSearchCarsOnline.setImageResource(R.mipmap.online);
            else
                ivElementSearchCarsOnline.setImageResource(R.mipmap.ic_image_brightness_1);

            rbElementSearchCars.setRating(test.optInt("rating"));
            tvElementSearchCarsName.setText(test.optString("partnerName"));
            tvElementSearchCarsMoney.setText("£" + CommonUtil.getDecimal(test.optDouble("ETA")));
            Log.i("Yes", totalRecord + " " + data.length());
            if ((position + 1) == data.length() && totalRecord != data.length()) {
                doPartnerList(data.length());
            }
            return vi;
        }
    }



}
