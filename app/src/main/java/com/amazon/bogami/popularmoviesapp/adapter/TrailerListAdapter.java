package com.amazon.bogami.popularmoviesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.amazon.bogami.popularmoviesapp.R;
import com.amazon.bogami.popularmoviesapp.model.Trailer;

import java.util.ArrayList;

public class TrailerListAdapter extends ArrayAdapter<Trailer> {
    private Context context;
    private LayoutInflater inflater;

    private ArrayList<Trailer> trailerList;

    public TrailerListAdapter(Context context, ArrayList<Trailer> trailerList) {
        super(context, R.layout.trailer_item, trailerList);

        this.context = context;
        this.trailerList = trailerList;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.trailer_item, parent, false);
        }

        TextView trailerNameView = (TextView) convertView.findViewById(R.id.trailer_name);
        trailerNameView.setText(trailerList.get(position).getName());

        return convertView;
    }

    @Override
    public int getCount() {
        return trailerList.size();
    }

    @Override
    public Trailer getItem(int position) {
        return trailerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
