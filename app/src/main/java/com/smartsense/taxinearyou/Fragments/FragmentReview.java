package com.smartsense.taxinearyou.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.Adapters.AdapterFragmentReview;
import com.smartsense.taxinearyou.PartnerDetails;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentReview extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    ListView lvReviewListView;
    TextView tvFragmentReview1, tvFragmentReview2, tvFragmentReview3, tvFragmentReview4, tvFragmentReview5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);

        lvReviewListView = (ListView) rootView.findViewById(R.id.lvReviewListView);
        tvFragmentReview1 = (TextView) rootView.findViewById(R.id.tvFragmentReview1);
        tvFragmentReview2 = (TextView) rootView.findViewById(R.id.tvFragmentReview2);
        tvFragmentReview3 = (TextView) rootView.findViewById(R.id.tvFragmentReview3);
        tvFragmentReview4 = (TextView) rootView.findViewById(R.id.tvFragmentReview4);
        tvFragmentReview5 = (TextView) rootView.findViewById(R.id.tvFragmentReview5);

        if (PartnerDetails.rating != null && PartnerDetails.rating.size() > 1) {

            if (PartnerDetails.rating.get(1) != null)
                tvFragmentReview1.setText(PartnerDetails.rating.get(1));
            else
                tvFragmentReview1.setText("-");

            if (PartnerDetails.rating.get(2) != null)
                tvFragmentReview2.setText(PartnerDetails.rating.get(2));
            else
                tvFragmentReview2.setText("-");

            if (PartnerDetails.rating.get(3) != null)
                tvFragmentReview3.setText(PartnerDetails.rating.get(3));
            else
                tvFragmentReview3.setText("-");

            if (PartnerDetails.rating.get(4) != null)
                tvFragmentReview4.setText(PartnerDetails.rating.get(4));
            else
                tvFragmentReview4.setText("-");

            if (PartnerDetails.rating.get(5) != null)
                tvFragmentReview5.setText(PartnerDetails.rating.get(5));
            else
                tvFragmentReview5.setText("-");
        } else {
            tvFragmentReview1.setText("-");
            tvFragmentReview2.setText("-");
            tvFragmentReview3.setText("-");
            tvFragmentReview4.setText("-");
            tvFragmentReview5.setText("-");
        }
        feedBack();

        return rootView;
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void feedBack() {
        final String tag = "Feed Back";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("partnerId", PartnerDetails.partnerId));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(getActivity(), getResources().getString(R.string.get_data), CommonUtil.utf8Convert(builder, Constants.Events.GET_FEED_BACK), tag, this, this);

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.cancelProgressDialog();
        CommonUtil.errorToastShowing(getActivity());
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        CommonUtil.cancelProgressDialog();
        if (jsonObject != null) {
            if (jsonObject.optInt("status") == Constants.STATUS_SUCCESS)
                if (jsonObject.optJSONObject("json") != null && jsonObject.optJSONObject("json").optJSONArray("feedbackArray") != null && jsonObject.optJSONObject("json").optJSONArray("feedbackArray").length() > 0) {
                    AdapterFragmentReview adapterFragmentReview = new AdapterFragmentReview(getActivity(), jsonObject.optJSONObject("json").optJSONArray("feedbackArray"));
                    lvReviewListView.setAdapter(adapterFragmentReview);
                    setListViewHeightBasedOnChildren(lvReviewListView);
                } else {

                }
            else
                CommonUtil.conditionAuthentication(getActivity(), jsonObject);
        } else
            CommonUtil.jsonNullError(getActivity());
    }
}
