package com.smartsense.taxinearyou.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.PartnerDetails;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentAvailability extends Fragment {

    TextView tvAvailabilityWaitingTime, tvAvailabilityDriverNmae;
    ImageView ivFragmentAvailabilityStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_availability, container, false);

        tvAvailabilityDriverNmae = (TextView) rootView.findViewById(R.id.tvAvailabilityDriverNmae);
        tvAvailabilityWaitingTime = (TextView) rootView.findViewById(R.id.tvAvailabilityWaitingTime);
        ivFragmentAvailabilityStatus = (ImageView) rootView.findViewById(R.id.ivFragmentAvailabilityStatus);

        tvAvailabilityDriverNmae.setText(PartnerDetails.taxiTypeName);
        tvAvailabilityWaitingTime.setText(PartnerDetails.waitingTime);

        if (PartnerDetails.available == 0)
            ivFragmentAvailabilityStatus.setImageResource(android.R.drawable.presence_online);
        else
            ivFragmentAvailabilityStatus.setImageResource(R.mipmap.ic_image_brightness_1);


        return rootView;

    }
}
