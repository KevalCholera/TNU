package com.smartsense.taxinearyou.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.TripDetails;
import com.smartsense.taxinearyou.utill.CircleImageView1;

import org.json.JSONArray;
import org.json.JSONObject;


public class AdapterMyTrips extends BaseAdapter {
    private JSONArray data;
    private LayoutInflater inflater = null;
    int red, green, yellow;
    Activity a;
    LinearLayout lyElementMyTripLeft, lyElementMyTripStatusLayout;
    int finalHeight;
    private int borderWidth;

    public AdapterMyTrips(Activity a, JSONArray data) {
        this.data = data;
        this.a = a;
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


    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)

            vi = inflater.inflate(R.layout.element_my_trips, null);
        red = vi.getResources().getColor(R.color.red);
        green = vi.getResources().getColor(R.color.green);
        yellow = vi.getResources().getColor(R.color.Yellow);

        TextView tvElementMyTripsAmount = (TextView) vi.findViewById(R.id.tvElementMyTripsAmount);
        TextView tvElementMyTripsFrom = (TextView) vi.findViewById(R.id.tvElementMyTripsFrom);
        TextView tvElementMyTripsTo = (TextView) vi.findViewById(R.id.tvElementMyTripsTo);
        final TextView tvElementMyTripsStatus = (TextView) vi.findViewById(R.id.tvElementMyTripsStatus);
        TextView tvElementMyTripsTaxiProvider = (TextView) vi.findViewById(R.id.tvElementMyTripsTaxiProvider);
        TextView tvElementMyTripsDateTime = (TextView) vi.findViewById(R.id.tvElementMyTripsDateTime);
        LinearLayout lyElementMyTripMain = (LinearLayout) vi.findViewById(R.id.lyElementMyTripMain);

        lyElementMyTripStatusLayout = (LinearLayout) vi.findViewById(R.id.lyElementMyTripStatusLayout);
        lyElementMyTripLeft = (LinearLayout) vi.findViewById(R.id.lyElementMyTripLeft);
        JSONObject test = data.optJSONObject(position);
        Log.i("Test", test.toString());

        tvElementMyTripsAmount.setText(test.optString("Amount"));
        tvElementMyTripsFrom.setText(test.optString("From"));
        tvElementMyTripsTo.setText(test.optString("To"));
        tvElementMyTripsStatus.setText(test.optString("Status"));
        tvElementMyTripsTaxiProvider.setText(test.optString("Taxi_Provider"));
        tvElementMyTripsDateTime.setText(test.optString("Date_Time"));

        if (tvElementMyTripsStatus.getText().toString().equals("Cancelled"))
            tvElementMyTripsStatus.setBackgroundColor(red);
        else if (tvElementMyTripsStatus.getText().toString().equals("Completed"))
            tvElementMyTripsStatus.setBackgroundColor(green);
        else
            tvElementMyTripsStatus.setBackgroundColor(yellow);

        ViewTreeObserver vto = lyElementMyTripStatusLayout.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                lyElementMyTripStatusLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                finalHeight = lyElementMyTripStatusLayout.getMeasuredHeight();
                Log.i("Height", String.valueOf(finalHeight));

                int height = finalHeight / 30;
                Log.i("Height1", String.valueOf(height));
                for (int i = 0; i <= height; i++) {
                    lyElementMyTripLeft.addView(createNewTextView());
                }

                return true;
            }
        });

        lyElementMyTripMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(a, TripDetails.class);
                myIntent.putExtra("key", tvElementMyTripsStatus.getText().toString());
                a.startActivity(myIntent);
            }
        });
        return vi;
    }

    private CircleImageView1 createNewTextView() {
        CircleImageView1 circleImageView = new CircleImageView1(a);
        circleImageView.setMaxWidth((int) a.getResources().getDimension(R.dimen.tvMarginTopBottomStartEndAll));
        circleImageView.setMaxHeight((int) a.getResources().getDimension(R.dimen.tvMarginTopBottomStartEndAll));
        circleImageView.setBackgroundColor(a.getResources().getColor(R.color.white));
        circleImageView.setBorderColor(a.getResources().getColor(R.color.element));
        circleImageView.setBorderWidth((int) a.getResources().getDimension(R.dimen.civTripDetailsBorderWidth));
        return circleImageView;
    }
}
