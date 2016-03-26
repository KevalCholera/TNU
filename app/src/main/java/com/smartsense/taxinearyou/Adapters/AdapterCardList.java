package com.smartsense.taxinearyou.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartsense.taxinearyou.CardDetails;
import com.smartsense.taxinearyou.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class AdapterCardList extends BaseAdapter {
    private JSONArray data;
    private LayoutInflater inflater = null;
    Activity a;

    public AdapterCardList(Activity a, JSONArray data) {
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

            vi = inflater.inflate(R.layout.element_card_list, null);

        TextView tvElementCardListCardNumber, tvElementCardListExpiryDate, tvElementCardListEditCard, tvElementCardListRemoveCard;

        tvElementCardListCardNumber = (TextView) vi.findViewById(R.id.tvElementCardListCardNumber);
        tvElementCardListExpiryDate = (TextView) vi.findViewById(R.id.tvElementCardListExpiryDate);
        tvElementCardListEditCard = (TextView) vi.findViewById(R.id.tvElementCardListEditCard);
        tvElementCardListRemoveCard = (TextView) vi.findViewById(R.id.tvElementCardListRemoveCard);

        JSONObject test = data.optJSONObject(position);
        Log.i("Test", test.toString());

        tvElementCardListCardNumber.setText("Card xx" + test.optString("Card_No"));
        tvElementCardListExpiryDate.setText(test.optString("Expiry_Date"));

        tvElementCardListEditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.startActivity(new Intent(a, CardDetails.class));
            }
        });

        return vi;
    }
}