package com.ghomovie.camtel.ghomovie.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ghomovie.camtel.ghomovie.R;
import com.ghomovie.camtel.ghomovie.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> {

    private static final String TAG = MovieRecyclerViewAdapter.class.getSimpleName();
    private List<Movie> movies;
    private Context mContext;

    final private MovieItemClickListener mOnClickListener;


    public MovieRecyclerViewAdapter(Context context, List<Movie> movies, MovieItemClickListener mOnClickListener){
        this.mContext = context;
        if(movies!=null)
            this.movies = movies;
        else
            this.movies = new ArrayList<Movie>();

        this.mOnClickListener = mOnClickListener;

    }
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layoutIdForListItem = R.layout.list_item_movies;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Log.d(TAG,"#"+position);
        holder.bind(this.movies.get(position));

    }

    public void addItems(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface MovieItemClickListener{
        void onMovieItemClick(int clickedItemIndex);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        ImageView iconView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            iconView = (ImageView) itemView.findViewById(R.id.item_movie_icon);
            itemView.setOnClickListener(this);
        }
        void bind(Movie movie){
            Log.i("image movie:",movie.getImage());
            Picasso.with(mContext)
                    .load("http://image.tmdb.org/t/p/w185//"+movie.getImage())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher_round)
                    .noFade()
                    .into(iconView);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onMovieItemClick(position);
        }
    }


}
