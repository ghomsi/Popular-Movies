package com.ghomovie.camtel.ghomovie;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.ghomovie.camtel.ghomovie.model.Movie;
import com.ghomovie.camtel.ghomovie.utils.MovieAdapter;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements main.OnFragmentInteractionListener {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //todo(1) gridlayout customization
        //todo(2) fragement implemantation
        //View rootView = inflater.inflate(R.layout.fragment_main,container, false);



        /*String[] movies = getResources().getStringArray(R.array.movies_names);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,movies);

        GridView gridview = findViewById(R.id.movie_gridview);

        gridview.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                launchDetailActivity(position);
            }
        });*/
    }




    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
