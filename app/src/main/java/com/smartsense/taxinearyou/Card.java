package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.smartsense.taxinearyou.Adapters.AdapterCard;

import org.json.JSONArray;
import org.json.JSONException;

public class Card extends AppCompatActivity implements View.OnClickListener {

    ListView lvCard;
    Button btCardDone, btCardAddCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvCard = (ListView) findViewById(R.id.lvCard);
        btCardDone = (Button) findViewById(R.id.btCardDone);
        btCardAddCard = (Button) findViewById(R.id.btCardAddCard);


        String data = "[{\"Card\":\"4564\"},{\"Card\":\"1256\"},{\"Card\":\"7596\"},{\"Card\":\"4521\"}]";
        try {
            JSONArray jsonArray = new JSONArray(data);
            AdapterCard adapterCard = new AdapterCard(this, jsonArray);
            lvCard.setAdapter(adapterCard);
            Log.i("card", jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btCardDone.setOnClickListener(this);
        btCardAddCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btCardDone:
                startActivity(new Intent(Card.this, PaymentDetails.class));
                break;
            case R.id.btCardAddCard:
                startActivity(new Intent(Card.this, AddCard.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.ratingforsearch, menu);
        return true;
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
