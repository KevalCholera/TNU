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

        lvReviewListView = (ListView) rootView.findViewById(R.id.lvReviewListView);
//
        String data = "[{\"list\":\"Loreum Ipsem is Simply dummy text of the printing and typessetting Industry. loreum ipSum has been the industry's standard dummy text ever since the 1500s\"},{\"list\":\"Loreum Ipsem is Simply dummy text of the printing and typessetting Industry . loreum ipSum has been the industry's standard dummy text ever since the 1500s\"},{\"list\":\"Loreum Ipsem is Simply dummy text of the printing and typessetting Industry . loreum ipSum has been the industry's standard dummy text ever since the 1500s\"},{\"list\":\"Loreum Ipsem is Simply dummy text of the printing and typessetting Industry . loreum ipSum has been the industry's standard dummy text ever since the 1500s\"},{\"list\":\"Loreum Ipsem is Simply dummy text of the printing and typessetting Industry . loreum ipSum has been the industry's standard dummy text ever since the 1500s\"},{\"list\":\"Loreum Ipsem is Simply dummy text of the printing and typessetting Industry . loreum ipSum has been the industry's standard dummy text ever since the 1500s\"},{\"list\":\"Loreum Ipsem is Simply dummy text of the printing and typessetting Industry . loreum ipSum has been the industry's standard dummy text ever since the 1500s\"}]";
        try {
            JSONArray jsonArray = new JSONArray(data);
            AdapterReview adapterReview = new AdapterReview(getActivity(), jsonArray);
            lvReviewListView.setAdapter(adapterReview);

            JSONArray jsonArray1 = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PARTNER_ARRAY, ""));
            JSONObject jsonObject = jsonArray1.optJSONObject(0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }
}
