package com.smartsense.taxinearyou;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.imageutil.DiskBitmapCache;
import com.smartsense.taxinearyou.imageutil.BitmapCache;
import com.smartsense.taxinearyou.imageutil.BitmapUtil;

public class TaxiNearYouApp extends Application {
    private static TaxiNearYouApp appInstance;
    public static final String TAG = TaxiNearYouApp.class.getSimpleName();
//    private static char[] KEYSTORE_PASSWORD = "gifkar".toCharArray();
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        this.setAppContext(getApplicationContext());
//        Parse.initialize(this, "9yjUxrJuubYfJQvh8gONZuZqTEu3gcpqB1mdRkpH", "w3znN1nsItMDKIZaTJ8qzdRDPnfFKeW6uAI56C8Y");
//        ParseInstallation.getCurrentInstallation().put("user", "customer");
//        ParseInstallation.getCurrentInstallation().saveInBackground();
        SharedPreferenceUtil.init(this);
    }

    public static synchronized TaxiNearYouApp getInstance() {
        return appInstance;
    }

    public static Context getAppContext() {
        return mAppContext;
    }
    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
//            requestQueue = Volley.newRequestQueue(getApplicationContext(),new HurlStack(null, newSslSocketFactory()));
        }
        return requestQueue;
    }

    public ImageLoader getDiskImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            imageLoader = new ImageLoader(this.requestQueue,
                    new DiskBitmapCache(getCacheDir(),
                            BitmapUtil.MAX_CATCH_SIZE));
        }
        return this.imageLoader;
    }

    public ImageLoader getCacheImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            imageLoader = new ImageLoader(this.requestQueue, new BitmapCache(
                    BitmapUtil.MAX_CATCH_SIZE));
        }
        return this.imageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

//    private SSLSocketFactory newSslSocketFactory() {
//        try {
//            // Get an instance of the Bouncy Castle KeyStore format
//            KeyStore trusted = KeyStore.getInstance("BKS");
//            // Get the raw resource, which contains the keystore with
//            // your trusted certificates (root and any intermediate certs)
//            InputStream in = GifkarApp.getInstance().getResources().openRawResource(R.raw.gifkar);
////            InputStream in = GifkarApp.getInstance().getResources().openRawResource(R.raw.local);
//            try {
//                // Initialize the keystore with the provided trusted certificates
//                // Provide the password of the keystore
//                trusted.load(in, KEYSTORE_PASSWORD);
//            } finally {
//                in.close();
//            }
//
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(trusted);
//
//            SSLContext context = SSLContext.getInstance("TLS");
//            context.init(null, tmf.getTrustManagers(), null);
//
//            SSLSocketFactory sf = context.getSocketFactory();
//            return sf;
//        } catch (Exception e) {
//            throw new AssertionError(e);
//        }
//    }
}