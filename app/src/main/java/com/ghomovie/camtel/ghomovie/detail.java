package com.ghomovie.camtel.ghomovie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ghomovie.camtel.ghomovie.model.Movie;
import com.squareup.picasso.Picasso;

public class detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Movie movie = intent.getExtras().getParcelable("movie");
        Log.i("overview",movie.getOverview());
        buildUI(getApplicationContext(),movie);
    }

    private  void buildUI(Context context, Movie movie){
        ImageView image = findViewById(R.id.image_view);
        Picasso.with(context)
                .load(movie.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round)
                .noFade()
                .into(image);
        TextView title = findViewById(R.id.detail_movie_title);
        title.setText(movie.getTitle());

        TextView description = findViewById(R.id.detail_movie_description);
        description.setText(movie.getOverview());

        RatingBar rate = findViewById(R.id.detail_ratingbar);
        rate.setRating(new Float(movie.getVote_average())-4);

        TextView date = findViewById(R.id.detail_movie_date);
        date.setText(movie.getRelease_date());

        TextView lang = findViewById(R.id.detail_movie_lang);
        lang.setText("lang:"+movie.getLg());

    }
}
