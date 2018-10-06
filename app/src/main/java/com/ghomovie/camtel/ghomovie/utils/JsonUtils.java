package com.ghomovie.camtel.ghomovie.utils;

import android.util.Log;

import com.ghomovie.camtel.ghomovie.model.Movie;
import com.ghomovie.camtel.ghomovie.model.Review;
import com.ghomovie.camtel.ghomovie.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String JSON_KEY="results";


    public static ArrayList parseMovieJson(String json) {

        JSONObject movieJson = null;
        Movie movie = null;

        List<Movie> movies = new ArrayList<Movie>();




        try {
            movieJson = new JSONObject(json);
            Log.i("movieJson", "parseMovieJson: "+movieJson);
            JSONArray results = movieJson.optJSONArray(JSON_KEY);

            if(results!=null){
                for(int i=0; i<results.length();i++){
                    movies.add(new Movie(
                            results.getJSONObject(i).optLong("id"),
                            results.getJSONObject(i).optString("title"),
                            results.getJSONObject(i).optString("original_language"),
                            results.getJSONObject(i).optString("overview"),
                            results.getJSONObject(i).optString("release_date"),
                            results.getJSONObject(i).optDouble("vote_average"),
                            results.getJSONObject(i).optString("poster_path")

                    ));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return (ArrayList) movies;
    }

    public static ArrayList parseMovieVideosJson(String json) {

        JSONObject videoJson = null;
        Trailer movie = null;

        List<Trailer> movies = new ArrayList<Trailer>();




        try {
            videoJson = new JSONObject(json);
            Log.i("videosJson", "parseMovieVideoJson: "+videoJson);
            JSONArray results = videoJson.optJSONArray(JSON_KEY);

            if(results!=null){
                for(int i=0; i<results.length();i++){
                    if(results.getJSONObject(i).optString("type").equals("Trailer"))
                        movies.add(new Trailer(
                                results.getJSONObject(i).optLong("id"),
                                results.getJSONObject(i).optString("name"),
                                results.getJSONObject(i).optString("key"),
                                results.getJSONObject(i).optString("site")

                        ));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return (ArrayList) movies;
    }

    public static ArrayList parseReviewJson(String json) {

        JSONObject reviewJson = null;
        Review review = null;

        List<Review> reviews = new ArrayList<Review>();




        try {
            reviewJson = new JSONObject(json);
            Log.i("reviewsJson", "parseReviewJson: "+reviewJson);
            JSONArray results = reviewJson.optJSONArray(JSON_KEY);

            if(results!=null){
                for(int i=0; i<results.length();i++){

                    reviews.add(new Review(
                            results.getJSONObject(i).optLong("id"),
                            results.getJSONObject(i).optString("author"),
                            results.getJSONObject(i).optString("url")

                    ));
                    Log.i("reviewsJsonLength", "="+reviews.size());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return (ArrayList) reviews;
    }
}
