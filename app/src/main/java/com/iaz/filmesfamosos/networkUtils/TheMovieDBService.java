package com.iaz.filmesfamosos.networkUtils;

import com.iaz.filmesfamosos.models.Response;
import com.iaz.filmesfamosos.models.ResponseReview;
import com.iaz.filmesfamosos.models.ResponseVideo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


interface TheMovieDBService {

    //TODO insert API key
    String API_KEY = "";

    @GET("3/movie/popular?api_key=" + API_KEY)
    Call<Response> getPopular();

    @GET("3/movie/top_rated?api_key=" + API_KEY)
    Call<Response> getTopRated();

    @GET("3/movie/upcoming?api_key=" + API_KEY)
    Call<Response> getUpcoming();

    @GET("3/movie/{movie_id}/videos?api_key=" + API_KEY)
    Call<ResponseVideo> getVideos(
            @Path("movie_id") String movieID);

    @GET("3/movie/{movie_id}/reviews?api_key=" + API_KEY)
    Call<ResponseReview> getReviews(
            @Path("movie_id") String movieID);

}

