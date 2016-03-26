package com.smartsense.taxinearyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.smartsense.taxinearyou.Adapters.AdapterSearchCar;

import org.json.JSONArray;
import org.json.JSONException;


public class SearchCars extends AppCompatActivity implements View.OnClickListener {

    ListView lvSearchCarsLine1;
    TextView tvSearchCarsFilter, tvSearchCarsDateTime;
    RadioButton rbSearchCarsRating, rbSearchCarsAvailability, rbSearchCarsPriceRange;

    ImageView ivSearchCarsFront, ivSearchCarsBack;
    LinearLayout llSearchCarsFilter, lySearchCarsDateTime;
    String a, b, c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_cars);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvSearchCarsLine1 = (ListView) findViewById(R.id.lvSearchCarsLine1);
        tvSearchCarsFilter = (TextView) findViewById(R.id.tvSearchCarsFilter);
        rbSearchCarsRating = (RadioButton) findViewById(R.id.rbSearchCarsRating);
        rbSearchCarsAvailability = (RadioButton) findViewById(R.id.rbSearchCarsAvailability);
        rbSearchCarsPriceRange = (RadioButton) findViewById(R.id.rbSearchCarsPriceRange);
        tvSearchCarsDateTime = (TextView) findViewById(R.id.tvSearchCarsDateTime);
        ivSearchCarsFront = (ImageView) findViewById(R.id.ivSearchCarsFront);
        ivSearchCarsBack = (ImageView) findViewById(R.id.ivSearchCarsBack);
        llSearchCarsFilter = (LinearLayout) findViewById(R.id.llSearchCarsFilter);
        lySearchCarsDateTime = (LinearLayout) findViewById(R.id.lySearchCarsDateTime);

        String data = "[{\"name\":\"Keval Cholera\",\"time\":\"(pls wait 40 min)\",\"address\":\"Hello Keval\",\"price\":40000,\"submit\":\"Book Now\"},{\"name\":\"Keval Cholera\",\"time\":\"(pls wait 40 min)\",\"address\":\"Hello Keval\",\"price\":40000,\"submit\":\"Book Now\"},{\"name\":\"Keval Cholera\",\"time\":\"(pls wait 40 min)\",\"address\":\"Hello Keval\",\"price\":40000,\"submit\":\"Book Now\"},{\"name\":\"Keval Cholera\",\"time\":\"(pls wait 40 min)\",\"address\":\"Hello Keval\",\"price\":40000,\"submit\":\"Book Now\"}]";
        try {
            JSONArray jsonArray = new JSONArray(data);
            AdapterSearchCar adapterSearchCar = new AdapterSearchCar(this, jsonArray);
            lvSearchCarsLine1.setAdapter(adapterSearchCar);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        llSearchCarsFilter.setOnClickListener(this);
        rbSearchCarsRating.setOnClickListener(this);
        rbSearchCarsAvailability.setOnClickListener(this);
        rbSearchCarsPriceRange.setOnClickListener(this);
        tvSearchCarsFilter.setOnClickListener(this);
        lySearchCarsDateTime.setOnClickListener(this);

        a = "Rating" + (char) 0x2191;
        b = "Price Range" + (char) 0x2191;
        c = "Availability" + (char) 0x2191;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ratingforsearch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSearchCarsFilter:
                startActivity(new Intent(SearchCars.this, Filter.class));
                break;
            case R.id.llSearchCarsFilter:
                startActivity(new Intent(SearchCars.this, Filter.class));
                break;
            case R.id.rbSearchCarsRating:
                rbSearchCarsPriceRange.setText("Price Range");
                rbSearchCarsAvailability.setText("Availability");
                if (rbSearchCarsRating.getText().toString().equalsIgnoreCase(a))
                    rbSearchCarsRating.setText("Rating" + (char) 0x2193);

                else if (!rbSearchCarsRating.getText().toString().equalsIgnoreCase(a))
                    rbSearchCarsRating.setText("Rating" + (char) 0x2191);

                break;
            case R.id.rbSearchCarsAvailability:
                rbSearchCarsPriceRange.setText("Price Range");
                rbSearchCarsRating.setText("Rating");
                if (rbSearchCarsAvailability.getText().toString().equalsIgnoreCase(c))
                    rbSearchCarsAvailability.setText("Availability" + (char) 0x2193);
                else if (!rbSearchCarsAvailability.getText().toString().equalsIgnoreCase(c))
                    rbSearchCarsAvailability.setText("Availability" + (char) 0x2191);
                break;
            case R.id.rbSearchCarsPriceRange:
                rbSearchCarsRating.setText("Rating");
                rbSearchCarsAvailability.setText("Availability");
                if (rbSearchCarsPriceRange.getText().toString().equalsIgnoreCase(b))
                    rbSearchCarsPriceRange.setText("Price Range" + (char) 0x2193);
                else if (!rbSearchCarsPriceRange.getText().toString().equalsIgnoreCase(b))
                    rbSearchCarsPriceRange.setText("Price Range" + (char) 0x2191);

                break;
            case R.id.lySearchCarsDateTime:
                startActivity(new Intent(SearchCars.this, Search.class));
                break;
        }
    }
}
