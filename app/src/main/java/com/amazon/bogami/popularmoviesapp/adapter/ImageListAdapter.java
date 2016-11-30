package com.amazon.bogami.popularmoviesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.amazon.bogami.popularmoviesapp.R;
import com.squareup.picasso.Picasso;

public class ImageListAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;

    private String[] imageUrls;

    public ImageListAdapter(Context context, String[] imageUrls) {
        super(context, R.layout.grid_cell, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.grid_cell, parent, false);
        }

        Picasso
                .with(context)
                .load(imageUrls[position])
                .fit()
                .into((ImageView) convertView);

        return convertView;
    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public String getItem(int position) {
        return imageUrls[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
