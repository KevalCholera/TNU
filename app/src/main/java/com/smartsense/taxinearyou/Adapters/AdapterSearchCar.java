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

import com.smartsense.taxinearyou.BookingInfo;
import com.smartsense.taxinearyou.PartnerRating;
import com.smartsense.taxinearyou.R;

import org.json.JSONArray;
import org.json.JSONObject;


public class AdapterSearchCar extends BaseAdapter {
    private JSONArray data;
    private LayoutInflater inflater = null;
    String object;
    Activity a;

    public AdapterSearchCar(Activity a, JSONArray data, String object) {
        this.data = data;
        this.a = a;
        this.object = object;
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

        TextView tvElementSearchCarsName = (TextView) vi.findViewById(R.id.tvElementSearchCarsName);
        TextView tvElementSearchCarsWaitingTime = (TextView) vi.findViewById(R.id.tvElementSearchCarsWaitingTime);
        TextView tvElementSearchCarsChat = (TextView) vi.findViewById(R.id.tvElementSearchCarsChat);
        TextView tvElementSearchCarsMoney = (TextView) vi.findViewById(R.id.tvElementSearchCarsMoney);
        TextView tvSearchCarsBookNow = (TextView) vi.findViewById(R.id.tvSearchCarsBookNow);
        LinearLayout llElementSearchCarsMain = (LinearLayout) vi.findViewById(R.id.llElementSearchCarsMain);
        ImageView ivElementSearchCarsOnline = (ImageView) vi.findViewById(R.id.ivElementSearchCarsOnline);
        RatingBar rbElementSearchCars = (RatingBar) vi.findViewById(R.id.rbElementSearchCars);
        llElementSearchCarsMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.startActivity(new Intent(a, PartnerRating.class));
            }
        });

        final JSONObject test = data.optJSONObject(position);
        Log.i("Test", test.toString());

        tvSearchCarsBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                a.startActivity(new Intent(a, BookingInfo.class));
//                a.startActivity(new Intent(a, BookingInfo.class).putExtra("object", object));
            }
        });

        if (test.optInt("isAvailable") == 1) {
            ivElementSearchCarsOnline.setImageResource(android.R.drawable.presence_online);
            tvElementSearchCarsChat.setText(test.optJSONObject("taxiType").optString("taxiTypeName"));
            tvElementSearchCarsWaitingTime.setText("(" + test.optString("fleetSize") + "min left)");
        } else {
            ivElementSearchCarsOnline.setImageResource(android.R.drawable.presence_invisible);
            tvElementSearchCarsChat.setText(test.optJSONObject("taxiType").optString("taxiTypeName"));
            tvElementSearchCarsWaitingTime.setText("(" + test.optString("fleetSize") + "min left)");
        }
        rbElementSearchCars.setRating(Float.valueOf(test.optJSONArray("partnerRating").optJSONObject(5).optString("rate")));
//        rbElementSearchCars.setRating(3);
        tvElementSearchCarsName.setText(test.optString("partnerName"));
        tvElementSearchCarsMoney.setText("£" + test.optString("ETA"));


        return vi;
    }

}