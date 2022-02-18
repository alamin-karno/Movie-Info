package com.alaminkarno.movieinfo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alaminkarno.movieinfo.R;
import com.alaminkarno.movieinfo.models.MovieModel;
import com.alaminkarno.movieinfo.view.utils.Credential;
import com.bumptech.glide.Glide;

import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> {

    private List<MovieModel> movies;
    private OnMovieListener onMovieListener;

    public MovieRecyclerViewAdapter(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_design_movie_item_list,parent,false);
        return new MovieViewHolder(view,onMovieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        MovieModel movie = movies.get(position);

        holder.title.setText(movie.getTitle());
        holder.release_date.setText(movie.getRelease_date());
        holder.vote_count.setText(movie.getVote_count()+" Reviews");
        holder.ratingBar.setRating(movie.getVote_average()/2);

        Glide.with(holder.imageView.getContext())
                .load(Credential.IMAGE_URL+movie.getPoster_path())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        if(movies != null){
            return movies.size();
        }

        return 0;

    }

    public void setMovies(List<MovieModel> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public MovieModel getSelectedMovie(int position){

        if(movies != null){
            if(movies.size()>0){
                return movies.get(position);
            }
        }
        return null;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title,release_date,vote_count;
        ImageView imageView;
        RatingBar ratingBar;

        OnMovieListener onMovieListener;

        public MovieViewHolder(@NonNull View itemView,OnMovieListener onMovieListener) {
            super(itemView);

            this.onMovieListener = onMovieListener;

            title = itemView.findViewById(R.id.movieTitle);
            release_date = itemView.findViewById(R.id.movieReleaseDate);
            vote_count = itemView.findViewById(R.id.movieVoteCount);
            imageView = itemView.findViewById(R.id.movieImage);
            ratingBar = itemView.findViewById(R.id.movieRatingBar);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            onMovieListener.onMovieClick(getAdapterPosition());
        }
    }
}
