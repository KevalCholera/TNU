package com.smartsense.taxinearyou;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.LocationSettingsHelper;

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

public class GooglePlaces extends FragmentActivity implements OnItemClickListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static ArrayList<String> resultList1;
    LinearLayout lyGooglePlaceCurrentLocation;
    View viewGooglePlace;
    TextView tvGooglePlacesCancel, tvGooglePlacesCurrentLocation;
    ImageButton ibGooglePlaceEmpty;
    EditText autoCompView;
    ListView lvGoogleSearch;
    private LocationSettingsHelper mSettingsHelper;
    //------------ make your specific key ------------
//    private static final String API_KEY = "AIzaSyDi0RWt263vcR_s6MAjN_3Lq4DIPCrW7JI";
    private GooglePlacesAutocompleteAdapter dataAdapter;
    private GooglePlacesAutocompleteAdapter1 dataAdapter1;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_places);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
//        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(GooglePlaces.this)
//                .addApi(Places.GEO_DATA_API)
//                .enableAutoManage(this, 0, this)
//                .addConnectionCallbacks(this)
//                .build();
        autoCompView = (EditText) findViewById(R.id.atv_places);
        tvGooglePlacesCancel = (TextView) findViewById(R.id.tvGooglePlacesCancel);
        ibGooglePlaceEmpty = (ImageButton) findViewById(R.id.ibGooglePlaceEmpty);
        lvGoogleSearch = (ListView) findViewById(R.id.lvGooglePlaces);
        viewGooglePlace = findViewById(R.id.viewGooglePlace);
        tvGooglePlacesCurrentLocation = (TextView) findViewById(R.id.tvGooglePlacesCurrentLocation);
        dataAdapter = new GooglePlacesAutocompleteAdapter(this, R.layout.element_google);
        dataAdapter1 = new GooglePlacesAutocompleteAdapter1(this, new JSONArray());
        lvGoogleSearch.setAdapter(dataAdapter1);
        lyGooglePlaceCurrentLocation = (LinearLayout) findViewById(R.id.lyGooglePlaceCurrentLocation);

        lvGoogleSearch.setTextFilterEnabled(true);
        lvGoogleSearch.setOnItemClickListener(this);
        tvGooglePlacesCancel.setOnClickListener(this);
        ibGooglePlaceEmpty.setOnClickListener(this);
        tvGooglePlacesCurrentLocation.setOnClickListener(this);
        lyGooglePlaceCurrentLocation.setOnClickListener(this);

        if (getIntent() != null && getIntent().hasExtra("typeAddress") && getIntent().getIntExtra("typeAddress", 0) != 1) {
            lyGooglePlaceCurrentLocation.setVisibility(View.GONE);
            viewGooglePlace.setVisibility(View.GONE);
        }


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

                dataAdapter1.getFilter().filter(s.toString());
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

        JSONObject jsonOb = (JSONObject) adapterView.getItemAtPosition(position);
        String areaName = jsonOb.optString("description");
        String placeId = jsonOb.optString("place_id");

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            String sb = "https://maps.googleapis.com/maps/api/place/details/json?key=" + Constants.GOOGLE_PLACE_API + "&placeid=" + placeId;

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
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(jsonResults.toString());
            fillLocationList(jsonObj, areaName, placeId);
        } catch (JSONException e) {
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
            String sb = PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON + "?key=" + Constants.GOOGLE_PLACE_API +
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
            String sb = PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON + "?key=" + Constants.GOOGLE_PLACE_API +
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
            case R.id.lyGooglePlaceCurrentLocation:
                tvGooglePlacesCurrentLocation.performClick();
                break;
            case R.id.tvGooglePlacesCurrentLocation:
                CommonUtil.closeKeyboard(GooglePlaces.this);
                ibGooglePlaceEmpty.performClick();
                if (CommonUtil.isGPS(getApplicationContext())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!checkPermission()) {
                            requestPermission();
                        } else {
                            getCurrentLocationSuggestions();
                        }
                    } else {
                        getCurrentLocationSuggestions();
                    }
                } else {
                    mSettingsHelper = new LocationSettingsHelper(this);
                    mSettingsHelper.checkSettings();
                }
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

        public void googlePlacesAutocomplete(JSONArray jsonArray) {
            resultList11 = jsonArray;
            notifyDataSetChanged();
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
                    if (constraint != null && constraint.length() > 1) {
                        // Retrieve the autocomplete results.
                        resultList1 = autocomplete1(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList1;
                        if (resultList1 != null) {
                            filterResults.count = resultList1.length();
                        }
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
//                    if (results != null && results.count > 0) {

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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: " + connectionResult.getErrorCode());

        CommonUtil.byToastMessage(this, getResources().getString(R.string.api_failed));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LocationSettingsHelper.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!checkPermission()) {
                                requestPermission();
                            } else {
                                getCurrentLocationSuggestions();
                            }
                        } else {
                            getCurrentLocationSuggestions();
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        CommonUtil.byToastMessage(this, getResources().getString(R.string.turnDeviceGPS));
                        break;
                    default:
                        break;
                }
                break;
            case 11:
                if(resultCode==Activity.RESULT_OK){
                    setResult(Activity.RESULT_OK, new Intent().putExtra("typeAddress", data.getIntExtra("typeAddress", 0)).putExtra("address", data.getStringExtra("address")).putExtra("AreaName",  data.getStringExtra("AreaName")));
                }
                finish();
                break;
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            showMessageOKCancel("You need to allow access to Location",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSION_REQUEST_CODE);
                            }
                        }
                    });

        } else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocationSuggestions();
                } else {
                    CommonUtil.byToastMessage(this, getResources().getString(R.string.google_perm_denied));
                }
                break;
        }
    }

    public void getCurrentLocationSuggestions() {
        startActivityForResult(new Intent(this, GoogleMap.class).putExtra("typeAddress", getIntent().getIntExtra("typeAddress", 0)), 11);
//        final LocationFinderService myLocation = new LocationFinderService();
//        if (!CommonUtil.isInternetAvailable(GooglePlaces.this))
//            CommonUtil.byToastMessage(this, getResources().getString(R.string.nointernet_try_again_msg));
//        else {
//            CommonUtil.showProgressDialog(this, getResources().getString(R.string.searching));
//            LocationFinderService.LocationResult locationResult = new LocationFinderService.LocationResult() {
//                @Override
//                public void gotLocation(Location location) {
//                    //Got the location!
//                    myLocation.stopLocation();
//                    CommonUtil.cancelProgressDialog();
//                    if (location != null) {
//                        fillLocation(location);
//                    } else {
////                        CommonUtil.cancelProgressDialog();
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                CommonUtil.byToastMessage(GooglePlaces.this, getResources().getString(R.string.unavailable_to_find_place));
//                            }
//                        });
//
//                    }
//                }
//            };
//
//            myLocation.getLocation(this, locationResult);
//        }
    }

    public void fillLocation(Location location) {

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            String googleSuggestionLink = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location.getLatitude() + "," + location.getLongitude() + "&radius=10&key=" + Constants.GOOGLE_PLACE_API;
            Log.i("Location1", googleSuggestionLink);

            URL url = new URL(googleSuggestionLink);

            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            final JSONObject jsonObj = new JSONObject(jsonResults.toString());
            Log.i("Location1", jsonObj.toString());
            if (jsonObj.optString("status").equalsIgnoreCase("OK")) {
                String areaName;
                if (jsonObj.optJSONArray("results").optJSONObject(0).optString("name").equalsIgnoreCase(jsonObj.optJSONArray("results").optJSONObject(0).optString("vicinity"))) {
                    areaName = jsonObj.optJSONArray("results").optJSONObject(0).optString("name") + ", " + "United Kingdom";
                } else {
                    areaName = jsonObj.optJSONArray("results").optJSONObject(0).optString("name") + ", " + jsonObj.optJSONArray("results").optJSONObject(0).optString("vicinity") + ", " + "United Kingdom";
                }
                String placeId = jsonObj.optJSONArray("results").optJSONObject(0).optString("place_id");
                if (jsonObj.optJSONArray("results").length() == 1) {
                    fillLocationList(jsonObj, areaName, placeId);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray placeArray = new JSONArray();
                                for (int i = 0; i < jsonObj.optJSONArray("results").length(); i++) {
                                    String areaName = "";
                                    if (jsonObj.optJSONArray("results").optJSONObject(i).optString("name").equalsIgnoreCase(jsonObj.optJSONArray("results").optJSONObject(i).optString("vicinity"))) {
                                        areaName = jsonObj.optJSONArray("results").optJSONObject(i).optString("name") + ", " + "United Kingdom";
                                    } else {
                                        areaName = jsonObj.optJSONArray("results").optJSONObject(i).optString("name") + ", " + jsonObj.optJSONArray("results").optJSONObject(i).optString("vicinity") + ", " + "United Kingdom";
                                    }
                                    String placeId = jsonObj.optJSONArray("results").optJSONObject(i).optString("place_id");
                                    JSONObject newJson = new JSONObject();
                                    newJson.put("description", areaName);
                                    newJson.put("place_id", placeId);
                                    placeArray.put(newJson);
                                }
                                dataAdapter1.googlePlacesAutocomplete(placeArray);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            } else
                CommonUtil.byToastMessage(GooglePlaces.this, getResources().getString(R.string.unavailable_to_find_place));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillLocationList(JSONObject jsonObj, String areaName, String placeId) {
        try {
            JSONObject addObj = new JSONObject();
            String AreaName = areaName;
            String AreaAddress = "";
            String AreaLat = "";
            String AreaLong = "";
            String AreaPostalCode = "";
            String AreaCity = "";

            if (jsonObj.optString("status").equalsIgnoreCase("OK")) {
                JSONObject jsonObj1 = jsonObj.optJSONObject("result");
                placeId= jsonObj1.optString("place_id");
                JSONArray types = jsonObj1.optJSONArray("types");
                AreaName = jsonObj1.optString("name");
                if (jsonObj1.has("formatted_address")) {
                    String[] str = jsonObj1.optString("formatted_address").split(",");
                    Log.i("Yes", jsonObj1.optString("name") + " " + str[0].trim());
                    if (jsonObj1.optString("name").trim().equalsIgnoreCase(str[0].trim())) {
                        AreaAddress = jsonObj1.optString("formatted_address");
                    } else {
                        AreaAddress = jsonObj1.optString("name") + ", " + jsonObj1.optString("formatted_address");
//                        AreaAddress = jsonObj1.optString("formatted_address");
                    }
                }
                if (jsonObj1.has("geometry")) {
                    AreaLat = jsonObj1.optJSONObject("geometry").optJSONObject("location").optString("lat");
                    AreaLong = jsonObj1.optJSONObject("geometry").optJSONObject("location").optString("lng");
                }
                if (jsonObj1.has("address_components")) {
                    JSONArray jsonArray = jsonObj1.optJSONArray("address_components");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        for (int j = 0; j < jsonArray.optJSONObject(i).optJSONArray("types").length(); j++) {
                            if (jsonArray.optJSONObject(i).optJSONArray("types").optString(j).equalsIgnoreCase("postal_code")) {
                                AreaPostalCode = jsonArray.optJSONObject(i).optString("long_name");
                            }
                            if (jsonArray.optJSONObject(i).optJSONArray("types").optString(j).equalsIgnoreCase("postal_town") || jsonArray.optJSONObject(i).optJSONArray("types").optString(0).equalsIgnoreCase("locality")) {
                                AreaCity = jsonArray.optJSONObject(i).optString("long_name");
                            }
                        }

                    }
                }
            }
//            AreaName = AreaName.replace(" ", "%20");
//            AreaAddress= AreaName.replace(" ", "%20");
//            AreaCity= AreaCity.replace(" ", "%20");
//            AreaPostalCode= AreaPostalCode.replace(" ", "%20");
            addObj.put("viaAreaName", AreaName.equalsIgnoreCase("") ? " " : AreaName);
            addObj.put("viaAreaPlaceid", placeId.equalsIgnoreCase("") ? " " : placeId);
            addObj.put("viaAreaLat", AreaLat.equalsIgnoreCase("") ? " " : AreaLat);
            addObj.put("viaAreaLong", AreaLong.equalsIgnoreCase("") ? " " : AreaLong);
            addObj.put("viaAreaAddress", AreaAddress.equalsIgnoreCase("") ? " " : AreaAddress);
            addObj.put("viaFullAddress", AreaAddress.equalsIgnoreCase("") ? " " : AreaAddress);
            addObj.put("viaAreaPostalCode", AreaPostalCode.equalsIgnoreCase("") ? " " : AreaPostalCode);
            addObj.put("viaAreaCity", AreaCity.equalsIgnoreCase("") ? " " : AreaCity);
            System.out.println("jsonObj: " + addObj.toString());

            setResult(Activity.RESULT_OK, new Intent().putExtra("typeAddress", getIntent().getIntExtra("typeAddress", 0)).putExtra("address", addObj.toString()).putExtra("AreaName", AreaAddress));
            finish();
            CommonUtil.closeKeyboard(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}