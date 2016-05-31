package com.smartsense.taxinearyou.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartsense.taxinearyou.LostItemDetail;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.utill.CircleImageView1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class AdapterLostItem extends BaseAdapter {
    private JSONArray data;
    private LayoutInflater inflater = null;
    TextView tvLostItemFrom, tvLostItemTo, tvLostItemProvider, tvLostItemDateTime, tvLostItemStatus, tvLostItemLostItem, tvLostItemTNR;
    Activity a;
    private AlertDialog alert;
    LinearLayout lyElementLostItemStatus, lyElementLostItemMain, lyElementLostItemLeft, lyElementLostItemRight;
    CircleImageView1 circleImageView;

    public AdapterLostItem(Activity a, JSONArray data) {
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
            vi = inflater.inflate(R.layout.element_lost_item, null);

        tvLostItemTNR = (TextView) vi.findViewById(R.id.tvLostItemTNR);
        tvLostItemFrom = (TextView) vi.findViewById(R.id.tvLostItemFrom);
        tvLostItemTo = (TextView) vi.findViewById(R.id.tvLostItemTo);
        tvLostItemProvider = (TextView) vi.findViewById(R.id.tvLostItemProvider);
        tvLostItemDateTime = (TextView) vi.findViewById(R.id.tvLostItemDateTime);
        tvLostItemStatus = (TextView) vi.findViewById(R.id.tvLostItemStatus);
        tvLostItemLostItem = (TextView) vi.findViewById(R.id.tvLostItemLostItem);
        lyElementLostItemMain = (LinearLayout) vi.findViewById(R.id.lyElementLostItemMain);
        lyElementLostItemStatus = (LinearLayout) vi.findViewById(R.id.lyElementLostItemStatus);
        lyElementLostItemLeft = (LinearLayout) vi.findViewById(R.id.lyElementLostItemLeft);
        lyElementLostItemRight = (LinearLayout) vi.findViewById(R.id.lyElementLostItemRight);
//        civElementLostItemCircleLeft = (ListView) vi.findViewById(R.id.civElementLostItemCircleLeft);

        ViewTreeObserver vto = lyElementLostItemStatus.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {

                lyElementLostItemStatus.getViewTreeObserver().removeOnPreDrawListener(this);
                int finalHeight = lyElementLostItemStatus.getMeasuredHeight();
                int height = finalHeight / 30;

                for (int i = 0; i < height; i++)
                    createNewTextView();

                return true;
            }
        });

        final JSONObject test = data.optJSONObject(position);

        tvLostItemTNR.setText(test.optString("pnr"));
        tvLostItemFrom.setText(test.optString("fromArea"));
        tvLostItemTo.setText(test.optString("toArea"));
        tvLostItemProvider.setText(test.optString("partnerName"));
        try {
            tvLostItemDateTime.setText(new SimpleDateFormat("dd.MM.yyyy hh:mm aa").format(new SimpleDateFormat("dd-MMMM-yyyy HH:mm").parse(test.optString("rideDate"))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvLostItemStatus.setText(test.optString("status"));
        tvLostItemLostItem.setText(test.optString("color") + " color\n" + test.optString("itemDescription"));

        lyElementLostItemMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.startActivity(new Intent(a, LostItemDetail.class).putExtra("lostItemDetails", test.toString()));
            }
        });
        return vi;
    }

    public void createNewTextView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);
        layoutParams.setMargins(5, 10, 5, 0);

        CircleImageView1 circleImageView = new CircleImageView1(a);
        circleImageView.setLayoutParams(layoutParams);
        circleImageView.setBackgroundColor(ContextCompat.getColor(a, R.color.white));
        circleImageView.setBorderColor(ContextCompat.getColor(a, R.color.element));
        circleImageView.setBorderWidth(2);

        CircleImageView1 circleImageView1 = new CircleImageView1(a);
        circleImageView.setLayoutParams(layoutParams);
        circleImageView.setBackgroundColor(ContextCompat.getColor(a, R.color.white));
        circleImageView.setBorderColor(ContextCompat.getColor(a, R.color.element));
        circleImageView.setBorderWidth(2);

        lyElementLostItemRight.addView(circleImageView);
        lyElementLostItemLeft.addView(circleImageView1);
    }
}