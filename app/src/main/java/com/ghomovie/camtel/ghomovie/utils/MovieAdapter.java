package com.ghomovie.camtel.ghomovie.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import com.ghomovie.camtel.ghomovie.R;
import com.ghomovie.camtel.ghomovie.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Activity context, List<Movie> movies){

        super(context,0,movies);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Movie movie = getItem(position);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movies, parent,false);

        ImageView iconView = (ImageView) rootView.findViewById(R.id.item_movie_icon);
        Picasso.with(getContext())
                .load(movie.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round)
                .noFade()
                .into(iconView);

        return rootView;
    }
}
