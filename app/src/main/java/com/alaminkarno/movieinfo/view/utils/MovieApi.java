package com.alaminkarno.movieinfo.view.utils;

import com.alaminkarno.movieinfo.models.MovieModel;
import com.alaminkarno.movieinfo.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page
    );

    @GET("3/movie/{movie_id}")
    Call<MovieModel> getMovie(
            @Path("movie_id") int movie_id,
            @Query("api_key") String key
    );

    @GET("3/movie/popular")
    Call<MovieSearchResponse> getPopularMovie(
            @Query("api_key") String key,
            @Query("page") int page
    );
}
