package com.appdeveloper.allenwang.mvp.data;


import com.appdeveloper.allenwang.mvp.model.MovieDetails;
import com.appdeveloper.allenwang.mvp.model.Search;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApi {
    // api key can be hidden in gradle file
    @GET("?apikey=5b57c62d")
    Observable<Search> getMoives(@Query("s") String title);

    @GET("?apikey=5b57c62d")
    Observable<MovieDetails> getMoiveDetails(@Query("i") String id);
}
