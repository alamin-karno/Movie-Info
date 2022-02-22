package com.alaminkarno.movieinfo.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alaminkarno.movieinfo.models.MovieModel;
import com.alaminkarno.movieinfo.request.MovieApiClient;

import java.util.List;

public class MovieRepository {

    private static MovieRepository instance;

    private MovieApiClient movieApiClient;

    private String mQuery;
    private int mPageNumber;

    public  static MovieRepository getInstance(){

        if(instance == null){
            instance = new MovieRepository();
        }

        return instance;
    }

    private MovieRepository(){
        movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieApiClient.getMovies();
    }

    public LiveData<List<MovieModel>> getPopularMovies(){
        return movieApiClient.getPopularMovies();
    }

    public void searchMovieApi(String query,int pageNumber){

        mQuery = query;
        mPageNumber = pageNumber;

        movieApiClient.searchMovieApi(query,pageNumber);
    }

    public void searchPopularMovieApi(int pageNumber){

        mPageNumber = pageNumber;

        movieApiClient.searchPopularMovieApi(pageNumber);
    }

    public void popularNextPage(){
        searchPopularMovieApi(mPageNumber+1);
    }

    public void searchNextPage(){

        searchMovieApi(mQuery,mPageNumber+1);
    }
}

