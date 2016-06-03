package com.smartsense.taxinearyou.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AdapterMyTrips extends BaseAdapter {
    private JSONArray data;
    private LayoutInflater inflater = null;
    Activity a;
    LinearLayout lyElementMyTripLeft, lyElementMyTripRight, lyElementMyTripStatusLayout;
    int finalHeight;

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

        TextView tvElementMyTripsAmount = (TextView) vi.findViewById(R.id.tvElementMyTripsAmount);
        TextView tvElementMyTripsFrom = (TextView) vi.findViewById(R.id.tvElementMyTripsFrom);
        TextView tvElementMyTripsTo = (TextView) vi.findViewById(R.id.tvElementMyTripsTo);
        final TextView tvElementMyTripsStatus = (TextView) vi.findViewById(R.id.tvElementMyTripsStatus);
        TextView tvElementMyTripsTaxiProvider = (TextView) vi.findViewById(R.id.tvElementMyTripsTaxiProvider);
        TextView tvElementMyTripsDateTime = (TextView) vi.findViewById(R.id.tvElementMyTripsDateTime);
        LinearLayout lyElementMyTripMain = (LinearLayout) vi.findViewById(R.id.lyElementMyTripMain);

        lyElementMyTripStatusLayout = (LinearLayout) vi.findViewById(R.id.lyElementMyTripStatusLayout);
        lyElementMyTripLeft = (LinearLayout) vi.findViewById(R.id.lyElementMyTripLeft);
        lyElementMyTripRight = (LinearLayout) vi.findViewById(R.id.lyElementMyTripRight);
        final JSONObject test = data.optJSONObject(position);
        Log.i("Test", test.toString());

        tvElementMyTripsAmount.setText("£" + test.optString("estimatedAmount"));
        tvElementMyTripsFrom.setText(test.optString("from"));
        tvElementMyTripsTo.setText(test.optString("to"));
        tvElementMyTripsStatus.setText(test.optString("status"));
        tvElementMyTripsTaxiProvider.setText(test.optString("partner"));
        try {
            tvElementMyTripsDateTime.setText(new SimpleDateFormat("dd-MM-yyyy \n hh:mm aa").format(new SimpleDateFormat("dd-MMM-yyyy HH:mm").parse(test.optString("bookingTime"))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (test.optString("status").equals("Cancelled"))
            tvElementMyTripsStatus.setBackgroundColor(ContextCompat.getColor(a, R.color.red));
        else if (tvElementMyTripsStatus.getText().toString().equals("Complete"))
            tvElementMyTripsStatus.setBackgroundColor(ContextCompat.getColor(a, R.color.green));
        else
            tvElementMyTripsStatus.setBackgroundColor(ContextCompat.getColor(a, R.color.Yellow));

        return vi;
    }
}