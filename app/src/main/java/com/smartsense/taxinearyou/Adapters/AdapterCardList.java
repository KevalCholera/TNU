package com.smartsense.taxinearyou.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.smartsense.taxinearyou.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class AdapterCardList extends BaseAdapter {
    private JSONArray data;
    private LayoutInflater inflater = null;
    Activity a;
    public static int pos=-1;

    public AdapterCardList(Activity a, JSONArray data) {
        this.data = data;
        this.a = a;
        Log.i("Yes","Here");
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.length();
    }

    public void adapterCardList() {
         notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return data.optJSONObject(position);
    }


    public long getItemId(int position) {
        return position;
    }



    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        Log.i("Yes","Here1");
        if (convertView == null)
            vi = inflater.inflate(R.layout.element_card_list, null);
        TextView tvElementCardListCardNumber;
        final CheckBox check = (CheckBox) vi.findViewById(R.id.cbCardSave);
        if (pos == position)
            check.setChecked(true);
        else
            check.setChecked(false);
        tvElementCardListCardNumber = (TextView) vi.findViewById(R.id.tvElementCardListCardNumber);
        JSONObject test = data.optJSONObject(position);
        tvElementCardListCardNumber.setText("xxxx xxxx xxxx " + test.optString("last4"));
        return vi;
    }
}