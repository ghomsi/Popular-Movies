package com.ghomovie.camtel.ghomovie.utils;

import android.util.Log;

import com.ghomovie.camtel.ghomovie.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String JSON_KEY="results";


    public static ArrayList parseSandwichJson(String json) {

        JSONObject movieJson = null;
        Movie movie = null;

        List<Movie> movies = new ArrayList<Movie>();




        try {
            movieJson = new JSONObject(json);
            Log.i("movieJson", "parseSandwichJson: "+movieJson);
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
}
