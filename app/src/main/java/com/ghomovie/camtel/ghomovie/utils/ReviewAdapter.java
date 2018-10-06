package com.ghomovie.camtel.ghomovie.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ghomovie.camtel.ghomovie.R;
import com.ghomovie.camtel.ghomovie.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private List<Review> reviews;
    private Context mContext;

    final private ReviewItemClickListener mOnClickListener;

    public ReviewAdapter(@NonNull Context context, List<Review> reviews, ReviewItemClickListener mOnClickListener) {
        this.reviews = reviews;
        this.mContext= context;
        this.mOnClickListener = mOnClickListener;
    }


    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.list_item_review;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        Log.d(TAG,"#"+position);
        holder.bind(this.reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public interface ReviewItemClickListener{
        void onReviewItemClick(int clickedItemIndex);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView author;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.review_author);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onReviewItemClick(position);
        }

        void bind(Review review){
            Log.i("review-author",review.getAuthor());
            author.setText("written by: "+review.getAuthor());
        }
    }
}
