package com.iaz.filmesfamosos.networkUtils;

import com.iaz.filmesfamosos.models.Response;
import com.iaz.filmesfamosos.models.ResponseReview;
import com.iaz.filmesfamosos.models.ResponseVideo;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public final class TheMovieDBApi {

    private static final String TMDB_API_BASE_URL = "https://api.themoviedb.org/";
    private static final TheMovieDBService theMovieDBService;

    static {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TMDB_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        theMovieDBService = retrofit.create(TheMovieDBService.class);
    }


    public static void getPopular(Callback<Response> callback) {
        Call<Response> call = theMovieDBService.getPopular();
        call.enqueue(callback);
    }

    public static void getTopRated(Callback<Response> callback) {
        Call<Response> call = theMovieDBService.getTopRated();
        call.enqueue(callback);
    }

    public static void getUpcoming(Callback<Response> callback) {
        Call<Response> call = theMovieDBService.getUpcoming();
        call.enqueue(callback);
    }

    public static void getVideo(String movieId, Callback<ResponseVideo> callback) {
        Call<ResponseVideo> call = theMovieDBService.getVideos(movieId);
        call.enqueue(callback);
    }

    public static void getReviews(String movieId, Callback<ResponseReview> callback) {
        Call<ResponseReview> call = theMovieDBService.getReviews(movieId);
        call.enqueue(callback);
    }
}
