
package com.appdeveloper.allenwang.mvp.data;

import com.appdeveloper.allenwang.mvp.model.MovieDetails;
import com.appdeveloper.allenwang.mvp.model.Search;

import io.reactivex.Observable;

/**
 * Defines an interface to the service API that is used by this application. All data request should
 * be piped through this interface.
 */
public interface ServiceApi {
    Observable<Search> getMovies(String query);
    Observable<MovieDetails> getMovie(String noteId);
}
