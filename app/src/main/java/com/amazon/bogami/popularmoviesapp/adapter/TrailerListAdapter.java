package com.amazon.bogami.popularmoviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amazon.bogami.popularmoviesapp.R;
import com.amazon.bogami.popularmoviesapp.model.Trailer;

import java.util.ArrayList;

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Trailer> trailerList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public Button playButton;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.trailer_name);
            playButton = (Button) v.findViewById(R.id.playButton);
        }
    }

    public TrailerListAdapter(Context context, ArrayList<Trailer> trailerList) {
        this.context = context;
        this.trailerList = trailerList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item, parent, false);

        TrailerListAdapter.ViewHolder vh = new TrailerListAdapter.ViewHolder(convertView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Trailer trailer = trailerList.get(position);
        holder.name.setText(trailer.getName());
        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(
                        new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey())
                        )
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

}
