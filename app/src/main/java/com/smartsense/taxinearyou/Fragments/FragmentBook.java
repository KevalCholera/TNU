package com.smartsense.taxinearyou.Fragments;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.smartsense.taxinearyou.GoogleApi;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.SearchCars;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentBook extends Fragment implements View.OnClickListener {


    TextView tvBookDateTime, tvBookLuggage, tvBookPassenger, tvBookFrom, tvBookTo, tvBookvia1, tvBookvia2, tvBookSearchCars;
    RadioButton rbBookNow, rbBookToday, rbBookTomorrow;
    ImageView imgBookFromToReverse, ivBookVia, ivBookDeleteVia1, ivBookDeleteVia2;
    LinearLayout llBookVia1, llBookVia2;
    SimpleDateFormat timeFormatNow, dateFormat, timeFormatTomorrow;
    Date myDateTimeNow, myDateTomorrow, Now, tomorrow;
    String finalDateTimeNow, timepickerTime, finalTimeTomorrow, finalDateTomorrow, finalDateNow;
    int i = 0;
    Calendar calendar;
    CoordinatorLayout clSearch;
    TimePickerDialog mTimePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_book, container, false);

        tvBookFrom = (TextView) rootView.findViewById(R.id.tvBookFrom);
        tvBookTo = (TextView) rootView.findViewById(R.id.tvBookTo);
        tvBookvia1 = (TextView) rootView.findViewById(R.id.tvBookvia1);
        tvBookvia2 = (TextView) rootView.findViewById(R.id.tvBookvia2);
        tvBookDateTime = (TextView) rootView.findViewById(R.id.tvBookDateTime);
        tvBookLuggage = (TextView) rootView.findViewById(R.id.tvBookLuggage);
        tvBookPassenger = (TextView) rootView.findViewById(R.id.tvBookPassenger);
        tvBookSearchCars = (TextView) rootView.findViewById(R.id.tvBookSearchCars);
        rbBookNow = (RadioButton) rootView.findViewById(R.id.rbBookNow);
        rbBookToday = (RadioButton) rootView.findViewById(R.id.rbBookToday);
        rbBookTomorrow = (RadioButton) rootView.findViewById(R.id.rbBookTomorrow);
        imgBookFromToReverse = (ImageView) rootView.findViewById(R.id.imgBookFromToReverse);
        ivBookVia = (ImageView) rootView.findViewById(R.id.ivBookVia);
        llBookVia1 = (LinearLayout) rootView.findViewById(R.id.llBookVia1);
        llBookVia2 = (LinearLayout) rootView.findViewById(R.id.llBookVia2);
        ivBookDeleteVia1 = (ImageView) rootView.findViewById(R.id.ivBookDeleteVia1);
        ivBookDeleteVia2 = (ImageView) rootView.findViewById(R.id.ivBookDeleteVia2);

        calendar = Calendar.getInstance();
        TodayDateTime();
        clSearch = (CoordinatorLayout) getActivity().findViewById(R.id.clSearch);

        tvBookFrom.setOnClickListener(this);
        tvBookTo.setOnClickListener(this);
        tvBookvia1.setOnClickListener(this);
        tvBookvia2.setOnClickListener(this);
        tvBookLuggage.setOnClickListener(this);
        tvBookPassenger.setOnClickListener(this);
        rbBookNow.setOnClickListener(this);
        rbBookToday.setOnClickListener(this);
        rbBookTomorrow.setOnClickListener(this);
        tvBookSearchCars.setOnClickListener(this);
        tvBookDateTime.setOnClickListener(this);
        ivBookVia.setOnClickListener(this);
        imgBookFromToReverse.setOnClickListener(this);
        llBookVia1.setOnClickListener(this);
        llBookVia2.setOnClickListener(this);
        ivBookDeleteVia1.setOnClickListener(this);
        ivBookDeleteVia2.setOnClickListener(this);

        return rootView;
    }

    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        String hour = "";
        if (hours < 10)
            hour = "0" + hours;
        else
            hour = String.valueOf(hours);

        // Append in a StringBuilder
        timepickerTime = new StringBuilder().append(hour).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
    }

    public void tomorrow() {

        timeFormatTomorrow = new SimpleDateFormat("hh : mm aa");//hh == 12 hours && HH == 24 hours  aa == am/pm
        myDateTomorrow = new Date();
        finalTimeTomorrow = timeFormatTomorrow.format(myDateTomorrow);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        tomorrow = calendar.getTime();
        finalDateTomorrow = dateFormat.format(tomorrow);
        tvBookDateTime.setText(finalDateTomorrow + " -- " + finalTimeTomorrow);
    }

    public void TodayDateTime() {
        timeFormatNow = new SimpleDateFormat("dd-MMM-yyyy -- hh : mm aa");//hh == 12 hours && HH == 24 hours  aa == am/pm
        myDateTimeNow = new Date();
        finalDateTimeNow = timeFormatNow.format(myDateTimeNow);
        tvBookDateTime.setText(finalDateTimeNow);
    }

    public void setDateTime() {
        if (i == 2 || i == 3) {
            SimpleDateFormat timeStampFormat2 = new SimpleDateFormat("dd-MMM-yyyy");//hh == 12 hours && HH == 24 hours  aa == am/pm
            Now = new Date();
            finalDateNow = timeStampFormat2.format(Now);

            final int hour = calendar.get(Calendar.HOUR_OF_DAY);
            final int minute = calendar.get(Calendar.MINUTE);

            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    updateTime(selectedHour, selectedMinute);
                    if (i == 2)
                        tvBookDateTime.setText(finalDateNow + " -- " + timepickerTime);
                    else if (i == 3)
                        tvBookDateTime.setText(finalDateTomorrow + " -- " + timepickerTime);
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rbBookNow:
                TodayDateTime();
                i = 1;
                break;

            case R.id.rbBookToday:
                TodayDateTime();
                i = 2;
                break;

            case R.id.rbBookTomorrow:
                tomorrow();
                i = 3;
                break;

            case R.id.tvBookDateTime:
                setDateTime();
                break;

            case R.id.tvBookSearchCars:
//                if (TextUtils.isEmpty(tvBookFrom.getText().toString()))
//                    CommonUtil.showSnackBar(getActivity(), getString(R.string.enter_from), clSearch);
//                else if (TextUtils.isEmpty(tvBookTo.getText().toString()))
//                    CommonUtil.showSnackBar(getActivity(), getString(R.string.enter_to), clSearch);
//                else
                startActivity(new Intent(getActivity(), SearchCars.class));
                break;

            case R.id.ivBookVia:
                if (llBookVia1.getVisibility() == View.VISIBLE) {
                    llBookVia2.setVisibility(View.VISIBLE);
                }
                llBookVia1.setVisibility(View.VISIBLE);
                break;

            case R.id.ivBookDeleteVia1:
                llBookVia1.setVisibility(View.GONE);
                break;

            case R.id.ivBookDeleteVia2:
                llBookVia2.setVisibility(View.GONE);
                break;

            case R.id.tvBookFrom:
                startActivityForResult(new Intent(getActivity(), GoogleApi.class), 1);
                break;

            case R.id.tvBookTo:
                startActivityForResult(new Intent(getActivity(), GoogleApi.class), 2);
                break;

            case R.id.tvBookvia1:
                startActivityForResult(new Intent(getActivity(), GoogleApi.class), 3);
                break;

            case R.id.tvBookvia2:
                startActivityForResult(new Intent(getActivity(), GoogleApi.class), 4);
                break;
        }
    }
}


