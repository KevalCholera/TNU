package com.smartsense.taxinearyou.utill;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.Search;
import com.smartsense.taxinearyou.SignIn;
import com.smartsense.taxinearyou.SnackBar.TSnackbar;
import com.smartsense.taxinearyou.TaxiNearYouApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

//import org.apache.http.HttpStatus;


public class CommonUtil {

    static ProgressDialog pDialog;
    SQLiteDatabase sqLiteDatabase;
    static AlertDialog alert;

    public static void showProgressDialog(Context activity, String msg) {
        pDialog = new ProgressDialog(activity);
//        pDialog.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        pDialog.setMessage(msg);
        pDialog.setCancelable(false);
        pDialog.show();
    }


    public static void cancelProgressDialog() {
        if (pDialog != null)
            pDialog.cancel();
    }

    public static boolean isValidEmail(CharSequence strEmail) {
        return !TextUtils.isEmpty(strEmail) && android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
    }

    public static void alertBox(final Activity context, final String msg, final boolean check, final boolean canFinish) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(msg);
        if (check)
            builder.setIcon(ContextCompat.getDrawable(context, android.R.drawable.progress_horizontal));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (check)
                    context.startActivity(new Intent(context, Search.class));
                else {
                    if (canFinish)
                        context.finish();
                }
            }
        });
        builder.create();
        builder.show();
    }

    static public ActionBar getActionBar(Activity a) {
        return ((AppCompatActivity) a).getSupportActionBar();
    }

    static public Toolbar getToolBar(Activity a) {
        return (Toolbar) ((AppCompatActivity) a).getSupportActionBar().getCustomView();
    }

    public static boolean isInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

//    public static void volleyErrorHandler(VolleyError volleyError, Activity activity) {
//
//        NetworkResponse response = null;
//
//        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
//            // CommonUtil.ShowAlert(activity,
//            // "No connection" + volleyError.getMessage());
//            response = volleyError.networkResponse;
//        } else if (volleyError instanceof AuthFailureError) {
//            response = volleyError.networkResponse;
//        } else if (volleyError instanceof ServerError) {
//            response = volleyError.networkResponse;
//        } else if (volleyError instanceof NetworkError) {
//            response = volleyError.networkResponse;
//        } else if (volleyError instanceof ParseError) {
//            String error = volleyError.getMessage();
//            // CommonUtil.ShowAlert(activity, "Wrong data recived" + error);
//
//        }
//
//        if (response != null && response.data != null) {
//
//            switch (response.statusCode) {
//                case HttpStatus.SC_NOT_FOUND:
//
////				Toast.makeText(activity, "Page not found ERROR 404", Toast.LENGTH_LONG).show();
//
//                    CommonUtil
//                            .alertBox(activity, "", "Page not found Error code :404");
//                    break;
//                case HttpStatus.SC_INTERNAL_SERVER_ERROR:
//                    CommonUtil.alertBox(activity, "", "Server Error code :500");
//                    break;
//                default:
//                    CommonUtil.alertBox(activity, "", "Error");
//                    break;
//            }
//        }
//    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
//            NetworkInfo[] info = connectivity.getAllNetworkInfo();
//            if (info != null)
//                for (int i = 0; i < info.length; i++)
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
            NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;

    }

    public static boolean isGPS(Context context) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    public static void errorToastShowing(Activity activity) {
        Toast.makeText(activity, "Internet Error", Toast.LENGTH_SHORT).show();
    }

    public static void successToastShowing(Activity activity, JSONObject jsonObject) {
        Toast.makeText(activity, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
    }

    public static void jsonNullError(Activity activity) {
        Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
    }

    public static void conditionAuthentication(Activity activity, JSONObject jsonObject) {
//    public static void conditionAuthentication(Activity activity, JSONObject jsonObject, CoordinatorLayout coordinatorLayout) {
        successToastShowing(activity, jsonObject);
        if (jsonObject.has("json") && jsonObject.optJSONObject("json").has("errorCode") && jsonObject.optJSONObject("json").optInt("errorCode") == Constants.ErrorCode.UNAUTHENTICATED_OPERATION) {
            successToastShowing(activity, jsonObject);
            activity.startActivity(new Intent(activity, SignIn.class));
            SharedPreferenceUtil.clear();
            SharedPreferenceUtil.save();
        }
    }

    public static void jsonRequestPOST(Activity activity, String msg, String urlType, HashMap<String, String> params, String tag, Response.Listener<JSONObject> requestListener, Response.ErrorListener errorListener) {
        CommonUtil.showProgressDialog(activity, msg);
        DataRequest dataRequest = new DataRequest(Request.Method.POST, urlType, params, requestListener, errorListener);
        dataRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }

    public static void jsonRequestGET(Activity activity, String msg, String builder, String tag, Response.Listener<JSONObject> requestListener, Response.ErrorListener errorListener) {
        CommonUtil.showProgressDialog(activity, msg);
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder, null, requestListener, errorListener);
        dataRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }

    public static void jsonRequestNoProgressBar(String builder, String tag, Response.Listener<JSONObject> requestListener, Response.ErrorListener errorListener) {
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder, null, requestListener, errorListener);
        dataRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }

//    public Cursor rawQuery(DataBaseHelper dbHelper, String sqlQuery) {
//        Log.i("sqlQuery", sqlQuery);
//        Cursor cursor = null;
//        try {
//            sqLiteDatabase = dbHelper.openDataBase();
//            cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
//            cursor.moveToFirst();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            sqLiteDatabase.close();
//        }
//        return cursor;
//    }

//    public Boolean execSQL(DataBaseHelper dbHelper, String sqlQuery) {
//        // TODO Auto-generated method stub
//        Boolean check = false;
//        try {
//            sqLiteDatabase = dbHelper.getWritableDatabase();
//            sqLiteDatabase.execSQL(sqlQuery);
//            check = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            check = false;
//        } finally {
//            sqLiteDatabase.close();
//        }
//        return check;
//
//    }

//    public long insert(DataBaseHelper dbHelper, String table, ContentValues values) {
//        sqLiteDatabase = dbHelper.getReadableDatabase();
//        String nullColumnHack = null;
//        return sqLiteDatabase.insert(table, nullColumnHack, values);
//    }

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.NO_WRAP);
        return temp;
    }

    public static Bitmap decodeFromBitmap(String encodedstring) {
        Bitmap decodedByte;
        try {
            byte[] decodedString = Base64.decode(encodedstring, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        } catch (Exception e) {
            e.printStackTrace();
            decodedByte = null;
        }
        return decodedByte;
    }

    static public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    static public String getRealPathFromURI(Uri uri, Activity a) {
        Cursor cursor = a.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static void openAppRating(Context context) {
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName()));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager().queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName.equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
            context.startActivity(webIntent);
        }
    }

    static public void closeKeyboard(Activity a) {
        try {
            InputMethodManager inputManager = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(a.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static Boolean isOnline(Context context) {
//        try {
//            showProgressDialog(context, "Wait...");
//            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
//            int returnVal = p1.waitFor();
//            boolean reachable = (returnVal == 0);
//            cancelProgressDialog();
//            return reachable;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return false;
//    }

    public static boolean isOnline() {
        try {
            checkStrictModeThread();
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1000);
            urlc.connect();

            return (urlc.getResponseCode() == 200);

        } catch (IOException e) {

            return false;
        }
    }

    public static void checkStrictModeThread() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public static String beforeAlaram(int id) {
        switch (id) {
            case 1:
                return "1 Day Before";
            case 2:
                return "2 Day Before";
            case 3:
                return "1 Hour Before";
            default:
                return "1 Day Before";
        }
    }

    public static int checkCartCount() {
        try {
            if (SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PROD_LIST, "") != "") {
                JSONArray productArray = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PROD_LIST, ""));
                if (productArray.length() < 1) {
                    return 0;
                } else {
                    return productArray.length();
                }
            } else {
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void disableView(View v) {

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                disableView(child);
            }
        }
    }

    public static boolean isApplicationRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (context.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }

    public static void showSnackBar(Activity activity, String msg, View view) {
        TSnackbar snackbar = TSnackbar.make(view, msg, TSnackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
    }

    public static void openDialogs(final Activity activity, final String buton_click, int layout, int button, String successText) {

        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(activity);
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View dialog = inflater.inflate(R.layout.dialog_all, null);
            LinearLayout linearLayout = (LinearLayout) dialog.findViewById(layout);
            Button button1 = (Button) dialog.findViewById(button);

            TextView tvDialogAllSuccess = (TextView) dialog.findViewById(R.id.tvDialogAllSuccess);
            tvDialogAllSuccess.setText(successText);

            linearLayout.setVisibility(View.VISIBLE);

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (buton_click.equalsIgnoreCase("Payment Details"))
                        activity.startActivity(new Intent(activity, Search.class));
                    else if (buton_click.equalsIgnoreCase("Recover Email"))
                        activity.startActivity(new Intent(activity, SignIn.class));
                    else
                        alert.dismiss();
                    activity.finish();
                }
            });
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(false);
            alert = alertDialogs.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}