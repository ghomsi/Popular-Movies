package com.ghomovie.camtel.ghomovie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ghomovie.camtel.ghomovie.model.Movie;
import com.ghomovie.camtel.ghomovie.utils.JsonUtils;
import com.ghomovie.camtel.ghomovie.utils.MovieAdapter;
import com.ghomovie.camtel.ghomovie.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link main.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class main extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int GROUPID = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private MovieAdapter movieAdapter;
    private ArrayList<Movie> mMoviesList;

    private View rootView;
    private MenuItem mPopular;
    private MenuItem mRate;

    Movie[] movies = {
            new Movie("https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Grilled_ham_and_cheese_014.JPG/800px-Grilled_ham_and_cheese_014.JPG"),
            new Movie("https://upload.wikimedia.org/wikipedia/commons/c/ca/Bosna_mit_2_Bratw%C3%BCrsten.jpg"),
            new Movie("https://upload.wikimedia.org/wikipedia/commons/4/48/Chivito1.jpg")
    };

    public main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment main.
     */
    // TODO: Rename and change types and number of parameters
    public static main newInstance(String param1, String param2) {
        main fragment = new main();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //if(savedInstanceState==null || !savedInstanceState.containsKey("movies")){
            URL url = NetworkUtils.buildUrl("p");
            new MovieDBQueryTask().execute(url);

        //}else{
        //    mMoviesList = savedInstanceState.getParcelableArrayList("movies");
        //}
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mPopular = menu.add(GROUPID,0,1,R.string.popular);
        mPopular.setCheckable(true);

        //mPopular.setIcon();

        mRate = menu.add(GROUPID,1,2,R.string.top_rated);
        mRate.setCheckable(true);

        checkedController(true);
    }

    public void checkedController(Boolean a){
        if(a){
            mPopular.setChecked(true);
            mRate.setChecked(false);
        }else{
            mPopular.setChecked(false);
            mRate.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                Toast.makeText(getActivity(), R.string.popular, Toast.LENGTH_SHORT).show();
                URL url1 = NetworkUtils.buildUrl("p");
                new MovieDBQueryTask().execute(url1);
                checkedController(true);
                return  true;
            case 1:
                Toast.makeText(getActivity(), R.string.top_rated, Toast.LENGTH_SHORT).show();
                URL url2 = NetworkUtils.buildUrl("r");
                new MovieDBQueryTask().execute(url2);
                checkedController(false);
                return true;
            default:
                break;

        }
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("movies",mMoviesList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_main, container, false);


        return rootView;

    }

    private void launchDetailActivity(int position, ArrayList<Movie> mMoviesList) {
        Intent intent = new Intent(getContext(), detail.class);
        intent.putExtra("movie", mMoviesList.get(position));
        startActivity(intent);
        //Log.i("movie",mMoviesList.get(position).getTitle());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class MovieDBQueryTask extends AsyncTask<URL, Void,String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL moviedbURL = urls[0];
            String results = null;
            try {
                results = NetworkUtils.getResponseFromHttpurl(moviedbURL);

            } catch (IOException e) {
                //Toast.makeText(getActivity(), "connection problem...", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null && !s.equals("")){
                Log.i("moviedb response",s);
                mMoviesList= new ArrayList<Movie>(new JsonUtils().parseSandwichJson(s));
                movieAdapter = new MovieAdapter(getActivity(), mMoviesList);

                GridView gridView = (GridView) rootView.findViewById(R.id.movies_gridview);

                gridView.setAdapter(movieAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        launchDetailActivity(position,mMoviesList);
                    }
                });
            }
        }
    }
}
