package com.smartsense.taxinearyou.Fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.onesignal.OneSignal;
import com.smartsense.taxinearyou.AccountSecurity;
import com.smartsense.taxinearyou.CardList;
import com.smartsense.taxinearyou.GeneralInformation;
import com.smartsense.taxinearyou.LostItem;
import com.smartsense.taxinearyou.More;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.SignIn;
import com.smartsense.taxinearyou.TaxiNearYouApp;
import com.smartsense.taxinearyou.utill.CircleImageView1;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;
import com.smartsense.taxinearyou.utill.MultipartRequestJson;
import com.smartsense.taxinearyou.utill.WakeLocker;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class FragmentMenu extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {

    CircleImageView1 cvAccountPhoto;
    TextView tvAccountPersonName;
    TextView tvAccountGeneralInfo, tvAccountAccountSecurity, tvAccountPayment, tvAccountCredits, tvAccountLostItems, tvAccountLogout, tvAccountMore;
    final int REQUEST_CAMERA = 0;
    final int SELECT_FILE = 1;
    int whichSelect;
    ImageView ivEditProfilePhoto;
    Button btAccountActivateNow;
    LinearLayout llFragmentMenuLogOut, llFragmentMenuMore, llFragmentMenuLostItem, llFragmentMenuCredit, llFragmentMenuPayment, llFragmentMenuAccountSecurity, llFragmentMenuGeneralInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        getActivity().registerReceiver(tripMessageReceiver, new IntentFilter(String.valueOf(Constants.Events.UPDATE_PROFILE_PIC)));
        getActivity().registerReceiver(tripMessageReceiver, new IntentFilter(String.valueOf(Constants.Events.UPDATE_GENERAL_INFO)));

        cvAccountPhoto = (CircleImageView1) rootView.findViewById(R.id.cvAccountPhoto);
        tvAccountPersonName = (TextView) rootView.findViewById(R.id.tvAccountPersonName);
        tvAccountGeneralInfo = (TextView) rootView.findViewById(R.id.tvAccountGeneralInfo);
        tvAccountAccountSecurity = (TextView) rootView.findViewById(R.id.tvAccountAccountSecurity);
        tvAccountPayment = (TextView) rootView.findViewById(R.id.tvAccountPayment);
        tvAccountCredits = (TextView) rootView.findViewById(R.id.tvAccountCredits);
        tvAccountLostItems = (TextView) rootView.findViewById(R.id.tvAccountLostItems);
        tvAccountMore = (TextView) rootView.findViewById(R.id.tvAccountMore);
        tvAccountLogout = (TextView) rootView.findViewById(R.id.tvAccountLogout);
        btAccountActivateNow = (Button) rootView.findViewById(R.id.btAccountActivateNow);
        ivEditProfilePhoto = (ImageView) rootView.findViewById(R.id.ivEditProfilePhoto);
        llFragmentMenuLogOut = (LinearLayout) rootView.findViewById(R.id.llFragmentMenuLogOut);
        llFragmentMenuMore = (LinearLayout) rootView.findViewById(R.id.llFragmentMenuMore);
        llFragmentMenuLostItem = (LinearLayout) rootView.findViewById(R.id.llFragmentMenuLostItem);
        llFragmentMenuCredit = (LinearLayout) rootView.findViewById(R.id.llFragmentMenuCredit);
        llFragmentMenuPayment = (LinearLayout)rootView.findViewById(R.id.llFragmentMenuPayment);
        llFragmentMenuAccountSecurity = (LinearLayout) rootView.findViewById(R.id.llFragmentMenuAccountSecurity);
        llFragmentMenuGeneralInfo = (LinearLayout) rootView.findViewById(R.id.llFragmentMenuGeneralInfo);

        tvAccountLogout.setOnClickListener(this);
        tvAccountAccountSecurity.setOnClickListener(this);
        tvAccountLostItems.setOnClickListener(this);
        tvAccountGeneralInfo.setOnClickListener(this);
        tvAccountPayment.setOnClickListener(this);
        tvAccountMore.setOnClickListener(this);
        ivEditProfilePhoto.setOnClickListener(this);
        btAccountActivateNow.setOnClickListener(this);
        llFragmentMenuGeneralInfo.setOnClickListener(this);
        llFragmentMenuAccountSecurity.setOnClickListener(this);
        llFragmentMenuPayment.setOnClickListener(this);
        llFragmentMenuCredit.setOnClickListener(this);
        llFragmentMenuLostItem.setOnClickListener(this);
        llFragmentMenuMore.setOnClickListener(this);
        llFragmentMenuLogOut.setOnClickListener(this);

        setDataInActivity();

        return rootView;
    }

    public void setDataInActivity() {
        if (SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_STATUS, "").equalsIgnoreCase("1"))
            btAccountActivateNow.setVisibility(View.GONE);
        else
            btAccountActivateNow.setVisibility(View.VISIBLE);

        if (btAccountActivateNow.getVisibility() != View.VISIBLE) {
            cvAccountPhoto.setBorderColor(ContextCompat.getColor(getActivity(), R.color.dark_green));
        }

        if (!TextUtils.isEmpty(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_PROIMG, "")))
            Picasso.with(getActivity())
                    .load(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_PROIMG, ""))
                    .error(R.mipmap.icon_user)
                    .placeholder(R.mipmap.icon_user)
                    .into(cvAccountPhoto);

        tvAccountPersonName.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_FULLNAME, ""));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAccountLogout:
                doLogout();
                break;
            case R.id.tvAccountAccountSecurity:
                startActivity(new Intent(getActivity(), AccountSecurity.class));
                break;
            case R.id.tvAccountLostItems:
                startActivity(new Intent(getActivity(), LostItem.class));
                break;
            case R.id.tvAccountGeneralInfo:
                startActivityForResult(new Intent(getActivity(), GeneralInformation.class),10);
                break;
            case R.id.tvAccountPayment:
            case R.id.llFragmentMenuPayment:
                startActivity(new Intent(getActivity(), CardList.class));
                break;
            case R.id.tvAccountMore:
                startActivity(new Intent(getActivity(), More.class));
                break;
            case R.id.ivEditProfilePhoto:
                images();
                break;
            case R.id.btAccountActivateNow:
                activeAccount();
                break;
            case R.id.llFragmentMenuGeneralInfo:
                tvAccountGeneralInfo.performClick();
                break;
            case R.id.llFragmentMenuAccountSecurity:
                tvAccountAccountSecurity.performClick();
                break;
            case R.id.llFragmentMenuCredit:
                tvAccountCredits.performClick();
                break;
            case R.id.llFragmentMenuLostItem:
                tvAccountLostItems.performClick();
                break;
            case R.id.llFragmentMenuMore:
                tvAccountMore.performClick();
                break;
            case R.id.llFragmentMenuLogOut:
                tvAccountLogout.performClick();
                break;
        }
    }

    private void doLogout() {
        final String tag = "do Logout";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(getActivity(), getResources().getString(R.string.login_out), CommonUtil.utf8Convert(builder, Constants.Events.EVENT_LOGOUT), tag, this, this);
    }

    private void activeAccount() {
        final String tag = "Active Account";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                    .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(getActivity(), getResources().getString(R.string.send_data), CommonUtil.utf8Convert(builder, Constants.Events.ACTIVE_ACCOUNT), tag, this, this);
    }

    private void doUpload(Intent data) {
        final String tag = "Do Upload";
        long lengthBitmap;
        Bitmap bitmap;

        if (whichSelect == REQUEST_CAMERA) {
            bitmap = (Bitmap) data.getExtras().get("data");
            lengthBitmap = 100;
        } else {
            Uri uri = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getActivity().getContentResolver().openInputStream(uri);
            } catch (NullPointerException | FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageInByte = stream.toByteArray();
            lengthBitmap = imageInByte.length / 2187;
            Log.i("lengthBitmap", lengthBitmap + "");
        }

        if (lengthBitmap <= 500) {

            try {
                String imageBase64 = CommonUtil.BitMapToString(bitmap);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userProfilePic", imageBase64);
                jsonObject.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
                jsonObject.put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""));

                String urlType = Constants.BASE_URL_PHOTO;
                HashMap<String, String> params = new HashMap<>();
                params.put("userProfilePic", imageBase64);
                params.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
                params.put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""));
                params.put("__eventid", Constants.Events.UPDATE_PROFILE_PIC + "");
                params.put("json", jsonObject.toString());
                Log.i("params", params.toString());

                CommonUtil.jsonRequestPOST(getActivity(), getResources().getString(R.string.updating), urlType, params, tag, this, this);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            CommonUtil.byToastMessage(getActivity(), getResources().getString(R.string.less_100));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null)
            switch (requestCode) {
                case SELECT_FILE:
                    List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    doUpload(path.get(0));
                    break;
                case REQUEST_CAMERA:
                    doUpload(data);
                    break;
                case 10:

                    break;
            }
//        else
//            Toast.makeText(getContext(), getResources().getString(R.string.cancel_camera), Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        tvAccountPersonName.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_FULLNAME, ""));
    }


    public void images() {
        final CharSequence[] items = {getResources().getString(R.string.take_photo), getResources().getString(R.string.select_pic), getResources().getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.select_source));
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getResources().getString(R.string.take_photo))) {
                    whichSelect = REQUEST_CAMERA;

                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals(getResources().getString(R.string.select_pic))) {
                    whichSelect = SELECT_FILE;
                    Intent intent = new Intent(getActivity(), MultiImageSelectorActivity.class);
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
                    startActivityForResult(intent, SELECT_FILE);
//                    Intent intImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intImage.setType("image/*");
//                    startActivityForResult(intImage, SELECT_FILE);
                } else if (items[item].equals(getResources().getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.cancelProgressDialog();
        CommonUtil.errorToastShowing(getActivity());
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (response.getInt("__eventid")) {
                        case Constants.Events.EVENT_LOGOUT:
                            SharedPreferenceUtil.clear();
                            SharedPreferenceUtil.save();
                            OneSignal.sendTag("emailId", "");
                            startActivity(new Intent(getActivity(), SignIn.class));
                            getActivity().finish();
                            break;
                        case Constants.Events.UPDATE_PROFILE_PIC:

                            CommonUtil.alertBox(getActivity(), response.optString("msg"));

                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_PROIMG, Constants.BASE_URL_IMAGE_POSTFIX + response.optJSONObject("json").optJSONObject("user").optString("profilePic"));
                            SharedPreferenceUtil.save();

                            Picasso.with(getActivity())
                                    .load(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_PROIMG, ""))
                                    .error(R.mipmap.imgtnulogo)
                                    .placeholder(R.mipmap.imgtnulogo)
                                    .into(cvAccountPhoto);

                            break;
                        case Constants.Events.ACTIVE_ACCOUNT:
                            CommonUtil.alertBox(getActivity(), response.optString("msg"));
                            break;
                    }
                } else {
                    CommonUtil.conditionAuthentication(getActivity(), response);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else CommonUtil.jsonNullError(getActivity());
    }

    private final BroadcastReceiver tripMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WakeLocker.acquire(context);
            Log.i("Push ", intent.getStringExtra(Constants.EXTRAS));
            try {
//                JSONObject pushData = new JSONObject(intent.getStringExtra(Constants.EXTRAS));
//                CommonUtil.storeUserData(pushData.optJSONObject("user"));
                setDataInActivity();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    @Override
    public void onResume() {
        super.onResume();
//        LinearLayout llToolbarAll = (LinearLayout) getActivity().findViewById(R.id.llToolbarAll);
//        llToolbarAll.setVisibility(View.GONE);
    }

    private void doUpload(String outputFile) {
        final String tag = "doUpload";
        File file = null;
        file = new File(outputFile);
        CommonUtil.showProgressDialog(getActivity(), "Uploading...");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, ""));
        MultipartRequestJson multipartRequest = new MultipartRequestJson(Constants.BASE_URL_IMAGE_POSTFIX + "/CustomerProfilePic",
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        CommonUtil.cancelProgressDialog();
                        try {
                            if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                                Log.i("Push ", response.toString());
//                                ivProfileImage.setDefaultImageResId(R.drawable.ic_user);
                                SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_PROIMG, Constants.BASE_URL_IMAGE_POSTFIX +response.optJSONObject("json").optJSONObject("userInfo").optString("profilePic"));
                                SharedPreferenceUtil.save();
                                if (!TextUtils.isEmpty(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_PROIMG, "")))
                                    Picasso.with(getActivity())
                                            .load(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_PROIMG, ""))
                                            .error(R.mipmap.icon_user)
                                            .placeholder(R.mipmap.icon_user)
                                            .into(cvAccountPhoto);
//                                Log.d("response", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_PROIMG, ""));
                                CommonUtil.alertBox(getActivity(), response.optString("msg"));
//                                ivProfileImage.setDefaultImageResId(R.drawable.ic_user);
//                                ivProfileImage.setImageUrl(Constants.BASE_URL + "/images/users/" + userInfo.optString("image"), imageLoader);
//                                final AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
//                                alert.setTitle("Success!");
//                                alert.setMessage(response.optString("message"));
//                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//
//                                    }
//
//                                });
//                                alert.show();

                            } else {
//                                JsonErrorShow.jsonErrorShow(response, );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                CommonUtil.alertBox(getActivity(), "", getActivity().getResources().getString(R.string.nointernet_try_again_msg));
                CommonUtil.cancelProgressDialog();

                Log.e("Volley Request Error", error.getLocalizedMessage());

            }

        }, file, params);
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        TaxiNearYouApp.getInstance().addToRequestQueue(multipartRequest, tag);

    }
}
