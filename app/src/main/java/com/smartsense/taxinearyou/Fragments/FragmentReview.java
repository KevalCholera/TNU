package com.smartsense.taxinearyou.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.smartsense.taxinearyou.Adapters.AdapterReview;
import com.smartsense.taxinearyou.R;

import org.json.JSONArray;
import org.json.JSONException;

public class FragmentReview extends Fragment {

    ListView lvReviewListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);

        lvReviewListView = (ListView) rootView.findViewById(R.id.lvReviewListView);
//
        String data = "[{\"list\":\"Loreum Ipsem is Simply dummy text of the printing and typessetting Industry . loreum ipSum has been the industry's standard dummy text ever since the 1500s\"},{\"list\":\"Loreum Ipsem is Simply dummy text of the printing and typessetting Industry . loreum ipSum has been the industry's standard dummy text ever since the 1500s\"},{\"list\":\"Loreum Ipsem is Simply dummy text of the printing and typessetting Industry . loreum ipSum has been the industry's standard dummy text ever since the 1500s\"},{\"list\":\"Loreum Ipsem is Simply dummy text of the printing and typessetting Industry . loreum ipSum has been the industry's standard dummy text ever since the 1500s\"},{\"list\":\"Loreum Ipsem is Simply dummy text of the printing and typessetting Industry . loreum ipSum has been the industry's standard dummy text ever since the 1500s\"},{\"list\":\"Loreum Ipsem is Simply dummy text of the printing and typessetting Industry . loreum ipSum has been the industry's standard dummy text ever since the 1500s\"},{\"list\":\"Loreum Ipsem is Simply dummy text of the printing and typessetting Industry . loreum ipSum has been the industry's standard dummy text ever since the 1500s\"}]";
        try {
            JSONArray jsonArray = new JSONArray(data);
            AdapterReview adapterReview = new AdapterReview(getActivity(), jsonArray);
            lvReviewListView.setAdapter(adapterReview);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return rootView;
    }
}
