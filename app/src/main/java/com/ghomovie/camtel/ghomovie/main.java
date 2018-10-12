package com.ghomovie.camtel.ghomovie;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ghomovie.camtel.ghomovie.data.AppDatabase;
import com.ghomovie.camtel.ghomovie.data.MovieContract;
import com.ghomovie.camtel.ghomovie.data.MoviesDBHelper;
import com.ghomovie.camtel.ghomovie.model.Movie;
import com.ghomovie.camtel.ghomovie.utils.JsonUtils;
import com.ghomovie.camtel.ghomovie.utils.MovieAdapter;
import com.ghomovie.camtel.ghomovie.utils.MovieRecyclerViewAdapter;
import com.ghomovie.camtel.ghomovie.utils.NetworkUtils;
import com.ghomovie.camtel.ghomovie.viewModel.MovieListViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link main.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class main extends Fragment
        implements MovieRecyclerViewAdapter.MovieItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static final int POPULAR_SORT = 0;
    public static final int TOP_RATED_SORT = 1;
    public static final int FAVORITE_SORT = 2;

    private static final int CURSOR_LOADER_ID =0;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int NUMBERS_OF_COLUMNS=2;

    private static final int GROUPID = 1;
    private static int CHECKEDVALUE;

    private static final String LIFECYCLE_CALLBACKS_TEXT_KEY = "movies";
    private static final String LIFECYCLE_CALLBACKS_CHECKEDbtn_KEY = "checked";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    MovieRecyclerViewAdapter.MovieItemClickListener mOnClickListener;

    private MovieAdapter movieAdapter;
    private MovieRecyclerViewAdapter mMovieAdapter;
    private ArrayList<Movie> mMoviesList;


    private View rootView;
    private MenuItem mPopular;
    private MenuItem mRate;
    private MenuItem mFavorite;
    private RecyclerView mMoviesRV;
    private MoviesDBHelper myDB;
    private MovieListViewModel viewMovieModel;





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
        viewMovieModel = ViewModelProviders.of(this).get(MovieListViewModel.class);


        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_TEXT_KEY)){
                mMoviesList = savedInstanceState.getParcelableArrayList(LIFECYCLE_CALLBACKS_TEXT_KEY);
            }
            if(savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_CHECKEDbtn_KEY)){
                CHECKEDVALUE = savedInstanceState.getInt(LIFECYCLE_CALLBACKS_CHECKEDbtn_KEY);
            }
        }else{
            CHECKEDVALUE = 1;
            URL url = NetworkUtils.buildUrl("p");
            new MovieDBQueryTask().execute(url);
        }

    }

    @Override
    public void onDestroy() {
        AppDatabase.destroyIntance();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mPopular = menu.add(GROUPID,0,1,R.string.popular);
        mPopular.setCheckable(true);

        mRate = menu.add(GROUPID,1,2,R.string.top_rated);
        mRate.setCheckable(true);

        mFavorite = menu.add(GROUPID,2,3,R.string.favorite);
        mFavorite.setCheckable(true);

        checkedController(CHECKEDVALUE);
    }

    public void checkedController(Integer a){
        CHECKEDVALUE = a;
        if(a==1){
            mPopular.setChecked(true);
            mRate.setChecked(false);
            mFavorite.setChecked(false);
        }else if(a==2){
            mPopular.setChecked(false);
            mRate.setChecked(true);
            mFavorite.setChecked(false);
        }else{
            mPopular.setChecked(false);
            mRate.setChecked(false);
            mFavorite.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case POPULAR_SORT:
                Toast.makeText(getActivity(), R.string.popular, Toast.LENGTH_SHORT).show();
                URL url1 = NetworkUtils.buildUrl("p");
                new MovieDBQueryTask().execute(url1);
                checkedController(1);
                return  true;
            case TOP_RATED_SORT:
                Toast.makeText(getActivity(), R.string.top_rated, Toast.LENGTH_SHORT).show();
                URL url2 = NetworkUtils.buildUrl("r");
                new MovieDBQueryTask().execute(url2);
                checkedController(2);
                return true;
            case FAVORITE_SORT:
                Toast.makeText(getActivity(), R.string.favorite, Toast.LENGTH_SHORT).show();
                viewAll();
                return true;
            default:
                break;

        }
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(LIFECYCLE_CALLBACKS_TEXT_KEY,mMoviesList);
        outState.putInt(LIFECYCLE_CALLBACKS_CHECKEDbtn_KEY,CHECKEDVALUE);
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mMoviesRV = (RecyclerView) rootView.findViewById(R.id.rv_movies);
        mMoviesRV.setLayoutManager(new GridLayoutManager(getActivity(),NUMBERS_OF_COLUMNS));
        mMoviesRV.setHasFixedSize(true);

        mOnClickListener=this;

        mMovieAdapter = new MovieRecyclerViewAdapter(getActivity(),mMoviesList,  mOnClickListener);
        mMoviesRV.setAdapter(mMovieAdapter);




        return rootView;

    }

    private void launchDetailActivity(int position) {
        //Log.i("movie",mMoviesList.get(position).getTitle());

        Intent intent = new Intent(getContext(), detail.class);
        intent.putExtra("movie", mMoviesList.get(position));
        startActivity(intent);

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

    @Override
    public void onMovieItemClick(int clickedItemIndex) {
        launchDetailActivity(clickedItemIndex);
    }


    public void viewAll(){
        viewMovieModel.getItemAndMovieList().observe(getActivity(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mMoviesList = (ArrayList<Movie>) movies;
                if(mMoviesList.size()==0){
                    Toast.makeText(getActivity(), R.string.Efavorite, Toast.LENGTH_SHORT).show();
                    return;
                }
                checkedController(3);
                mMovieAdapter.addItems(movies);
            }
        });

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

    public class MovieDBQueryTask extends AsyncTask<URL, Void,String> implements com.ghomovie.camtel.ghomovie.MovieDBQueryTask {

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

                mMoviesList= new ArrayList<Movie>(new JsonUtils().parseMovieJson(s));
                mMovieAdapter.addItems(mMoviesList);





            }
        }
    }
}
