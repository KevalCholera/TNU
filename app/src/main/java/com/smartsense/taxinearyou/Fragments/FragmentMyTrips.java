package com.smartsense.taxinearyou.Fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

import java.util.ArrayList;

import javax.xml.transform.Result;

public class FragmentMyTrips extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    ImageView ivMyTripsNoTrips;
    ListView lvMyTrips;
    LinearLayout llFragmentMyTrips;
    final int request = 1;
    private boolean flag_loading;
    int pageNumber = 0;
    int pageSize = 10;
    ArrayList<Integer> arrayList;
    int selected = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_trips, container, false);

        ivMyTripsNoTrips = (ImageView) rootView.findViewById(R.id.ivMyTripsNoTrips);
        lvMyTrips = (ListView) rootView.findViewById(R.id.lvMyTrips);
        llFragmentMyTrips = (LinearLayout) rootView.findViewById(R.id.llFragmentMyTrips);

        getActivity().registerReceiver(tripMessageReceiver, new IntentFilter(Constants.PushList.PUSH_MY_TRIP));
        arrayList = new ArrayList<>();
        doMyTrip(pageNumber, pageSize);

        lvMyTrips.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && !flag_loading) {
                    flag_loading = true;
                    pageNumber++;
                    pageSize += 10;
                    doMyTrip(pageNumber, pageSize);
                }
            }
        });

        return rootView;
    }

    private void doMyTrip(int pageNumber, int pageSize) {
        final String tag = "My Trip";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("pageSize", pageSize).put("pageNumber", pageNumber)
                    .put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("offset", 1));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestNoProgressBar(CommonUtil.utf8Convert(builder, Constants.Events.EVENT_MY_TRIP), tag, this, this);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.errorToastShowing(getActivity());
        CommonUtil.cancelProgressDialog();
        flag_loading = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case request:
                    arrayList = new ArrayList<>();
                    doMyTrip(pageNumber, pageSize);
                    lvMyTrips.setSelection(selected);
                    break;
            }
    }

    @Override
    public void onResume() {
        super.onResume();
        arrayList = new ArrayList<>();
    }

    @Override
    public void onResponse(final JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            if (response.optInt("status") == Constants.STATUS_SUCCESS) {
                switch (response.optInt("__eventid")) {
                    case Constants.Events.EVENT_MY_TRIP:
                        try {
                            int length = response.optJSONObject("json").optJSONArray("rideArray").length();

                            if (!arrayList.contains(length)) {
                                arrayList.add(length);

                                if (response.optJSONObject("json").optJSONArray("rideArray").length() > 0) {
                                    flag_loading = false;
                                    lvMyTrips.setVisibility(View.VISIBLE);
                                    llFragmentMyTrips.setVisibility(View.GONE);
                                    AdapterMyTrips adapterMyTrips = new AdapterMyTrips(getActivity(), response.optJSONObject("json").optJSONArray("rideArray"));
                                    lvMyTrips.setAdapter(adapterMyTrips);

                                    lvMyTrips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            selected = position;
                                            startActivityForResult(new Intent(getActivity(), TripDetails.class).putExtra("key", response.optJSONObject("json").optJSONArray("rideArray").optJSONObject(position).toString()), request);
                                        }
                                    });
                                } else {
                                    lvMyTrips.setVisibility(View.GONE);
                                    llFragmentMyTrips.setVisibility(View.VISIBLE);
                                }
                            }
//                            else CommonUtil.alertBox(getActivity(), "All data set", false, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            } else
                CommonUtil.conditionAuthentication(getActivity(), response);
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
