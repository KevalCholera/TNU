package com.smartsense.taxinearyou.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartsense.taxinearyou.AccountSecurity;
import com.smartsense.taxinearyou.CardList;
import com.smartsense.taxinearyou.GeneralInformation;
import com.smartsense.taxinearyou.LostItem;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.SignIn;
import com.smartsense.taxinearyou.utill.CircleImageView1;

public class FragmentMenu extends android.support.v4.app.Fragment implements View.OnClickListener {

    CircleImageView1 cvAccountPhoto;
    TextView tvAccountPersonName;
    TextView tvAccountGeneralInfo, tvAccountAccountSecurity, tvAccountPayment,
            tvAccountCredits, tvAccountLostItems, tvAccountLogout;

    ImageView ivEditProfilePhoto;
    Button btAccountActivateNow;

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
        tvAccountLogout = (TextView) rootView.findViewById(R.id.tvAccountLogout);
        btAccountActivateNow = (Button) rootView.findViewById(R.id.btAccountActivateNow);
        ivEditProfilePhoto = (ImageView) rootView.findViewById(R.id.ivEditProfilePhoto);

        if (btAccountActivateNow.getVisibility() == View.VISIBLE) {
            cvAccountPhoto.setBorderColor(getResources().getColor(R.color.Yellow));
            ivEditProfilePhoto.setBackground(getResources().getDrawable(R.drawable.circular_view_yellow_colored));
        }


        tvAccountLogout.setOnClickListener(this);
        tvAccountAccountSecurity.setOnClickListener(this);
        tvAccountLostItems.setOnClickListener(this);
        tvAccountGeneralInfo.setOnClickListener(this);
        tvAccountPayment.setOnClickListener(this);
        return rootView;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAccountLogout:
                startActivity(new Intent(getActivity(), SignIn.class));
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
        }
    }
}
