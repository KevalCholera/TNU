package com.smartsense.taxinearyou.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartsense.taxinearyou.R;


public class FragmentCredit extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_credit, container, false);

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
//        LinearLayout llToolbarAll = (LinearLayout) getActivity().findViewById(R.id.llToolbarAll);
//        llToolbarAll.setVisibility(View.GONE);
    }
}
