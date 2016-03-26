package com.smartsense.taxinearyou.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartsense.taxinearyou.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class AdapterReview extends BaseAdapter
{
    private JSONArray data;
    private LayoutInflater inflater=null;


    public AdapterReview(Activity a, JSONArray data)
    {
        this.data=data;
        inflater =(LayoutInflater)a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    public int getCount()
    {
        return data.length();
    }


    public Object getItem(int position)
    {
        return position;
    }



    public long getItemId(int position)
    {
        return position;
    }



    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi=convertView;
        if(convertView==null)

            vi = inflater.inflate(R.layout.element_review, null);

        TextView list = (TextView) vi.findViewById(R.id.tvElementReviewText);

        JSONObject test=data.optJSONObject(position);
        Log.i("Test", test.toString());

        list.setText(test.optString("list"));

        return vi;
    }

}