package com.alaminkarno.movieinfo.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.alaminkarno.movieinfo.models.MovieModel;
import com.alaminkarno.movieinfo.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private MovieRepository movieRepository;

    public MovieListViewModel() {
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieRepository.getMovies();
    }

    public LiveData<List<MovieModel>> getPopularMovies(){
        return movieRepository.getPopularMovies();
    }

    public void searchMovieApi(String query,int pageNumber){
        movieRepository.searchMovieApi(query,pageNumber);
    }

    public void searchPopularMovieApi(int pageNumber){
        movieRepository.searchPopularMovieApi(pageNumber);
    }

    public void searchNextPage(){
        movieRepository.searchNextPage();
    }
}
