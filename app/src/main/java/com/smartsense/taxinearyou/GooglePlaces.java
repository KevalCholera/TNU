package com.smartsense.taxinearyou;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.smartsense.taxinearyou.utill.CommonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

public class GooglePlaces extends FragmentActivity implements OnItemClickListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static ArrayList<String> resultList1;
    TextView tvGooglePlacesCancel, tvGooglePlacesCurrentLocation;
    ImageButton ibGooglePlaceEmpty;
    EditText autoCompView;
    ListView lvGoogleSearch;
    //------------ make your specific key ------------
//    private static final String API_KEY = "AIzaSyDi0RWt263vcR_s6MAjN_3Lq4DIPCrW7JI";
    private static final String API_KEY = "AIzaSyCp1vbSHgiC1lsNUYb-PuDs3kJ4wYEKu3I";
    private GooglePlacesAutocompleteAdapter dataAdapter;
    private GooglePlacesAutocompleteAdapter1 dataAdapter1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_places);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(GooglePlaces.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .build();
        autoCompView = (EditText) findViewById(R.id.atv_places);
        tvGooglePlacesCancel = (TextView) findViewById(R.id.tvGooglePlacesCancel);
        tvGooglePlacesCurrentLocation = (TextView) findViewById(R.id.tvGooglePlacesCurrentLocation);
        ibGooglePlaceEmpty = (ImageButton) findViewById(R.id.ibGooglePlaceEmpty);
        lvGoogleSearch = (ListView) findViewById(R.id.lvGooglePlaces);
        dataAdapter = new GooglePlacesAutocompleteAdapter(this, R.layout.element_google);
        dataAdapter1 = new GooglePlacesAutocompleteAdapter1(this, new JSONArray());
        lvGoogleSearch.setAdapter(dataAdapter);
        lvGoogleSearch.setTextFilterEnabled(true);
        lvGoogleSearch.setOnItemClickListener(this);
        tvGooglePlacesCancel.setOnClickListener(this);
        ibGooglePlaceEmpty.setOnClickListener(this);
        tvGooglePlacesCurrentLocation.setOnClickListener(this);

        autoCompView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(autoCompView.getText().toString()))
                    ibGooglePlaceEmpty.setVisibility(View.VISIBLE);
                else
                    ibGooglePlaceEmpty.setVisibility(View.GONE);
                dataAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        //        System.out.println(addressObj + " " + resultList1.get(position));
//        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
//                .getPlaceById(mGoogleApiClient, resultList1.get(position));
//        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);


        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            String sb = "https://maps.googleapis.com/maps/api/place/details/json?key=" + API_KEY + "&placeid=" + resultList1.get(position);

            URL url = new URL(sb);

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            JSONObject addObj = new JSONObject();
            String AreaName = (String) adapterView.getItemAtPosition(position);
            String AreaPlaceid = resultList1.get(position);
            String AreaAddress = "";
            String AreaLat = "";
            String AreaLong = "";
            String AreaPostalCode = "";
            String AreaCity = "";
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            if (jsonObj.optString("status").equalsIgnoreCase("OK")) {
                JSONObject jsonObj1 = jsonObj.optJSONObject("result");
                JSONArray types = jsonObj1.optJSONArray("types");
                if (jsonObj1.has("formatted_address")) {
                    AreaAddress = jsonObj1.optString("formatted_address");
                }
                if (jsonObj1.has("geometry")) {
                    AreaLat = jsonObj1.optJSONObject("geometry").optJSONObject("location").optString("lat");
                    AreaLong = jsonObj1.optJSONObject("geometry").optJSONObject("location").optString("lng");
                }
                if (jsonObj1.has("address_components")) {
                    JSONArray jsonArray = jsonObj1.optJSONArray("address_components");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (jsonArray.optJSONObject(i).optJSONArray("types").optString(0).equalsIgnoreCase("postal_code")) {
                            AreaPostalCode = jsonArray.optJSONObject(i).optString("long_name");
                        }
                        if (jsonArray.optJSONObject(i).optJSONArray("types").optString(0).equalsIgnoreCase("postal_town") || jsonArray.optJSONObject(i).optJSONArray("types").optString(0).equalsIgnoreCase("locality")) {
                            AreaCity = jsonArray.optJSONObject(i).optString("long_name");
                        }

                    }
                }
            }

//            AreaName = AreaName.replace(" ", "%20");
//            AreaAddress= AreaName.replace(" ", "%20");
//            AreaCity= AreaCity.replace(" ", "%20");
//            AreaPostalCode= AreaPostalCode.replace(" ", "%20");
            addObj.put("viaAreaName", AreaName.equalsIgnoreCase("") ? "" : AreaName);
            addObj.put("viaAreaPlaceid", AreaPlaceid.equalsIgnoreCase("") ? "" : AreaPlaceid);
            addObj.put("viaAreaAddress", AreaAddress.equalsIgnoreCase("") ? "" : AreaAddress);
            addObj.put("viaAreaLat", AreaLat.equalsIgnoreCase("") ? "" : AreaLat);
            addObj.put("viaAreaLong", AreaLong.equalsIgnoreCase("") ? "" : AreaLong);
            addObj.put("viaAreaPostalCode", AreaPostalCode.equalsIgnoreCase(" ") ? "" : AreaPostalCode);
            addObj.put("viaAreaCity", AreaCity.equalsIgnoreCase(" ") ? "" : AreaCity);
            System.out.println("jsonObj: " + addObj.toString());

            setResult(Activity.RESULT_OK, new Intent().putExtra("typeAddress", getIntent().getIntExtra("typeAddress", 0)).putExtra("address", addObj.toString()).putExtra("AreaName", AreaName));
            finish();
            CommonUtil.closeKeyboard(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//
//        String str = (String) adapterView.getItemAtPosition(position);
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();


        }
    };

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;


        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            String sb = PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON + "?key=" + API_KEY +
                    "&components=country:uk" +
                    "&input=" + URLEncoder.encode(input, "utf8");

            URL url = new URL(sb);

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.optJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<>(predsJsonArray.length());
            resultList1 = new ArrayList<>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.optJSONObject(i));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.optJSONObject(i).optString("description"));
                resultList1.add(predsJsonArray.optJSONObject(i).optString("place_id"));

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    public static JSONArray autocomplete1(String input) {
        ArrayList<String> resultList = null;
        JSONArray predsJsonArray = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            String sb = PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON + "?key=" + API_KEY +
                    "&components=country:uk" +
                    "&input=" + URLEncoder.encode(input, "utf8");

            URL url = new URL(sb);

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }


        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            predsJsonArray = jsonObj.optJSONArray("predictions");

            // Extract the Place descriptions from the results
//            resultList = new ArrayList<>(predsJsonArray.length());
//            resultList1 = new ArrayList<>(predsJsonArray.length());
//            for (int i = 0; i < predsJsonArray.length(); i++) {
//                System.out.println(predsJsonArray.getJSONObject(i));
//                System.out.println("============================================================");
//                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
//                resultList1.add(predsJsonArray.getJSONObject(i).getString("place_id"));
//            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return predsJsonArray;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibGooglePlaceEmpty:
                if (!TextUtils.isEmpty(autoCompView.getText().toString()))
                    autoCompView.setText("");
                break;
            case R.id.tvGooglePlacesCancel:
                finish();
                CommonUtil.closeKeyboard(this);
                break;
            case R.id.tvGooglePlacesCurrentLocation:
//                https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=cruise&key=AIzaSyCp1vbSHgiC1lsNUYb-PuDs3kJ4wYEKu3I
                break;
        }
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList = new ArrayList<>();

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList == null ? 0 : resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        if (resultList != null) {
                            filterResults.count = resultList.size();
                        }
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
        }
    }

    class GooglePlacesAutocompleteAdapter1 extends BaseAdapter implements Filterable {
        private JSONArray resultList11;
        private JSONArray resultList1;
        private LayoutInflater inflater = null;
        Activity a;

        public GooglePlacesAutocompleteAdapter1(Activity a, JSONArray resultList11) {
            this.resultList11 = resultList11;
            this.a = a;
            inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return resultList11 == null ? 0 : resultList11.length();
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public JSONObject getItem(int index) {
            return resultList11.optJSONObject(index);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.element_google_search, null);

            TextView tvElementGoogle = (TextView) vi.findViewById(R.id.tvElementGoogle);
            TextView tvElementGoogle1 = (TextView) vi.findViewById(R.id.tvElementGoogle1);


            JSONObject test = resultList11.optJSONObject(position);
            try {
                String[] str = test.optString("description").split(",", 2);
                tvElementGoogle.setText(str[0].trim());
                tvElementGoogle1.setText(str[1].trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return vi;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        Log.i("Yes", "here");
                        // Retrieve the autocomplete results.
                        resultList1 = autocomplete1(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList1;
                        if (resultList1 != null) {
                            filterResults.count = resultList1.length();
                        }
                        Log.e("VALUES", filterResults.values.toString());
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
//                    if (results != null && results.count > 0) {
                    Log.i("Yes", "here1");
                    resultList11 = (JSONArray) results.values;
                    notifyDataSetChanged();
//                    } else {
//                        notifyDataSetInvalidated();
//                    }
                }
            };
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.ratingforsearch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

}