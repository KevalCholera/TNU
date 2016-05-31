package com.smartsense.taxinearyou.Fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.Adapters.AdapterMyTrips;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.TaxiNearYouApp;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.DataRequest;
import com.smartsense.taxinearyou.utill.JsonErrorShow;

import org.json.JSONException;
import org.json.JSONObject;


public class FragmentMyTrips extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    ImageView ivMyTripsNoTrips;
    ListView lvMyTrips;
    LinearLayout llFragmentMyTrips;
    private CoordinatorLayout clSearch;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_trips, container, false);

        ivMyTripsNoTrips = (ImageView) rootView.findViewById(R.id.ivMyTripsNoTrips);
        lvMyTrips = (ListView) rootView.findViewById(R.id.lvMyTrips);
        llFragmentMyTrips = (LinearLayout) rootView.findViewById(R.id.llFragmentMyTrips);
        clSearch = (CoordinatorLayout) getActivity().findViewById(R.id.clSearch);

        doMyTrip();
        return rootView;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(getActivity(), "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
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
                                } else {
                                    lvMyTrips.setVisibility(View.GONE);
                                    llFragmentMyTrips.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, getActivity(), clSearch);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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

        CommonUtil.showProgressDialog(getActivity(), "getting data...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }
}
