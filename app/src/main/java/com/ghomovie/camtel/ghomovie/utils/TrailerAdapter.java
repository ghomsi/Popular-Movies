package com.ghomovie.camtel.ghomovie.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ghomovie.camtel.ghomovie.R;
import com.ghomovie.camtel.ghomovie.model.Trailer;

import java.util.List;

public class TrailerAdapter  extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();

    private List<Trailer> trailers;
    private Context mContext;

    final private TrailerItemClickListener mOnClickListener;

    public TrailerAdapter(Context context, List<Trailer> movies, TrailerItemClickListener mOnClickListener){

        this.trailers = movies;
        this.mContext = context;
        this.mOnClickListener = mOnClickListener;
    }



    @NonNull
    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.list_item_trailer;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerViewHolder holder, int position) {
        Log.d(TAG,"#"+position);
        holder.bind(this.trailers.get(position));
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public interface TrailerItemClickListener{
        void onTrailerItemClick(int clickedItemIndex);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView partText;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            partText = itemView.findViewById(R.id.trailer_part);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onTrailerItemClick(position);
        }

        void bind(Trailer trailer){
            Log.i("trailer-name",trailer.getName());
            partText.setText(trailer.getName());
        }
    }
}
