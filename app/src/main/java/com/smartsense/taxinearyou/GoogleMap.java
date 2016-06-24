package com.smartsense.taxinearyou;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.LocationFinderService;

import org.json.JSONObject;

import java.util.List;

public class GoogleMap extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private com.google.android.gms.maps.GoogleMap mMap;


    EditText etMapSearch;
    ImageButton ibGooglePlaceBack, ibGooglePlacesMyLocation, ibGooglePlacesSave;
    Context context;
    ImageView ivMapImage;
    private JSONObject addObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        getCurrentLocationSuggestions();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ibGooglePlacesMyLocation = (ImageButton) findViewById(R.id.ibGooglePlacesMyLocation);
        ibGooglePlacesSave = (ImageButton) findViewById(R.id.ibGooglePlacesSave);
        ibGooglePlaceBack = (ImageButton) findViewById(R.id.ibGooglePlaceBack);
        ivMapImage = (ImageView) findViewById(R.id.ivMapImage);
        etMapSearch = (EditText) findViewById(R.id.etMapSearch);

        context = GoogleMap.this;

        ibGooglePlacesMyLocation.setOnClickListener(this);

        ibGooglePlaceBack.setOnClickListener(this);

        ibGooglePlacesSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibGooglePlacesMyLocation:
                getCurrentLocationSuggestions();
                break;
            case R.id.ibGooglePlaceBack:
                finish();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
            case R.id.ibGooglePlacesSave:


                //finish();
                if (TextUtils.isEmpty(etMapSearch.getText().toString())) {
                    Toast.makeText(GoogleMap.this, "Please select location", Toast.LENGTH_SHORT).show();
                } else {
                    setResult(Activity.RESULT_OK, new Intent().putExtra("typeAddress", getIntent().getIntExtra("typeAddress", 0)).putExtra("address", addObj.toString()).putExtra("AreaName", etMapSearch.getText().toString()));
                    finish();
                }

                break;
        }
    }

    public void latlng() {
        Point mdispSize = new Point();
        mdispSize.set(Math.round(ivMapImage.getX()), Math.round(ivMapImage.getY()));
        Projection myProjection;
        LatLng markerPosition;
        myProjection = mMap.getProjection();
        markerPosition = myProjection.fromScreenLocation(mdispSize);
//        etMapSearch.setTag(markerPosition);
        new ReverseGeocodingTask(getBaseContext()).execute(markerPosition);
    }

    private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {
        Context mContext;

        public ReverseGeocodingTask(Context context) {
            super();
            mContext = context;
        }

        // Finding address using reverse geocoding
        @Override
        protected String doInBackground(LatLng... params) {
            Geocoder geocoder = new Geocoder(mContext);
            double latitude = params[0].latitude;
            double longitude = params[0].longitude;

            List<Address> addresses = null;
            String addressText = "";

            try {

                addresses = geocoder.getFromLocation(latitude, longitude, 1);


                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);

                    Log.i("address", "" + address.toString());
                    addressText = "";
                    if (address.getMaxAddressLineIndex() > 0) {
                        for (int i = 0; i < address.getMaxAddressLineIndex()+1; i++) {
                            String s = address.getMaxAddressLineIndex() == i ? "" : ", ";
                            addressText = addressText + address.getAddressLine(i) + s;

                        }
                    }

//                    addressText = String.format("%s, %s, %s",
//                            address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
//                            address.getLocality() == null ? address.getAdminArea() : address.getLocality(),
//                            address.getCountryName());


                    addObj = new JSONObject();
                    String AreaName = address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : " ";
//                    String AreaName = address.getFeatureName() == null ? " " : address.getFeatureName();

//                    String AreaAddress = address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : " ";
                    String AreaAddress = addressText;
                    String AreaLat = String.valueOf(latitude);
                    String AreaLong = String.valueOf(longitude);
                    String AreaPostalCode = address.getPostalCode() == null ? " " : address.getPostalCode();
                    String AreaCity = address.getLocality() == null ? address.getAdminArea() : address.getLocality();

                    addObj.put("viaAreaName", AreaName.equalsIgnoreCase(null) ? " " : AreaName);
                    addObj.put("viaAreaPlaceid", " ");
                    addObj.put("viaAreaLat", AreaLat.equalsIgnoreCase(null) ? " " : AreaLat);
                    addObj.put("viaAreaLong", AreaLong.equalsIgnoreCase(null) ? " " : AreaLong);
                    addObj.put("viaAreaAddress", AreaAddress.equalsIgnoreCase(null) ? " " : AreaAddress);
                    addObj.put("viaAreaPostalCode", AreaPostalCode.equalsIgnoreCase(null) ? " " : AreaPostalCode);
                    addObj.put("viaAreaCity", AreaCity.equalsIgnoreCase(null) ? " " : AreaCity);
                    addObj.put("viaFullAddress", AreaName.equalsIgnoreCase(null) ? " " : AreaName);
                    Log.i("jsonObj: ", addObj.toString());


                }
            } catch (
                    Exception e
                    )

            {
                e.printStackTrace();
            }

            return addressText;
        }

        @Override
        protected void onPostExecute(String addressText) {
            etMapSearch.setText(addressText);

        }

    }

    @Override
    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.507340980937535, -0.12766644358634949), 10.0f));
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setOnCameraChangeListener(new com.google.android.gms.maps.GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                latlng();
            }
        });
    }

    public void getCurrentLocationSuggestions() {

        final LocationFinderService myLocation = new LocationFinderService();
        if (!CommonUtil.isInternetAvailable(GoogleMap.this))
            CommonUtil.byToastMessage(this, getResources().getString(R.string.nointernet_try_again_msg));
        else {
            CommonUtil.showProgressDialog(this, getResources().getString(R.string.searching));
            LocationFinderService.LocationResult locationResult = new LocationFinderService.LocationResult() {
                @Override
                public void gotLocation(final Location location) {
                    //Got the location!
                    myLocation.stopLocation();
                    CommonUtil.cancelProgressDialog();
                    if (location != null) {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(current)
                                        .zoom(16)
                                        .build();
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                                        300, null);
                            }
                        });
                    } else {
//                        CommonUtil.cancelProgressDialog();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                CommonUtil.byToastMessage(GoogleMap.this, getResources().getString(R.string.unavailable_to_find_place));
                            }
                        });

                    }
                }
            };

            myLocation.getLocation(this, locationResult);
        }
    }

//    public void fillLocationList(String AreaName,
//                                 String AreaAddress,
//                                 String AreaLat,
//                                 String AreaLong,
//                                 String AreaPostalCode,
//                                 String AreaCity) {
//        try {
//            JSONObject addObj = new JSONObject();
//            addObj.put("viaAreaName", AreaName.equalsIgnoreCase("") ? " " : AreaName);
//            addObj.put("viaAreaPlaceid", " ");
//            addObj.put("viaAreaLat", AreaLat.equalsIgnoreCase("") ? " " : AreaLat);
//            addObj.put("viaAreaLong", AreaLong.equalsIgnoreCase("") ? " " : AreaLong);
//            addObj.put("viaAreaAddress", AreaAddress.equalsIgnoreCase("") ? " " : AreaAddress);
//            addObj.put("viaAreaPostalCode", AreaPostalCode.equalsIgnoreCase("") ? " " : AreaPostalCode);
//            addObj.put("viaAreaCity", AreaCity.equalsIgnoreCase("") ? " " : AreaCity);
//            Log.i("jsonObj: ", addObj.toString());
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

