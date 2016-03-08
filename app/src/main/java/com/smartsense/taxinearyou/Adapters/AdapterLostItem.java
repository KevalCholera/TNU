package com.smartsense.taxinearyou.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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


public class AdapterLostItem extends BaseAdapter {
    private JSONArray data;
    private LayoutInflater inflater = null;
    TextView tvLostItemFrom, tvLostItemTo, tvLostItemProvider, tvLostItemDateTime, tvLostItemStatus, tvLostItemLostItem, tvLostItemTNR;
    Activity a;
    //    ListView civElementLostItemCircleLeft;
    private AlertDialog alert;
    LinearLayout lyElementLostItemStatus, lyElementLostItemMain, lyElementLostItemLeft;
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
//        civElementLostItemCircleLeft = (ListView) vi.findViewById(R.id.civElementLostItemCircleLeft);

        ViewTreeObserver vto = lyElementLostItemStatus.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {

                lyElementLostItemStatus.getViewTreeObserver().removeOnPreDrawListener(this);

                int finalHeight = lyElementLostItemStatus.getMeasuredHeight();
                Log.i("Height", String.valueOf(finalHeight));

                int height = finalHeight / 30;
                Log.i("Height1", String.valueOf(height));

//                for (int i = 0; i < height; i++) {

                circleImageView = new CircleImageView1(a);
                circleImageView.setMaxWidth(10);
                circleImageView.setMaxHeight(10);
                circleImageView.setBackgroundColor(a.getResources().getColor(R.color.white));
                circleImageView.setBorderColor(a.getResources().getColor(R.color.element));
                circleImageView.setBorderWidth(R.dimen.civTripDetailsBorderWidth);

                lyElementLostItemLeft.addView(circleImageView);
//                }
                return true;
            }
        });

        JSONObject test = data.optJSONObject(position);
        Log.i("Test", test.toString());

        tvLostItemTNR.setText(test.optString("tnr"));
        tvLostItemFrom.setText(test.optString("from"));
        tvLostItemTo.setText(test.optString("to"));
        tvLostItemProvider.setText(test.optString("provider"));
        tvLostItemDateTime.setText(test.optString("date_time"));
        tvLostItemStatus.setText(test.optString("status"));
        tvLostItemLostItem.setText(test.optString("lost_item"));

        lyElementLostItemMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.startActivity(new Intent(a, LostItemDetail.class));
            }
        });
        return vi;
    }

    private CircleImageView1 createNewTextView() {
        CircleImageView1 circleImageView = new CircleImageView1(a);
        circleImageView.setMaxWidth(R.dimen.tvMarginTopBottomStartEndAll);
        circleImageView.setMaxHeight(R.dimen.tvMarginTopBottomStartEndAll);
        circleImageView.setBackgroundColor(a.getResources().getColor(R.color.white));
        circleImageView.setBorderColor(a.getResources().getColor(R.color.element));
        circleImageView.setBorderWidth(R.dimen.civTripDetailsBorderWidth);
        return circleImageView;
    }
}