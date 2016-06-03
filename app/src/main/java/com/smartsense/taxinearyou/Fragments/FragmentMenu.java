package com.smartsense.taxinearyou.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
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
import com.smartsense.taxinearyou.utill.DataRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class FragmentMenu extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {

    CircleImageView1 cvAccountPhoto;
    TextView tvAccountPersonName;
    TextView tvAccountGeneralInfo, tvAccountAccountSecurity, tvAccountPayment,
            tvAccountCredits, tvAccountLostItems, tvAccountLogout, tvAccountMore;
    private int image = 1;
    ImageView ivEditProfilePhoto;
    Button btAccountActivateNow;
    CoordinatorLayout clSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

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
        clSearch = (CoordinatorLayout) getActivity().findViewById(R.id.clSearch);

        if (SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_STATUS, "").equalsIgnoreCase("1"))
            btAccountActivateNow.setVisibility(View.GONE);

        if (btAccountActivateNow.getVisibility() == View.VISIBLE) {
            cvAccountPhoto.setBorderColor(ContextCompat.getColor(getActivity(), Color.GREEN));
            ivEditProfilePhoto.setBackgroundResource(R.drawable.circular_view_yellow_colored);
        }

        if (!TextUtils.isEmpty(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_PROIMG, "")))
            Picasso.with(getActivity())
                    .load(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_PROIMG, ""))
                    .error(R.mipmap.imgtnulogo)
                    .placeholder(R.mipmap.imgtnulogo)
                    .into(cvAccountPhoto);

        tvAccountPersonName.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_FULLNAME, ""));

        tvAccountLogout.setOnClickListener(this);
        tvAccountAccountSecurity.setOnClickListener(this);
        tvAccountLostItems.setOnClickListener(this);
        tvAccountGeneralInfo.setOnClickListener(this);
        tvAccountPayment.setOnClickListener(this);
        tvAccountMore.setOnClickListener(this);
        ivEditProfilePhoto.setOnClickListener(this);
        btAccountActivateNow.setOnClickListener(this);
        return rootView;
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
                startActivity(new Intent(getActivity(), GeneralInformation.class));
                break;
            case R.id.tvAccountPayment:
                startActivity(new Intent(getActivity(), CardList.class));
                break;
            case R.id.tvAccountMore:
                startActivity(new Intent(getActivity(), More.class));
                break;
            case R.id.ivEditProfilePhoto:
                Intent intImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intImage.setType("image/*");
                startActivityForResult(intImage, image);
                break;
            case R.id.btAccountActivateNow:
                activeAccount();
                break;
        }
    }

    private void doLogout() {
        final String tag = "doLogout";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.EVENT_LOGOUT + "&json=")
                    .append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                            .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(getActivity(), getResources().getString(R.string.login_out), builder.toString(), tag, this, this);
    }

    private void activeAccount() {
        final String tag = "Active Account";
        StringBuilder builder = new StringBuilder();
        JSONObject jsonData = new JSONObject();

        try {
            builder.append(Constants.BASE_URL + Constants.BASE_URL_POSTFIX + Constants.Events.ACTIVE_ACCOUNT + "&json=")
                    .append(jsonData.put("token", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""))
                            .put("userId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_ID, "")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonUtil.jsonRequestGET(getActivity(), getResources().getString(R.string.send_data), builder.toString(), tag, this, this);
    }

    private void doUpload(Intent data) {
        final String tag = "Do Upload";
        Uri uri = data.getData();
        InputStream inputStream;

        try {
            inputStream = getActivity().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
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

        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == image && resultCode == Activity.RESULT_OK) {
            doUpload(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                            startActivity(new Intent(getActivity(), SignIn.class));
                            getActivity().finish();
                            break;
                        case Constants.Events.UPDATE_PROFILE_PIC:

                            if (!TextUtils.isEmpty(Constants.BASE_URL_IMAGE_POSTFIX + response.optJSONObject("json").optJSONObject("user").optString("profilePic")))
                                Picasso.with(getActivity())
                                        .load(response.optJSONObject("json").optJSONObject("user").optString("profilePic"))
                                        .error(R.mipmap.imgtnulogo)
                                        .placeholder(R.mipmap.imgtnulogo)
                                        .into(cvAccountPhoto);

                            CommonUtil.successToastShowing(getActivity(), response);
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_PROIMG, Constants.BASE_URL_IMAGE_POSTFIX + response.optJSONObject("json").optJSONObject("user").optString("profilePic"));
                            SharedPreferenceUtil.save();
                            break;
                        case Constants.Events.ACTIVE_ACCOUNT:
                            CommonUtil.successToastShowing(getActivity(), response);
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
}
