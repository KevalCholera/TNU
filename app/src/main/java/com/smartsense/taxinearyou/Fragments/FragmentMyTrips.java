package com.smartsense.taxinearyou.Fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.Adapters.AdapterMyTrips;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.TripDetails;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.LocationSettingsHelper;
import com.smartsense.taxinearyou.utill.WakeLocker;

import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.transform.Result;


public class FragmentMyTrips extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    ImageView ivMyTripsNoTrips;
    ListView lvMyTrips;
    LinearLayout llFragmentMyTrips;
    final int request = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_trips, container, false);

        ivMyTripsNoTrips = (ImageView) rootView.findViewById(R.id.ivMyTripsNoTrips);
        lvMyTrips = (ListView) rootView.findViewById(R.id.lvMyTrips);
        llFragmentMyTrips = (LinearLayout) rootView.findViewById(R.id.llFragmentMyTrips);
        getActivity().registerReceiver(tripMessageReceiver, new IntentFilter(
                Constants.PushList.PUSH_MY_TRIP));
        doMyTrip();
        return rootView;
    }

    private void doMyTrip() {
        final String tag = "My Trip";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.EVENT_MY_TRIP + "&json=")
                    .append(jsonData.put("pageSize", 10).put("pageNumber", 0)
                            .put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                            .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                            .put("offset", 0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestNoProgressBar(builder.toString(), tag, this, this);
//        CommonUtil.jsonRequestGET(getActivity(), getResources().getString(R.string.get_data), builder.toString(), tag, this, this);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.errorToastShowing(getActivity());
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case request:
                    doMyTrip();
                    break;
            }
    }

    @Override
    public void onResponse(final JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (response.getInt("__eventid")) {
                        case Constants.Events.EVENT_MY_TRIP:
                            try {
                                if (response.optJSONObject("json").optJSONArray("rideArray").length() > 0) {
                                    lvMyTrips.setVisibility(View.VISIBLE);
                                    llFragmentMyTrips.setVisibility(View.GONE);
                                    AdapterMyTrips adapterMyTrips = new AdapterMyTrips(getActivity(), response.optJSONObject("json").optJSONArray("rideArray"));
                                    lvMyTrips.setAdapter(adapterMyTrips);

                                    lvMyTrips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            startActivityForResult(new Intent(getActivity(), TripDetails.class).putExtra("key", response.optJSONObject("json").optJSONArray("rideArray").optJSONObject(position).toString()), request);
                                        }
                                    });

                                } else {
                                    lvMyTrips.setVisibility(View.GONE);
                                    llFragmentMyTrips.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
//                else
//                    CommonUtil.conditionAuthentication(getActivity(), response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            CommonUtil.jsonNullError(getActivity());
    }

    private final BroadcastReceiver tripMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(
                    Constants.EXTRAS);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getActivity());
            // ConstantClass.logPrint("msg rcv from gcm ", newMessage);
            // Releasing wake lock
            WakeLocker.release();
        }
    };

    @Override
    public void onDestroy() {
        try {
            getActivity().unregisterReceiver(tripMessageReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
