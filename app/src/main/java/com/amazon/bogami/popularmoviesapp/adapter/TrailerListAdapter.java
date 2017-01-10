package com.amazon.bogami.popularmoviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.trailer_item, parent, false);
        }

        TextView trailerNameView = (TextView) convertView.findViewById(R.id.trailer_name);
        trailerNameView.setText(trailerList.get(position).getName());

        Button button = (Button) convertView.findViewById(R.id.playButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(
                    new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + trailerList.get(position).getKey())
                    )
                );
            }
        });

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
