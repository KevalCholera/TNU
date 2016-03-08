package com.smartsense.taxinearyou.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

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

        Button btElementCards = (Button) vi.findViewById(R.id.btElementCards);
        LinearLayout lyElementCards = (LinearLayout) vi.findViewById(R.id.lyElementCards);

        JSONObject test = data.optJSONObject(position);
        Log.i("Test", test.toString());

        btElementCards.setText("Card xx" + test.optString("Card"));

        return vi;
    }
}