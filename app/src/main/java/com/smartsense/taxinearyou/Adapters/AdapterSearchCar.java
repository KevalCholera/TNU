package com.smartsense.taxinearyou.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.BookingInfo;
import com.smartsense.taxinearyou.PartnerDetails;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public int getCount() {
        return data.length();
    }

    public Object getItem(int position) {
        return position;
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
                        .putExtra("ETA", tvElementSearchCarsMoney.getText().toString())
                        .putExtra("partnerName", tvElementSearchCarsName.getText().toString())
                        .putExtra("taxiTypeName",test.optJSONObject("taxiType").optString("taxiTypeName"))
                        .putExtra("waitingTime", tvElementSearchCarsWaitingTime.getText().toString())
                        .putExtra("rating", rating)
                        .putExtra("partnerId", (String) tvElementSearchCarsChat.getTag())
                        .putExtra("logoPath", test.optString("logoPath")));
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
                Log.i("Partner", jsonObject.toString());

                SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_CUSTOMER_SELECTION, jsonObject.toString());
                SharedPreferenceUtil.save();

                if (SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_STATUS, "").equalsIgnoreCase("1"))
                    a.startActivity(new Intent(a, BookingInfo.class));
                else
                    CommonUtil.alertBox(a, a.getResources().getString(R.string.msg_activate_account), false, false);
            }
        });

        if (test.optJSONObject("taxiType").optInt("status") == 0)
            tvElementSearchCarsWaitingTime.setText("(10 to 20 minutes for a taxi)");
        else
            tvElementSearchCarsWaitingTime.setText("(20 to 45 minutes for a taxi)");

        tvElementSearchCarsChat.setText(test.optJSONObject("taxiType").optString("taxiTypeName"));
        tvElementSearchCarsChat.setTag(test.optJSONObject("taxiType").optString("partnerId"));

        if (test.optInt("availability") == 0)
            ivElementSearchCarsOnline.setImageResource(android.R.drawable.presence_online);
        else
            ivElementSearchCarsOnline.setImageResource(R.mipmap.ic_image_brightness_1);

        rbElementSearchCars.setRating(test.optInt("rating"));
        tvElementSearchCarsName.setText(test.optString("partnerName"));
        tvElementSearchCarsMoney.setText("£" + test.optString("ETA") + ".00");

        return vi;
    }
}