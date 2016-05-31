package com.smartsense.taxinearyou.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.Adapters.AdapterReview;
import com.smartsense.taxinearyou.PartnerDetails;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentReview extends Fragment {

    ListView lvReviewListView;
    TextView tvFragmentReview1, tvFragmentReview2, tvFragmentReview3, tvFragmentReview4, tvFragmentReview5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);

        tvFragmentReview1 = (TextView)rootView.findViewById(R.id.tvFragmentReview1);
        tvFragmentReview2 = (TextView)rootView.findViewById(R.id.tvFragmentReview2);
        tvFragmentReview3 = (TextView)rootView.findViewById(R.id.tvFragmentReview3);
        tvFragmentReview4 = (TextView)rootView.findViewById(R.id.tvFragmentReview4);
        tvFragmentReview5 = (TextView)rootView.findViewById(R.id.tvFragmentReview5);

        tvFragmentReview1.setText(PartnerDetails.rating.get(1));
        tvFragmentReview2.setText(PartnerDetails.rating.get(2));
        tvFragmentReview3.setText(PartnerDetails.rating.get(3));
        tvFragmentReview4.setText(PartnerDetails.rating.get(4));
        tvFragmentReview5.setText(PartnerDetails.rating.get(5));

        return rootView;
    }
}
