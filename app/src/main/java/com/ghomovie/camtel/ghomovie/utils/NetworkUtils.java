package com.ghomovie.camtel.ghomovie.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static String MOVIE_BASE_URL="https://api.themoviedb.org/3/movie/550";
    final static String POPULAR_MOVIE_BASE_URL="https://api.themoviedb.org/3/movie/popular";
    final static String RATE_MOVIE_BASE_URL="https://api.themoviedb.org/3/movie/top_rated";
    final static String PARAM_KEY ="api_key";
    final static String KEY = "";


    public static URL buildUrl(String sort){

        String link = MOVIE_BASE_URL;
        if(sort.equals("p")) link = POPULAR_MOVIE_BASE_URL;
        if(sort.equals("r")) link = RATE_MOVIE_BASE_URL;

        Uri builtUri = Uri.parse(link)
                .buildUpon()
                .appendQueryParameter(PARAM_KEY,KEY)
                .build();
        URL url = null;
        try{
            url= new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpurl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return  scanner.next();
            }else{
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }

}
