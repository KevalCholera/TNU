package com.smartsense.taxinearyou.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
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
import com.smartsense.taxinearyou.utill.JsonErrorShow;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentMenu extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {

    CircleImageView1 cvAccountPhoto;
    TextView tvAccountPersonName;
    TextView tvAccountGeneralInfo, tvAccountAccountSecurity, tvAccountPayment,
            tvAccountCredits, tvAccountLostItems, tvAccountLogout, tvAccountMore;

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
            cvAccountPhoto.setBorderColor(ContextCompat.getColor(getActivity(), R.color.Yellow));
            ivEditProfilePhoto.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circular_view_yellow_colored));
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

        CommonUtil.showProgressDialog(getActivity(), "Logging Out ...");
        DataRequest dataRequest = new DataRequest(Request.Method.GET, builder.toString(), null, this, this);
        TaxiNearYouApp.getInstance().addToRequestQueue(dataRequest, tag);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(getActivity(), "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
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
                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, getActivity(), clSearch);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
