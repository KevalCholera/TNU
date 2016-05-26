package com.smartsense.taxinearyou.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentAvailability extends Fragment {

    TextView tvAvailabilityWaitingTime, tvAvailabilityDriverNmae;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_availability, container, false);

        tvAvailabilityDriverNmae = (TextView) rootView.findViewById(R.id.tvAvailabilityDriverNmae);
        tvAvailabilityWaitingTime = (TextView) rootView.findViewById(R.id.tvAvailabilityWaitingTime);

        try {
            JSONArray jsonArray = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PARTNER_ARRAY, ""));
            JSONObject jsonObject = jsonArray.optJSONObject(0);
            tvAvailabilityDriverNmae.setText(jsonObject.optString("partnerName"));
            tvAvailabilityWaitingTime.setText(jsonObject.optString("("+"fleetSize" + "mins to arrive)"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;

    }

}
