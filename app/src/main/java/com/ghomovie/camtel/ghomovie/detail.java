package com.ghomovie.camtel.ghomovie;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ghomovie.camtel.ghomovie.data.MoviesDBHelper;
import com.ghomovie.camtel.ghomovie.model.Movie;
import com.ghomovie.camtel.ghomovie.model.Review;
import com.ghomovie.camtel.ghomovie.model.Trailer;
import com.ghomovie.camtel.ghomovie.utils.JsonUtils;
import com.ghomovie.camtel.ghomovie.utils.NetworkUtils;
import com.ghomovie.camtel.ghomovie.utils.ReviewAdapter;
import com.ghomovie.camtel.ghomovie.utils.TrailerAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.PendingIntent.getActivity;

public class detail extends AppCompatActivity implements ReviewAdapter.ReviewItemClickListener,TrailerAdapter.TrailerItemClickListener {
    @Nullable
    @BindView(R.id.image_view) ImageView image;
    @BindView(R.id.detail_movie_title) TextView title;
    @BindView(R.id.detail_movie_description) TextView description;
    @BindView(R.id.detail_ratingbar) RatingBar rate;
    @BindView(R.id.detail_movie_date) TextView date;
    @BindView(R.id.detail_movie_lang) TextView lang;
    @BindView(R.id.mark_button) Button btnfavorite;

    private static final int NUMBERS_OF_COLUMNS=2;

    private ArrayList<Trailer> mTrailersList;
    private ArrayList<Review> mReviewsList;

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    private RecyclerView mReviewRV;
    private RecyclerView mTrailerRV;
    private MoviesDBHelper myDB;
    private Movie movie;

    ReviewAdapter.ReviewItemClickListener mOnClickListener;
    TrailerAdapter.TrailerItemClickListener mOnClickListenerT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        myDB = new MoviesDBHelper(this);

        mOnClickListener=this;
        mOnClickListenerT=this;

        movie = intent.getExtras().getParcelable("movie");
        Log.i("id",movie.getId().toString());
        buildUI(getApplicationContext(),movie);
        AddData();
    }

    private  void buildUI(Context context, Movie movie){
        Picasso.with(context)
                .load(movie.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round)
                .noFade()
                .into(image);
        title.setText(movie.getTitle());
        description.setText(movie.getOverview());
        rate.setRating(new Float(movie.getVote_average())-4);
        date.setText(movie.getRelease_date());
        lang.setText("lang:"+movie.getLg());

        NetworkUtils.MOVIEID=movie.getId(); // first get the Id of the movie;
        URL url1 = NetworkUtils.buildUrl("videos");
        new MovieDBQueryTask(false).execute(url1);
        URL url2 = NetworkUtils.buildUrl("reviews");
        new MovieDBQueryTask(true).execute(url2);

    }

    public void AddData(){
        btnfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDB.insertData(movie);
                if(isInserted)
                    Toast.makeText(getApplicationContext(),"INSERTED to your favorites",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(),"not INSERTED to your favorites",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void openMedia(Uri file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(file);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onReviewItemClick(int clickedItemIndex) {
        Log.i("list:","review clicked");
        Review review = mReviewsList.get(clickedItemIndex);
        Toast.makeText(getApplicationContext(), "opening review by "+review.getAuthor(), Toast.LENGTH_SHORT).show();
        Uri webpage = Uri.parse(review.getUrl());
        openMedia(webpage);
    }

    @Override
    public void onTrailerItemClick(int clickedItemIndex) {
        Log.i("grid:","trailer clicked");
        Trailer trailer = mTrailersList.get(clickedItemIndex);
        Toast.makeText(getApplicationContext(), "videos trailer", Toast.LENGTH_SHORT).show();

        Uri webpage = Uri.parse(trailer.getVideo());
        openMedia(webpage);
    }

    public void insertData(Movie movie){
        //ContentValues[] movieValueArr = new ContentValues[]
    }


    public class MovieDBQueryTask extends AsyncTask<URL, Void,String> implements com.ghomovie.camtel.ghomovie.MovieDBQueryTask {
        private Boolean review;

        public MovieDBQueryTask(Boolean review){
            this.review=review;
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL moviedbURL = urls[0];
            String results = null;
            try {
                results = NetworkUtils.getResponseFromHttpurl(moviedbURL);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null && !s.equals("")){
                if(this.review){
                    mReviewsList= new ArrayList<Review>(new JsonUtils().parseReviewJson(s));
                    Log.i("moviedb trailers rep",mTrailersList.get(0).getKey());


                    mReviewRV = findViewById(R.id.review_listview);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    mReviewRV.setLayoutManager(layoutManager);
                    mReviewRV.setHasFixedSize(true);

                    reviewAdapter = new ReviewAdapter(getApplicationContext(),mReviewsList, mOnClickListener);

                    mReviewRV.setAdapter(reviewAdapter);


                }else{
                    mTrailersList= new ArrayList<Trailer>(new JsonUtils().parseMovieVideosJson(s));
                    Log.i("moviedb trailers rep",mTrailersList.get(0).getKey());

                    mTrailerRV = findViewById(R.id.trailer_gridview);
                    mTrailerRV.setLayoutManager(new GridLayoutManager(getApplicationContext(),NUMBERS_OF_COLUMNS));

                    trailerAdapter = new TrailerAdapter(getApplicationContext(), mTrailersList, mOnClickListenerT);

                    mTrailerRV.setAdapter(trailerAdapter);
                }

            }
        }
    }
}
