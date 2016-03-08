package com.smartsense.taxinearyou.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.smartsense.taxinearyou.Adapters.AdapterMyTrips;
import com.smartsense.taxinearyou.R;

import org.json.JSONArray;
import org.json.JSONException;


public class FragmentMyTrips extends Fragment {

    ImageView ivMyTripsNoTrips;
    ListView lvMyTrips;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_trips, container, false);

        ivMyTripsNoTrips = (ImageView) rootView.findViewById(R.id.ivMyTripsNoTrips);
        lvMyTrips = (ListView) rootView.findViewById(R.id.lvMyTrips);

        String data = "[{\"Amount\":\"$350.00\",\"From\":\"Solihull Rd, Shiral,Sohill, West Midland B90s 3LG, UK\",\"To\":\"Battern Parks Rd, London SW8, UK \",\"Taxi_Provider\":\"Lyft LLC\",\"Date_Time\":\"10.02.2016 10:30am\",\"Status\":\"Cancelled\"},{\"Amount\":\"$350.00\",\"From\":\"Solihull Rd, Shiral,Sohill, West Midland B90s 3LG, UK\",\"To\":\"Battern Parks Rd, London SW8, UK \",\"Taxi_Provider\":\"Lyft LLC\",\"Date_Time\":\"10.02.2016 10:30am\",\"Status\":\"Cancelled\"},{\"Amount\":\"$400.00\",\"From\":\"UK\",\"To\":\"US\",\"Taxi_Provider\":\"LLOUYT\",\"Date_Time\":\"20.02.2016 10:50am\",\"Status\":\"Completed\"},{\"Amount\":\"$350.00\",\"From\":\"Solihull Rd, Shiral,Sohill, West Midland B90s 3LG, UK\",\"To\":\"Battern Parks Rd, London SW8, UK \",\"Taxi_Provider\":\"Lyft LLC\",\"Date_Time\":\"10.02.2016 10:30am\",\"Status\":\"Waiting\"}]";
        try {
            JSONArray jsonArray = new JSONArray(data);
            AdapterMyTrips adapterMyTrips = new AdapterMyTrips(getActivity(), jsonArray);
            lvMyTrips.setAdapter(adapterMyTrips);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }
}
