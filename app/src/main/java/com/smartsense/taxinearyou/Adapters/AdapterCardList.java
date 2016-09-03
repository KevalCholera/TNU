package com.smartsense.taxinearyou.Adapters;

import android.app.Activity;
import android.content.Context;
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
    public static int pos;

    public AdapterCardList(Activity a, JSONArray data) {
        this.data = data;
        this.a = a;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.length();
    }

    public void adapterCardList() {
         notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return position;
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)

            vi = inflater.inflate(R.layout.element_card_list, null);

        TextView tvElementCardListCardNumber, tvElementCardListExpiryDate, tvElementCardListEditCard, tvElementCardListRemoveCard;
        final CheckBox check = (CheckBox) vi.findViewById(R.id.cbCardSave);
        if (pos == position)
            check.setChecked(true);
        else
            check.setChecked(false);
        tvElementCardListCardNumber = (TextView) vi.findViewById(R.id.tvElementCardListCardNumber);
        tvElementCardListCardNumber = (TextView) vi.findViewById(R.id.tvElementCardListCardNumber);
//        tvElementCardListExpiryDate = (TextView) vi.findViewById(R.id.tvElementCardListExpiryDate);
//        tvElementCardListEditCard = (TextView) vi.findViewById(R.id.tvElementCardListEditCard);
//        tvElementCardListRemoveCard = (TextView) vi.findViewById(R.id.tvElementCardListRemoveCard);

        JSONObject test = data.optJSONObject(position);


        tvElementCardListCardNumber.setText("xxxx xxxx xxxx " + test.optString("last4"));
//        tvElementCardListExpiryDate.setText(test.optString("Expiry_Date"));




        return vi;
    }
}