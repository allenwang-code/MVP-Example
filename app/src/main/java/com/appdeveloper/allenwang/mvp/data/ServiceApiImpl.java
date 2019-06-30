
package com.appdeveloper.allenwang.mvp.data;

import com.appdeveloper.allenwang.mvp.model.MovieDetails;
import com.appdeveloper.allenwang.mvp.model.Search;

import io.reactivex.Observable;

public class ServiceApiImpl implements ServiceApi {

    private RetrofitApi retrofitApi;

    public ServiceApiImpl(RetrofitApi retrofitApi){
        this.retrofitApi = retrofitApi;
    }

    @Override
    public Observable<Search> getMovies(String query) {
        return retrofitApi.getMoives(query);
    }

    @Override
    public Observable<MovieDetails> getMovie(final String id) {
        return retrofitApi.getMoiveDetails(id);
    }
}
