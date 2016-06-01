package com.smartsense.taxinearyou.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.Adapters.AdapterFragmentReview;
import com.smartsense.taxinearyou.PartnerDetails;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.TaxiNearYouApp;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.DataRequest;

import org.json.JSONArray;
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

        tvFragmentReview1.setText(PartnerDetails.rating.get(1));
        tvFragmentReview2.setText(PartnerDetails.rating.get(2));
        tvFragmentReview3.setText(PartnerDetails.rating.get(3));
        tvFragmentReview4.setText(PartnerDetails.rating.get(4));
        tvFragmentReview5.setText(PartnerDetails.rating.get(5));
        feedBack();

        return rootView;
    }

    private void feedBack() {
        final String tag = "Feed Back";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.GET_FEED_BACK + "&json=")
                    .append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                            .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                            .put("partnerId", PartnerDetails.partnerId));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.showProgressDialog(getActivity(), "getting data...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.cancelProgressDialog();
        CommonUtil.errorToastShowing(getActivity());
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        CommonUtil.cancelProgressDialog();
        if (jsonObject != null && jsonObject.optInt("status") == Constants.STATUS_SUCCESS && jsonObject.optJSONObject("json") != null && jsonObject.optJSONObject("json").optJSONArray("feedbackArray") != null && jsonObject.optJSONObject("json").optJSONArray("feedbackArray").length() > 0) {
            AdapterFragmentReview adapterFragmentReview = new AdapterFragmentReview(getActivity(), jsonObject.optJSONObject("json").optJSONArray("feedbackArray"));
            lvReviewListView.setAdapter(adapterFragmentReview);
        }
    }
}
