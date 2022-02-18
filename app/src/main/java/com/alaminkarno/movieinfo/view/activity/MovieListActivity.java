package com.alaminkarno.movieinfo.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alaminkarno.movieinfo.R;
import com.alaminkarno.movieinfo.adapters.MovieRecyclerViewAdapter;
import com.alaminkarno.movieinfo.adapters.OnMovieListener;
import com.alaminkarno.movieinfo.models.MovieModel;
import com.alaminkarno.movieinfo.view.utils.Credential;
import com.alaminkarno.movieinfo.viewModels.MovieListViewModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

    private RecyclerView movieRecyclerView;
    private Toolbar toolbar;
    private ViewFlipper viewFlipper;
    private SearchView searchView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private MovieListViewModel movieListViewModel;
    private boolean isPopular = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        initialize();

        configureRecyclerView();

        getSearchResult();

        observeAnyChange();

        observePopularMovieChange();

        movieListViewModel.searchPopularMovieApi(1);
    }



    private void getSearchResult(){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //movieListViewModel.searchMovieApi(query,1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                movieListViewModel.searchMovieApi(newText,1);
                return false;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopular = false;
            }
        });
    }

    private void configureRecyclerView(){
        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this);
        movieRecyclerView.setAdapter(movieRecyclerViewAdapter);
    }

    private void observeAnyChange(){

        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if(movieModels != null){
                    for(MovieModel movieModel: movieModels){

                        Log.d("tag","onChange: "+movieModel.getPoster_path());

                        movieRecyclerViewAdapter.setMovies(movieModels);

                        setSliderImageView(movieModel.getBackdrop_path(),movieModel.getTitle());
                    }
                }
            }
        });
    }

    private void observePopularMovieChange() {

        movieListViewModel.getPopularMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if(movieModels != null){
                    for(MovieModel movieModel: movieModels){

                        Log.d("tag","onChange: "+movieModel.getTitle());

                        movieRecyclerViewAdapter.setMovies(movieModels);

                        //setSliderImageView(movieModel.getBackdrop_path());
                    }

                }
            }
        });
    }

    private void setSliderImageView(String backdrop_path,String title) {



        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        /*TextView textView = new TextView(getApplicationContext());
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextSize(16);*/

        Glide.with(this)
                .load(Credential.IMAGE_URL+backdrop_path)
                .into(imageView);
        //textView.setText(title);

        //viewFlipper.addView(textView);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        viewFlipper.startFlipping();

        viewFlipper.setInAnimation(getApplicationContext(),android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getApplicationContext(),android.R.anim.slide_out_right);
    }

    private void initialize() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = findViewById(R.id.searchView);
        viewFlipper = findViewById(R.id.viewFlipper);

        movieRecyclerView = findViewById(R.id.movieRecyclerView);
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        movieRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(movieRecyclerView.canScrollHorizontally(1)){
                    movieListViewModel.searchNextPage();
                }
            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(this,MovieDetailsActivity.class);
        intent.putExtra("movie",movieRecyclerViewAdapter.getSelectedMovie(position));
        startActivity(intent);
    }

    @Override
    public void onMovieCategoryClick(String category) {

    }

    /*private void getRetrofitResponse() {

        MovieApi movieApi = RetrofitService.getMovieApi();

        Call<MovieSearchResponse> responseCall = movieApi
                .searchMovie(Credential.API_KEY,"Action",1);

        responseCall.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                if(response.code() == 200){

                    Log.d("tag",response.body().toString());

                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());

                    for(MovieModel movie : movies){
                        Log.d("tag",movie.getTitle());

                    }
                }
                else{
                    try {
                        Log.d("tag","Error: "+response.errorBody().toString());
                    }
                    catch (Exception e){
                        Log.d("tag",e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {

            }
        });
    }

    private void getRetrofitResponseAccordingToID(){

        MovieApi movieApi = RetrofitService.getMovieApi();

        Call<MovieModel> responseCall = movieApi.getMovie(343611,Credential.API_KEY);

        responseCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if(response.code() == 200){

                    MovieModel movieModel = response.body();

                    Log.d("tag",movieModel.getTitle());
                }
                else{
                    try {
                        Log.d("tag","Error: "+response.errorBody().toString());
                    }
                    catch (Exception e){
                        Log.d("tag",e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    }*/
}