package com.smartsense.taxinearyou;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.smartsense.taxinearyou.Adapters.AdapterCardList;

import org.json.JSONArray;
import org.json.JSONException;

public class CardList extends AppCompatActivity {

    ListView lvCardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvCardList = (ListView) findViewById(R.id.lvCardList);
        String data = "[{\"Card_No\":\"4567\",\"Expiry_Date\":\"16/20\"},{\"Card_No\":\"4521\",\"Expiry_Date\":\"30/25\"},{\"Card_No\":\"7854\",\"Expiry_Date\":\"10/29\"},{\"Card_No\":\"9854\",\"Expiry_Date\":\"15/45\"}]";
        try {
            JSONArray jsonArray = new JSONArray(data);
            AdapterCardList adapterCardList = new AdapterCardList(this, jsonArray);
            lvCardList.setAdapter(adapterCardList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ratingforsearch, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
