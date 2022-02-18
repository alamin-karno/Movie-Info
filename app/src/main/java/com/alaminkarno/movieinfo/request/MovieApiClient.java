package com.alaminkarno.movieinfo.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alaminkarno.movieinfo.models.MovieModel;
import com.alaminkarno.movieinfo.response.MovieSearchResponse;
import com.alaminkarno.movieinfo.view.AppExecutors;
import com.alaminkarno.movieinfo.view.utils.Credential;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    private static MovieApiClient instance;

    private MutableLiveData<List<MovieModel>> mMovies;
    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    private MutableLiveData<List<MovieModel>> mPopularMovies;
    private RetrievePopularMoviesRunnable retrievePopularMoviesRunnable;

    public static MovieApiClient getInstance(){

        if(instance == null){
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient(){

        mMovies = new MutableLiveData<>();
        mPopularMovies = new MutableLiveData<>();
    }


    public LiveData<List<MovieModel>> getMovies(){
        return mMovies;
    }

    public LiveData<List<MovieModel>> getPopularMovies(){
        return mPopularMovies;
    }

    public void searchMovieApi(String query, int pageNumber){

        if(retrieveMoviesRunnable != null ){
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query,pageNumber);

        final Future futureHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                futureHandler.cancel(true);
            }
        },3000, TimeUnit.MILLISECONDS);

    }

    public void searchPopularMovieApi(int pageNumber){

        if(retrievePopularMoviesRunnable != null ){
            retrievePopularMoviesRunnable = null;
        }

        retrievePopularMoviesRunnable = new RetrievePopularMoviesRunnable(pageNumber);

        final Future futureHandler = AppExecutors.getInstance().networkIO().submit(retrievePopularMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                futureHandler.cancel(true);
            }
        },3000, TimeUnit.MILLISECONDS);

    }

    private class RetrieveMoviesRunnable implements  Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {

            try{
                Response response = getMovies(query,pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());

                    if(pageNumber == 1){
                        mMovies.postValue(list);
                    }
                    else{
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }

                }
                else{
                    String error = response.errorBody().toString();
                    Log.d("tag","Error"+error);
                    mMovies.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }


        }

        private Call<MovieSearchResponse> getMovies(String query,int pageNumber){
            return RetrofitService.getMovieApi().searchMovie(Credential.API_KEY, query, pageNumber);
        }

        private void cancelRequest(){
            Log.d("tag","Cancel Search Request");
            cancelRequest = true;
        }
    }


    private class RetrievePopularMoviesRunnable implements  Runnable{

        private int pageNumber;
        boolean cancelRequest;

        public RetrievePopularMoviesRunnable(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {

            try{
                Response response = getPopularMovies(pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());

                    if(pageNumber == 1){
                        mMovies.postValue(list);
                    }
                    else{
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }

                }
                else{
                    String error = response.errorBody().toString();
                    Log.d("tag","Error"+error);
                    mMovies.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }


        }

        private Call<MovieSearchResponse> getPopularMovies(int pageNumber){
            return RetrofitService.getMovieApi().getPopularMovie(Credential.API_KEY,pageNumber);
        }

        private void cancelRequest(){
            Log.d("tag","Cancel Search Request");
            cancelRequest = true;
        }
    }


}
