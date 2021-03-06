package com.smartsense.taxinearyou.utill;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class LocationFinderService {
    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_enabled = false;
    Context context;
    boolean network_enabled = false;

    public boolean getLocation(Context context, LocationResult result) {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult = result;
        this.context = context;
        if (lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        //don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled) {
            locationResult.gotLocation(null);
            return false;
        }
        try {
            if (gps_enabled)
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
            if (network_enabled)
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        timer1 = new Timer();
        timer1.schedule(new GetLastLocation(), 10000);
        return true;
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            try {
                lm.removeUpdates(this);
                lm.removeUpdates(locationListenerNetwork);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        public void onProviderDisabled(String provider) {
            locationResult.gotLocation(null);
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            try {
                lm.removeUpdates(this);
                lm.removeUpdates(locationListenerGps);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        public void onProviderDisabled(String provider) {
            locationResult.gotLocation(null);
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            try {
                if (CommonUtil.isGPS(context)) {
                    lm.removeUpdates(locationListenerGps);
                    lm.removeUpdates(locationListenerNetwork);

                    Location net_loc = null, gps_loc = null;
                    if (gps_enabled)
                        gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (network_enabled)
                        net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    //if there are both values use the latest one
                    if (gps_loc != null && net_loc != null) {
                        if (gps_loc.getTime() > net_loc.getTime())
                            locationResult.gotLocation(gps_loc);
                        else
                            locationResult.gotLocation(net_loc);
                        return;
                    }

                    if (gps_loc != null) {
                        locationResult.gotLocation(gps_loc);
                        return;
                    }
                    if (net_loc != null) {
                        locationResult.gotLocation(net_loc);
                        return;
                    }
                    locationResult.gotLocation(null);
                } else
                    locationResult.gotLocation(null);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }

    public void stopLocation() {
        if (timer1 != null)
            timer1.cancel();
        if (lm != null)
            try {
                lm.removeUpdates(locationListenerNetwork);
                lm.removeUpdates(locationListenerGps);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
    }
}