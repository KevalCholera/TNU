package com.smartsense.taxinearyou.Fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private CoordinatorLayout clSearch;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_trips, container, false);

        ivMyTripsNoTrips = (ImageView) rootView.findViewById(R.id.ivMyTripsNoTrips);
        lvMyTrips = (ListView) rootView.findViewById(R.id.lvMyTrips);
        clSearch = (CoordinatorLayout) getActivity().findViewById(R.id.clSearch);
//        String data = "[{\"Amount\":\"$350.00\",\"From\":\"Solihull Rd, Shiral,Sohill, West Midland B90s 3LG, UK\",\"To\":\"Battern Parks Rd, London SW8, UK \",\"Taxi_Provider\":\"Lyft LLC\",\"Date_Time\":\"10.02.2016 10:30am\",\"Status\":\"Cancelled\"},{\"Amount\":\"$350.00\",\"From\":\"Solihull Rd, Shiral,Sohill, West Midland B90s 3LG, UK\",\"To\":\"Battern Parks Rd, London SW8, UK \",\"Taxi_Provider\":\"Lyft LLC\",\"Date_Time\":\"10.02.2016 10:30am\",\"Status\":\"Cancelled\"},{\"Amount\":\"$400.00\",\"From\":\"UK\",\"To\":\"US\",\"Taxi_Provider\":\"LLOUYT\",\"Date_Time\":\"20.02.2016 10:50am\",\"Status\":\"Completed\"},{\"Amount\":\"$350.00\",\"From\":\"Solihull Rd, Shiral,Sohill, West Midland B90s 3LG, UK\",\"To\":\"Battern Parks Rd, London SW8, UK \",\"Taxi_Provider\":\"Lyft LLC\",\"Date_Time\":\"10.02.2016 10:30am\",\"Status\":\"Waiting\"}]";

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
//                    switch (response.getInt("__eventid")) {
//                        case Constants.Events.EVENT_MY_TRIP:
                    try {
                        AdapterMyTrips adapterMyTrips = new AdapterMyTrips(getActivity(), response.optJSONObject("json").optJSONArray("rideArray"));
                        lvMyTrips.setAdapter(adapterMyTrips);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                            break;
//                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, getActivity(), clSearch);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void doMyTrip() {
        final String tag = "login";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.EVENT_MY_TRIP + "&json="
                    + jsonData.put("pageSize", 10).put("pageNumber", 1).put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "")).put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        CommonUtil.showProgressDialog(this, "Login...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }
}
