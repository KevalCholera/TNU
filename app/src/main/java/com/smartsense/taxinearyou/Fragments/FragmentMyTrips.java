package com.smartsense.taxinearyou.Fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.TripDetails;
import com.smartsense.taxinearyou.utill.CircleImageView1;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.WakeLocker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class FragmentMyTrips extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    ImageView ivMyTripsNoTrips;
    ListView lvMyTrips;
    LinearLayout llFragmentMyTrips;
    final int request = 1;

    int pageNumber = 0;
    int totalRecord = 0;
    int pageSize;
    int selected = 0;
    AdapterMyTrips adapterMyTrips = null;
    ProgressBar pbMyTrips;
    private SwipeRefreshLayout srMyTrips;
    TextView tvFragmentMyTrips;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_trips, container, false);
        pageNumber = 0;
        totalRecord = 0;
        pageSize = SharedPreferenceUtil.getInt(Constants.PAGE_LIMIT,9);
        adapterMyTrips = null;

        tvFragmentMyTrips = (TextView) rootView.findViewById(R.id.tvFragmentMyTrips);
        ivMyTripsNoTrips = (ImageView) rootView.findViewById(R.id.ivMyTripsNoTrips);
        srMyTrips = (SwipeRefreshLayout) rootView.findViewById(R.id.srMyTrips);
        lvMyTrips = (ListView) rootView.findViewById(R.id.lvMyTrips);
        pbMyTrips = (ProgressBar) rootView.findViewById(R.id.pbMyTrips);
        llFragmentMyTrips = (LinearLayout) rootView.findViewById(R.id.llFragmentMyTrips);

        lvMyTrips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                JSONObject obj = (JSONObject) parent.getItemAtPosition(position);
                startActivityForResult(new Intent(getActivity(), TripDetails.class).putExtra("key", obj.toString()), request);
            }
        });
        srMyTrips.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNumber = 0;
                totalRecord = 0;
                adapterMyTrips = null;
                doMyTrip(pageNumber, false);
            }
        });
        getActivity().registerReceiver(tripMessageReceiver, new IntentFilter(String.valueOf(Constants.Events.EVENT_CHANGE)));
        getActivity().registerReceiver(tripMessageReceiver, new IntentFilter(String.valueOf(Constants.Events.CANCEL_RIDE)));
        getActivity().registerReceiver(tripMessageReceiver, new IntentFilter(String.valueOf(Constants.Events.BookRide)));
        doMyTrip(pageNumber, false);

//        lvMyTrips.setOnScrollListener(new AbsListView.OnScrollListener() {
//
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
//
//                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && !flag_loading) {
//                    flag_loading = true;
//                    pageNumber++;
//
//                    doMyTrip(pageNumber);
//                }
//            }
//        });

        return rootView;
    }

    private void doMyTrip(int pageNumber, Boolean check) {
        final String tag = "My Trip";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("pageSize", pageSize).put("pageNumber", pageNumber)
                    .put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))
                    .put("offset", pageNumber));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (check)
            CommonUtil.showProgressDialog(getActivity(), getResources().getString(R.string.get_data));
        CommonUtil.jsonRequestNoProgressBar(CommonUtil.utf8Convert(builder, Constants.Events.EVENT_MY_TRIP), tag, this, this);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.errorToastShowing(getActivity());
        CommonUtil.cancelProgressDialog();
        if (srMyTrips.isRefreshing()) {
            srMyTrips.setRefreshing(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case request:
                    pageNumber = 0;
                    totalRecord = 0;
                    adapterMyTrips = null;
                    doMyTrip(pageNumber, true);
                    break;
            }
    }

    @Override
    public void onResume() {
        super.onResume();
//        pageNumber = 0;
//        totalRecord = 0;
//        adapterMyTrips = null;

    }

    @Override
    public void onResponse(final JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (srMyTrips.isRefreshing()) {
            srMyTrips.setRefreshing(false);
        }
        if (response != null) {
            if (response.optInt("status") == Constants.STATUS_SUCCESS) {
                switch (response.optInt("__eventid")) {
                    case Constants.Events.EVENT_MY_TRIP:
                        try {

                            if (response.optJSONObject("json").optJSONArray("rideArray").length() > 0) {
                                totalRecord = response.optJSONObject("json").optInt("totalRecord");
                                lvMyTrips.setVisibility(View.VISIBLE);
                                llFragmentMyTrips.setVisibility(View.GONE);
                                fillMyTrips(response.optJSONObject("json").optJSONArray("rideArray"));
                            } else {
                                lvMyTrips.setVisibility(View.GONE);
                                llFragmentMyTrips.setVisibility(View.VISIBLE);
                                tvFragmentMyTrips.setText(response.optString("msg"));
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
            WakeLocker.acquire(context);
            Log.i("Push ", intent.getStringExtra(Constants.EXTRAS));
            pageNumber = 0;
            totalRecord = 0;
            adapterMyTrips = null;
            doMyTrip(pageNumber, true);
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

    public void fillMyTrips(JSONArray jsonArray) {
        if (adapterMyTrips == null) {
            adapterMyTrips = new AdapterMyTrips(getActivity(), jsonArray);
            lvMyTrips.setAdapter(adapterMyTrips);
        } else {
            adapterMyTrips.adapterMyTrips(jsonArray);
        }
    }

    public class AdapterMyTrips extends BaseAdapter {
        private JSONArray data;
        private LayoutInflater inflater = null;
        Activity a;
        LinearLayout lyElementMyTripLeft, lyElementMyTripRight, lyElementMyTripStatusLayout;

        public AdapterMyTrips(Activity a, JSONArray data) {
            this.data = data;
            this.a = a;
            inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void adapterMyTrips(JSONArray data) {
            for (int i = 0; i < data.length(); i++) {
                this.data.put(data.optJSONObject(i));
            }
            notifyDataSetChanged();
        }

        public int getCount() {
            return data.length();
        }

        public Object getItem(int position) {
            return data.optJSONObject(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (convertView == null)

                vi = inflater.inflate(R.layout.element_my_trips, null);

            TextView tvElementMyTripsAmount = (TextView) vi.findViewById(R.id.tvElementMyTripsAmount);
            TextView tvElementMyTripsFrom = (TextView) vi.findViewById(R.id.tvElementMyTripsFrom);
            TextView tvElementMyTripsTo = (TextView) vi.findViewById(R.id.tvElementMyTripsTo);
            final TextView tvElementMyTripsStatus = (TextView) vi.findViewById(R.id.tvElementMyTripsStatus);
            TextView tvElementMyTripsTaxiProvider = (TextView) vi.findViewById(R.id.tvElementMyTripsTaxiProvider);
            TextView tvElementMyTripsDateTime = (TextView) vi.findViewById(R.id.tvElementMyTripsDateTime);

            lyElementMyTripStatusLayout = (LinearLayout) vi.findViewById(R.id.lyElementMyTripStatusLayout);
            lyElementMyTripLeft = (LinearLayout) vi.findViewById(R.id.lyElementMyTripLeft);
            lyElementMyTripRight = (LinearLayout) vi.findViewById(R.id.lyElementMyTripRight);
            final JSONObject test = data.optJSONObject(position);

            tvElementMyTripsAmount.setText("£" + CommonUtil.getDecimal(test.optDouble("estimatedAmount")));
            tvElementMyTripsFrom.setText(test.optString("from"));
            tvElementMyTripsTo.setText(test.optString("to"));
            tvElementMyTripsStatus.setText(test.optString("status"));
            tvElementMyTripsTaxiProvider.setText(test.optString("partner"));
            try {
                tvElementMyTripsDateTime.setText(Constants.DATE_FORMAT_DATE_TIME.format(Constants.DATE_FORMAT_SET.parse(test.optString("pickTime").trim())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (test.optString("status").equals("Cancelled"))
                tvElementMyTripsStatus.setBackgroundColor(ContextCompat.getColor(a, R.color.red));
            else if (tvElementMyTripsStatus.getText().toString().equals("Complete"))
                tvElementMyTripsStatus.setBackgroundColor(ContextCompat.getColor(a, R.color.dark_green));
            else
                tvElementMyTripsStatus.setBackgroundColor(ContextCompat.getColor(a, R.color.Yellow));
            if ((position + 1) == data.length() && totalRecord != data.length()) {
                doMyTrip(data.length(), true);
            }
            return vi;
        }
//        public void circularView(View vi){
//            TextView event_class = new TextView(this.a);
//            event_class.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1f));
//            event_class.setTextColor(this.a.getResources().getColor(R.color.white));
//            event_class.setText(event_classes);
//            event_class.setBackgroundColor(a.getResources().getColor(R.color._gray));
//            event_class.setPadding(10, 10, 10, 10);
//
//            llElementDatePageEvent.addView(viewAddData);
//        }
    }
}
