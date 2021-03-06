package com.smartsense.taxinearyou.utill;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.mpt.storage.SharedPreferenceUtil;
import com.onesignal.OneSignal;
import com.smartsense.taxinearyou.GeneralInformation;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.Search;
import com.smartsense.taxinearyou.SecurityQuestion;
import com.smartsense.taxinearyou.SignIn;
import com.smartsense.taxinearyou.SnackBar.TSnackbar;
import com.smartsense.taxinearyou.TaxiNearYouApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.http.HttpStatus;


public class CommonUtil {

    static ProgressDialog pDialog;
    SQLiteDatabase sqLiteDatabase;
    static AlertDialog alert;

    public static void showProgressDialog(Context activity, String msg) {
        activity.setTheme(R.style.New_Style);
        pDialog = new ProgressDialog(activity);
//        pDialog.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        pDialog.setMessage(msg);
        pDialog.setCancelable(false);
        pDialog.show();
        activity.setTheme(R.style.AppTheme);
    }


    public static void cancelProgressDialog() {
        if (pDialog != null)
            pDialog.cancel();
    }

    public static boolean isValidEmail(CharSequence strEmail) {
        return !TextUtils.isEmpty(strEmail) && android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
    }

    public static boolean isLegalPassword(String pass) {

        Pattern p = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", Pattern.DOTALL);
        Matcher m = p.matcher(pass);

        return m.find();
    }

    public static boolean isSpecialChar(String pass) {

        Pattern p = Pattern.compile("[&@!#+]", Pattern.DOTALL);
        Matcher m = p.matcher(pass);

        return m.find();
    }

    public static void alertBox(final Activity context, final String msg) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setCancelable(false);
//        builder.setMessage(msg);
//        builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                if (msg.equalsIgnoreCase(context.getResources().getString(R.string.event_complete))) {
//                    context.finish();
//                    context.setTheme(R.style.AppTheme);
//                }
//            }
//        });
//        builder.create();
//        builder.show();

        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialog = inflater.inflate(R.layout.dialog_all, null);
            LinearLayout linearLayout;
            Button button1;
            linearLayout = (LinearLayout) dialog.findViewById(R.id.lyPopUpGen);
            TextView tvPopupLocatedEmail = (TextView) dialog.findViewById(R.id.tvPopupGen);
            if (context instanceof SecurityQuestion) {
                tvPopupLocatedEmail.setGravity(Gravity.LEFT);
            }
            tvPopupLocatedEmail.setText(msg);
            button1 = (Button) dialog.findViewById(R.id.btPopupGen);
            linearLayout.setVisibility(View.VISIBLE);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    if (msg.equalsIgnoreCase(context.getResources().getString(R.string.event_complete))) {
                        context.finish();
                        context.setTheme(R.style.AppTheme);
                    } else if (context instanceof SecurityQuestion) {
                        context.startActivity(new Intent(context, SignIn.class));
                        context.finish();
                    } else if (context instanceof GeneralInformation) {
//                        context.finish();
//                        context.setTheme(R.style.AppTheme);
                    }
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

    public static void byToastMessage(Activity activity, String msg) {
        activity.setTheme(R.style.New_Style);
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        activity.setTheme(R.style.AppTheme);
    }

    public static void errorToastShowing(Activity activity) {
        activity.setTheme(R.style.New_Style);
        Toast.makeText(activity, activity.getResources().getString(R.string.inter_error), Toast.LENGTH_SHORT).show();
        activity.setTheme(R.style.AppTheme);
    }

    public static void successToastShowing(Activity activity, JSONObject jsonObject) {
        activity.setTheme(R.style.New_Style);
        Toast.makeText(activity, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
        activity.setTheme(R.style.AppTheme);
    }

    public static void jsonNullError(Activity activity) {
        activity.setTheme(R.style.New_Style);
        Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
        activity.setTheme(R.style.AppTheme);
    }

    public static void conditionAuthentication(Activity activity, JSONObject jsonObject) {
        activity.setTheme(R.style.New_Style);
        if (jsonObject.has("json") && jsonObject.optJSONObject("json").has("errorCode") && jsonObject.optJSONObject("json").optInt("errorCode") == Constants.ErrorCode.UNAUTHENTICATED_OPERATION) {
            Toast.makeText(activity, activity.getResources().getString(R.string.session_expire), Toast.LENGTH_SHORT).show();
            SharedPreferenceUtil.clear();
            SharedPreferenceUtil.save();
            OneSignal.sendTag("emailId", "");
            activity.startActivity(new Intent(activity, SignIn.class));
            activity.finish();
            activity.setTheme(R.style.AppTheme);
        } else
            successToastShowing(activity, jsonObject);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void FitSystemUI(CoordinatorLayout coordinatorLayout) {
        coordinatorLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
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

    public static InputFilter textFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence c, int arg1, int arg2, Spanned arg3, int arg4, int arg5) {
            StringBuilder sbText = new StringBuilder(c);
            String text = sbText.toString();
            if (text.contains(" ")) {
                return "";
            }
            return c;
        }
    };

    public static void handlerDialog(final AlertDialog alert, final LinearLayout linearLayout) {
        Handler mHandler = new Handler();
        Runnable mRunnable = new Runnable() {
            public void run() {
                alert.show();
                alert.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                linearLayout.setVisibility(View.VISIBLE);
            }
        };
        mHandler.postDelayed(mRunnable, 500);
    }

    public static String text(EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().equalsIgnoreCase(""))
                    s = "";
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return "";
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
//            View view = a.getCurrentFocus();
//            if (view != null) {
            if (isKeyboardVisible(a)) {
                InputMethodManager imm = (InputMethodManager) a.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isKeyboardVisible(Activity activity) {
        ///This method is based on the one described at http://stackoverflow.com/questions/4745988/how-do-i-detect-if-software-keyboard-is-visible-on-android-device
        Rect r = new Rect();
        View contentView = activity.findViewById(android.R.id.content);
        contentView.getWindowVisibleDisplayFrame(r);
        int screenHeight = contentView.getRootView().getHeight();

        int keypadHeight = screenHeight - r.bottom;

        return
                (keypadHeight > screenHeight * 0.15);
    }

    public static void hideKeyboard(Context activityContext) {

        InputMethodManager imm = (InputMethodManager)
                activityContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        //android.R.id.content ( http://stackoverflow.com/a/12887919/2077479 )
        View rootView = ((Activity) activityContext)
                .findViewById(android.R.id.content).getRootView();

        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
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

    public static String utf8Convert(StringBuilder builder, int apiKey) {
        String url = "";
        try {
            url = Constants.BASE_URL + Constants.BASE_URL_POSTFIX + apiKey + "&json="
                    + URLEncoder.encode(builder.toString(), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String changeTimeSmallToCaps(String timeBack, String from, String to) {

        return timeBack.replace(from, to);
    }

    public static String getDecimal(Double x) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        String s = formatter.format(x);
        return s;
    }

    public static void showSnackBar(String msg, View view) {
        TSnackbar snackbar = TSnackbar
                .make(view, msg, TSnackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.WHITE);
        TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
    }

    public static void openDialogs(final Activity activity, final String buton_click, int layout, int button, String successText, int textViewId) {

        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(activity);
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View dialog = inflater.inflate(R.layout.dialog_all, null);
            LinearLayout linearLayout = (LinearLayout) dialog.findViewById(layout);
            Button button1 = (Button) dialog.findViewById(button);

            TextView tvDialogAllSuccess = (TextView) dialog.findViewById(textViewId);
            tvDialogAllSuccess.setText(successText);

            linearLayout.setVisibility(View.VISIBLE);

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (buton_click.equalsIgnoreCase("Payment Details"))
                        activity.startActivity(new Intent(activity, Search.class));
                    if (buton_click.equalsIgnoreCase("Payment Details1"))
                        activity.startActivity(new Intent(activity, Search.class).putExtra("checkWallet", true));
                    else if (buton_click.equalsIgnoreCase("Recover Email"))
                        activity.startActivity(new Intent(activity, SignIn.class));
                    else if (buton_click.equalsIgnoreCase("Payment Fail"))
                        activity.startActivity(new Intent(activity, Search.class).putExtra("checkFlag", true));
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

    public static void openDialogs1(final Activity activity, final String buton_click, int layout, int button, String successText, int textViewId) {

        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(activity);
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View dialog = inflater.inflate(R.layout.dialog_all, null);
            LinearLayout linearLayout = (LinearLayout) dialog.findViewById(layout);
            Button button1 = (Button) dialog.findViewById(button);

            TextView tvDialogAllSuccess = (TextView) dialog.findViewById(textViewId);
            tvDialogAllSuccess.setText(successText);

            linearLayout.setVisibility(View.VISIBLE);

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();

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

    public static void storeUserData(JSONObject jsonObject) {
        if (jsonObject.optString("userId").equalsIgnoreCase(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""))) {
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_FULLNAME, jsonObject.optString("firstName") + " " + jsonObject.optString("lastName"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_FIRST, jsonObject.optString("firstName"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_LAST, jsonObject.optString("lastName"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_EMAIL, jsonObject.optString("primaryEmailId"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ALTERNATE_EMAIL, jsonObject.optString("alternateEmailId"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_MNO, jsonObject.optString("mobileNo"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_PROIMG, Constants.BASE_URL_IMAGE_POSTFIX + jsonObject.optString("profilePic"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_STATUS, jsonObject.optString("isActivated"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.RECEIVE_ORG_OFFERS, jsonObject.optString("receiveOrgOffers"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.RECEIVE_TNU_OFFERS, jsonObject.optString("receiveTnuOffers"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_QUESTION1, jsonObject.optString("secQ1"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_QUESTION2, jsonObject.optString("secQ2"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_ANSWER1, jsonObject.optString("ans1"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_ANSWER2, jsonObject.optString("ans2"));
            SharedPreferenceUtil.save();
        }
    }

    public static boolean CheckCreditCard(String ccNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}