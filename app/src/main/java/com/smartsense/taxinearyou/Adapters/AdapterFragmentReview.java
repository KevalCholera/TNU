package com.smartsense.taxinearyou.Adapters;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.utill.CircleImageView1;
import com.smartsense.taxinearyou.utill.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by keval on 01-Jun-16.
 */
public class AdapterFragmentReview extends BaseAdapter {
    private JSONArray data;
    private LayoutInflater inflater = null;
    Activity a;

    public AdapterFragmentReview(Activity a, JSONArray data) {
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
            vi = inflater.inflate(R.layout.element_review, null);

        JSONObject jsonObject = data.optJSONObject(position);

        CircleImageView1 cvElementReviewCustomerPic = (CircleImageView1) vi.findViewById(R.id.cvElementReviewCustomerPic);
        TextView tvElementReviewCustomerName = (TextView) vi.findViewById(R.id.tvElementReviewCustomerName);
        TextView tvElementReviewTimeSince = (TextView) vi.findViewById(R.id.tvElementReviewTimeSince);
        TextView tvElementReviewCustomerFeedback = (TextView) vi.findViewById(R.id.tvElementReviewCustomerFeedback);

        if (!TextUtils.isEmpty(jsonObject.optString("logo")))
            Picasso.with(a)
                    .load(Constants.BASE_URL_IMAGE_POSTFIX + jsonObject.optString("logo"))
                    .error(R.mipmap.icon_user)
                    .placeholder(R.mipmap.icon_user)
                    .into(cvElementReviewCustomerPic);

        tvElementReviewCustomerFeedback.setText(jsonObject.optString("feedback").trim());
        tvElementReviewTimeSince.setText(jsonObject.optString("timeSince").trim());
        tvElementReviewCustomerName.setText(jsonObject.optString("customerName").trim());

        return vi;
    }
}