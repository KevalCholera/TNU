package com.smartsense.taxinearyou.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.smartsense.taxinearyou.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class AdapterCard extends BaseAdapter {
    private JSONArray data;
    private LayoutInflater inflater = null;
    Activity a;

    public AdapterCard(Activity a, JSONArray data) {
        this.data = data;
        this.a = a;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.length();
    }


    public Object getItem(int position) {
        return data.optJSONObject(position);
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)

            vi = inflater.inflate(R.layout.element_card, null);
        TextView tvElementCardListCardNumber = (TextView) vi.findViewById(R.id.tvElementCardListCardNumber);
        TextView tvElementCardListExpiryDate = (TextView) vi.findViewById(R.id.tvElementCardListExpiryDate);
        TextView tvElementCardListRemoveCard = (TextView) vi.findViewById(R.id.tvElementCardListRemoveCard);

        JSONObject test = data.optJSONObject(position);
        Log.i("Test", test.toString());

        tvElementCardListCardNumber.setText("xxxx xxxx xxxx " + test.optString("last4"));
        tvElementCardListExpiryDate.setText(test.optString("exp_month") + " - " + test.optString("exp_year"));
        tvElementCardListRemoveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(a, "CLick", Toast.LENGTH_SHORT).show();
            }
        });
        return vi;
    }
}