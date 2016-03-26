package com.smartsense.taxinearyou;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;

import com.smartsense.taxinearyou.Adapters.AdapterLostItem;

import org.json.JSONArray;
import org.json.JSONException;

public class LostItem extends AppCompatActivity implements View.OnClickListener {

    ListView lvLostItemList;
    RadioButton rbLostItemOpen, rbLostItemInprogress, rbLostItemClosed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item);

        lvLostItemList = (ListView) findViewById(R.id.lvLostItemList);

        rbLostItemClosed = (RadioButton) findViewById(R.id.rbLostItemClosed);
        rbLostItemOpen = (RadioButton) findViewById(R.id.rbLostItemOpen);
        rbLostItemInprogress = (RadioButton) findViewById(R.id.rbLostItemInprogress);

        rbLostItemClosed.setOnClickListener(this);
        rbLostItemOpen.setOnClickListener(this);
        rbLostItemInprogress.setOnClickListener(this);

        String data = "[{\"from\":\"US\",\"to\":\"Canada\",\"provider\":\"Keval\",\"date_time\":\"10.02.2016 12-06am\",\"status\":\"open\",\"lost_item\":\"bag\",\"tnr\":\"tnu123\"},{\"from\":\"US\",\"to\":\"Canada\",\"provider\":\"Keval\",\"date_time\":\"10.02.2016 12-06am\",\"status\":\"open\",\"lost_item\":\"bag\",\"tnr\":\"tnu123\"},{\"from\":\"US\",\"to\":\"Canada\",\"provider\":\"Keval\",\"date_time\":\"10.02.2016 12-06am\",\"status\":\"open\",\"lost_item\":\"bag\",\"tnr\":\"tnu123\"},{\"from\":\"US\",\"to\":\"Canada\",\"provider\":\"Keval\",\"date_time\":\"10.02.2016 12-06am\",\"status\":\"open\",\"lost_item\":\"bag\",\"tnr\":\"tnu123\"}]";
        try {
            JSONArray jsonArray = new JSONArray(data);
            AdapterLostItem adapterLostItem = new AdapterLostItem(this, jsonArray);
            lvLostItemList.setAdapter(adapterLostItem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rbLostItemOpen:
                break;
            case R.id.rbLostItemInprogress:
                break;
            case R.id.rbLostItemClosed:
                break;
        }
    }
}
